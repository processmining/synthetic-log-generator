package nl.tue.declare.appl.framework.gui;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MappingInfoWindow
    extends JWindow {

  /**
	 * 
	 */
	private static final long serialVersionUID = -8057735800457170591L;
private int x, y;
  private int width = 400;
  private int height = 300;

  private JLabel label = new JLabel("   ");
  private ArrayList<Object> log;

    public MappingInfoWindow(Frame owner) {
      super(owner);
      log = new ArrayList<Object>();
      log.add("** Starting plugin import **");
      log.add(new Integer(0));

      getRootPane().setPreferredSize(new Dimension(width, height));
      getRootPane().setSize(new Dimension(width, height));
      getRootPane().setBorder(BorderFactory.createEtchedBorder());

      JPanel p = new JPanel(new FlowLayout());
        p.add(new JLabel("NAME"));
      getRootPane().getContentPane().add(p, BorderLayout.NORTH);

      JPanel p2 = new JPanel(new BorderLayout());

      p2.add(new JLabel("IMAGE"), BorderLayout.CENTER);

      p = new JPanel(new FlowLayout());
      p.add(new JLabel("<html>Please wait while loading plugins.</html>"));
      p2.add(p, BorderLayout.SOUTH);

      getRootPane().getContentPane().add(p2, BorderLayout.CENTER);

      p = new JPanel(new FlowLayout());
      p.add(label);
      getRootPane().getContentPane().add(p, BorderLayout.SOUTH);
      pack();

      width = getRootPane().getContentPane().getWidth();
      height = getRootPane().getContentPane().getHeight();

      x = (Toolkit.getDefaultToolkit().getScreenSize().width - width) / 2;
      y = (Toolkit.getDefaultToolkit().getScreenSize().height - height) / 2;
      open();
    }

  public MappingInfoWindow() {
    super();
    log = new ArrayList<Object>();
    log.add("** Starting plugin import **");
    log.add(new Integer(0));

    getRootPane().setPreferredSize(new Dimension(width, height));
    getRootPane().setSize(new Dimension(width, height));
    getRootPane().setBorder(BorderFactory.createEtchedBorder());

    JPanel p = new JPanel(new FlowLayout());
    p.add(new JLabel("NAME"));
    getRootPane().getContentPane().add(p, BorderLayout.NORTH);

    JPanel p2 = new JPanel(new BorderLayout());

    p2.add(new JLabel("IMAGE"), BorderLayout.CENTER);

    p = new JPanel(new FlowLayout());
    p.add(new JLabel("<html>Please wait while loading plugins.</html>"));
    p2.add(p, BorderLayout.SOUTH);

    getRootPane().getContentPane().add(p2, BorderLayout.CENTER);

    p = new JPanel(new FlowLayout());
    p.add(label);
    getRootPane().getContentPane().add(p, BorderLayout.SOUTH);

    pack();

    width = getRootPane().getContentPane().getWidth();
    height = getRootPane().getContentPane().getHeight();

    x = (Toolkit.getDefaultToolkit().getScreenSize().width - width) / 2;
    y = (Toolkit.getDefaultToolkit().getScreenSize().height - height) / 2;
    open();
  }

  public void open() {
    setBounds(x, y, width, height);
    setVisible(true);
  }

  public void close() {
    setVisible(false);
    dispose();
  }

  public void changeText(String s, int status) {
    label.setText(s);
    log.add("* " + s);
    log.add(new Integer(status));
    repaint();
  }

  public ArrayList<Object> getLog() {
    return log;
  }

  public void actionPerformed(ActionEvent e) {
    this.setVisible(false);
  }
}
