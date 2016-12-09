package nl.tue.declare.appl.framework.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.*;

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
public class FrmMapping
    extends OkCancelDialog {

  /**
	 * 
	 */
	private static final long serialVersionUID = -3459430221619896252L;

  private JLabel model = new JLabel();
  private JButton btnModel = new JButton("change model");

  private Container data = null;
  private Container team = null;

  private ILoadListener loadListener;
  
  public FrmMapping(Frame parent, Container aMonitorFrame,
                    Container data, Container team) {
    super(parent, "", aMonitorFrame);
    this.setDataContainer(data);
    this.setTeamContainer(team);
    btnModel.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			reload();			
		}});
  }


   private void reload() {
    if (this.loadListener != null) {
      this.loadListener.reload();
    }
  }

  protected boolean canClose() {
    boolean result = true;
    if (this.loadListener != null) {
      try {
        result = this.loadListener.onOk();
      }
      catch (Exception ex) {
        JOptionPane.showInternalMessageDialog(this.getContentPane(),
                                              ex.getMessage(),
                                              "Cannot load the instance",
                                              JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    return result;
  }

  /*private boolean checkCancel() {
    boolean result = true;
    if (this.loadListener != null) {
      try {
        result = this.loadListener.onCancel();
      }
      catch (Exception ex) {
        JOptionPane.showInternalMessageDialog(this.getContentPane(),
                                              ex.getMessage(),
                                              "Cannot load the instance",
                                              JOptionPane.ERROR_MESSAGE);
        return false;

      }
    }
    return result;
  }*/

  private JPanel prepareData() {
    JPanel dataPanel = new TPanel(new BorderLayout(2, 2));

    dataPanel.add(new JLabel("data"), BorderLayout.NORTH);
    if (this.data != null) {
      JScrollPane pane = new JScrollPane(this.data);
      dataPanel.add(pane, BorderLayout.CENTER);
    }
    return dataPanel;
  }

  private JPanel prepareTeam() {
    JPanel main = new TPanel(new BorderLayout(2, 2));
    if (this.team != null) {
      main.add(this.team, BorderLayout.CENTER);
    }
    return main;
  }

  private void setDataContainer(Container data) {
    this.data = data;
  }

  private void setTeamContainer(Container team) {
    this.team = team;
  }

  public void setModel(String string) {
    this.model.setText("Local model: " + string);
  }

  public void addLoadListener(ILoadListener loadListener) {
    this.loadListener = loadListener;
  }

@Override
protected Component getContent() {
    JPanel main = new TPanel(new BorderLayout(2, 2));

    JPanel content = new JPanel(new GridLayout(2, 1));

    JPanel data = this.prepareData();
    JPanel team = this.prepareTeam();

    content.add(data);
    content.add(team);

    JPanel modelPanel = new JPanel();
    modelPanel.add(model);
    modelPanel.add(this.btnModel);

    main.add(modelPanel, BorderLayout.NORTH);
    main.add(content, BorderLayout.CENTER);

    return main;
}
}
