package nl.tue.declare.appl.design.template.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import nl.tue.declare.appl.util.swing.*;
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
public class ConstraintGroupPanel
    extends JPanel implements ActionListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = -5839622780928063053L;
private ConstraintGroupTableModel model = new ConstraintGroupTableModel();
  private ConstraintGroupTable table = new ConstraintGroupTable(model);

  private JButton add = new ButtonAdd();
  private JButton edit = new ButtonEdit();
  private JButton delete = new ButtonDelete();

  private JButton warning = new JButton("edit");
  private JLabel warningLevel = new JLabel();

  private IConstraintGroupActionListener listener;

  public ConstraintGroupPanel(LayoutManager layout, boolean isDoubleBuffered) {
    super(layout, isDoubleBuffered);
    setup();
    this.listener = null;
  }

  public ConstraintGroupPanel(LayoutManager layout) {
    super(layout);
    setup();
  }

  public ConstraintGroupPanel(boolean isDoubleBuffered) {
    super(isDoubleBuffered);
    setup();
  }

  public ConstraintGroupPanel() {
    super();
    setup();
  }

  private void setup() {

    JPanel groups = this.groupsPanel();
    JPanel wrn = this.warningPanel();

    JSplitPane split = new TSplitPane(JSplitPane.HORIZONTAL_SPLIT, groups, wrn);
    this.setLayout(new BorderLayout());
    this.add(split, BorderLayout.CENTER);
    // install myself as listener on all buttons
    this.add.addActionListener(this);
    this.edit.addActionListener(this);
    this.delete.addActionListener(this);

    this.warning.addActionListener(this);
  }

  private JPanel groupsPanel() {
    JPanel main = new JPanel(new BorderLayout());

    JScrollPane scroll = new JScrollPane(table);

    JPanel buttons = new JPanel(new FlowLayout());
    buttons.add(this.add);
    buttons.add(this.edit);
    buttons.add(this.delete);

    main.add(scroll, BorderLayout.CENTER);
    main.add(buttons, BorderLayout.SOUTH);

    return main;
  }

  private JPanel warningPanel() {
    JPanel componenets = new JPanel(new FlowLayout(FlowLayout.LEFT));

    this.warningLevel.setForeground(Color.black);
    this.warningLevel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.black),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                                ));

    componenets.add(new JLabel("warning level  "));
    componenets.add(this.warningLevel);
    componenets.add(this.warning);

    return componenets;
  }

  public void setListener(IConstraintGroupActionListener l) {
    this.listener = l;
  }

  public void addGroup(ConstraintGroup group) {
    this.model.addRow(group);
  }

  public ConstraintGroup getSelectedGroup() {
    return this.table.getSelected();
  }

  public void actionPerformed(ActionEvent e) {
    if (e != null) {
      Object source = e.getSource();
      if (source != null) {
        if (source == add) {
          this.add();
        }
        if (source == edit) {
          this.edit();
        }
        if (source == delete) {
          this.delete();
        }
        if (source == warning) {
          this.warning();
        }
      }
    }
  }

  private void add() {
    if (this.listener != null) {
      ConstraintGroup group = listener.add();
      if (group != null) {
        this.model.addRow(group);
      }
    }
  }

  private void edit() {
    if (this.listener != null) {
      ConstraintGroup group = table.getSelected();
      if (group != null) {
        if (this.listener.edit(group)) {
          this.model.updateRow(group);
        }
      }
    }
  }

  private void delete() {
    if (this.listener != null) {
      ConstraintGroup group = table.getSelected();
      if (group != null) {
        if (this.listener.delete(group)) {
          model.remove(group);
        }
      }
    }
  }

  private void warning() {
    if (this.listener != null) {
      ConstraintWarningLevel level = listener.warning();
      if (level != null) {
        this.warningLevel.setText(Integer.toString(level.getLevel()));
      }
    }
  }

  public void setWarning(ConstraintWarningLevel level) {
    this.warningLevel.setText(Integer.toString(level.getLevel()));
  }

  public void add(ConstraintGroup group) {
    if (group != null) {
      this.model.addRow(group);
    }
  }

  public void clear() {
    this.model.clear();
  }
}
