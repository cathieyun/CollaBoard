package canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
/**
 * Class representing a user's toolbar settings (color and thickness).
 *
 */
public class ToolbarModel {

    private Color color;
    private Stroke stroke;
    private int userID;
    
    public ToolbarModel(int userID){
        this.userID = userID;
        this.color = Color.BLACK;
        this.stroke = new BasicStroke(5);
    }
    
    public void setColor(Color color){
        this.color = color;
    }
    public Color getColor(){
        return color;
    }
    
    public int getUserID(){
    	return userID;
    }
    
    public void setStroke(Stroke stroke){
        this.stroke = stroke;
    }
    
    public Stroke getStroke(){
        return stroke;
    }
}
