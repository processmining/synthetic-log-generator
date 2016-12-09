package org.processmining.framework.util.ui.widgets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.fluxicon.slickerbox.components.RoundedPanel;
import com.fluxicon.slickerbox.ui.SlickerScrollBarUI;

/**
 * List (JList) with SlickerBox L&F
 * 
 * @author mwesterg
 * 
 */
public class ProMList extends RoundedPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JList jList;

	public ProMList(final String title) {
		super(10, 5, 0);
		jList = new JList();
		setup(title);
	}

	public ProMList(final String title, final ListModel providers) {
		super(10, 5, 0);
		jList = new JList(providers);
		setup(title);
	}

	private void setup(final String title) {
		jList.setBackground(WidgetColors.COLOR_LIST_BG);
		jList.setForeground(WidgetColors.COLOR_LIST_FG);
		jList.setSelectionBackground(WidgetColors.COLOR_LIST_SELECTION_BG);
		jList.setSelectionForeground(WidgetColors.COLOR_LIST_SELECTION_FG);

		final JScrollPane scroller = new JScrollPane(jList);
		scroller.setOpaque(false);
		scroller.setBorder(BorderFactory.createEmptyBorder());
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		final JScrollBar vBar = scroller.getVerticalScrollBar();
		vBar.setUI(new SlickerScrollBarUI(vBar, new Color(0, 0, 0, 0), new Color(160, 160, 160),
				WidgetColors.COLOR_NON_FOCUS, 4, 12));
		vBar.setOpaque(true);
		vBar.setBackground(WidgetColors.COLOR_ENCLOSURE_BG);

		final JLabel providersLabel = new JLabel(title);
		providersLabel.setOpaque(false);
		providersLabel.setForeground(WidgetColors.COLOR_LIST_SELECTION_FG);
		providersLabel.setFont(providersLabel.getFont().deriveFont(13f));
		providersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		providersLabel.setHorizontalAlignment(SwingConstants.CENTER);
		providersLabel.setHorizontalTextPosition(SwingConstants.CENTER);

		setBackground(WidgetColors.COLOR_ENCLOSURE_BG);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(providersLabel);
		add(Box.createVerticalStrut(8));
		add(scroller);
		setMinimumSize(new Dimension(200, 100));
		setMaximumSize(new Dimension(1000, 1000));
		setPreferredSize(new Dimension(1000, 200));
	}

	/**
	 * @return
	 */
	public Object[] getSelectedValues() {
		return jList.getSelectedValues();
	}
	
	/**
	 * @param selectionMode
	 */
	public void setSelectionMode(int selectionMode) {
		jList.setSelectionMode(selectionMode);
	}
	
	/**
	 * @param selectedValues
	 */
	public void setSelection(Object... selectedValues) {
		jList.clearSelection();
		for (Object value : selectedValues)
		jList.setSelectedValue(value, true);
	}
	
	/**
	 * @param selectedValues
	 */
	public void setSelection(Iterable<Object> selectedValues) {
		jList.clearSelection();
		for (Object value : selectedValues)
		jList.setSelectedValue(value, true);		
	}
}
