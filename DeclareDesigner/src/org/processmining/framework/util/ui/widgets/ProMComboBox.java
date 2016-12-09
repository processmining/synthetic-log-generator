package org.processmining.framework.util.ui.widgets;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JComboBox;

/**
 * @author michael
 * 
 */
public class ProMComboBox extends JComboBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static <T> Object[] toArray(final Iterable<T> values) {
		final ArrayList<T> valueList = new ArrayList<T>();
		for (final T value : values) {
			valueList.add(value);
		}
		return valueList.toArray();
	}

	private final BorderPanel borderPanel;
	private final BorderPanel buttonPanel;

	/**
	 * @param <T>
	 * @param values
	 */
	public <T> ProMComboBox(final Iterable<T> values) {
		this(ProMComboBox.toArray(values));
	}

	/**
	 * @param values
	 */
	public ProMComboBox(final Object[] values) {
		super(values);
		borderPanel = new BorderPanel(15, 3);
		borderPanel.setOpaque(true);
		borderPanel.setBackground(WidgetColors.COLOR_LIST_BG);
		borderPanel.setForeground(WidgetColors.COLOR_ENCLOSURE_BG);
		buttonPanel = new BorderPanel(15, 3);
		buttonPanel.setOpaque(true);
		buttonPanel.setBackground(WidgetColors.COLOR_ENCLOSURE_BG);
		buttonPanel.setForeground(WidgetColors.COLOR_ENCLOSURE_BG);
		setOpaque(false);
		setBackground(WidgetColors.COLOR_LIST_BG);
		setForeground(WidgetColors.COLOR_LIST_FG);
		setMinimumSize(new Dimension(200, 30));
		setMaximumSize(new Dimension(1000, 30));
		setPreferredSize(new Dimension(1000, 30));

		setUI(new ProMComboBoxUI(this));
	}

	@Override
	public void paintComponent(final Graphics g) {
		if (!Boolean.TRUE.equals(getClientProperty("JComboBox.isTableCellEditor"))) {
		final Rectangle bounds = getBounds();
		buttonPanel.setBounds(bounds);
		buttonPanel.paintComponent(g);
		final Dimension d = new Dimension();
		d.setSize(bounds.getWidth() - bounds.getHeight(), bounds.getHeight());
		bounds.setSize(d);
		borderPanel.setBounds(bounds);
		borderPanel.paintComponent(g);
		}
		super.paintComponent(g);
	}
}
