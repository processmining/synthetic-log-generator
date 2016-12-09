package nl.tue.declare.appl.design.model;

import java.awt.*;

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.domain.model.*;

class ConditionSyntaxChecker {
  private static String lineSeparator = "\n";
  /**
   *
   * @param formula String
   * @param assignment AssignmentModel
   * @param parent Component
   * @return boolean
   */
  public static boolean check(String formula, AssignmentModel assignment,
                              Component parent) {
    boolean result = false;
    String frm = "Formula: " + formula + lineSeparator;
    try {
      // ComparisonSyntaxChecker.check(formula);
      ConditionEvaluator evaluator = new ConditionEvaluator(assignment);
      evaluator.parseCondition(formula);
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
   * @param assignment AssignmentModel
   * @param parent Component
   * @return boolean
   */
  public static boolean checkNotify(String formula, AssignmentModel assignment,
                                    Component parent) {
    boolean result = false;
    String frm = "Expression: " + formula + lineSeparator;
    try {
      ConditionEvaluator evaluator = new ConditionEvaluator(assignment);
      evaluator.parseCondition(formula);
      String message = "The syntax is correct." + lineSeparator;
      message += frm;
      MessagePane.inform(parent, message);
      result = true;
    }
    catch (Exception ex) {
      String message = "Error in the condition. The syntax is not correct." +
          lineSeparator;
      message += frm;
      message += "Error: " + lineSeparator + ex.getMessage() + lineSeparator;
      MessagePane.error(parent, message);
    }
    return result;
  }

  /**
   * ConditionSyntaxChecker
   */
}
