package nl.tue.declare.appl.worklist.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.appl.worklist.WorklistCoordinator;

public class LogInFrame
    extends MainFrame implements ActionListener, KeyListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = -2666699845672277288L;
JButton ok = new JButton("ok");
  JButton cancel = new JButton("cancel");

  JTextField username = new JTextField("");
  JPasswordField password = new JPasswordField("");

  JPanel settingsPanel;
  JTextField info = new JTextField();
  JTextField request = new JTextField();
  JTextField host = new JTextField();
  //private static boolean settings = false;

  WorklistCoordinator coordinator;

  public LogInFrame(String title, WorklistCoordinator coordinator) {
    this(title, coordinator, "", "");
  }

  public LogInFrame(String title, WorklistCoordinator coordinator,
                    String userName, String password) {
    super(title, 380);
    this.coordinator = coordinator;
    this.username.setText(userName);
    this.password.setText(password);
    prepare();
    pack();
    setResizable(false);
    middleSreen();
  }

  private void prepare() {
    JPanel data = this.prepareDataPanel();
    JPanel buttons = this.prepareButtonsPanel();

    JPanel main = new JPanel(new BorderLayout(2, 2));
    main.add(data, BorderLayout.CENTER);
    main.add(buttons, BorderLayout.SOUTH);
    this.setContentPane(main);
  }

  private JPanel prepareDataPanel() {
    JPanel panel = new TPanel(new GridLayout(2, 2));

    // *** create labels
    JLabel jLabelUserName = new JLabel("  username");
    JLabel jLabelPassword = new JLabel("  password");

// *** set echo character for password fields
    password.setEchoChar('*');

    username.addKeyListener(this);
    password.addKeyListener(this);

// *** user name
    panel.add(jLabelUserName);
    panel.add(username);
// *** password
    panel.add(jLabelPassword);
    panel.add(password);

    JButton settingsBtn = new JButton("settings >>");
    settingsBtn.addActionListener(
        new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        settings();
      }
    }
    );

    // panel.add(settingsBtn);

    settingsPanel = prepareSettingsPanel();
    settingsPanel.setVisible(false);

    JPanel main = new JPanel(new GridLayout(2, 1));
    main.add(panel);
    main.add(settingsPanel);
    return panel;
  }

  private JPanel prepareButtonsPanel() {
    JPanel panel = new JPanel(new FlowLayout());

    ok.addActionListener(this);
    cancel.addActionListener(this);

    ok.addKeyListener(this);
    cancel.addKeyListener(this);

    panel.add(ok);
    panel.add(cancel);
    return panel;
  }

  private JPanel prepareSettingsPanel() {
    JPanel main = new TPanel(new GridLayout(3, 1), "engine connection");

    JPanel infoPanel = new JPanel(new BorderLayout());
    infoPanel.add(new JLabel("information port"), BorderLayout.WEST);
    infoPanel.add(info, BorderLayout.CENTER);

    JPanel requestPanel = new JPanel(new BorderLayout());
    requestPanel.add(new JLabel("request port"), BorderLayout.WEST);
    requestPanel.add(request, BorderLayout.CENTER);

    JPanel hostPanel = new JPanel(new BorderLayout());
    hostPanel.add(new JLabel("engine host"), BorderLayout.WEST);
    hostPanel.add(host, BorderLayout.CENTER);

    main.add(hostPanel);
    main.add(infoPanel);
    main.add(requestPanel);

    return main;
  }

  public void actionPerformed(ActionEvent e) {
    if (e != null) {
      Object source = e.getSource();
      if (source != null) {
        if (source == ok) {
          this.ok();
        }
        if (source == cancel) {
          this.cancel();
        }
      }
    }
  }

  private void ok() {
    String un = this.username.getText();
    char[] p = this.password.getPassword();
    if (un != null && p.length > 0) {
      this.coordinator.logIn(un, new String(p));
    }
    else {
      MessagePane.inform(this,
                         "You did not enter your username and/or password!");
    }
  }

  private void cancel() {
    this.coordinator.cancelLogIn();
  }

  public void keyTyped(KeyEvent e) {
  }

  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == KeyEvent.VK_ENTER) {
      Object source = e.getSource();
      if (source == this.username || source == this.password ||
          source == this.ok) {
        this.ok();
        return;
      }
      if (source == this.cancel) {
        this.cancel();
        return;
      }
    }
  }

  private void settings() {

  }

  public void keyReleased(KeyEvent e) {
  }

}
