package nl.tue.declare.appl.worklist.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class GraphComponent extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int ARROW_LENGTH = 5;
	private static final int ARROW_WIDTH = 3;

	private static final int OUTER = 3;
	private static final int INNER = 3;
	private static final int GRACE = 10;

	private static final int BAR_WIDTH = 80;

	
	protected List<String> labels;
	protected List<Double> values;
	protected List<Color> fills, strokes;

	private double step = 10;

	private double max = 100;
	
	public GraphComponent() {
		labels = new ArrayList<String>();
		values = new ArrayList<Double>();
		fills = new ArrayList<Color>();
		strokes = new ArrayList<Color>();
		setBorder(BorderFactory.createEmptyBorder(5, 50, 10, 5));
	}
	
	
	public void paintComponent(Graphics g) {
		Insets insets = getInsets();
		g.translate(getX(), getY());
		int w = getWidth() - insets.left - insets.right;
		int h = getHeight() - insets.top - insets.bottom;
		g.translate(insets.left, insets.top);
		Graphics2D g2d = null;
		if (g instanceof Graphics2D) g2d = (Graphics2D) g;
		
		double max = getMax();
		double step = getStep();
		
		g.setColor(getForeground());
		
		// Draw y-labels and dohickeys
		for (double i = step; i <= max; i += step) {
			g.drawLine(-OUTER, scaleY(h, max, i), INNER, scaleY(h, max, i));
			String label = "" + i + getUnit();
			Rectangle2D indent = g.getFontMetrics().getStringBounds(label, g);
			g.drawString(label, (int) (0 - OUTER - 5 - indent.getWidth()), scaleY(h, max, i) + g.getFontMetrics().getHeight() / 2);
		}
		
		// Draw x-dohickeys
		for (int i = 1; i < labels.size(); i++) {
			g.drawLine( scaleX(w, labels.size(), i), h + OUTER,scaleX(w, labels.size(), i), h - INNER);
		}

		// Draw bars
		int width = scaleX(w, labels.size(), BAR_WIDTH / 100.0);
		for (int i = 0; i < labels.size(); i++) {
			String label = labels.get(i);
			double value = values.get(i);
			Color stroke = strokes.get(i);
			Color fill = fills.get(i);
			if (stroke == null) stroke = getForeground();
			if (fill == null) fill = getBackground();
			int height = scaleX(h, getMax(), value);
			int x = scaleX(w, labels.size(), i + (100 - BAR_WIDTH) / 2.0 / 100.0);
			int y = h - height;
			if (g2d != null) {
				g2d.setPaint(new GradientPaint(x, y, stroke.brighter().brighter(), x + width, h + 1, stroke));
				g2d.fill(new Rectangle(x, y, width + 1, height + 1));
				g2d.setPaint(new GradientPaint(x, y, fill, x + width, h + 1, fill.brighter()));
				g2d.fill(new Rectangle(x + 1, y + 1, width -1 , height - 1));
			} else {
				g.setColor(fill);
				g.fillRect(x, y, width + 1, height);
				g.setColor(stroke);
				g.drawRect(x, y, width + 1, height);
			}
			
			if (g2d != null) {
			g.setColor(getForeground());
			AffineTransform transform = g2d.getTransform();
			double textLength = g2d.getFontMetrics().getStringBounds(label, g2d).getWidth();
			int tx = scaleX(w, labels.size(), i + 0.5) + g2d.getFontMetrics().getHeight() / 2 - 2;
			int ty = height >= textLength + 10?h - 5:h - 5 - height;
			if (ty - textLength < 0) ty = h - 10;
			System.out.println(tx + ", " + ty);
			g2d.translate(tx, ty);
			g2d.rotate(-Math.PI/2);
			g2d.drawString(label, 0, 0);
			g2d.setTransform(transform);
			}
		}
		System.out.println("======================");
		
		g.setColor(getForeground());

		// Draw coordinate system
		g.drawLine(0, h, w, h);
		g.drawLine(w, h, w - ARROW_LENGTH, h - ARROW_WIDTH);
		g.drawLine(w, h, w - ARROW_LENGTH, h + ARROW_WIDTH);
		g.drawLine(0, h, 0, 0);
		g.drawLine(0, 0, -ARROW_WIDTH, ARROW_LENGTH);
		g.drawLine(0, 0, ARROW_WIDTH, ARROW_LENGTH);
			}


	private String getUnit() {
		return "%";
	}


	private int scaleY(int h, double max, double i) {
		return (int) (h - ((h - GRACE) * i / max));
	}
	private int scaleX(int h, double max, double i) {
		return (int) ((h - GRACE) * i / max);
	}
	
	private double getStep() {
		return step;
	}

	private double getMax() {
		return max;
	}

	public void addValue(String label, double value, Color fill, Color stroke) {
		labels.add(label);
		values.add(value);
		fills.add(fill);
		strokes.add(stroke);
	}
	
	public void addValue(String label, double value) {
		addValue(label, value, null, null);
		repaint();
	}


	/**
	 * @param max the max to set
	 */
	public void setMax(double max) {
		this.max = max;
		repaint();
	}


	/**
	 * @param step the step to set
	 */
	public void setStep(double step) {
		this.step = step;
		repaint();
	}
}
