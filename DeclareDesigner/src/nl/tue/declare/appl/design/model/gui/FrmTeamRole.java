package nl.tue.declare.appl.design.model.gui;


import info.clearthought.layout.TableLayout;


import java.awt.*;
import javax.swing.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.model.TeamRole;
import nl.tue.declare.domain.organization.Role;

public class FrmTeamRole extends OkCancelDialog {

	private static final long serialVersionUID = 2001281229765916813L;

	private JTextField name = new JTextField();
	private JList rolesList = new JList();
	private static double p = TableLayout.PREFERRED;
	private static double size[][] = { { p, TableLayout.FILL },
			{ p, p} };

	public FrmTeamRole(Frame parent, String title, Container aMonitorFrame) {
		super(parent, title, aMonitorFrame);
		this.rolesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public void toTeamRole(TeamRole role){
		if (role != null){
			role.setName(name.getText());
			role.setRole((Role) getSelecetdList(rolesList));
		}
	}
	
	public void fromTeamRole(TeamRole role){
		if (role != null){
		   name.setText(role.getName());
		   setSelectedList(rolesList, role.getRole());
		} else{
			name.setText("");
			setSelectedList(rolesList, null);
		}
	}		
	
	public Object getSelectedRole(){
		return rolesList.getSelectedValue();
	}

	public void fillRoles(Iterable<?> list) {
		this.fillList(list, this.rolesList);
	}

	@Override
	protected Component getContent() {
		JPanel panel = new TPanel(new TableLayout(size));

		panel.add(new JLabel("name"),"0,0");
		panel.add(this.name, "1,0");

		panel.add(new JLabel("ogranizational role"),"0,1");
		panel.add(new JScrollPane(rolesList), "1,1");

		return panel;
	}
}
