package canvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ToolbarGUI extends JFrame implements ActionListener{
    //TODO: implement the action listener for each button
    private ToolbarModel toolbar;
    public ToolbarGUI(ToolbarModel toolbar){
        try {
            //make it so that button colors show up
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
         } catch (Exception e) {
                    e.printStackTrace();
         }
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.BLACK, Color.MAGENTA, Color.WHITE};
        JPanel main = new JPanel();

        this.add(main);
        this.setTitle("Toolbar");
        this.toolbar = toolbar;
        this.setSize(80,400);
        this.setLocation(475,200);
        JButton[] buttons = new JButton[colors.length];
        for (int i = 0; i< colors.length; i++){
            JButton button = new JButton();
            button.setBackground(colors[i]);
            buttons[i] = button;
        }
        JButton small = new JButton("Small");
        JButton med = new JButton("Medium");
        JButton large = new JButton("Large");
        JButton undo = new JButton("undo");
        JButton redo = new JButton("redo");
        GroupLayout layout = new GroupLayout(main);
        main.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(small)
                .addComponent(med)
                .addComponent(large)
                .addComponent(buttons[0]) //TODO:  put these in a loop
                .addComponent(buttons[1])
                .addComponent(buttons[2])
                .addComponent(buttons[3])
                .addComponent(buttons[4])
                .addComponent(buttons[5])
                .addComponent(buttons[6])
                .addComponent(buttons[7])
                .addComponent(undo)
                .addComponent(redo)
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
             );
        
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
    }
    
    public static void main(final String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ToolbarModel toolbar = new ToolbarModel();
                ToolbarGUI main = new ToolbarGUI(toolbar);
                main.setVisible(true);
            }
        });
    }
    
    
}
