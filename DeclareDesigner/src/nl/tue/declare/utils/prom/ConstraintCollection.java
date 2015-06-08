package nl.tue.declare.utils.prom;

import java.io.*;

import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.template.*;
import nl.tue.declare.execution.ltl.ConstraintParser;

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
public class ConstraintCollection
    extends AbstractCollection {
  public ConstraintCollection(PrintStream out) {
    super(new ConstraintParser(), out);
  }

  void add(ConstraintDefinition constraint) {
    lines.add("");
    lines.add(SEPARATOR);
    lines.add("");
    lines.add(header(constraint));
    ConstraintTemplate template = constraint;
    lines.add("{");
    lines.add("    <h2>" + template.getDisplay() + "</h2>"); // add display
    if (!template.getDescription().equals("")) {
      lines.add("    <p> " + template.getDescription() + "</p>"); // add description
    }
    // add parameters to the formula
    for (Parameter parameter: constraint.getParameters()) {
      String brn = "";
      // collect branches
      for (ActivityDefinition branch: constraint.getBranches(parameter)) {
        if (!brn.equals("")) {
          brn += " or ";
        }
        //brn += branch.getActivityDefinition().getName();
        brn += branch.getName();
      }
      lines.add("    <p> parameter(s) [" + parameter.getName() + "] ->" + brn + "</p>");
    }
    lines.add("<p> type: "+(constraint.getMandatory()?"mandatory":"optional")+"</p>");
    lines.add("}   ");
    lines.add(" " + constraintFormula(constraint) + " ;");
  }

  private String header(ConstraintDefinition constraint) {
    String parameters = "";
    for (Parameter parameter: constraint.getParameters()) {
      String brn = "";
      for (ActivityDefinition branch: constraint.getBranches(parameter)) {
        if (!brn.equals("")) {
          brn += "_";
        }
        //brn += branch.getActivityDefinition().getName().replaceAll(" ", "");
        brn += branch.getName().replaceAll(" ", "");
      }
      if (!parameters.equals("")) {
        parameters += "_";
      }
      parameters += brn;
    }
    return "formula " +
        constraint.getName().replaceAll(" ", "_") +
        "_" + parameters + " () :=";
  }

  private String constraintFormula(ConstraintDefinition constraint) {
    String result = "";
    try {
      result =  ( (ConstraintParser) parser).parse(constraint);
      
      result = formula(result);
    }
    catch (Exception ex) {
      //ignore
    }
    return result;
  }

  /*protected IProposition activity(Proposition proposition) {
    return new Proposition(WorkflowModelElement + " " + EQUALS + " \"" + proposition.getActivity() +
        "\"");
  }*/
}
