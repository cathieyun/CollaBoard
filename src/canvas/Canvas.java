package canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.UIManager;

import client.ClientCanvasModel;
import client.User;

import canvas.Freehand;
import canvas.Line;
import canvas.Oval;

/**
 * Canvas represents a drawing surface on which a user can draw.
 */

public class Canvas extends JPanel{

    // image where the user's drawing is stored
	private Image drawingBuffer;
    public static Stroke SMALL = new BasicStroke(2);
    public static Stroke MED = new BasicStroke(5);
    public static Stroke LARGE = new BasicStroke(20);
    private ClientCanvasModel canvasModel;
    private PrintWriter out;
    boolean isDrawingOval = false;
    private User user;
    static Map<String,Color> colors; //HashMap storing all the colors and their string representations.
    {
        Map<String,Color> temp = new HashMap<String, Color>();
        temp.put("bl", Color.BLUE);
        temp.put("y", Color.YELLOW);
        temp.put("r", Color.RED);
        temp.put("o", Color.ORANGE);
        temp.put("blk", Color.BLACK);
        temp.put("w", Color.WHITE);
        temp.put("g", Color.GREEN);
        temp.put("m", Color.MAGENTA);
        colors = Collections.unmodifiableMap(temp);
    }
    static Map<String,Stroke> thicknesses; //HashMap storing all thicknesses and their string representations
    {
        Map<String,Stroke> temp = new HashMap<String, Stroke>();
        temp.put("s", new BasicStroke(2));
        temp.put("m", new BasicStroke(5));
        temp.put("l", new BasicStroke(20));
        thicknesses = temp;
    }

    /**
     * Make a canvas.
     * 
     * @param width
     *            width in pixels
     * @param height
     *            height in pixels
     */
    public Canvas(int width, int height, final ClientCanvasModel canvasModel, User user, OutputStream outputStream) {
        this.user = user;
        this.out = new PrintWriter(outputStream, true);
        this.canvasModel = canvasModel;
        this.setPreferredSize(new Dimension(width, height));
        try { //make it so that lookAndFeel is consistent with the toolbarGUI.
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
         } catch (Exception e) {
              e.printStackTrace();
         }
        addDrawingController();
        // note: we can't call makeDrawingBuffer here, because it only
        // works *after* this canvas has been added to a window. Have to
        // wait until paintComponent() is first called.

    }
    
    /**
     * Toggles the state of the canvas from drawing/not drawing an oval.
     */
    public void toggleDrawingOval(){
        isDrawingOval = !isDrawingOval;
    }

    public ClientCanvasModel getCanvasModel(){
        return canvasModel;
    }
    
    public void setCanvasModel(ClientCanvasModel c){
        canvasModel = c;
    }
    
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        // If this is the first time paintComponent() is being called,
        // make our drawing buffer.
        if (drawingBuffer == null) {
            makeDrawingBuffer();
        }
        
        // Copy the drawing buffer to the screen.
        g.drawImage(drawingBuffer, 0, 0, null);
    }
    
    /*
     * Make the drawing buffer and draw some starting content for it.
     */
    private void makeDrawingBuffer() {
        drawingBuffer = createImage(getWidth(), getHeight());
        fillWithWhite();
    }
    
    /*
     * Make the drawing buffer entirely white.
     */
    public void fillWithWhite() {
        final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0,  0,  getWidth(), getHeight());
        
        // IMPORTANT!  every time we draw on the internal drawing buffer, we
        // have to notify Swing to repaint this component on the screen.
        this.repaint();
    }
    
    /*
     * Draw a happy smile on the drawing buffer.
     */
    private void drawSmile() {
        final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();

        // all positions and sizes below are in pixels
        final Rectangle smileBox = new Rectangle(20, 20, 100, 100); // x, y, width, height
        final Point smileCenter = new Point(smileBox.x + smileBox.width/2, smileBox.y + smileBox.height/2);
        final int smileStrokeWidth = 3;
        final Dimension eyeSize = new Dimension(9, 9);
        final Dimension eyeOffset = new Dimension(smileBox.width/6, smileBox.height/6);
        
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(smileStrokeWidth));
        
        // draw the smile -- an arc inscribed in smileBox, starting at -30 degrees (southeast)
        // and covering 120 degrees
        g.drawArc(smileBox.x, smileBox.y, smileBox.width, smileBox.height, -30, -120);
        
        // draw some eyes to make it look like a smile rather than an arc
        for (int side: new int[] { -1, 1 }) {
            g.fillOval(smileCenter.x + side * eyeOffset.width - eyeSize.width/2,
                       smileCenter.y - eyeOffset.height - eyeSize.width/2,
                       eyeSize.width,
                       eyeSize.height);
        }
        
        // IMPORTANT!  every time we draw on the internal drawing buffer, we
        // have to notify Swing to repaint this component on the screen.
        this.repaint();
    }
    
	/**
	 * Draw a line between two points (x1, y1) and (x2, y2), specified in pixels
	 * relative to the upper-left corner of the drawing buffer.
	 */
	private void drawLineSegment(int x1, int y1, int x2, int y2, String color,
		String thickness, boolean areUndoingOrRedoing) {
		Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();

		// configure the color and thickness depending on we are undoing/redoing or not
		if (areUndoingOrRedoing){
	        g.setStroke(thicknesses.get(thickness));
	        g.setColor(colors.get(color));
		}
		else{
	        g.setColor(user.getToolbar().getColor());
	        g.setStroke(user.getToolbar().getStroke());
		}

		// after a series of undo operations, if a user begins to draw again,
		// all edits after the current edit are discarded
		if (!areUndoingOrRedoing) {
			canvasModel.preventRedoAfterThisEdit();
		}

		g.drawLine(x1, y1, x2, y2);

		// IMPORTANT! every time we draw on the internal drawing buffer, we
		// have to notify Swing to repaint this component on the screen.
		this.repaint();
	}

	/**
	 * Draws an oval on the screen. Because this method is continuously called
	 * when the mouse is dragged, it gives the appearance that the "Draw Oval"
	 * function on the whiteboard draws a resizable oval.
	 * 
	 * This method assumes that the oval is being drawn for the first time. As
	 * such, no redos after possible after this operation.
	 * 
	 * @param oval
	 *            the oval to draw on the screen; includes thickness and color
	 *            information
	 */
	private void drawOval(Oval oval) {
		Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();

		// redraw all the drawinObjects on the canvas in order for the circle to appear as if it's resizing
		fillWithWhite();
		for (int i = 0; i < canvasModel.getUndoIndex(); i++) {
			DrawingObject currentDrawingObject = canvasModel.getIthDrawingObject(i);
			drawDrawingObject(currentDrawingObject);
		}
		
		// get information about the oval to be drawn
		int width = oval.getWidth();
		int height = oval.getHeight();
		int x = oval.getTopLeftX();
		int y = oval.getTopLeftY();
		String color = oval.getColor();
		String thickness = oval.getThickness();

		// get and set the color and thickness of the oval
	    g.setStroke(thicknesses.get(thickness));
	    g.setColor(colors.get(color));

		g.drawOval(x, y, width, height);
		
		// after a series of undo operations, if a user begins to draw again,
		// all edits after the current edit are discarded
		canvasModel.preventRedoAfterThisEdit();

		this.repaint();
	}

	/*
	 * Helper method that adds the mouse listener that supports the user's freehand drawing.
	 * Called by the constructor.
	 */
	private void addDrawingController() {
		DrawingController controller = new DrawingController();
		addMouseListener(controller);
		addMouseMotionListener(controller);
	}

	/**
	 * Undos the last DrawingObject completed on the canvas.
	 */
	public void undo() {
	    //TODO: send message to server
		fillWithWhite();
		for (int i = 0; i < canvasModel.getUndoIndex() - 1; i++) {
			DrawingObject currentDrawingObject = canvasModel.getIthDrawingObject(i);
			drawDrawingObject(currentDrawingObject);
		}

		// prevent the index from going below 0.
		if (canvasModel.getUndoIndex() > 0) {
			canvasModel.decrementIndex();
		}
	}
	/**
	 * Send the undo/redo message to the server.
	 * Called when the user clicks the undo or redo button on the toolbar.
	 * @param undo - true if undo message, false if redo message
	 */
	protected void sendUndoRedoMessage(boolean undo){
	    if (undo){
	        out.println("undo " + user.getUserID() + " " + user.getWhiteboardID());
	    }
	    else{

	        out.println("redo " + user.getUserID() + " " + user.getWhiteboardID());
	    }
	}
	/**
	 * Redraws the last DrawingObject to have been undone from the canvas.
	 */
	public void redo() {
		if (canvasModel.getUndoIndex() < canvasModel.getListSize()) {
			DrawingObject currentDrawingObject = canvasModel.getIthDrawingObject(canvasModel.getUndoIndex());
			drawDrawingObject(currentDrawingObject);
			canvasModel.incrementIndex();
		}
	}
	
	/**
	 * Redraws a drawingObject onto the screen by checking the type of
	 * particular drawingObject and calling the appropriate methods to redraw
	 * the type.
	 * 
	 * Draws the specified drawing object.
	 * 
	 * @param d
	 *            the drawingObject to redraw onto the canvas
	 */
	public void drawDrawingObject(DrawingObject d) {
		if (d instanceof Freehand) {
			Freehand freehand = (Freehand) d;
			redrawLinesInFreehand(freehand);
		}
		else if (d instanceof Oval) {
			Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();
			Oval oval = (Oval) d;
			
			// get and set the color and thickness
			String thickness = oval.getThickness();
			String color = oval.getColor();
			
		    g.setStroke(thicknesses.get(thickness));
		    g.setColor(colors.get(color));
			
			g.drawOval(oval.getTopLeftX(), oval.getTopLeftY(), oval.getWidth(), oval.getHeight());
		}
		
		this.repaint();
	}
	
	/**
	 * This method is used to redaw the lines in a Freehand object.
	 * 
	 * @param freehand
	 *            the Freehand object whose lines are to be redrawn onto the GUI
	 */
	private void redrawLinesInFreehand(Freehand freehand) {
	    //TODO: send message to server
		for (Line l : freehand.getLineList()) {
			int x1 = l.getX1();
			int x2 = l.getX2();
			int y1 = l.getY1();
			int y2 = l.getY2();
			String color = l.getColor();
			String thickness = l.getThickness();
			this.drawLineSegment(x1, y1, x2, y2, color, thickness, true);	
		}
	}
	
	/**
	 * DrawingController handles the user's freehand drawing.
	 */
	private class DrawingController implements MouseListener,
			MouseMotionListener {
		// store the coordinates of the last mouse event, so we can
		// draw a line segment from that last point to the point of the next
		// mouse event.
		private int lastX, lastY;
		private DrawingObject currentDrawingObject;
		private int shapeStartX;
		private int shapeStartY;

		/*
		 * When mouse button is pressed down, start drawing.
		 */
		public void mousePressed(MouseEvent e) {
			if (isDrawingOval) {
				shapeStartX = e.getX();
				shapeStartY = e.getY();
			} else {
				lastX = e.getX();
				lastY = e.getY();
				currentDrawingObject = new Freehand(new ArrayList<Line>());
			}
		}

		/*
		 * When mouse moves while a button is pressed down, draw a line segment.
		 */
		public void mouseDragged(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			
			// configure the color and thickness
		    String color = "b";
			String thickness = "m";
			for (Entry<String, Color> entry: colors.entrySet()){
			    if (entry.getValue().equals(user.getToolbar().getColor())){
			        color = entry.getKey();
			    }
			}
            for (Entry<String, Stroke> entry: thicknesses.entrySet()){
                    if (entry.getValue().equals(user.getToolbar().getStroke())){
                        thickness = entry.getKey();
                }
            }
            
			if (isDrawingOval) {
				Oval oval = new Oval(shapeStartX, shapeStartY, x, y, color, thickness);
				currentDrawingObject = oval;
				drawOval(oval);
			} else if (currentDrawingObject instanceof Freehand) {
	                Freehand freehand = (Freehand) currentDrawingObject;
	                freehand.getLineList()
					    .add(new Line(lastX, lastY, x, y, color,
								thickness));
	                drawLineSegment(lastX, lastY, x, y, color,
						thickness, false);
				lastX = x;
				lastY = y;
			}
		}

		public void mouseMoved(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
	        out.println("draw "+ currentDrawingObject.toString() + " " + user.getUserID() + " " + user.getWhiteboardID());
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}
}
