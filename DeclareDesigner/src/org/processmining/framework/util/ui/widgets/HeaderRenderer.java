package org.processmining.framework.util.ui.widgets;

import java.awt.Component;
import java.awt.ComponentOrientation;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Renderer for headers for tables
 * 
 * @author mwesterg
 * 
 */
public class HeaderRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HeaderRenderer() {
		setHorizontalAlignment(SwingConstants.CENTER);
		setOpaque(true);

		// This call is needed because DefaultTableCellRenderer calls setBorder()
		// in its constructor, which is executed after updateUI()
		setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
	}

	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean selected,
			final boolean focused, final int row, final int column) {
		final JTableHeader h = table != null ? table.getTableHeader() : null;

		if (h != null) {
			setEnabled(h.isEnabled());
			setComponentOrientation(h.getComponentOrientation());

			setForeground(h.getForeground());
			setBackground(h.getBackground());
			setFont(h.getFont());
		} else {
			/*
			 * Use sensible values instead of random leftover values from the
			 * last call
			 */
			setEnabled(true);
			setComponentOrientation(ComponentOrientation.UNKNOWN);

			setForeground(UIManager.getColor("TableHeader.foreground"));
			setBackground(UIManager.getColor("TableHeader.background"));
			setFont(UIManager.getFont("TableHeader.font"));
		}

		setValue(value);

		return this;
	}

	@Override
	public void updateUI() {
		super.updateUI();
		setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
	}
}