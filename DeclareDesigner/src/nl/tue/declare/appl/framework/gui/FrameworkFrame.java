package nl.tue.declare.appl.framework.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

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
public class FrameworkFrame
    extends MainFrame implements ActionListener, WindowListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = -5782202590037032521L;

private static final String TITLE = " Framework";

  private JButton changeModel = new JButton("change model");
  private JButton changeTeam = new JButton("change team");
  private JButton complete = new JButton("complete");
  private JButton load = new JButton("load available");
  private JButton loadEmpty = new JButton("load empty");

  private JTextField number = new JTextField("");

  private AssignmentCoordinatorTableModel activeModel = new
      AssignmentCoordinatorTableModel();
  private AssignmentCoordinatorTable activeList = new
      AssignmentCoordinatorTable(activeModel);

  private CaseManagerTableModel arrivedModel = new CaseManagerTableModel();
  private CaseManagerTable arrivedList = new CaseManagerTable(arrivedModel);

  private JTextField yawlPort = new JTextField();
  private JTextField processLogFile = new JTextField();

  private JTextField requestResponsePort = new JTextField();
  private JTextField informationPort = new JTextField();

  private JTextField recommendationPort = new JTextField();
  private JTextField recommendationHost = new JTextField();

  private IFrameworkFrameListener listener = null;

  /**
   *
   */
  public FrameworkFrame() {
    super(TITLE, 150);
    try {
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.setNumberEmpty(1);
      prepareGUI();
      this.addWindowListener(this);
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  /**
   *
   */
  private void prepareGUI() {
    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.add("instances", prepareExternal());
    tabbedPane.add("settings", prepareSettings());

    this.arrivedList.getSelectionModel().addListSelectionListener(new
        ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
          listener.arrivedSelected();
        }
      }

    }
    );
    this.activeList.getSelectionModel().addListSelectionListener(new
        ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
          listener.activeSelected();
        }
      }

    }
    );

    Container desktop = getContentPane();
    desktop.setLayout(new BorderLayout(2, 2));
    desktop.add(tabbedPane, BorderLayout.CENTER);
  }

  /**
   * prepareExternal
   *
   * @return JPanel
   */
  public JPanel prepareExternal() {
    JPanel external = this.prepareAvailable();
    JPanel started = this.prepareActive();

    JPanel data = new JPanel(new GridLayout(2, 1));
    data.add(external);
    data.add(started);

    prepareTable();

    return data;
  }

  /**
   *
   * @return JPanel
   */
  private JPanel prepareActive() {
    JPanel started = new TPanel(new BorderLayout(2, 2));

    JScrollPane scrollPaneActive = new JScrollPane(activeList);

    started.add(scrollPaneActive, BorderLayout.CENTER);
    started.add(new JLabel("active instances"), BorderLayout.NORTH);

    JPanel buttons = new JPanel(new FlowLayout());
    complete.addActionListener(this);
    buttons.add(complete);
    changeModel.addActionListener(this);
    buttons.add(changeModel);
    changeTeam.addActionListener(this);
    //buttons.add(changeTeam);

    started.add(buttons, BorderLayout.SOUTH);

    return started;
  }

  /**
   *
   * @return JPanel
   */
  private JPanel prepareAvailable() {
    JPanel external = new TPanel(new BorderLayout(2, 2));

    JScrollPane scrollPane = new JScrollPane(this.arrivedList);
    external.add(scrollPane, BorderLayout.CENTER);

    external.add(new JLabel("available instances"), BorderLayout.NORTH);

    JPanel buttons = new JPanel(new FlowLayout());
    load.addActionListener(this);
    loadEmpty.addActionListener(this);
    buttons.add(load);
    buttons.add(this.prepareEmptyPanel());

    external.add(buttons, BorderLayout.SOUTH);

    return external;
  }

  private JPanel prepareEmptyPanel() {
    JPanel main = new JPanel(new FlowLayout());

    JPanel info = new JPanel(new BorderLayout(2, 2));
    info.add(new JLabel("number of instances to load for empty: "),
             BorderLayout.WEST);
    info.add(this.number, BorderLayout.CENTER);
    main.add(loadEmpty);
    main.add(info);
    return main;
  }

  /**
   *
   */
  private void prepareTable() {
    arrivedList.getColumnModel().getColumn(0).setPreferredWidth(100);
  }

  private JPanel prepareSettings() {
    JPanel main = new JPanel(new BorderLayout(2, 2));

    JButton settingsSave = new JButton("save");
    settingsSave.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveSettings();
      }
    });

    JButton settingsRestore = new JButton("restore");
    settingsRestore.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        restoreSettings();
      }
    });

    JPanel buttons = new JPanel(new FlowLayout());
    buttons.add(settingsSave);
    buttons.add(settingsRestore);

    JPanel up = new JPanel(new BorderLayout(2, 2));

    up.add(prepareSettingsData(), BorderLayout.CENTER);
    up.add(buttons, BorderLayout.SOUTH);

    main.add(up, BorderLayout.CENTER);

    return main;
  }
  
  private JPanel prepareSettingsData() {
    JPanel main = new JPanel(new BorderLayout());
    main.add(prepareYawlPanel(), BorderLayout.NORTH);
    JPanel second = new JPanel(new BorderLayout());
    second.add(prepareRecommendationPanel(), BorderLayout.NORTH);
    JPanel third = new JPanel(new BorderLayout());
    third.add(prepareWorklistPanel(), BorderLayout.NORTH);
    second.add(third, BorderLayout.CENTER);

    main.add(second, BorderLayout.CENTER);
    return main;
  }

  private JPanel prepareYawlPanel() {
    JPanel port = new TPanel(new BorderLayout(), "YAWL");
    port.add(new JLabel("port"), BorderLayout.WEST);
    port.add(yawlPort, BorderLayout.CENTER);
    return port;
  }

  private JPanel prepareRecommendationPanel() {
    JPanel main = new TPanel(new GridLayout(1, 2), "Recommendation");

    JPanel port = new JPanel(new BorderLayout());
    port.add(new JLabel("port"), BorderLayout.WEST);
    port.add(recommendationPort, BorderLayout.CENTER);

    JPanel host = new JPanel(new BorderLayout());
    host.add(new JLabel("host"), BorderLayout.WEST);
    host.add(recommendationHost, BorderLayout.CENTER);

    main.add(port);
    main.add(host);
    return main;
  }

  private JPanel prepareWorklistPanel() {
    JPanel main = new TPanel(new GridLayout(1, 2), "Worklist");

    JPanel port = new JPanel(new BorderLayout());
    port.add(new JLabel("request-response port"), BorderLayout.WEST);
    port.add(requestResponsePort, BorderLayout.CENTER);

    JPanel host = new JPanel(new BorderLayout());
    host.add(new JLabel("information port"), BorderLayout.WEST);
    host.add(informationPort, BorderLayout.CENTER);

    main.add(port);
    main.add(host);
    return main;
  }

  private void saveSettings() {
    if (listener != null) {
      listener.saveSettings();
    }
  }

  private void restoreSettings() {
    if (listener != null) {
      listener.restoreSettings();
    }
  }

  /**
   *
   * @param listener ActionListener
   */
  public void addButtonsActionListener(ActionListener listener) {
    load.addActionListener(listener);
    complete.addActionListener(listener);
  }

  /**
   *
   * @return CaseManagerTableModel
   */
  public CaseManagerTableModel getArrivedModel() {
    return arrivedModel;
  }

  /**
   *
   * @return CaseManagerTableModel
   */
  public AssignmentCoordinatorTableModel getActiveModel() {
    return activeModel;
  }

  /**
   *
   * @return JTable
   */
  public CaseManagerTable getArrivedTable() {
    return arrivedList;
  }

  /**
   *
   * @return JTable
   */
  public AssignmentCoordinatorTable getActiveTable() {
    return activeList;
  }

  /**
   *
   * @param listener IFrameworkFrameListener
   */
  public void addFrameworkFrameListener(IFrameworkFrameListener listener) {
    this.listener = listener;
  }

  /**
   *
   * @param e ActionEvent
   */
  public void actionPerformed(ActionEvent e) {
    if (e != null) {
      Object source = e.getSource();
      if (source != null && listener != null) {
        if (source == this.load) {
          listener.load();
        }
        if (source == this.complete) {
          listener.complete();
        }
        if (source == this.loadEmpty) {
          listener.loadEmpty();
        }
        if (source == this.changeModel) {
          listener.changeModel();
        }
        if (source == this.changeTeam) {
          listener.changeTeam();
        }
      }
    }
  }

  public void setNumberEmpty(int nr) {
    if (nr > 0) {
      String str = Integer.toString(nr);
      for (int i = nr; i < 10; i++) {
        str += " ";
      }
      number.setText(str);
    }
  }

  public int getNumberEmpty() {
    Integer integer = new Integer(1);
    try {
      String str = number.getText().replace(" ", "");
      integer = Integer.parseInt(str);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return integer.intValue();
  }

  public void windowOpened(WindowEvent e) {
  }

  public void windowClosing(WindowEvent e) {
    if (listener != null) {
      listener.finish();
    }
  }

  public void windowClosed(WindowEvent e) {
  }

  public void windowIconified(WindowEvent e) {
  }

  public void windowDeiconified(WindowEvent e) {
  }

  public void windowActivated(WindowEvent e) {
  }

  public void windowDeactivated(WindowEvent e) {
  }

  public String getProcessLogFile() {
    return this.processLogFile.getText();
  }

  public void setProcessLogFile(String str) {
    processLogFile.setText(str);
  }

  public String getYawlPost() {
    return yawlPort.getText();
  }

  public void setYawlPort(String str) {
    yawlPort.setText(str);
  }

  public String getRecommendationHost() {
    return recommendationHost.getText();
  }

  public void setRecommendationHost(String str) {
    recommendationHost.setText(str);
  }

  public String getRecommendationPort() {
    return recommendationPort.getText();
  }

  public void setRecommendationPort(String str) {
    recommendationPort.setText(str);
  }

  public String getReqestPort() {
    return requestResponsePort.getText();
  }

  public void setReqestPort(String str) {
    requestResponsePort.setText(str);
  }

  public void setInformationPort(String str) {
    this.informationPort.setText(str);
  }

  public String getInformationPort() {
    return informationPort.getText();
  }

  public void arrived(boolean enabled) {
    this.load.setEnabled(enabled);
  }

  public void active(boolean enabled) {
    this.complete.setEnabled(enabled);
    this.changeModel.setEnabled(enabled);
    this.changeTeam.setEnabled(enabled);
  }
}
