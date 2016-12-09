package org.processmining.framework.util.ui.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.fluxicon.slickerbox.components.RoundedPanel;
import com.fluxicon.slickerbox.ui.SlickerScrollBarUI;

/**
 * TextAres with SlickerBox L&F
 * 
 * @author mwesterg
 * 
 */
public class ProMTextArea extends RoundedPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JTextArea textArea;

	public ProMTextArea() {
		super(10, 5, 0);
		textArea = new JTextArea();
		final JScrollPane logarea = new JScrollPane(textArea);
		textArea.setOpaque(true);
		textArea.setBackground(WidgetColors.COLOR_ENCLOSURE_BG);
		textArea.setForeground(WidgetColors.COLOR_LIST_SELECTION_FG);
		textArea.setSelectedTextColor(WidgetColors.COLOR_LIST_SELECTION_FG);
		textArea.setSelectionColor(WidgetColors.COLOR_LIST_SELECTION_BG);
		textArea.setCaretColor(WidgetColors.COLOR_LIST_SELECTION_FG);

		logarea.setOpaque(true);
		logarea.setBackground(WidgetColors.COLOR_ENCLOSURE_BG);
		logarea.setBorder(BorderFactory.createEmptyBorder());
		logarea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		logarea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JScrollBar vBar = logarea.getVerticalScrollBar();
		vBar.setUI(new SlickerScrollBarUI(vBar, new Color(0, 0, 0, 0), new Color(160, 160, 160),
				WidgetColors.COLOR_NON_FOCUS, 4, 12));
		vBar.setOpaque(true);
		vBar.setBackground(WidgetColors.COLOR_ENCLOSURE_BG);
		vBar = logarea.getHorizontalScrollBar();
		vBar.setUI(new SlickerScrollBarUI(vBar, new Color(0, 0, 0, 0), new Color(160, 160, 160),
				WidgetColors.COLOR_NON_FOCUS, 4, 12));
		vBar.setOpaque(true);
		vBar.setBackground(WidgetColors.COLOR_ENCLOSURE_BG);

		setBackground(WidgetColors.COLOR_ENCLOSURE_BG);
		setLayout(new BorderLayout());
		setMinimumSize(new Dimension(200, 100));
		setMaximumSize(new Dimension(1000, 1000));
		setPreferredSize(new Dimension(1000, 200));

		add(Box.createHorizontalStrut(5), BorderLayout.WEST);
		add(logarea, BorderLayout.CENTER);
		add(Box.createHorizontalStrut(5), BorderLayout.EAST);
	}
	
	/**
	 * @param editable
	 */
	public ProMTextArea(boolean editable) {
		this();
		if (!editable) {
			setEditable(false);
			setLineWrap(true);
		}
	}
	
	public void setTabSize(int size) {
		textArea.setTabSize(size);
	}

	public int getTabSize() {
		return textArea.getTabSize();
	}
	
	public void append(final String text) {
		textArea.append(text);
	}

	public int getLength() {
		return textArea.getDocument().getLength();
	}

	public String getText() {
		return textArea.getText();
	}

	public void scrollToEnd() {
		setCaretPosition(getLength());
	}

	public void setCaretPosition(final int position) {
		textArea.setCaretPosition(position);
	}

	public void setEditable(final boolean editable) {
		textArea.setEditable(editable);
	}

	public void setText(final String text) {
		textArea.setText(text);
	}
	
	public void setLineWrap(boolean wrap) {
		textArea.setLineWrap(wrap);
		textArea.setWrapStyleWord(wrap);
	}
	
	public boolean getLineWrap() {
		return textArea.getLineWrap();
	}
}
