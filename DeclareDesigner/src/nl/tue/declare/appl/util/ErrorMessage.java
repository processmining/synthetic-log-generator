package nl.tue.declare.appl.util;

import java.io.*;

import java.awt.*;
import javax.swing.*;
/**
 * <p>
 * Title: DECLARE
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: TU/e
 * </p>
 * 
 * @author Maja Pesic
 * @version 1.0
 */
public class ErrorMessage extends CloseDialog{

	private static final long serialVersionUID = -8385536758474905436L;
	private boolean exit = false;
	private Throwable error;

	public ErrorMessage(Frame parent, Throwable t, String message, boolean exit) {
		super(parent, "Error: " + message);
		error = t;
		this.exit = exit;
	}


	private JComponent getError() {
		JTextArea pane = new JTextArea();
		pane.setEditable(false);

		pane.append("MESSAGE: " + "\t" + error.getMessage() + "\n");

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		error.printStackTrace(printWriter);
		pane.append(writer.toString());

		return pane;
	}
	
	@Override
	protected Dimension getExplicitSize(){
		return new Dimension(800,500);
	}
	
	@Override
	protected void onClose(){
		if (exit) {
			System.exit(-1);
		}	
	}

	@Override
	protected Component getContent() {
		return new JScrollPane(getError(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	
	public static void showError(Frame parent, Throwable t, String message) {
		showError(parent, t, message, false);
	}

	public static void showError(Frame parent, Throwable t, String message,
			boolean exit) {
		ErrorMessage frm = new ErrorMessage(parent, t, message, exit);
		frm.showCentered();
	}
}
