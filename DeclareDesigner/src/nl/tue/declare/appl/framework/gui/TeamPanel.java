package nl.tue.declare.appl.framework.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.model.*;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author Maja Pesic
 * @version 1.0
 */
public class TeamPanel
    extends TPanel implements ActionListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = 7682730598054543945L;
TeamTableModel teamTableModel = new TeamTableModel();
  TeamTable teamTable = new TeamTable(teamTableModel);

  JButton addTeam = new JButton("add participants(s)");
  JButton removeTeam = new JButton("remove participant(s)");

  ITeamListener listener;

  public TeamPanel() {
    super(new BorderLayout(2, 2), "participants");

    listener = null;

    add(new JScrollPane(this.teamTable), BorderLayout.CENTER);

    JPanel buttons = new JPanel(new FlowLayout());
    buttons.add(this.addTeam);
    buttons.add(this.removeTeam);

    this.addTeam.addActionListener(this);
    this.removeTeam.addActionListener(this);

    add(buttons, BorderLayout.SOUTH);
  }

  public void setListener(ITeamListener listener) {
    this.listener = listener;
  }

  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (source == this.addTeam) {
      this.addTeam();
    }
    if (source == this.removeTeam) {
      this.removeTeam();
    }
  }

  private void addTeam() {
    if (this.listener != null) {
      this.listener.addTeamMembers();
    }
  }

  private void removeTeam() {
    if (this.listener != null) {
      this.listener.removeTeamMembers();
    }
  }

  public void clear() {
    teamTableModel.clear();
  }

  public void add(TeamRole role, String str) {
    teamTableModel.addRow(role, str);
  }

  /**
   * Get selected team role
   * @return The selected team role.
   */
  public TeamRole getSelectedTeamRole() {
    Object selected = this.teamTable.getSelected();
    if (selected != null) {
      if (selected instanceof TeamRole) {
        return (TeamRole) selected;
      }
    }
    return null;
  }
}
