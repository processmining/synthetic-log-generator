package nl.tue.declare.appl.design.model.gui;

import java.util.List;

import java.awt.*;
import javax.swing.*;

import nl.tue.declare.domain.organization.Role;

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
public class AssignmentPanel
    extends JPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = -9079802798769750273L;

private JTabbedPane tp = new JTabbedPane();

  private ModelPanel work = new WorkPanel();
  private TeamPanel team = new TeamPanel();
  private ModelPanel data = new DataPanel();

  public AssignmentPanel() {
    super();
    setLayout(new BorderLayout());
    add(tp, BorderLayout.CENTER);
  }

  public void set(boolean work, boolean team, boolean data) {
    if (work) {
      tp.add("work", this.work);
    }
    if (team) {
      tp.add("people", this.team);
    }
    if (data) {
      tp.add("data", this.data);
    }
  }

  /**
   * preview
   *
   * @param com Component
   */
  public void preview(Component com) {
    if (work instanceof WorkPanel) {
      ( (WorkPanel) work).preview(com);
    }
  }

  public void fillRoles(List<Role> list) {
    team.fillRoles(list);
  }

  public Object getSelectedRole() {
    return team.getSelectedRole();
  }

  public void setDataPanel(Component component) {
    this.data.setLayout(new BorderLayout(2, 2));
    data.add(component);
  }

  public void setTeamPanel(Component component) {
    this.team.setLayout(new BorderLayout(2, 2));
    team.add(component);
  }

}
