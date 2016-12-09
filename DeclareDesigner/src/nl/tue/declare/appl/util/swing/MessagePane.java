package nl.tue.declare.appl.util.swing;

import java.awt.*;
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
public class MessagePane {

  public static final int OK_OPTION = JOptionPane.OK_OPTION;

  /**
   * ask
   */
  public static boolean ask(Component parent, String question) {
    Object[] options = {
        "YES", "NO"};
    return JOptionPane.showOptionDialog(parent, question, "confirmation",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.QUESTION_MESSAGE, null,
                                        options, options[1]) ==
        JOptionPane.OK_OPTION;
  }

  /**
   *
   * @param parent Component
   * @param question String
   */
  public static void inform(Component parent, String question) {
    Object[] options = {
        "OK"};
    JOptionPane.showOptionDialog(parent, question, "confirmation",
                                 JOptionPane.DEFAULT_OPTION,
                                 JOptionPane.INFORMATION_MESSAGE, null,
                                 options, options[0]);
  }

  /**
   *
   * @param parent Component
   * @param question String
   */
  public static void error(Component parent, String message) {
    Object[] options = {
        "OK"};
    JOptionPane.showOptionDialog(parent, message, "error",
                                 JOptionPane.DEFAULT_OPTION,
                                 JOptionPane.ERROR_MESSAGE, null,
                                 options, options[0]);
  }

  public static String input(String message) {
    return JOptionPane.showInputDialog(message);
  }

  public static Object input(Object[] possibleValues, String message,
                             String title) {
    return JOptionPane.showInputDialog(null, message, title,
                                       JOptionPane.INFORMATION_MESSAGE, null,
                                       possibleValues, possibleValues[0]);

  }
}
