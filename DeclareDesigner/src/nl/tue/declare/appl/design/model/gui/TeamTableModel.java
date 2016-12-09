package nl.tue.declare.appl.design.model.gui;

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
public class TeamTableModel
    extends TTableModel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 3488575460337039442L;

public TeamTableModel() {
    super(0, new Object[] {"model role", "system role"});
  }

  public void addRow(TeamRole teamRole) {
    addRow(new Object[] {teamRole, teamRole.getRole()});
  }
}
