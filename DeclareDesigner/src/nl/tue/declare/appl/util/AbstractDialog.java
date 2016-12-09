package nl.tue.declare.appl.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public abstract class AbstractDialog extends JDialog {

	private static final long serialVersionUID = -3685059762288542506L;

	private  boolean result;
	private Container monitor = null;

	protected ActionListener positive = null;
	protected ActionListener negative = null;

	protected Component buttons = null;
	protected Component content = null;

	private boolean display = false;

	public AbstractDialog(Frame parent, String title, Container monitor) {
		super(parent, title, true);
		this.monitor = monitor;
		setUp();
	}
	
	public AbstractDialog(Frame parent, String title) {
		this(parent, title, parent);
	}

	public AbstractDialog(Dialog parent, String title, Container monitor) {
		super(parent, title, true);
		this.monitor = monitor;
		setUp();
	}
	
	public AbstractDialog(Dialog parent, String title) {
		this(parent, title, parent);
	}

	private void setUp() {
		positive = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (canClose()){
					AbstractDialog.this.close(true);
				}
			}
		};
		negative = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AbstractDialog.this.close(false);
			}
		};
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	protected void close(boolean result){
		AbstractDialog.this.result = result;
		setVisible(false);		
	}	
	protected abstract Button[] getCloseButtons();

	protected abstract Component getContent();

	public boolean showCentered() {
		setypGUI();
		position(monitor);
		setVisible(true);
		return result;
	}

	public boolean show(int x, int y, Dimension size) {
		setypGUI();
		setBounds(x, y, size.width, size.height);
		setVisible(true);
		return result;
	}

	private void setypGUI() {
		if (!display) {
			setLayout(new BorderLayout());
			add(getContentPanel(), BorderLayout.CENTER);// contents in the center
			add(getButtonsPanel(), BorderLayout.SOUTH); // buttons bellow
			pack();		
			setSize(getExplicitSize());
			display = true;
		}
	}
	
	/**
	 * This method is used for cases when the dialog should have a predefined size. 
	 * By default it returns the size of the dialog after it has been packed.
	 * If you want your dialog to have an explicitly defined size, overwrite this method.	
	 * @return the size of the dialog.
	 */
	protected Dimension getExplicitSize(){
		return this.getSize();
	}
	
	protected boolean canClose(){ 
		return true;
	}

	private Component getButtonsPanel() {
		if (buttons == null) {
			JPanel bttnPanel = new JPanel(new FlowLayout()); // add all buttons
			Button[] buttnos = getCloseButtons();
			for (Button button : buttnos) {
				bttnPanel.add(button);
			}
			buttons = bttnPanel;
		}
		return buttons;
	}
	

	private Component getContentPanel() {
		if (content == null) {
			content = getContent();
		}
		return content;
	}

	private void position(Container monitor) {
		Dimension dim = monitor.getSize();
		Point loc = monitor.getLocationOnScreen();

		Dimension size = getSize();

		loc.x += (dim.width - size.width) / 2;
		loc.y += (dim.height - size.height) / 2;

		if (loc.x < 0) {
			loc.x = 0;
		}
		if (loc.y < 0) {
			loc.y = 0;
		}

		Dimension screen = getToolkit().getScreenSize();

		if (size.width > screen.width) {
			size.width = screen.width;
		}
		if (size.height > screen.height) {
			size.height = screen.height;
		}

		if (loc.x + size.width > screen.width) {
			loc.x = screen.width - size.width;
		}

		if (loc.y + size.height > screen.height) {
			loc.y = screen.height - size.height;
		}

		setBounds(loc.x, loc.y, size.width, size.height);
	}

	protected void fillList(Iterable<?> anList, JList anJList) {
		FrameUtil.fillList(anList, anJList);
	}

	protected Object getSelecetdList(JList anJList) {
		return FrameUtil.getSelecetdList(anJList);
	}

	public static Object[] getSelecetdAllList(JList anJList) {
		return FrameUtil.getSelecetdAllList(anJList);
	}

	protected void setSelectedList(JList list, Object object) {
		FrameUtil.setSelectedList(list, object);
	}

	protected Object[] getSelectedMultipleList(JList anJList) {
		return FrameUtil.getSelectedMultipleList(anJList);
	}


	protected Container getMonitor(){
		return monitor;
	}
	
	public void setMonitor(Container monitor){
		this.monitor = monitor;
	}

	protected class Button extends JButton {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1033654679679008547L;

		Button(String text, boolean pos) {
			super(text);
			if (pos) {
				addActionListener(positive);
			} else {
				addActionListener(negative);
			}
		}
	}
}
