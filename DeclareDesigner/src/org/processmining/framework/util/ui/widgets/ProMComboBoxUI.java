package org.processmining.framework.util.ui.widgets;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

public class ProMComboBoxUI extends BasicComboBoxUI {
	public static ComponentUI createUI(final JComponent c) {
		return new ProMComboBoxUI((JComboBox) c);
	}

	private final JComboBox component;

	public ProMComboBoxUI(final JComboBox c) {
		component = c;
	}

	@Override
	public void configureArrowButton() {
		super.configureArrowButton();
		arrowButton.setBorder(BorderFactory.createEmptyBorder());
	}

	@Override
	public void paintCurrentValueBackground(final Graphics g, final Rectangle bounds, final boolean hasFocus) {
	}

	@Override
	protected void configureEditor() {
		super.configureEditor();
		if (editor instanceof JComponent) {
			((JComponent) editor).setBorder(BorderFactory.createEmptyBorder());
		}
	}

	@Override
	protected JButton createArrowButton() {
		final JButton button = new BasicArrowButton(SwingConstants.SOUTH, WidgetColors.COLOR_ENCLOSURE_BG,
				WidgetColors.COLOR_ENCLOSURE_BG, WidgetColors.COLOR_LIST_FG, WidgetColors.COLOR_ENCLOSURE_BG);
		button.setName("ComboBox.arrowButton");
		return button;
	}

	@Override
	protected ComboPopup createPopup() {
		final BasicComboPopup result = new ProMComboBoxPopup(component);
		return result;
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();
		comboBox.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
	}

	@Override
	protected Rectangle rectangleForCurrentValue() {
		final int width = comboBox.getWidth();
		final int height = comboBox.getHeight();
		final Insets insets = getInsets();
		int buttonSize = height - (insets.top + insets.bottom);
		if (arrowButton != null) {
			buttonSize = arrowButton.getWidth();
		}
		return new Rectangle(insets.left + 3, insets.top - 1, width
				- (insets.left + insets.right + buttonSize + 3 + 3 + 10), height - (insets.top + insets.bottom) + 1);
	}

}
