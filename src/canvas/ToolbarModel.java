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
    private int userID;
    
    public ToolbarModel(int userID){
    	this.userID = userID;
        this.color = Color.BLACK;
        this.stroke = new BasicStroke(5);
    }
    
    public void setColor(Color color){
        System.out.println("I am now this color: "+ color.toString());
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
