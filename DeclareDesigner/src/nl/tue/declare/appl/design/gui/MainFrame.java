package nl.tue.declare.appl.design.gui;

import java.awt.*;
import javax.swing.*;

import nl.tue.declare.appl.*;

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
public class MainFrame
    extends JFrame {

  /**
	 * 
	 */
	private static final long serialVersionUID = -710344195019020975L;
    private final String NAME = Project.NAME + " Designer";

  //JDesktopPane desktop;
  //private JInternalFrame frame = null;

  public MainFrame() {
    try {
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      jbInit();
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  /**
   * Component initialization.
   *
   * @throws java.lang.Exception
   */
  private void jbInit() throws Exception {
    //Make the big window be indented 50 pixels from each edge
    //of the screen.
    setTitle(NAME);
    int inset = 50;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(inset, inset,
              screenSize.width - inset * 2,
              screenSize.height - inset * 2);
  }


  //Quit the application.
  protected void quit() {
    System.exit(0);
  }

}
