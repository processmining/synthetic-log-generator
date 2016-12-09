package nl.tue.declare.graph.template;

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

import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.template.*;
import nl.tue.declare.graph.*;
import nl.tue.declare.graph.model.*;
import nl.tue.declare.utils.*;

public class TemplateView {

  ConstraintTemplate template;
  AssignmentModel model;
  AssignmentModelView view;

  public TemplateView(ConstraintTemplate anTemplate) {
    super();
    model = new AssignmentModel(anTemplate.getLanguage());
    view = new AssignmentModelView(model);
    template = anTemplate;
    model.addListener(view);
    if (anTemplate != null) {
      this.createCells();
    }
  }

  /**
   * createGraph
   *
   * @return DGraph
   */
  public DGraph createGraph() {
    view.getGraph().getSelectionModel().clearSelection();
    return view.getGraph();
  }

  /**
   * getCellsBinary
   */
  private void createCells() {
    ConstraintDefinition constraint = model.createConstraintDefinition(template);
    //int count = template.parameterCount();
    ActivityDefinitonCell[] cells = new ActivityDefinitonCell[constraint.
        parameterCount()];
    // ActivityDefinition [] params = new ActivityDefinition [count];
    double total = 0;
    int c =0;
    for (Parameter parameter: constraint.getParameters()) {

      ActivityDefinition activityDefinition = model.addActivityDefinition();
      activityDefinition.setName(parameter.getName());
      //params[i] = activityDefinition;
      constraint.addBranch(parameter,activityDefinition);

      ActivityDefinitonCell activityDefinitionCell = view.
          getActivityDefinitionCell(activityDefinition);
      total = activityDefinitionCell.getWidth() + 10;
      cells[c++] = activityDefinitionCell;
    }

    double radius = total / 2 + 100;
    Point[] points = Circle.getPoints(radius, cells.length);

    for (int i = 0; i < cells.length; i++) {
      ActivityDefinitonCell cell = cells[i];
      int x = points[i].x + (new Double(cell.getWidth() / 2)).intValue();
      int y = points[i].y + (new Double(cell.getHeight() / 2)).intValue();
      cell.setPosition(new Point(x, y));
    }
    model.addConstraintDefiniton(constraint);
  }

}
