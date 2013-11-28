package canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

public class ToolbarModel {
/**
 * Each connection has a toolbar
 */
    private Color color;
    private Stroke stroke;
    public ToolbarModel(){
        this.color = Color.BLACK;
        this.stroke = new BasicStroke(5);
    }
    
    public void setColor(Color color){
        this.color = color;
    }
    public Color getColor(){
        return color;
    }
    
    public void setStroke(Stroke stroke){
        this.stroke = stroke;
    }
    
    public Stroke getStroke(){
        return stroke;
    }
}
