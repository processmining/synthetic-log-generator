package nl.tue.declare.appl.worklist.gui;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.*;

public class WorklistFrame
    extends MainFrame implements MouseListener, ActionListener,
    ListSelectionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = -4673130463135724175L;

private static final String TITLE = "Worklist";

  private JList assignments = null;
  private JPanel model = null;

  private JButton close = new JButton("close instance");

  private IWorklistFrameListener listener = null;

  private Map<Object, Boolean> enabled;
  private Map<Object, Color> state;

  /**
   *
   * @param user String
   */
  public WorklistFrame(String user) {
    super(TITLE + " - " + user, 150);
    assignments = new JList();
    assignments.setCellRenderer(new MyCellRenderer());
    FrameUtil.iniList(assignments);
    this.enabled = new HashMap<Object, Boolean> ();
    this.state = new HashMap<Object, Color> ();
    prepare();
  }

  /**
   *
   * @return JList
   */
  public JList getAssignmentList() {
    return this.assignments;
  }

  /**
   *
   */
  private void prepare() {
    JPanel list = this.prepareList();

    model = new TPanel(new BorderLayout());

    this.setLayout(new BorderLayout());
    
    TSplitPane split = new TSplitPane(JSplitPane.HORIZONTAL_SPLIT, list, model);
    

    add(split, BorderLayout.CENTER);
  }

  /**
   *
   * @return JPanel
   */
  private JPanel prepareList() {
    this.assignments.addListSelectionListener(this);

    JPanel panel = new TPanel(new BorderLayout(), "instances");
    JScrollPane scroll = new JScrollPane(this.assignments);
    panel.add(scroll, BorderLayout.CENTER);
    panel.add(close, BorderLayout.SOUTH);
    close.addActionListener(this);
    return panel;
  }

  /**
   *
   * @param object Object
   */
  public void addAssignment(Object object) {
    this.setState(object, false, null);
    FrameUtil.addToList(assignments, object);
    if (assignments.getModel().getSize() == 1) { // if first assignment added
      assignmentSelected(); // select this assignment
      this.select(object);
    }
    repaint();
  }

  /**
   *
   * @param object Object
   */
  public void removeAssignment(Object object) {
    int index = assignments.getSelectedIndex();
    FrameUtil.removeFromList(assignments, object);
    int size = assignments.getModel().getSize();
    index = (index < size) ? index : (size - 1);
    assignmentSelected();
    this.select(index);
    if (size == 0) {
      this.addWorkPanel(null);
    }
  }

  private void select(Object object) {
    assignments.clearSelection();
    assignments.setSelectedValue(object, true);
  }

  /**
   *
   * @return Object
   */
  public Object getSelectedAssignment() {
    return FrameUtil.getSelecetdList(assignments);
  }

  /**
   *
   * @param index int
   * @return Object
   */
  public Object getAssignmentAt(int index) {
    return FrameUtil.getItemList(assignments, index);
  }

  /**
   *
   * @param e MouseEvent
   */
  public void mouseClicked(MouseEvent e) {
    if (e != null) {
      Object source = e.getSource();
      if (source != null) {
        if (source == this.assignments) {
          this.assignmentSelected();
        }
      }
    }
  }

  private void assignmentSelected() {
    Object assignment = this.getSelectedAssignment();
    if (assignment != null) {
      if (listener != null) {
        this.listener.assignmentSelected(assignment);
      }
    }
  }

  /**
   *
   * @param e MouseEvent
   */
  public void mousePressed(MouseEvent e) {}

  public void mouseReleased(MouseEvent e) {}

  public void mouseEntered(MouseEvent e) {}

  public void mouseExited(MouseEvent e) {}

  public void addWorklistFrameListener(IWorklistFrameListener listener) {
    this.listener = listener;
  }

  /**
   *
   * @param workPanel JPanel
   */
  public void addWorkPanel(JPanel workPanel) {
    model.removeAll();
    if (workPanel != null) {
      model.add(workPanel);
    }
    try {
      model.revalidate();
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
    repaint();
  }

  /**
   *
   * @param e ActionEvent
   */
  public void actionPerformed(ActionEvent e) {
    if (e != null) {
      Object source = e.getSource();
      if (source != null) {
        if (source == this.close) {
          this.closeAssignment();
        }
      }
    }
  }

  /**
   *
   */
  private void closeAssignment() {
    Object selected = this.getSelectedAssignment();
    if (selected != null && listener != null) {
      listener.closeAssignment(selected);
    }
  }

  /**
   *
   * @param object Object
   * @param enabled Boolean
   * @param color Color
   */
  public void setState(Object object, boolean enabled, Color color) {
    this.enabled.put(object, new Boolean(enabled));
    this.state.put(object, color);
  }

  /**
   *
   * @param index int
   */
  private void select(int index) {
    try {
      //this.assignments.getSelectionModel().clearSelection();
      this.assignments.getSelectionModel().setSelectionInterval(index, index);
    }
    catch (Throwable e) {}
  }

  /**
   *
   * @param e ListSelectionEvent
   */
  public void valueChanged(ListSelectionEvent e) {
    JList list = (JList) e.getSource();
    ListSelectionModel lsm = list.getSelectionModel();
    if (lsm.isSelectionEmpty()) {
      if (listener != null) {
        this.listener.assignmentSelected(null);
      }
    }
    else {
      int minIndex = lsm.getMinSelectionIndex();
      if (lsm.isSelectedIndex(minIndex)) {
        Object selected = this.getAssignmentAt(minIndex);
        if (selected != null && listener != null) {
          listener.assignmentSelected(selected);
        }
      }
    }
  }

  /**
   *
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
  class MyCellRenderer
      extends JLabel implements ListCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2248506063017409644L;

	public MyCellRenderer() {
      setOpaque(true);
    }

    public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus) {
      //setEnabled(list.isEnabled());
      setText(value.toString());
      // font is bold?
      Font listFont = list.getFont();
      Boolean bold = enabled.get(value);
      if (bold != null) {
        if (bold) {
          Font font = new Font(listFont.getName(),
                               Font.BOLD,
                               listFont.getSize());
          setFont(font);
        }
      }
      // get the color
      Color color = state.get(value);
      if (color != null) {
        //setBackground(isSelected ? color : Color.white);
        // setForeground(isSelected ? Color.black : color);
        setBackground(isSelected ? Color.lightGray : Color.white);
        setForeground(color);
      }
      return this;
    }
  }
}
