package nl.tue.declare.appl.util;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class OkCancelDialog extends AbstractDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 898631367862962146L;
	
	private Button ok = new Button("ok",true);
	private Button cancel = new Button("cancel",false);

	public OkCancelDialog(Dialog parent, String title, Container monitorFrame) {
		super(parent, title, monitorFrame);
		setUp();
	}
	
	public OkCancelDialog(Dialog parent, String title, Container monitorFrame, boolean yes_no) {
		this(parent, title, monitorFrame);
		setType(yes_no);
		setUp();
	}
	
	public OkCancelDialog(Dialog parent, String title) {
		super(parent, title);
		setUp();
	}
	
	public OkCancelDialog(Dialog parent, String title, boolean yes_no) {
		super(parent, title);
		setType(yes_no);
		setUp();
	}
	
	public OkCancelDialog(Frame parent, String title, Container monitorFrame) {
		super(parent, title, monitorFrame);
		setUp();
	}
	
	public OkCancelDialog(Frame parent, String title, Container monitorFrame, boolean yes_no) {
		super(parent, title, monitorFrame);
		setType(yes_no);
		setUp();
	}
	
	public OkCancelDialog(Frame parent, String title) {
		super(parent, title);
		setUp();
	}
	
	public OkCancelDialog(Frame parent, String title, boolean yes_no) {
		super(parent, title);
		setType(yes_no);
		setUp();
	}
	
	private void setUp(){
		ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				onOk();			
			}});
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				onCancel();			
			}});
		
	}
	
	private void setType(boolean yes_no){
		if (yes_no){
			ok.setText("yes");
			cancel.setText("no");
		} else {
			ok.setText("ok");
			cancel.setText("cancel");
		}
	}
	
	@Override
	protected Button[] getCloseButtons() {
		return new Button[]{ok,cancel};
	}
	
	protected void setOkEnabled(boolean enabled){
		ok.setEnabled(enabled);
	}
	
	protected void setCancelEnabled(boolean enabled){
		cancel.setEnabled(enabled);
	}
	
	protected void onCancel(){
		// intentionally left empty		
	}
	
	protected void onOk(){
		// intentionally left empty		
	}
}
