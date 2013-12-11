package client;



import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import canvas.DrawingObject;
import canvas.ToolbarModel;

/**
 * GUI for the Collaborative whiteboard. 
 * Most methods are package-private to prevent rep exposure.
 *
 */
public class CollaboardGUI extends JFrame{
    private JPanel userSelect; //username selection pane 
    private JPanel whiteboardSelect; //whiteboard selection pane
    private Canvas canvas; //canvas for user to draw on
    private JPanel panels; //contains userSelect and whiteboardSelect. used for CardLayout
    private final JTextField usernameField; //field in which user enters a username
    private final JTextField whiteboardField; //field in which user enters a whiteboardID to create a new whiteboard at the selection page
    private final JTextField changeWhiteboardField; //field in which user enters a whiteboardID to switch whiteboards from the main whiteboard pane
    private final JLabel error; //error message to display for invalid username inputs
    private User user; //user that is using the whiteboard
    private PrintWriter out; //sends messages to the server
    private ArrayList<Integer> whiteboards; //stores the list of active whiteboardIDs
    private JLabel createWhiteboard; //displays instruction to create a whiteboard, and changes to an error message in the case of an error
    private JTable currentWhiteboards;
    private DefaultTableModel whiteboardsModel;
    private JTable currentUsers; //table displaying the list of active users on the current whiteboard
    private DefaultTableModel usersModel; //model for currentUsers
    private JFrame window; //the JFrame containing canvas, the toolbar, currentUsers, and the whiteboard switching header
    public CollaboardGUI(User user, OutputStream outputStream, InputStream inputStream){
        this.user = user;
        this.canvas = new Canvas(800, 600, new ClientCanvasModel(), user, outputStream);
        // canvas.paintComponent(canvas.getGraphics());
        //this.users = new ArrayList<String>();
        this.out = new PrintWriter(outputStream, true);
        this.whiteboards = new ArrayList<Integer>();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.addWindowListener(new WindowAdapter() { //send the bye message upon closing the window.
			public void windowClosing(WindowEvent e) {
				out.println("bye");
			}
		});
        //instantiate the panels in the CardLayout and add them
        CardLayout layout = new CardLayout();
        panels = new JPanel(layout);
        userSelect = new JPanel();
        whiteboardSelect = new JPanel();   
        panels.add(userSelect,"user");
        panels.add(whiteboardSelect,"whiteboard");
        
        this.setTitle("Collaboard");
        this.changeWhiteboardField = new JTextField(15);
        usernameField = new JTextField(15);
        error = new JLabel("Invalid username or username taken.");
        whiteboardField = new JTextField(15);
        whiteboardField.addActionListener(new CreateWhiteboardListener());
        this.setSize(350,200);
        this.setLocation(600,200);
        initializeUserPane();
        this.add(panels);
        layout.show(panels, "user");
        this.setVisible(true);
    }
    /**
     * Many of the following are package-private methods that are called by the Client class.
     */
    ArrayList<Integer> getWhiteboards(){
        return whiteboards;
    }
    /**
     * Display the error message for a invalid username input.
     */
    void displayUserTakenError(){
        error.setVisible(true);
    }
    /**
     * Helper method that initializes the username selection pane. Called in the constructor.
     */
    private void initializeUserPane(){
        GroupLayout layout = new GroupLayout(userSelect);
        userSelect.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        JLabel instruction = new JLabel("Please select a username.");
        JButton newUsernameButton = new JButton("Go!");
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(instruction)
                .addGroup(layout.createSequentialGroup()
                   .addComponent(usernameField)
                   .addComponent(newUsernameButton))
                .addComponent(error)
             );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                    .addComponent(instruction)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(usernameField)
                        .addComponent(newUsernameButton))
                    .addComponent(error)
             );
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
    /**
     * Method that initializes the whiteboard selection pane.
     * Called after receiving a list of active Whiteboards from the server.
     */
    void initializeWhiteboardPane(){
        JButton chooseWhiteboard = new JButton("Go");
        JButton makeNewWhiteboard = new JButton("Create!");
        makeNewWhiteboard.addActionListener(new CreateWhiteboardListener());
        JLabel selectWhiteboard = new JLabel("Select an existing whiteboard below");
        currentWhiteboards = new JTable();
        chooseWhiteboard.addActionListener(new SelectWhiteboardListener(currentWhiteboards));
        createWhiteboard = new JLabel("Enter a new integer > 0 not displayed below to create a new whiteboard");
        JScrollPane whiteboardsList = new JScrollPane(currentWhiteboards);
        whiteboardsModel = new DefaultTableModel(new String [] {"Existing Whiteboards"},0){
            //prevent user from editing cells
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        currentWhiteboards.setModel(whiteboardsModel);
        for (int i: whiteboards){
            whiteboardsModel.addRow(new String[]{Integer.toString(i)});
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
        selectWhiteboard.setVisible(true);
        whiteboardsList.setVisible(true);
        createWhiteboard.setVisible(true);
        whiteboardField.setVisible(true);
    }
    
    /**
     * Advances GUI to the whiteboard selection page. 
     * Called after receiving a "validuser" message from the server.
     */
    void goToWhiteboardSelect(){
        CollaboardGUI.this.setSize(500,500);
        CardLayout layout = (CardLayout) panels.getLayout();
        layout.show(panels, "whiteboard");
    }
    /**
     * Helper method to initialize the canvas.
     * Called after receiving a "ready" message from the server.
     */
    void initializeCanvas(){
        currentUsers = new JTable();
        usersModel = new DefaultTableModel(new String[]{"Current Users"},0){
            //prevent user from editing cells
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        currentUsers.setModel(usersModel);
        JScrollPane usersList = new JScrollPane(currentUsers);
        JPanel changeWhiteboard = new JPanel();
        JLabel changeWhiteboardLabel = new JLabel("Input a whiteboard number to switch to that board");
        JButton changeWhiteboardButton = new JButton("Go!");
        changeWhiteboardField.addActionListener(new SwitchWhiteboardListener());
        changeWhiteboardButton.addActionListener(new SwitchWhiteboardListener());
        changeWhiteboard.add(changeWhiteboardLabel);
        changeWhiteboard.add(changeWhiteboardField);
        changeWhiteboard.add(changeWhiteboardButton);
        changeWhiteboard.setPreferredSize(new Dimension(900,50));
        usersList.setPreferredSize(new Dimension(100,200));
        ToolbarGUI toolbarGUI = new ToolbarGUI(user.getToolbar());
        window = new JFrame("Whiteboard " + user.getWhiteboardID());    
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = window.getContentPane();
        container.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        container.add(changeWhiteboard, c);
        c.gridwidth = 1;
        c.gridy = 1;
        c.gridheight = 2;
        container.add(canvas, c);
        c.gridx = 1;
        c.gridheight = 1;
        container.add(usersList, c);
        c.gridy = 2;
        container.add(toolbarGUI, c);
        window.setLocation(300,100);
        window.pack();
      window.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
              out.println("exit " + user.getUsername() + " " + user.getUserID() + " " + user.getWhiteboardID());
              out.println("bye");
              }
      });
        window.setVisible(true);
        window.setResizable(false); //don't let the user resize the window.
        CollaboardGUI.this.dispose();
    }
 
    /**
     * Displays an error message if the desired whiteboard ID is already taken.
     * Called after receiving a "whiteboardtaken" message from the server.
     */
    void displayWhiteboardTakenError(){
        createWhiteboard.setText("Whiteboard ID already taken. Select it from below or choose a new integer.");

    }
    
    /**
     * Called by client after receiving a "validwhiteboard" message.
     */
    void enterCanvas(){
        user.setWhiteboardID(Integer.parseInt(whiteboardField.getText()));
        System.out.println("enter "+ user.getUsername()+ " " + user.getUserID() + " " + user.getWhiteboardID());
        new ProtocolWorker("enter "+ user.getUsername()+ " " + user.getUserID() + " " +  user.getWhiteboardID()).execute();     
    }
    void clearCanvas(){
        canvas.fillWithWhite();
    }
    Canvas getCanvas(){
        return canvas;
    }
    ClientCanvasModel getCanvasModel() {
        return canvas.getCanvasModel();
    }
    void setCanvasModel(ClientCanvasModel c){
        canvas.setCanvasModel(c);
    }
    
    /**
     * Draws the specified object.
     * @param d
     */
    void drawObject(DrawingObject d){
        canvas.drawDrawingObject(d);
    }
    void addWhiteboard(int ID){
        if (whiteboardsModel != null){
            //if whiteboardsModel hasn't been initialized yet, don't do anything.
            whiteboardsModel.addRow(new String[]{Integer.toString(ID)});
        }
    }
    /**
     * Adds the specified user to the table of active users.
     * @param user
     */
    void addUser(String user){
        usersModel.addRow(new String[]{user});
    }
    /**
     * Remove the specified user from the table.
     * @param user
     */
    void removeUser(String user){
        for (int row = 0; row <= currentUsers.getRowCount()-1; row++){
            if (user.equals(currentUsers.getValueAt(row,0))){
                usersModel.removeRow(row);
                break;
            }
        }
    }
    
    /**
     * ActionListener that listens for the actions pertaining to creation of a new username
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
                new ProtocolWorker("makeuser " + desiredUsername + " " + user.getUserID()).execute();
                //send to the server
                user.setUsername(desiredUsername);
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
                createWhiteboard.setText("Enter a new integer > 0 not displayed below to create a new whiteboard");
                int newWhiteboard = Integer.parseInt(whiteboardField.getText());
                if (newWhiteboard < 1) throw new NumberFormatException();
                new ProtocolWorker("makeboard "+newWhiteboard).execute();
                //send a message to the server to create a new whiteboard
            }catch(NumberFormatException e1){
                //display an error.
                createWhiteboard.setText("Invalid input. Try again or choose a whiteboard from below.");
            }
            
        }
    }
    /**
     * ActionListener that is called when the user selects an existing canvas.
     */
    private class SelectWhiteboardListener implements ActionListener{
        private JTable table;
        public SelectWhiteboardListener(JTable table){
            this.table=table;
        }
        @Override
        public void actionPerformed(ActionEvent arg0) {
            user.setWhiteboardID(Integer.parseInt((String) table.getValueAt(table.getSelectedRow(),table.getSelectedColumn())));
            System.out.println("enter "+ user.getUsername()+ " " + user.getWhiteboardID());
            new ProtocolWorker("enter "+ user.getUsername()+ " " + user.getUserID() + " " + user.getWhiteboardID()).execute();
        }
        
    }
    /**
     * ActionListener that is called when the user tries to switch whiteboards
     * from the main Whiteboard JFrame (window).
     */
    private class SwitchWhiteboardListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent arg0) {
            try{
                int targetWhiteboard = Integer.parseInt(changeWhiteboardField.getText());
                if (targetWhiteboard < 1 || targetWhiteboard == user.getWhiteboardID()){
                    throw new NumberFormatException(); //do nothing
                }
                changeWhiteboardField.setText(""); //clear the field
                int oldID = user.getWhiteboardID();
                user.setWhiteboardID(targetWhiteboard);
                window.setTitle("Whiteboard " + user.getWhiteboardID());
                clearCanvas();
                //clear the current users
                usersModel = new DefaultTableModel(new String[]{"Current Users"},0){
                    //prevent user from editing cells
                    @Override
                    public boolean isCellEditable(int row, int column) {
                       return false;
                    }
                };
                currentUsers.setModel(usersModel);
                //clear the Jtable
                canvas.setCanvasModel(new ClientCanvasModel()); //new clientcanvasmodel
                //send a message to the server 
                new ProtocolWorker("exit "+ user.getUsername() + " " + user.getUserID() + " " + oldID + "\nswitchboard " + user.getUsername() + " " + user.getUserID() + " " + targetWhiteboard).execute();
            }catch(NumberFormatException e){
                //do nothing
            }
            
        }
        
    }
    
    /**
     * SwingWorker that passes messages to the server in a background thread.
     */
     private class ProtocolWorker extends SwingWorker<String, Object>{
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
    /**
     * GUI by which the user can call undo, redo, draw ovals, and change the color and thickness of the stroke.
     */
    public class ToolbarGUI extends JPanel{
        //below is low priority
        //TODO: Add an "exit" button.
        private final ToolbarModel toolbar;
        public ToolbarGUI(final ToolbarModel toolbar){
            try {
                //make it so that button colors show up
                UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
             } catch (Exception e) {
                  e.printStackTrace();
             }
            Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.BLACK, Color.MAGENTA, Color.WHITE};
            this.toolbar = toolbar;
            this.setSize(100,500);
            JButton[] buttons = new JButton[colors.length];
            for (int i = 0; i< colors.length; i++){
                final Color currentColor = colors[i];
                final JButton button = new JButton();
                button.setBackground(currentColor);
                button.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        toolbar.setColor(currentColor);
                    }  
                });
                buttons[i] = button;
            }

            JButton small = new JButton("Small");
            small.addActionListener(new StrokeListener(2));
            JButton med = new JButton("Medium");
            med.addActionListener(new StrokeListener(5));
            JButton large = new JButton("Large");
            large.addActionListener(new StrokeListener(20));
            JButton undo = new JButton("Undo");
            JButton redo = new JButton("Redo");
            JButton exit = new JButton("Exit");
            JToggleButton oval = new JToggleButton("Draw Oval");
            GroupLayout layout = new GroupLayout(this);
            this.setLayout(layout);
            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(small)
                    .addComponent(med)
                    .addComponent(large)
                    .addComponent(buttons[0])
                    .addComponent(buttons[1])
                    .addComponent(buttons[2])
                    .addComponent(buttons[3])
                    .addComponent(buttons[4])
                    .addComponent(buttons[5])
                    .addComponent(buttons[6])
                    .addComponent(buttons[7])
                    .addComponent(undo)
                    .addComponent(redo)
                    .addComponent(oval)
                    .addComponent(exit)
                 );
            layout.setVerticalGroup(
                    layout.createSequentialGroup()
                    .addComponent(small)
                    .addComponent(med)
                    .addComponent(large)
                    .addComponent(buttons[0])
                    .addComponent(buttons[1])
                    .addComponent(buttons[2])
                    .addComponent(buttons[3])
                    .addComponent(buttons[4])
                    .addComponent(buttons[5])   
                    .addComponent(buttons[6])
                    .addComponent(buttons[7])
                    .addComponent(undo)
                    .addComponent(redo)
                    .addComponent(oval)
                    .addComponent(exit)
                 );
            
            undo.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    canvas.sendUndoRedoMessage(true); //send message to the server
                }
            });
            
            redo.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    canvas.sendUndoRedoMessage(false); //send message to the server
                }
            });
            
            oval.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    canvas.toggleDrawingOval();
                }
            });         
            exit.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    WindowEvent wev = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
                    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
                }
            });        
        }
        
        /**
         * Listener that alters the thickness of the user's strokes according to the button pressed.
         *
         */
        public class StrokeListener implements ActionListener{
            private int thickness;
            public StrokeListener(int thickness){
                this.thickness = thickness;
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                toolbar.setStroke(new BasicStroke(thickness));            
            }
            
        }   
    }
}
