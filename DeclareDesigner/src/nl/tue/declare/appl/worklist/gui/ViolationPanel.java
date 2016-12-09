package nl.tue.declare.appl.worklist.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.template.*;

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
class ViolationPanel
    extends TPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = -7553901007963054823L;
  JTextArea group = new TTextArea();
  JTextArea description = new TTextArea();
  JTextArea priority = new TTextArea();
  TTextArea message = new TTextArea();

  public ViolationPanel(ConstraintDefinition constraint) {
    super(new GridLayout(2, 1), "warning for " + constraint.getCaption());
    this.prepareUI();
    ConstraintLevel level = constraint.getLevel();
    if (level != null) {
      ConstraintGroup group = level.getGroup();
      this.setGroup(group.getName());
      this.setDescription(group.getDescription());
      this.setPriority(level.getLevel());
      this.setMessage(level.getMessage());
    }
  }

  /*private static String caption(ConstraintDefinition constraint) {
    return constraint.getName() + ": " + parameters(constraint) + "";
  }

  private static String parameters(ConstraintDefinition constraint) {
    String result = "";
    for (Parameter parameter: constraint.getParameters()) {
      if (!result.equals("")) {
        result += ", ";
      }
      result += branches(constraint,parameter);

    }
    return result;
  }*/

  /**
   *
   * @param parameter Parameter
   * @return String
   */
  /*private static String branches(ConstraintDefinition constraint, Parameter parameter) {
    String result = "";
    for (ActivityDefinition branch: constraint.getBranches(parameter)) {
      if (!result.equals("")) {
        result += ", ";
      }
      result += branch.getName();
    }
    return "[" + result + "]";
  }*/

  private void prepareUI() {
    this.readOnly(this.description);
    this.readOnly(this.group);
    this.readOnly(this.message);
    this.readOnly(this.priority);

    add(this.groupsPanel());
    add(this.priorityPanel());
  }

  private void readOnly(JTextComponent c) {
    FrameUtil.readOnly(c, this);
  }

  private JPanel priorityPanel() {
    JPanel main = new JPanel(new BorderLayout(2, 2));

    JPanel priorityPanel = new JPanel(new BorderLayout());
    JPanel priorityCaption = new JPanel(new BorderLayout());
    priorityCaption.add(new JLabel("priority     "), BorderLayout.NORTH);
    priorityPanel.add(priorityCaption, BorderLayout.WEST);
    priorityPanel.add(this.priority, BorderLayout.CENTER);

    JPanel messagePanel = new JPanel(new BorderLayout());
    JPanel messageCaption = new JPanel(new BorderLayout());
    messageCaption.add(new JLabel("message  "), BorderLayout.NORTH);
    messagePanel.add(messageCaption, BorderLayout.WEST);
    messagePanel.add(new JScrollPane(this.message), BorderLayout.CENTER);

    main.add(priorityPanel, BorderLayout.NORTH);
    main.add(messagePanel, BorderLayout.CENTER);
    return main;
  }

  private JPanel groupsPanel() {
    JPanel main = new JPanel(new BorderLayout(2, 2));

    JPanel comboPanel = new JPanel(new BorderLayout());
    JPanel comboCaption = new JPanel(new BorderLayout());
    comboCaption.add(new JLabel("group       "), BorderLayout.NORTH);
    comboPanel.add(comboCaption, BorderLayout.WEST);
    comboPanel.add(this.group, BorderLayout.CENTER);

    JPanel decriptionPanel = new JPanel(new BorderLayout());
    JPanel decriptionCaption = new JPanel(new BorderLayout());
    decriptionCaption.add(new JLabel("decription "), BorderLayout.NORTH);
    decriptionPanel.add(decriptionCaption, BorderLayout.WEST);
    decriptionPanel.add(new JScrollPane(this.description), BorderLayout.CENTER);

    main.add(comboPanel, BorderLayout.NORTH);
    main.add(decriptionPanel, BorderLayout.CENTER);
    return main;
  }

  private void setGroup(String group) {
    this.group.setText(group);
  }

  private void setDescription(String description) {
    this.description.setText(description);
  }

  private void setPriority(int priority) {
    this.priority.setText(Integer.toString(priority));
  }

  private void setMessage(String message) {
    this.message.setText(message);
  }

}
