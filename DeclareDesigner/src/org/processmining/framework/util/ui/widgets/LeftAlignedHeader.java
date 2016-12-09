package org.processmining.framework.util.ui.widgets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Header aligned to the left
 * 
 * @author mwesterg
 * 
 */
public class LeftAlignedHeader extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LeftAlignedHeader(final String title) {
		final JLabel hLabel = new JLabel(title);
		hLabel.setOpaque(false);
		hLabel.setForeground(WidgetColors.HEADER_COLOR);
		hLabel.setFont(hLabel.getFont().deriveFont(15f));
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(hLabel);
		add(Box.createHorizontalGlue());
	}

}
