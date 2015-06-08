package nl.tue.declare.appl.util;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class CloseDialog extends AbstractDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1458603184753943597L;
	
	private Button close = new Button("close",true);

	public CloseDialog(Dialog parent, String title, Container monitorFrame) {
		super(parent, title, monitorFrame);
		close.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				onClose();				
			}});
	}
	
	public CloseDialog(Dialog parent, String title) {
		super(parent, title);
	}
	
	public CloseDialog(Frame parent, String title, Container monitorFrame) {
		super(parent, title, monitorFrame);
	}
	
	public CloseDialog(Frame parent, String title) {
		super(parent, title);
	}
	
	@Override
	protected Button[] getCloseButtons() {
		return new Button[]{close};
	}
	
	protected void onClose(){
		// intentionally left empty		
	}
}
