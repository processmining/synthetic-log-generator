package nl.tue.declare.appl.design.model.gui;

import java.util.List;

import java.awt.*;

import nl.tue.declare.appl.design.organization.gui.*;
import nl.tue.declare.domain.organization.Role;

public class TeamPanel
    extends ModelPanel {
  /**
	 * 
	 */
	private static final long serialVersionUID = -8721180492287137621L;
PanelRoles panel;

  public TeamPanel(LayoutManager layout) {
    super(layout);
    prepare();
  }

  public TeamPanel() {
    super();
    prepare();
  }

  private void prepare() {
    panel = new PanelRoles();
    this.setLayout(new BorderLayout(2, 2));
    this.add(panel, BorderLayout.CENTER);
  }

  public void fillRoles(List<Role> list) {
    panel.fillRoles(list);
  }

  public Object getSelectedRole() {
    return panel.getSelectedRole();
  }

}
