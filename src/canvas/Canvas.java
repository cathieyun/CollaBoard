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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import whiteboard.Freehand;
import whiteboard.Line;


/**
 * Canvas represents a drawing surface that allows the user to draw on it
 * freehand, with the mouse.
 */

public class Canvas extends JPanel implements ItemListener {
	// image where the user's drawing is stored
	private Image drawingBuffer;
	private boolean erase;
	private ArrayList<Freehand> freehandList = new ArrayList<Freehand>();
	int freehandListUndoIndex = 0;
    public static Stroke SMALL = new BasicStroke(5);
    public static Stroke MED = new BasicStroke(15);
    public static Stroke LARGE = new BasicStroke(25);

	/**
	 * Make a canvas.
	 * 
	 * @param width
	 *            width in pixels
	 * @param height
	 *            height in pixels
	 */
	public Canvas(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		addDrawingController();
		// note: we can't call makeDrawingBuffer here, because it only
		// works *after* this canvas has been added to a window. Have to
		// wait until paintComponent() is first called.

		erase = false;
		JToggleButton eraseButton = new JToggleButton("Erase");
		eraseButton.setLocation(0, 10);
		eraseButton.setSize(50, 100);
		eraseButton.addItemListener(this);
		this.add(eraseButton);

		JButton undoButton = new JButton("Undo");
		undoButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.out.println("Freehand List size: " + freehandList.size());
				System.out.println("freehandListUndoIndex: " + freehandListUndoIndex);
				undo();
			}
		});
		
		undoButton.setLocation(0, 20);
		this.add(undoButton);

		JButton redoButton = new JButton("Redo");
		redoButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.out.println("Freehand List size: " + freehandList.size());
				System.out.println("freehandListUndoIndex: " + freehandListUndoIndex);
				redo();
			}
		});
		
		redoButton.setLocation(0, 40);
		this.add(redoButton);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		erase = !erase;
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
		drawSmile();
	}

	/*
	 * Make the drawing buffer entirely white.
	 */
	private void fillWithWhite() {
		final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		// IMPORTANT! every time we draw on the internal drawing buffer, we
		// have to notify Swing to repaint this component on the screen.
		this.repaint();
	}

	/*
	 * Draw a happy smile on the drawing buffer.
	 */
	private void drawSmile() {
		final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();

		// all positions and sizes below are in pixels
		final Rectangle smileBox = new Rectangle(20, 20, 100, 100); // x, y,
																	// width,
																	// height
		final Point smileCenter = new Point(smileBox.x + smileBox.width / 2,
				smileBox.y + smileBox.height / 2);
		final int smileStrokeWidth = 3;
		final Dimension eyeSize = new Dimension(9, 9);
		final Dimension eyeOffset = new Dimension(smileBox.width / 6,
				smileBox.height / 6);

		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(smileStrokeWidth));

		// draw the smile -- an arc inscribed in smileBox, starting at -30
		// degrees (southeast)
		// and covering 120 degrees
		g.drawArc(smileBox.x, smileBox.y, smileBox.width, smileBox.height, -30,
				-120);

		// draw some eyes to make it look like a smile rather than an arc
		for (int side : new int[] { -1, 1 }) {
			g.fillOval(smileCenter.x + side * eyeOffset.width - eyeSize.width
					/ 2, smileCenter.y - eyeOffset.height - eyeSize.width / 2,
					eyeSize.width, eyeSize.height);
		}

		// IMPORTANT! every time we draw on the internal drawing buffer, we
		// have to notify Swing to repaint this component on the screen.
		this.repaint();
	}

	/*
	 * Draw a line between two points (x1, y1) and (x2, y2), specified in pixels
	 * relative to the upper-left corner of the drawing buffer.
	 */
	private void drawLineSegment(int x1, int y1, int x2, int y2, String color, String thickness, boolean areUndoingOrRedoing) {
		Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();
		if (color == "Black") {
			g.setColor(Color.BLACK);
		} else if (color == "White") {
			g.setStroke(new BasicStroke(10));
			g.setColor(Color.WHITE);
		}
		
		// after a series of undo operations, if a user begins to draw again,
		// all edits after the current edit are discarded
		if (!areUndoingOrRedoing) {
			for (int i = freehandList.size() - 1; freehandListUndoIndex < freehandList.size(); i--) {
				System.out.println("My index is: " + i);
				System.out.println("My array size is: " + freehandList.size());
				freehandList.remove(i);
			}
		}

		g.drawLine(x1, y1, x2, y2);

		// IMPORTANT! every time we draw on the internal drawing buffer, we
		// have to notify Swing to repaint this component on the screen.
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

	private void undo() {
		fillWithWhite();
		
		for (int i = 0; i < freehandListUndoIndex - 1; i++) {
			Freehand freehand = freehandList.get(i);
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
		
		// prevent the index from going below 0.
		if (freehandListUndoIndex > 0) {
			--freehandListUndoIndex;
		}
	}
	
	private void redo() {
		if (freehandListUndoIndex < freehandList.size()) {
			Freehand freehand = freehandList.get(freehandListUndoIndex);
			for (Line l : freehand.getLineList()) {
				int x1 = l.getX1();
				int x2 = l.getX2();
				int y1 = l.getY1();
				int y2 = l.getY2();
				String color = l.getColor();
				String thickness = l.getThickness();
				this.drawLineSegment(x1, y1, x2, y2, color, thickness, true);
			}
			
			++freehandListUndoIndex;
		}
	}

	/*
	 * DrawingController handles the user's freehand drawing.
	 */
	private class DrawingController implements MouseListener,
			MouseMotionListener {
		// store the coordinates of the last mouse event, so we can
		// draw a line segment from that last point to the point of the next
		// mouse event.
		private int lastX, lastY;
		private Freehand freehand;

		/*
		 * When mouse button is pressed down, start drawing.
		 */
		public void mousePressed(MouseEvent e) {
			freehand = new Freehand(new ArrayList<Line>());
			lastX = e.getX();
			lastY = e.getY();
		}

		/*
		 * When mouse moves while a button is pressed down, draw a line segment.
		 */
		public void mouseDragged(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			if (!erase) {
				freehand.getLineList().add(new Line(lastX, lastY, x, y, "Black", "Medium"));
				drawLineSegment(lastX, lastY, x, y, "Black", "DUMMYTHICKNESS_CHANGEME", false);
			}
			else if (erase) {
				freehand.getLineList().add(new Line(lastX, lastY, x, y, "White", "Medium"));
				drawLineSegment(lastX, lastY, x, y, "White", "DUMMYTHICKNESS_CHANGEME", false);
			}
			lastX = x;
			lastY = y;
		}

		// Ignore all these other mouse events.
		public void mouseMoved(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
			freehandList.add(freehand);
			++freehandListUndoIndex;
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
				Canvas canvas = new Canvas(800, 600);
				window.add(canvas, BorderLayout.CENTER);
				window.pack();
				window.setVisible(true);
			}
		});
	}
}
