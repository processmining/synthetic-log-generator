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
public interface ILTLSyntaxCheckListener {
  /**
   * checkSyntax
   *
   * @param formula String
   */
  public boolean checkSyntax(String formula);

  /**
   * checkSyntax
   *
   * @param formula String
   */
  public void checkSyntaxNotify(String formula);
}
