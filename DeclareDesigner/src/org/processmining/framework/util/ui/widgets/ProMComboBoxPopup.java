package org.processmining.framework.util.ui.widgets;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JScrollBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicComboPopup;

import com.fluxicon.slickerbox.ui.SlickerScrollBarUI;

/**
 * @author michael
 * 
 */
public class ProMComboBoxPopup extends BasicComboPopup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProMComboBoxPopup(final JComboBox combo) {
		super(combo);
		setBorder(BorderFactory.createLineBorder(WidgetColors.COLOR_ENCLOSURE_BG, 3));
		setOpaque(true);
		setBackground(WidgetColors.COLOR_LIST_BG);
	}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
	}

	@Override
	protected void configureList() {
		super.configureList();
		list.setBackground(WidgetColors.COLOR_LIST_BG);
		list.setForeground(WidgetColors.COLOR_LIST_FG);
		list.setSelectionBackground(WidgetColors.COLOR_LIST_SELECTION_BG);
		list.setSelectionForeground(WidgetColors.COLOR_LIST_SELECTION_FG);
	}

	@Override
	protected void configureScroller() {
		super.configureScroller();
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		final JScrollBar vBar = scroller.getVerticalScrollBar();
		vBar.setUI(new SlickerScrollBarUI(vBar, new Color(0, 0, 0, 0), new Color(160, 160, 160),
				WidgetColors.COLOR_NON_FOCUS, 4, 12));
		vBar.setOpaque(true);
		vBar.setBackground(WidgetColors.COLOR_ENCLOSURE_BG);
	}

}
