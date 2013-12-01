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

import javax.swing.BoxLayout;
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
    private JLabel createWhiteboard;
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
        error = new JLabel("Invalid username or username taken.");
        whiteboardField = new JTextField(15);
        whiteboardField.addActionListener(new CreateWhiteboardListener());
        this.setSize(350,200);
        this.setLocation(600,200);
        initializeUserPane();
        //initializeWhiteboardPane();
        this.add(panels);
        layout.show(panels, "user");
        this.setVisible(true);
    }
    public ArrayList<Integer> getWhiteboards(){
        return whiteboards;
    }
    public void displayUserTakenError(){
        error.setVisible(true);
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
    
    public void initializeWhiteboardPane(){
        JButton chooseWhiteboard = new JButton("Go");
        JButton makeNewWhiteboard = new JButton("Create!");
        makeNewWhiteboard.addActionListener(new CreateWhiteboardListener());
        JLabel selectWhiteboard = new JLabel("Select an existing whiteboard below");
        JTable whiteboardIDs = new JTable();
        whiteboardIDs.setTableHeader(null);
        chooseWhiteboard.addActionListener(new SelectWhiteboardListener(whiteboardIDs));
        createWhiteboard = new JLabel("Enter a new integer > 0 not displayed below to create a new whiteboard");
        JScrollPane whiteboardsList = new JScrollPane(whiteboardIDs);
        DefaultTableModel model = new DefaultTableModel(0,1){
            //prevent user from editing cells
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        whiteboardIDs.setModel(model);
        System.out.println("There are " + whiteboards.size() + " whiteboards");
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
                displayUserTakenError();
            }
            else{
                new ProtocolWorker("makeuser " + desiredUsername + " " + user.getUserID()).execute(); //send to the server
            }        
        }   
    }
    /**
     * advances GUI to the whiteboard selection page
     */
    public void goToWhiteboardSelect(){
        CollaboardGUI.this.setSize(500,500);
        CardLayout layout = (CardLayout) panels.getLayout();
        layout.show(panels, "whiteboard");
    }
    /**
     * Helper method to initialize the canvas.
     * @param whiteboardID - ID of the whiteboard
     */
    private void initializeCanvas(int whiteboardID){
        ClientCanvasModel clientModel = new ClientCanvasModel();
        JPanel canvas = new Canvas(800, 600, clientModel, user, outputStream, inputStream);
        JFrame window = new JFrame("Canvas " + whiteboardID);    
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = window.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        window.setLocation(300,100);
        ToolbarGUI toolbarGUI = new ToolbarGUI(user.getToolbar());
        container.add(canvas);
        container.add(toolbarGUI);
        //window.add(container);
        window.pack();
        window.setVisible(true);
        CollaboardGUI.this.dispose();
    }
    
    /**
     * ActionListener that listens for actions pertaining to the creation of a new whiteboard.
     *
     */
    private class CreateWhiteboardListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                createWhiteboard.setText("Enter a new integer > 0 not displayed below to create a new whiteboard");
                int newWhiteboard = Integer.parseInt(whiteboardField.getText());
                if (newWhiteboard < 1) throw new NumberFormatException();
                new ProtocolWorker("makeboard "+newWhiteboard).execute();
                //send a message to the server to create a new whiteboard
            }catch(NumberFormatException e1){
                //if it's not a valid value
                //display error message
            }
            // TODO Auto-generated method stub
            
        }
    }
    
    public void displayWhiteboardTakenError(){
        createWhiteboard.setText("Whiteboard ID already taken. Select it from below or choose a new integer.");
    }
    
    /**
     * Called by client after receiving a "validwhiteboard" message.
     */
    public void toCanvas(){
        int newWhiteboard = Integer.parseInt(whiteboardField.getText());
        new ProtocolWorker("enter "+ user.getUsername()+ " " + newWhiteboard);
        initializeCanvas(newWhiteboard);
    }
    
    /**
     * ActionListener that is called when the user selects an existing canvas.
     *
     */
    private class SelectWhiteboardListener implements ActionListener{
        private JTable table;
        public SelectWhiteboardListener(JTable table){
            this.table=table;
        }
        @Override
        public void actionPerformed(ActionEvent arg0) {
            int whiteboardID = Integer.parseInt((String) table.getValueAt(table.getSelectedRow(),table.getSelectedColumn()));
            new ProtocolWorker("enter "+ user.getUsername()+ " " + whiteboardID);
            initializeCanvas(whiteboardID);
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
