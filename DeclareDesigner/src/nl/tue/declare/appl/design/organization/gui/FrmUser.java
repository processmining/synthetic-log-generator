package nl.tue.declare.appl.design.organization.gui;

import java.awt.*;
import javax.swing.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.domain.organization.User;

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
public class FrmUser extends OkCancelDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6563028210731056829L;

	// *** data fields
	private JTextField name = new JTextField();
	private JTextField lastName = new JTextField();
	private JTextField username = new JTextField();
	private JPasswordField password = new JPasswordField();
	private JPasswordField password2 = new JPasswordField();

	public FrmUser(Frame parent, String title, JInternalFrame aMonitorFrame) {
		super(parent, title, aMonitorFrame);
	}

	public boolean toUser(User user) {
		boolean ok = (new String(this.password.getPassword())).equals(new String(
				this.password2.getPassword()));
		if (ok && user != null) {
			user.setName(name.getText());
			user.setLastName(lastName.getText());
			user.setUserName(username.getText());
			user.setPassword(new String(this.password.getPassword()));
		}
		return ok;
	}

	public void fromUser(User user) {
		if (user == null) {
			name.setText("");
			lastName.setText("");
			username.setText("");
			password.setText("");
			password2.setText("");
		} else {
			name.setText(user.getName());
			lastName.setText(user.getLastName());
			username.setText(user.getUserName());
			password.setText(user.getPassword());
			password2.setText(user.getPassword());
		}
	}

	protected boolean canClose() {
		return (new String(this.password.getPassword())).equals(new String(
				this.password2.getPassword()));
	}

	@Override
	protected Component getContent() {
		JPanel content = new JPanel(new GridLayout(5, 2));

		// *** set echo character for password fields
		password.setEchoChar('*');
		password2.setEchoChar('*');

		// *** name
		content.add(new JLabel("name"));
		content.add(name);
		// *** last name
		content.add(new JLabel("last name"));
		content.add(lastName);
		// *** user name
		content.add(new JLabel("username"));
		content.add(username);
		// *** password
		content.add(new JLabel("password"));
		content.add(password);
		// *** password retyped
		content.add(new JLabel("retype password"));
		content.add(password2);

		return content;
	}
}
