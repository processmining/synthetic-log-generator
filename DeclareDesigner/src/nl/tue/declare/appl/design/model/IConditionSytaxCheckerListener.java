package nl.tue.declare.appl.design.model;

public interface IConditionSytaxCheckerListener {
  /**
   * checkSyntax
   *
   * @param expresion String
   */
  public boolean checkSyntax(String expression);

  /**
   * checkSyntax
   *
   * @param expression String
   */
  public void checkSyntaxNotify(String expression);
}
