package org.processmining.framework.util.ui.widgets;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Panel which adds a border around its contents
 * 
 * @author mwesterg
 * 
 */
public class BorderPanel extends JPanel {

	private final int size;
	private final int borderWidth;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BorderPanel(final int size, final int borderWidth) {
		super();
		this.size = size;
		this.borderWidth = borderWidth;
		setBorder(BorderFactory.createEmptyBorder(borderWidth, size, borderWidth, size));
	}

	@Override
	protected void paintComponent(final Graphics g) {
		final Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (isOpaque()) {
			g2d.setColor(getBackground());
			g2d.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, size * 2, size * 2);
		}
		if (borderWidth > 0) {
			g2d.setColor(getForeground());
			g2d.setStroke(new BasicStroke(borderWidth));
			g2d.drawRoundRect(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth - 1, getHeight() - borderWidth
					- 1, size * 2, size * 2);
		}
	}
}
