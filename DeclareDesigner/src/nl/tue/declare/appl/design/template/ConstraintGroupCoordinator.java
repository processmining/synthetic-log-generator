package nl.tue.declare.appl.design.template;

import java.awt.*;
import javax.swing.*;

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
import nl.tue.declare.appl.design.InternalCoordinator;
import nl.tue.declare.appl.design.template.gui.*;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.control.*;
import nl.tue.declare.domain.template.*;

public class ConstraintGroupCoordinator
    extends InternalCoordinator implements IConstraintGroupActionListener {

  ConstraintGroupPanel panel;
  FrmConstraintGroup frame;
  /**
   *
   * @param aMainFrame JFrame
   * @param panel ConstraintGroupPanel
   * @param monitorFrame Container
   */
  public ConstraintGroupCoordinator(JFrame aMainFrame,
                                    ConstraintGroupPanel panel,
                                    Container monitorFrame) {
    super(aMainFrame);
    this.panel = panel;
    this.panel.setListener(this);
    frame = new FrmConstraintGroup(aMainFrame, "Role", monitorFrame);
    this.fillFrom();
  }

  /**
   *
   */
  private void fillFrom() {
    panel.clear(); // first clear the panel from previous content
    ConstraintWarningLevel warning = Control.singleton().getConstraintTemplate().
        getConstraintWarningLevel();
    // fill the panel with all existing constraint groups
    for (int i = 0;
         i < Control.singleton().getConstraintTemplate().constraintGroupCount();
         i++) {
      ConstraintGroup group = Control.singleton().getConstraintTemplate().
          getGroupAt(i);
      panel.add(group);
    }
    panel.setWarning(warning);
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
  }

  /**
   *
   * @return ConstraintGroup
   */
  public ConstraintGroup add() {
    ConstraintGroup group = null;
    fillFormFromGroup(group, frame);
    frame.setTitle("Add constraint group");
    if (frame.showCentered()) {
      group = Control.singleton().getConstraintTemplate().createGroup();
      addConfirmed(group);
    }
    return group;
  }

  /**
   *
   * @param group ConstraintGroup
   * @return boolean
   */
  public boolean edit(ConstraintGroup group) {
    boolean result = false;
    if (group != null) {
      fillFormFromGroup(group, frame);
      frame.setTitle("Edit constraint group");      
      result = frame.showCentered();
      if (result) {
        editConfirmed(group);
      }
    }
    else {
      MessagePane.inform(frame,
                         "No group is selected. You must first select a group you want to edit.");
    }
    return result;
  }

  /**
   *
   * @param group ConstraintGroup
   */
  private void editConfirmed(ConstraintGroup group) {
    if (group != null) {
      this.fillGroupFromForm(group, frame);
      Control.singleton().getConstraintTemplate().editGroup(group);
    }
  }

  /**
   *
   * @param group ConstraintGroup
   */
  private void addConfirmed(ConstraintGroup group) {
    if (group != null) {
      this.fillGroupFromForm(group, frame);
      Control.singleton().getConstraintTemplate().addGroup(group);
    }
  }

  /**
   *
   * @param group ConstraintGroup
   * @param frame FrmConstraintGroup
   */
  private void fillFormFromGroup(ConstraintGroup group,
                                 FrmConstraintGroup frame) {
    if (frame != null) {
      frame.fromGroup(group);
    }
  }

  /**
   *
   * @param group ConstraintGroup
   * @param frame FrmConstraintGroup
   */
  private void fillGroupFromForm(ConstraintGroup group,
                                 FrmConstraintGroup frame) {
    if (group != null && frame != null) {
      frame.toGroup(group);
    }
  }

  /**
   *
   * @param group ConstraintGroup
   * @return boolean
   */
  public boolean delete(ConstraintGroup group) {
    boolean result = MessagePane.ask(frame,
                                     "Are you sure you want to delete constraint group " +
                                     group.getName() + ">");
    if (result) {
      result = Control.singleton().getConstraintTemplate().
          deleteConstraintGroup(group);
    }
    return result;
  }

  /**
   *
   * @return ConstraintWarningLevel
   */
  public ConstraintWarningLevel warning() {
    ConstraintWarningLevel warning = Control.singleton().getConstraintTemplate().
        getConstraintWarningLevel();
    Integer[] levels = ConstraintWarningLevel.possible();
    Object selected = MessagePane.input(levels, "Select the constraint level",
                                        "Constraint warning level");
    if (selected != null) {
      if (selected instanceof Integer) {
        Integer level = (Integer) selected;
        warning.setLevel(level.intValue());
        Control.singleton().getConstraintTemplate().editConstraintWarningLevel(
            warning);
      }
    }
    return warning;
  }
}
