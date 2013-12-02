package canvas;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Stroke;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import client.ClientCanvasModel;
import client.User;

import canvas.Freehand;
import canvas.Line;
import canvas.Oval;

/**
 * Canvas represents a drawing surface that allows the user to draw on it
 * freehand, with the mouse.
 */
//TODO: get the output stream for the client, and have it send messages to the server
//according to the server grammar when it draws, undos, or redos.
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
        temp.put("b", Color.BLUE);
        temp.put("y", Color.YELLOW);
        temp.put("r", Color.RED);
        temp.put("o", Color.ORANGE);
        temp.put("blk", Color.BLACK);
        temp.put("w", Color.WHITE);
        temp.put("g", Color.GREEN);
        temp.put("m", Color.MAGENTA);
        colors = Collections.unmodifiableMap(temp);
    }
    static Map<String,Stroke> thicknesses;
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
    public Canvas(int width, int height, final ClientCanvasModel canvasModel2, User user, OutputStream outputStream) {

        this.user = user;
        this.out = new PrintWriter(outputStream);
        this.canvasModel = canvasModel2;
        this.setPreferredSize(new Dimension(width, height));
        addDrawingController();
        // note: we can't call makeDrawingBuffer here, because it only
        // works *after* this canvas has been added to a window. Have to
        // wait until paintComponent() is first called.

        configureButtons();
    }
    
    private void configureButtons() {

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                undo();
//                System.out.println("Undo Action performed.");
//                System.out.println("Freehand List size: " + canvasModel.getListSize());
//                System.out.println("freehandListUndoIndex: " + canvasModel.getDrawingObjectListUndoIndex());
            }
        });
        
        undoButton.setLocation(0, 20);
        this.add(undoButton);

        JButton redoButton = new JButton("Redo");
        redoButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                redo();
//                System.out.println("Redo Action performed.");
//                System.out.println("Freehand List size: " + canvasModel.getListSize());
//                System.out.println("freehandListUndoIndex: " + canvasModel.getDrawingObjectListUndoIndex());
            }
        });
        
        redoButton.setLocation(0, 40);
        this.add(redoButton);
        
		JButton drawOvalButton = new JButton("Draw Oval");
		drawOvalButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
//				System.out.println("drawingObjectList size: "
//						+ canvasModel.getListSize());
//				System.out.println("drawingObjectListUndoIndex: "
//						+ canvasModel.getDrawingObjectListUndoIndex());
				isDrawingOval = !isDrawingOval;
			}
		});
		
		drawOvalButton.setLocation(0, 60);
        this.add(drawOvalButton);
    }
    
    public CanvasModel getCanvasModel(){
        return canvasModel;
    }
    /**
     * Draws the freehand defined by the input.
     * @param points - list of x and y coordinates (alternating)
     * @param color - color of the freehand
     * @param stroke - stroke of the freehand
     */
    public void drawFreehand(ArrayList<Integer> points, String color, String stroke){
        for (int i = 0; i < points.size(); i=i+2){
            drawLineSegment(points.get(i), points.get(i+1), points.get(i+2), points.get(i+3), color, stroke, false);
            //also add to clientcanvasModel
        }
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
    private void fillWithWhite() {
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
		// if (color == "Black") {
		// g.setColor(Color.BLACK);
		// } else if (color == "White") {
		// g.setStroke(new BasicStroke(10));
		// g.setColor(Color.WHITE);
		// }
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

	private void drawOval(int x, int y, int width, int height, boolean areUndoingOrRedoing) {
		fillWithWhite();
		Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();

		// redraw all the drawinObjects on the canvas in order for the circle to appear as if it's resizing
		for (int i = 0; i < canvasModel.getDrawingObjectListUndoIndex(); i++) {
			DrawingObject currentDrawingObject = canvasModel.getIthDrawingObject(i);
			redrawDrawingObject(currentDrawingObject);
		}

		g.drawOval(x, y, width, height);
		
		// after a series of undo operations, if a user begins to draw again,
		// all edits after the current edit are discarded
		if (!areUndoingOrRedoing) {
			canvasModel.preventRedoAfterThisEdit();
		}

		this.repaint();
	}

	/*
	 * Add the mouse listener that supports the user's freehand drawing.
	 */
	private void addDrawingController() {
		DrawingController controller = new DrawingController();
		addMouseListener(controller);
		addMouseMotionListener(controller);
	}

	/**
	 * Undos the last DrawingObject completed on the canvas.
	 */
	private void undo() {
	    //TODO: send message to server
		fillWithWhite();
		for (int i = 0; i < canvasModel.getDrawingObjectListUndoIndex() - 1; i++) {
			DrawingObject currentDrawingObject = canvasModel.getIthDrawingObject(i);
			redrawDrawingObject(currentDrawingObject);
		}

		// prevent the index from going below 0.
		if (canvasModel.getDrawingObjectListUndoIndex() > 0) {
			canvasModel.getAndDecrementIndex();
		}
	}

	/**
	 * Redraws the last DrawingObject to have been undone from the canvas.
	 */
	private void redo() {
		if (canvasModel.getDrawingObjectListUndoIndex() < canvasModel.getListSize()) {
			DrawingObject currentDrawingObject = canvasModel.getIthDrawingObject(canvasModel.getDrawingObjectListUndoIndex());
			redrawDrawingObject(currentDrawingObject);
			canvasModel.getAndIncrementIndex();
		}
	}
	
	/**
	 * Redraws a drawingObject onto the screen by checking the type of
	 * particular drawingObject and calling the appropriate methods to redraw
	 * the type.
	 * 
	 * @param d
	 *            the drawingObject to redraw onto the canvas
	 */
	private void redrawDrawingObject(DrawingObject d) {
		if (d instanceof Freehand) {
			Freehand freehand = (Freehand) d;
			drawLinesInFreehand(freehand);
		}
		else if (d instanceof Oval) {
			Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();
			Oval oval = (Oval) d;
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
	private void drawLinesInFreehand(Freehand freehand) {
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
				currentDrawingObject = new Oval(shapeStartX, shapeStartY, shapeStartX, shapeStartY);
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
			if (isDrawingOval) {
				Oval oval = new Oval(shapeStartX, shapeStartY, x, y);
				currentDrawingObject = oval;
				int width = oval.getWidth();
				int height = oval.getHeight();
				int x1 = oval.getTopLeftX();
				int y1 = oval.getTopLeftY();
				drawOval(x1, y1, width, height, false);
			} else {
				if (currentDrawingObject instanceof Freehand) {
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
	                Freehand freehand = (Freehand) currentDrawingObject;
	                freehand.getLineList()
					    .add(new Line(lastX, lastY, x, y, color,
								thickness));
	                drawLineSegment(lastX, lastY, x, y, color,
						thickness, false);
				}
				lastX = x;
				lastY = y;
			}
		}

		public void mouseMoved(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
			canvasModel.addDrawingObject(currentDrawingObject);
			canvasModel.getAndIncrementIndex();
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}

	/*
	 * Main program. Make a window containing a Canvas.
	 */
	public static void main(String[] args) {
		// set up the UI (on the event-handling thread)
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame window = new JFrame("Freehand Canvas");
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setLayout(new BorderLayout());
				Canvas canvas = new Canvas(800, 600, new ClientCanvasModel(),
						new User(1), new DataOutputStream(null));
				window.add(canvas, BorderLayout.CENTER);
				window.pack();
				window.setVisible(true);
			}
		});
	}
}
