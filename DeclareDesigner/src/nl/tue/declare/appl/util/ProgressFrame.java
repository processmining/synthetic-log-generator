package nl.tue.declare.appl.util;

import java.beans.*;

import java.awt.*;
import java.awt.event.*;
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
public class ProgressFrame
    extends MainFrame implements PropertyChangeListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = -6457792257164284238L;
private JProgressBar progress;
  // private ProgressListener listener;
  private JButton cancel;
  private JLabel description;
  private JLabel info;
  private boolean canceled;

  public ProgressFrame(String description, int min, int max) {
    super("Progress...");
    // create components
    progress = new JProgressBar();
    progress.setValue(0);
    progress.setMinimum(min);
    progress.setMaximum(max);
    progress.setIndeterminate(true);
    // listener = new ProgressListener(progress);
    cancel = new JButton("cancel");
    this.description = new JLabel(description);
    info = new JLabel();
    canceled = false;

    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancel();
      }
    });

    //addPropertyChangeListener(listener);
    progress.setIndeterminate(true);

    // setup this frame
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setContentPane(prepare());
    pack();
    setResizable(false);
    middleSreen();
  }

  private JPanel prepare() {
    JPanel main = new JPanel(new GridLayout(4, 1));

    main.add(description);
    main.add(info);
    main.add(progress);

    JPanel btn = new JPanel(new FlowLayout());
    btn.add(cancel);
    main.add(btn);

    return main;
  }

  public void setInfo(String info) {
    this.info.setText(info);
  }

  public synchronized boolean canceled() {
    return canceled;
  }

  private synchronized void cancel() {
    canceled = true;
    setVisible(false);
  }

  public void propertyChange(PropertyChangeEvent evt) {
    String strPropertyName = evt.getPropertyName();
    if ("progress".equals(strPropertyName)) {
      progress.setIndeterminate(false);
      progress.setValue( (Integer) evt.getNewValue());
    }
  }

  public void close() {
    this.setVisible(false);
    ;
  }
}
