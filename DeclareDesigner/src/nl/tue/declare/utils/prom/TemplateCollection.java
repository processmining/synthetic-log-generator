package nl.tue.declare.utils.prom;

import java.io.*;

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
class TemplateCollection
    extends AbstractCollection {

  TemplateCollection(PrintStream out) {
    super(new ConstraintParser(), out);
  }

  void add(ConstraintTemplate template) {
    lines.add("");
    lines.add(SEPARATOR);
    lines.add("");
    lines.add(header(template));
    lines.add("{");
    lines.add("    <h2>" + template.getDisplay() + "</h2>");
    lines.add("    <p> " + template.getDescription() + "</p>");
    lines.add("    <p> Arguments:<br>");
    lines.add("    <ul>");

    for (Parameter parameter: template.getParameters()) {
      lines.add(
          "    <li><b>" + parameter.getName() +
          "</b> of type set (<i>ate.WorkflowModelElement</i>)</li>");
    }
    lines.add("    </ul>");
    lines.add("    </p>");
    lines.add("}   ");
    lines.add(" " + formula(template) + " ;");
  }

  private String header(ConstraintTemplate template) {
    String parameters = "";
    for (Parameter parameter: template.getParameters()) {
      if (!parameters.equals("")) {
        parameters += ", ";
      }
      parameters += parameter.getName() + ": " + WorkflowModelElement;
    }

    return "formula " + template.getName().replaceAll(" ", "_") + " ( " +
        parameters + " ) :=";
  }
}
