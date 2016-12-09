package org.processmining.framework.util.ui.widgets;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;

import com.fluxicon.slickerbox.components.RoundedPanel;

/**
 * Panel with a header
 * 
 * @author mwesterg
 * 
 */
public class ProMHeaderPanel extends RoundedPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProMHeaderPanel(final String title) {
		super(15, 0, 3);
		setMinimumSize(new Dimension(200, 20));
		setMaximumSize(new Dimension(1000, 1000));
		setPreferredSize(new Dimension(200, 300));
		setBackground(WidgetColors.PROPERTIES_BACKGROUND);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		if (title != null) {
			add(new LeftAlignedHeader(title));
			add(Box.createVerticalStrut(15));
		}
	}

}
