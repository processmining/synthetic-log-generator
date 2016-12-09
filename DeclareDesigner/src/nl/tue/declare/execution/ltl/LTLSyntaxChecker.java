package nl.tue.declare.execution.ltl;

import ltl2aut.formula.DefaultParser;


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
public class LTLSyntaxChecker {
  private DefaultParser parser = null;
  
  public LTLSyntaxChecker() {
	super();
  }
  
 /**
   *
   * @param formula String
   * @throws Exception
   */
  public void check(String formula) throws Exception {
    parser = new DefaultParser(formula);
    try {
      parser.parse();
    }
    catch (Throwable t) {
      throw new Exception(t.getMessage());
    }
  }
}
