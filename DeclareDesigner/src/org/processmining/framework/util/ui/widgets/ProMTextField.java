package org.processmining.framework.util.ui.widgets;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JTextField;

/**
 * TextField with SlickerBox L&F
 * 
 * @author mwesterg
 * 
 */
public class ProMTextField extends BorderPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JTextField textField;

	public ProMTextField() {
		super(15, 3);
		setLayout(new BorderLayout());
		setOpaque(true);
		setBackground(WidgetColors.COLOR_LIST_BG);
		setForeground(WidgetColors.COLOR_ENCLOSURE_BG);
		textField = new JTextField();
		add(textField, BorderLayout.CENTER);
		textField.setBorder(null);
		textField.setOpaque(true);
		textField.setBackground(WidgetColors.COLOR_LIST_BG);
		textField.setForeground(WidgetColors.COLOR_LIST_SELECTION_FG);
		textField.setSelectionColor(WidgetColors.COLOR_LIST_SELECTION_BG);
		textField.setSelectedTextColor(WidgetColors.COLOR_LIST_SELECTION_FG);
		textField.setCaretColor(WidgetColors.COLOR_LIST_SELECTION_FG);
		setMinimumSize(new Dimension(200, 30));
		setMaximumSize(new Dimension(1000, 30));
		setPreferredSize(new Dimension(1000, 30));
	}

	public ProMTextField(final String initial) {
		this();
		setText(initial);
	}

	public String getText() {
		return textField.getText();
	}

	public void setEditable(final boolean editable) {
		textField.setEditable(editable);
	}

	public void setText(final String text) {
		textField.setText(text);
	}
}
