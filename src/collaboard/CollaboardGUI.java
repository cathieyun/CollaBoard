package collaboard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import client.ClientCanvasModel;
import client.User;


import whiteboard.Whiteboard;

import canvas.Canvas;
import canvas.CanvasModel;
import canvas.ToolbarGUI;


public class CollaboardGUI extends JFrame{
    private JPanel whiteboardSelect;
    private JPanel userSelect;
    private JPanel canvas;
    private JPanel panels;
    private final JTextField usernameField;
    private final JTextField whiteboardField;
    private final JLabel error;
    //private Collaboard collaboard;
    private User user; //ID of the user using this instance of CollaboardGUI
    private int userID;
    private OutputStream outputStream;
    private InputStream inputStream;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<Integer> whiteboards;
    public CollaboardGUI(User user, OutputStream outputStream, InputStream inputStream){  
        this.user = user;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        in = new BufferedReader(new InputStreamReader(inputStream));
        out = new PrintWriter(outputStream, true);
        whiteboards = new ArrayList<Integer>();
        this.userID = user.getUserID();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        
        //instantiate the panels in the CardLayout and add them
        CardLayout layout = new CardLayout();
        panels = new JPanel(layout);
        userSelect = new JPanel();
        whiteboardSelect = new JPanel();   
        panels.add(userSelect,"user");
        panels.add(whiteboardSelect,"whiteboard");
        
        this.setTitle("Collaboard");
        usernameField = new JTextField(15);
        error = new JLabel("Invalid username");
        whiteboardField = new JTextField(15);
        whiteboardField.addActionListener(new CreateWhiteboardListener());
        this.setSize(350,200);
        this.setLocation(600,200);
        initializeUserPane();
        initializeWhiteboardPane();
        this.add(panels);
        layout.show(panels, "user");
        this.setVisible(true);
    }
    public ArrayList<Integer> getWhiteboards(){
        return whiteboards;
    }
    private void initializeUserPane(){
        userSelect.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel instruction = new JLabel("Please select a username.");
        JButton newUsernameButton = new JButton("Go!");
        userSelect.add(instruction);
        userSelect.add(usernameField);
        userSelect.add(newUsernameButton);
        userSelect.add(error);
        instruction.setVisible(true);
        usernameField.setVisible(true);
        newUsernameButton.setVisible(true);
        error.setVisible(false);
        
        UsernameListener u = new UsernameListener();
        usernameField.addActionListener(u);
        newUsernameButton.addActionListener(u);
    }
    
    private void initializeWhiteboardPane(){
        JButton chooseWhiteboard = new JButton("Go");
        JButton makeNewWhiteboard = new JButton("Create!");
        makeNewWhiteboard.addActionListener(new CreateWhiteboardListener());
        JLabel selectWhiteboard = new JLabel("Select an existing whiteboard");
        JTable whiteboardIDs = new JTable();
        JLabel createWhiteboard = new JLabel("Create a new whiteboard");
        JScrollPane whiteboardsList = new JScrollPane(whiteboardIDs);
        DefaultTableModel model = new DefaultTableModel(0,1){
            //prevent user from editing cells
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        whiteboardIDs.setModel(model);
        out.println("whiteboards");
        for (int i: whiteboards){
            model.addRow(new String[]{Integer.toString(i)});
        }
        GroupLayout layout = new GroupLayout(whiteboardSelect);
        whiteboardSelect.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(createWhiteboard)
                .addGroup(layout.createSequentialGroup()
                   .addComponent(whiteboardField)
                   .addComponent(makeNewWhiteboard))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(selectWhiteboard)
                    .addComponent(chooseWhiteboard))
                .addComponent(whiteboardsList)
             );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                    .addComponent(createWhiteboard)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(makeNewWhiteboard)
                        .addComponent(whiteboardField))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(selectWhiteboard)
                        .addComponent(chooseWhiteboard))
                        .addComponent(whiteboardsList)

             );
        whiteboardIDs.setTableHeader(null);
        selectWhiteboard.setVisible(true);
        whiteboardsList.setVisible(true);
        createWhiteboard.setVisible(true);
        whiteboardField.setVisible(true);
    }
    
    /**
     * ActionListener that listens for the actions pertaining to creation of a new username
     *
     */
    private class UsernameListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent arg0) {
            error.setVisible(false);
            String desiredUsername = usernameField.getText();
            String regex = "[a-zA-Z0-9]+";
            if ( ! desiredUsername.matches(regex)) {
                error.setVisible(true); 
            }
            else{
                new ProtocolWorker("makeuser " + desiredUsername + " " + user.getUserID()).execute(); //send to the server
                CollaboardGUI.this.setSize(400,400);
                
                //send message to the server to set new username.
                CardLayout layout = (CardLayout) panels.getLayout();
                layout.show(panels, "whiteboard");
                //go to whiteboardselect page
            }        
        }   
    }
    
    /**
     * ActionListener that listens for actions pertaining to the creation of a new whiteboard.
     *
     */
    private class CreateWhiteboardListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                int newWhiteboard = Integer.parseInt(whiteboardField.getText());
                if (newWhiteboard < 1) throw new NumberFormatException();
                new ProtocolWorker("makeboard "+newWhiteboard).execute();
                //send a message to the server to create a new whiteboard
                ClientCanvasModel clientModel = new ClientCanvasModel();
                JPanel canvas = new Canvas(800, 600, clientModel, user, outputStream, inputStream);
                JFrame window = new JFrame("Canvas " + newWhiteboard);
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setLayout(new BorderLayout());
                window.setLocation(300,100);
                window.add(canvas, BorderLayout.CENTER);
                window.pack();
                window.setVisible(true);
                ToolbarGUI toolbarGUI = new ToolbarGUI(user.getToolbar());
                toolbarGUI.setVisible(true);
                CollaboardGUI.this.dispose();
            }catch(NumberFormatException e1){
                //if it's not a valid value
                //display error message
            }
            // TODO Auto-generated method stub
            
        }
    }
    
    /**
     * SwingWorker that passes messages to the server in a background thread.
     *
     */
    public class ProtocolWorker extends SwingWorker<String, Object>{
        private String message; //message to be sent
        public ProtocolWorker(String message){
            this.message = message;
        }
        @Override
        protected String doInBackground() throws Exception {
            //send a message to the server.
            out.println(message);
            return null;
        }
        
    }

}
