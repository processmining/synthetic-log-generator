package nl.tue.declare.appl.framework.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import nl.tue.declare.appl.design.model.gui.*;
import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.ButtonCancel;
import nl.tue.declare.appl.util.swing.ButtonOk;

public class FrmAdapt
    extends MainFrame implements ActionListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = -1678515935805720999L;
private JButton ok = new ButtonOk();
  private JButton cancel = new ButtonCancel();
  //private AssignmentPanel panel;
  private JPanel main = new JPanel(new BorderLayout());
  private JButton verify = new JButton("verify model");
  private JCheckBox migrate = new JCheckBox("migrate all instances");
  private JCheckBox save = new JCheckBox("save model");

  private IFrmAdaptListener listener = null;

  public FrmAdapt(String title) {
    super(title, 150);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setUp();
  }

  public void setAssignmanePanel(AssignmentPanel panel) {
    main.removeAll();
    main.add(panel, BorderLayout.CENTER);
  }

  public void setListener(IFrmAdaptListener listener) {
    this.listener = listener;
  }

  private boolean onOK() {
    boolean ok = true;
    if (listener != null) {
      ok = listener.onOk(this.getMigrate(), this.getSave());
    }
    return ok;
  }

  private void onVerify() {
    if (listener != null) {
      listener.verifyModel();
    }
  }

  private void setUp() {
    JPanel main = new JPanel(new BorderLayout());
    main.add(this.prepareData(), BorderLayout.CENTER);
    main.add(this.prepareButtons(), BorderLayout.SOUTH);
    this.setContentPane(main);
  }

  private JPanel prepareData() {
    return main;
  }

  private JPanel prepareButtons() {
    JPanel main = new JPanel(new GridLayout(2, 1));

    JPanel chck = new JPanel(new FlowLayout());
    chck.add(migrate);
    chck.add(save);

    JPanel btns = new JPanel(new FlowLayout());
    btns.add(this.ok);
    btns.add(this.cancel);
    btns.add(this.verify);
    ok.addActionListener(this);
    cancel.addActionListener(this);
    verify.addActionListener(this);

    main.add(chck);
    main.add(btns);
    return main;
  }

  /**
   * Invoked when an action occurs.
   *
   * @param e ActionEvent
   * @todo Implement this java.awt.event.ActionListener method
   */
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (source == ok) {
      if (onOK()) {
        setVisible(false);
      }
    }
    if (source == cancel) {
      if (listener != null) {
        listener.cancel();
      }
      setVisible(false);
    }
    if (source == verify) {
      this.onVerify();
    }
  }

  public boolean getMigrate() {
    return migrate.isSelected();
  }

  public boolean getSave() {
    return save.isSelected();
  }

  public interface IFrmAdaptListener {
    boolean onOk(boolean migrate, boolean future);

    void verifyModel();

    void cancel();
  }
}
