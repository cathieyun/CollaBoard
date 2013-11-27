package canvas;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

public class Freehand implements DrawingObject{
    private final List<Point> points;
    private Color color;
    public Freehand(List<Point> points, Color color){
        this.points = points;
        this.color = color;
    }
    
}
