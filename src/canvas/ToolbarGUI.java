package canvas;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class ToolbarGUI extends JPanel{
    //TODO: implement the action listener for the other non-color buttons. 
    private final ToolbarModel toolbar;
    public ToolbarGUI(final ToolbarModel toolbar){
        try {
            //make it so that button colors show up
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
         } catch (Exception e) {
              e.printStackTrace();
             //this throws some sort of error, i think because it conflicts with canvas 
             //but it doesn't really affect functionality. 
         }
        System.out.println("hi");
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
        JButton undo = new JButton("undo");//TODO: pass undo/redo events to Canvas
        JButton redo = new JButton("redo");
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(small)
                .addComponent(med)
                .addComponent(large)
                .addComponent(buttons[0]) //TODO:  figure put how to put these in a loop
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
//    public static void main(final String[] args){
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                ToolbarModel toolbar = new ToolbarModel();
//                ToolbarGUI main = new ToolbarGUI(toolbar);
//                main.setVisible(true);
//            }
//        });
//    }

    
    
}
