package nl.tue.declare.domain.instance;

import java.util.*;

import org.nfunk.jep.*;
import nl.tue.declare.domain.model.*;

public class ConditionEvaluator {

  private static final String OLD_AND = "/\\\\";
  private static final String OLD_OR = "\\\\/";

  private static final String NEW_AND = "&&";
  private static final String NEW_OR = "||";

  private JEP parser;
  private Variables variables;

  public ConditionEvaluator(AssignmentModel assignment) {
    super();
    this.variables = new Variables(assignment);
    parser = new JEP();
    parser.initFunTab(); // clear the contents of the function table
    parser.addStandardFunctions();
  }

  private Object evaluate(String expression) throws Exception {
    parser.initSymTab(); // clear the contents of the symbol table
    parser.addStandardConstants();
    parser.addComplex(); // among other things adds i to the symbol table
    String prepared = this.prepare(expression); // replace symbols to fit syntax
    variables.map();
    parser.parseExpression(prepared); // parse
    return this.updateResult(); // get parse result
  }

  public boolean parseCondition(String condition) throws Exception {
    boolean ok = condition.equals(""); // true if condition empty
    if (!ok) {
      Object result = evaluate(condition);
      if (result instanceof Double) {
        ok = ( ( (Double) result).equals(Double.valueOf("1.0")));
      }
    }
    return ok;
  }

  public boolean parseCondition(String condition, HashMap<String, Object>
      replace) throws Exception {
    boolean ok = condition.equals(""); // true if condition does not exist
    if (!ok) {
      Iterator<Map.Entry<String, Object>> iterator = replace.entrySet().
          iterator();
      while (iterator.hasNext()) { // add varables to parser one by one
        Map.Entry<String, Object> entry = iterator.next();
        String variable = entry.getKey();
        Object value = entry.getValue();
        variables.replace(variable, value);
      }
      Object result = this.evaluate(condition);
      if (result instanceof Double) {
        ok = ( ( (Double) result).equals(Double.valueOf("1.0")));
      }
    }
    return ok;
  }

  private Object updateResult() throws Exception {    
    Object result = parser.getValueAsObject(); // Get the value    
    String errorInfo = parser.getErrorInfo(); // Get the error information
    if ( errorInfo != null) {
      throw new Exception(errorInfo);
    }
    return result;
  }

  private String prepare(String condition) {
    String result = condition.replaceAll(OLD_AND, NEW_AND);
    result = result.replaceAll(OLD_OR, NEW_OR);
    return result;
  }

  /**
   *
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
  private class Variables
      implements Cloneable {

    private HashMap<String, Object> variables;
    private AssignmentModel assignment;
    /**
     * Variables
     *
     * @param assignment Assignment
     */
    public Variables(AssignmentModel assignment) {
      super();
      this.assignment = assignment;
      variables = new HashMap<String, Object>();
    }

    public void replace(String variable, Object value) {
      this.variables.put(variable, value);
    }

    /**
     * Gets varables form asssignment. Currently, data fields in assignment are
     * extracted as varables.
     */
    private void update() {
      // get all data elements from assignment
      for (int i = 0; i < assignment.getDataCount(); i++) {
        Object e = assignment.dataAt(i);
        if (e != null) {
          if (e instanceof DataField) { // if DataField then it is assignment
            DataField field = (DataField) e;
            // get value of the data field
            this.variables.put(field.getName(),
                               field.getObjectValue(field.getValue()));
          }
          else if (e instanceof DataElement) { // if DataElement then it is assignment model
            DataElement element = (DataElement) e;
            // get initial value of the data element
            this.variables.put(element.getName(),
                               element.getObjectValue(element.getInitial()));
          }
        }
      }
    }

    /**
     * Adds all variables to the parser.
     *
     * @param expression String
     */
    void map() {
      this.update(); // get all varables from assignemnt
      Iterator<Map.Entry<String, Object>> iterator = variables.entrySet().
          iterator();
      while (iterator.hasNext()) { // add varables to parser one by one
        Map.Entry<String, Object> entry = iterator.next();
        String variable = entry.getKey();
        Object value = entry.getValue();
        parser.addVariable(variable, value);
      }
    }
  }
}
