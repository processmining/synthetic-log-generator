package nl.tue.declare.appl.design.model;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import nl.tue.declare.appl.design.model.gui.*;
import nl.tue.declare.appl.util.swing.ButtonAdd;
import nl.tue.declare.appl.util.swing.ButtonDelete;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.organization.*;

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
public class TeamCoordinator
    extends ModelInternalCoordinator implements ActionListener {

  private TeamTableModel tableModel = new TeamTableModel();
  private TeamTable table = new TeamTable(tableModel);

  private JButton add = new ButtonAdd();
  private JButton delete = new ButtonDelete();

  private FrmTeamRole frmTeamRole;

  public TeamCoordinator(JFrame aMainFrame, AssignmentPanel panel,
                         AssignmentModel aModel) {
    super(panel, aModel);
    frmTeamRole = new FrmTeamRole(aMainFrame, "Team role",panel);
    prepareGUI();
    fillRoles();
  }

  private void prepareGUI() {
    JPanel main = new JPanel();
    main.setLayout(new BorderLayout(2, 2));

    JScrollPane scroll = new JScrollPane(table);
    main.add(scroll, BorderLayout.CENTER);

    JPanel buttons = new JPanel();
    buttons.setLayout(new FlowLayout());

    buttons.add(add);
    buttons.add(delete);
    main.add(buttons, BorderLayout.EAST);

    add.addActionListener(this);
    delete.addActionListener(this);

    panel.setTeamPanel(main);
  }

  /**
   * end
   *
   * @todo Implement this
   *   nl.tue.declare.appl.design.coordinate.InternalCoordinator method
   */
  public void end() {
  }

  /**
   * getInternalFrame
   *
   * @return JInternalFrame
   * @todo Implement this
   *   nl.tue.declare.appl.design.coordinate.InternalCoordinator method
   */
  public JInternalFrame getInternalFrame() {
    return null;
  }

  /**
   * start
   *
   * @todo Implement this
   *   nl.tue.declare.appl.design.coordinate.InternalCoordinator method
   */
  public void start() {
    fillRoles();
  }

  /**
   * addRole
   */
  public void addRole() {
   frmTeamRole.fillRoles(getAvailableRoles());      
    this.frmTeamRole.setTitle("Add a team role");
    if (this.frmTeamRole.showCentered()) {
      Object role = frmTeamRole.getSelectedRole();
      if (role != null) {
        if (role instanceof Role) {
          TeamRole teamRole = this.getTeamModel().add( (Role) role);
          frmTeamRole.toTeamRole(teamRole);
          this.fillRoles();
        }
      }
    }
  }


  private TeamModel getTeamModel() {
    return model.getTeamModel();
  }

  private void fillRoles() {
    this.tableModel.getDataVector().clear();
    TeamModel teamModel = model.getTeamModel();
    for (int i = 0; i < teamModel.getSize(); i++) {
      TeamRole teamRole = teamModel.get(i);
      this.tableModel.addRow(teamRole);
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e != null) {
      Object object = e.getSource();
      if (object != null) {
        if (object == add) {
          this.addRole();
        }
        if (object == delete) {
          this.deleteRole();
        }
      }
    }
  }

  private Collection<Role> getAvailableRoles() {
    return this.getControl().getOrganization().getRoles();
  }

  /**
   * deleteRole
   */
  public void deleteRole() {
    TeamRole teamRole = getSelectedRole();
    if (teamRole != null) {
      Object[] options = {
          "OK", "CANCEL"};
      int response = JOptionPane.showOptionDialog( /*frame*/panel,
          "Do you want to delete the team role - " +
          teamRole + "?",
          "confirmation",
          JOptionPane.DEFAULT_OPTION,
          JOptionPane.QUESTION_MESSAGE, null,
          options, options[1]);
      if (response == JOptionPane.OK_OPTION) {
        if (this.model.deleteTeamRole(teamRole)) {
          this.fillRoles();
        }
        else {
          JOptionPane.showInternalMessageDialog( /*frame*/panel,
              "The team role " + teamRole +
              " cannot be deleted because it is assigned to at least one activity definition.",
              "information", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    }
    else {
      JOptionPane.showInternalMessageDialog( /*frame*/panel,
          "No role is selected. You must first select the role you want to delete.",
          "information",
          JOptionPane.INFORMATION_MESSAGE);
    }
  }

  private TeamRole getSelectedRole() {
    int row = table.getSelectedRow();
    return (TeamRole) tableModel.getObject(row);
  }
}
