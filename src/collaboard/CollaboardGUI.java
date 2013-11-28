package collaboard;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.table.DefaultTableModel;

import canvas.Canvas;


public class CollaboardGUI extends JFrame{
    private JPanel whiteboardSelect;
    private JPanel userSelect;
    private JPanel canvas;
    private JPanel panels;
    private final JTextField usernameField;
    private final JTextField whiteboardField;
    private final JButton newUsernameButton;
    private final JLabel instruction;
    private final JLabel error;
    private Collaboard collaboard;
    public CollaboardGUI(Collaboard collaboard){ 
        this.collaboard = collaboard;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CardLayout layout = new CardLayout();
        panels = new JPanel(layout);
        userSelect = new JPanel();
        whiteboardSelect = new JPanel();
        canvas = new Canvas(800,800);
        panels.add(userSelect,"user");
        panels.add(whiteboardSelect,"whiteboard");
        panels.add(canvas, "canvas");
        this.setTitle("Collaboard");
        instruction = new JLabel("Please select a username.");
        newUsernameButton = new JButton("Go!");
        usernameField = new JTextField(15);
        error = new JLabel("Invalid username");
        whiteboardField = new JTextField(15);
        this.setSize(350,200);
        this.setLocation(475,200);
        initializeUserPane();
        initializeWhiteboardPane();
        this.add(panels);
        //this.add(userSelect);
        //this.add(whiteboardSelect);
        //this.add(canvas);
        //panels.setVisible(true);
        layout.show(panels, "user");
        this.setVisible(true);
    }
    
    private void initializeUserPane(){
        userSelect.setLayout(new FlowLayout(FlowLayout.CENTER));
        userSelect.add(instruction);
        userSelect.add(usernameField);
        userSelect.add(newUsernameButton);
        userSelect.add(error);
        instruction.setVisible(true);
        usernameField.setVisible(true);
        newUsernameButton.setVisible(true);
        error.setVisible(false);
        
        UsernameListener u = new UsernameListener(this);
        usernameField.addActionListener(u);
        newUsernameButton.addActionListener(u);
    }
    
    private void initializeWhiteboardPane(){
        JButton chooseWhiteboard = new JButton("Go");
        JButton makeNewWhiteboard = new JButton("Create!");
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
        
        Set<Integer> whiteboards = collaboard.getWhiteboards().keySet();
        //System.out.println(whiteboards.toString());
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
    
    public class UsernameListener implements ActionListener{
        private CollaboardGUI gui;
        public UsernameListener(CollaboardGUI gui){
            this.gui=gui;
        }
        @Override
        public void actionPerformed(ActionEvent arg0) {
            error.setVisible(false);
            String desiredUsername = usernameField.getText();
            String regex = "[a-zA-Z0-9]+";
            if ( ! desiredUsername.matches(regex)) {
                error.setVisible(true); 
            }
            else{
                gui.setSize(400,400);
                //send message to the server to set new username.
                CardLayout layout = (CardLayout) panels.getLayout();
                layout.show(panels, "whiteboard");
                //go to whiteboardselect page
            }
            
        }
        
    }
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Collaboard collaboard = new Collaboard();
                for (int i = 0; i < 10; i++){
                    collaboard.createNewWhiteboard(i,10,20);
                }
                CollaboardGUI main = new CollaboardGUI(collaboard);
                main.setVisible(true);
            }
        });
    }
}
