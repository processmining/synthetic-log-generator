package nl.tue.declare.appl.design.template;

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
import java.awt.*;

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.execution.ltl.LTLSyntaxChecker;

public class FormulaSyntaxChecker {
  private static String lineSeparator = "\n";
  private LTLSyntaxChecker checker;
  
  public FormulaSyntaxChecker() {
    super();
    checker = new LTLSyntaxChecker();
}
  
  /**
   *
   * @param formula String
   * @param parent Component
   * @return boolean
   */
  public boolean check(String formula, Component parent) {
    boolean result = false;
    String frm = "Formula: " + formula + lineSeparator;
    try {
      checker.check(formula);
      result = true;
    }
    catch (Exception ex) {
      String message = "The syntax is not correct." + lineSeparator;
      message += frm;
      message += "Error: " + lineSeparator + ex.getMessage() + lineSeparator;
      MessagePane.error(parent, message);
    }
    return result;
  }

  /**
   *
   * @param formula String
   * @param parent Component
   * @return boolean
   */
  public boolean checkNotify(String formula, Component parent) {
    boolean result = false;
    String frm = "Formula: " + formula + lineSeparator;
    try {
      checker.check(formula);
      String message = "The syntax is correct." + lineSeparator;
      message += frm;
      MessagePane.inform(parent, message);
      result = true;
    }
    catch (Exception ex) {
      String message = "The syntax is not correct." + lineSeparator;
      message += frm;
      message += "Error: " + lineSeparator + ex.getMessage() + lineSeparator;
      MessagePane.error(parent, message);
    }
    return result;
  }
}
