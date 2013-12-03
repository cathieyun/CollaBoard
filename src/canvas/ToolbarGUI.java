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
/**
 * GUI by which the user can call undo, redo, draw ovals, and change the color and thickness of the stroke.
 *
 */
public class ToolbarGUI extends JPanel{
    //below is low priority
    //TODO: Add an "exit" button.
    //TODO: Add a way to change whiteboards.
    private final ToolbarModel toolbar;
    private Canvas canvas;
    public ToolbarGUI(final ToolbarModel toolbar, final Canvas canvas){
        this.canvas = canvas;
        try {
            //make it so that button colors show up
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
         } catch (Exception e) {
              //e.printStackTrace();
             //this throws some sort of error, i think because it conflicts with canvas 
             //but it doesn't really affect functionality. 
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
        JButton oval = new JButton("Draw Oval");
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
                .addComponent(oval)
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
             );
        
    	undo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                canvas.undo();
                canvas.sendUndoRedoMessage(true);
            }
        });
    	
        redo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                canvas.redo();
                canvas.sendUndoRedoMessage(false);
            }
        });
        
        oval.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                canvas.toggleDrawingOval();
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
