package canvas;

import java.awt.Color;
import java.awt.Point;
import java.awt.Stroke;
import java.util.List;

public class Freehand implements DrawingObject{
    private final List<Point> points;
    private Color color;
    private Stroke stroke;
    public Freehand(List<Point> points, Color color, Stroke stroke){
        this.points = points;
        this.color = color;
        this.stroke = stroke;
    }
    public List<Point> getPoints() {
        return points;
    }
    public Color getColor() {
        return color;
    }

    public Stroke getStroke() {
        return stroke;
    }

    
    
    
}
