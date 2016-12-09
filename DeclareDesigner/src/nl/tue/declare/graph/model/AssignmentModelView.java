package nl.tue.declare.graph.model;

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
import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.geom.*;

import org.jgraph.graph.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.template.Parameter;
import nl.tue.declare.graph.*;

public class AssignmentModelView
    extends ModelView implements AssignmentModelListener {

  AssignmentModel model;
  DGraphSelectionListener graphListener;

  public AssignmentModelView(AssignmentModel aModel) {
    super();
    model = aModel;
    model.addListener(this);
    graphListener = new DGraphSelectionListener(graph);
    //graph.getGraphLayoutCache().setFactory(new LTLCellViewFactory());
    // two last lines are added on 04-07-2006
    refreshCells();
    clearSelection();
    graph.updateUI();
    graph.repaint();
  }

  /**
   * addActivityDefinition
   *
   * @param activityDefinition ActivityDefinition
   * @param point Point2D
   */
  public void addActivityDefinition(ActivityDefinition activityDefinition,
                                    Point2D point) {
    Double dx = new Double(point.getX());
    Double dy = new Double(point.getY());
    ActivityDefinitonCell view = this.createActivityCell(activityDefinition,
        dx.intValue(), dy.intValue());
    addVertex(view);
  }

  protected ActivityDefinitonCell createActivityCell(ActivityDefinition
      activityDefinition, int x, int y) {
    ActivityDefinitonCell view = new ActivityDefinitonCell(activityDefinition,
        x, y);
    return view;
  }

  /**
   * addConstraintDefinition
   *
   * @param object ConstraintDefinition
   */
  private void removeObject(Object object) {
    List<Object> list = new ArrayList<Object>();
    list.add(object);
    this.removeObjects(list);
  }

  /**
   * removeCell
   *
   * @param objects List
   */
  private void removeObjects(List<Object> objects) {
    // a list where all DefaulGraphCell objects will be stored
    List<Object> toRemove = new ArrayList<Object>();

    // loop through the list of user objects to find the DefaulGraphCell objects
    // that contain them as user objects
    for (int i = 0; i < objects.size(); i++) {
      Object object = objects.get(i);
      List<DefaultGraphCell> cells = getCells(object);
      if (cells != null) {
        toRemove.addAll(cells);
      }
    }
    removeCells(toRemove.toArray());
  }

  /**
   * editConstraintDefinition
   *
   * @param constraintDefinition ConstraintDefinition
   */
  public void editConstraintDefinition(ConstraintDefinition
                                       constraintDefinition) {
    if (constraintDefinition != null) {
      List<DefaultGraphCell> cells = this.getCells(constraintDefinition);
      if (cells == null) {
        return;
      }
      Iterator<DefaultGraphCell> iterator = cells.iterator();
      while (iterator.hasNext()) {
        DefaultGraphCell cell = iterator.next();
        if (cell instanceof ConstraintDefinitionEdge) {
          ( (ConstraintDefinitionEdge) cell).update();
          updateUI(cell);
        }
      }
    }
  }

  public void addConstraintDefinition(ConstraintDefinition constraintDefinition) {
    if (constraintDefinition != null) {

      if (constraintDefinition.isUnary()) { // unary constraint
    	Parameter parameter = constraintDefinition.getFirstParameter();
    	ActivityDefinition branch = constraintDefinition.getFirstBranch(parameter);
        ActivityDefinitonCell sourceCell = this.getView(branch);
        ConstraintDefinitionEdge edge = this.createConstraintCell(
            constraintDefinition, branch,parameter);
        edge.setLabel(true);
        addEdge(edge, sourceCell, sourceCell);
      }
      else { // binary constraint
        ConstraintConnector connector = new ConstraintConnector(
            constraintDefinition);
        addCell(connector);
        int x = 0;
        int y = 0;
        //int count = 0;
        boolean firstParameter = true;
        for (Parameter parameter: constraintDefinition.getParameters()) {
          ConstraintDefinitionEdge first = null;
          boolean firstBranch = true;
          for (ActivityDefinition branch : constraintDefinition.getBranches(parameter)) {
            ActivityDefinitonCell source = this.getView(branch);

            x += source.getBounds().getCenterX();
            y += source.getBounds().getCenterY();
            ConstraintDefinitionEdge edge = this.createConstraintCell(
                constraintDefinition, branch, parameter);

            DefaultGraphCell target = null;
            if (firstBranch) { // first branch
              first = edge;
              target = connector;
              if (firstParameter) { // only first branch of the first parameter has label
                edge.setLabel(true);
              }
            }
            else {
              target = first;
            }

            addEdge(edge, source, target);
            firstBranch = false;
            //count++;
            //f
          }
          firstParameter = false;
        }
        x /= constraintDefinition.parameterCount();
        y /= constraintDefinition.parameterCount();
        Rectangle2D bounds = new Rectangle2D.Double(x, y, 1, 1);

        connector.setBounds(bounds);
      }

    }
    super.updateUI();
  }

  protected ConstraintDefinitionEdge createConstraintCell(ConstraintDefinition
      constraint, ActivityDefinition parameter, Parameter formal) {
    return new ConstraintDefinitionEdge(constraint, parameter, formal);
   }

  public void updateActivityDefinition(ActivityDefinition activityDefinition) {
    updateUI();
  }

  public void addActivityDefinition(ActivityDefinition activityDefinition) {
    this.addActivityDefinition(activityDefinition, new Point(10, 10));
  }

  public void updateConstraintDefinition(ConstraintDefinition
                                         constraintDefinition) {
    this.editConstraintDefinition(constraintDefinition);
  }

  public void deleteActivityDefinition(ActivityDefinition activityDefinition) {
    this.removeObject(activityDefinition);
  }

  public void deleteConstraintDefiniton(ConstraintDefinition
                                        constraintDefinition) {
    this.removeObject(constraintDefinition);
  }

  /**
   *
   * @param constraintDefinition ConstraintDefinition
   * @param activityDefinition ActivityDefinition
   */
  /* public void addBranch(ConstraintDefinition constraintDefinition,
                         ActivityDefinition activityDefinition) {
     DefaultGraphCell cell = this.getCells(constraintDefinition).get(0);
     ParameterBranch branch = constraintDefinition.getBranchable().getBranch(activityDefinition);
     ConstraintDefinitionEdge constraintEdge = this.createConstraintCell(
         constraintDefinition, branch);
     ActivityDefinitonCell branchCell = this.getView(activityDefinition);
     // add the edge for the source and target cells.
     boolean forward = constraintDefinition.isForwardBranch();
     if (forward) {
       addEdge(constraintEdge, cell, branchCell);
     }
     else {
       addEdge(constraintEdge, branchCell, cell);
     }
   }*/

  /**
   *
   * @param constraintDefinition ConstraintDefinition
   * @param activityDefinition ActivityDefinition
   */
  public void deleteBranch(ConstraintDefinition constraintDefinition,
                           ActivityDefinition activityDefinition) {
    Collection<DefaultGraphCell> cells = this.getCells(constraintDefinition);
    Iterator<DefaultGraphCell> iterator = cells.iterator();
    ConstraintDefinitionEdge remove = null;
    while (iterator.hasNext()) {
      DefaultGraphCell cell = iterator.next();
      if (cell instanceof ConstraintDefinitionEdge) {
        ConstraintDefinitionEdge edge = (ConstraintDefinitionEdge) cell;
        Object source = edge.getSource();
        Object target = edge.getTarget();
        ActivityDefinitionPort port = null;
        // the edge to remove is fout if  source or/and target ports are to activity definitions
        if (source instanceof ActivityDefinitionPort) {
          port = (ActivityDefinitionPort) source;
          if (port.ActivityDefinitionView().getActivityDefinition() ==
              activityDefinition) {
            remove = edge;
          }
        }
        if (target instanceof ActivityDefinitionPort) {
          port = (ActivityDefinitionPort) target;
          if (port.ActivityDefinitionView().getActivityDefinition() ==
              activityDefinition) {
            remove = edge;
          }
        }
      }
    }
    // if we found the edge to remove
    if (remove != null) {
      Object source = remove.getSource();
      Object target = remove.getTarget();
      if (source instanceof ActivityDefinitionPort &&
          target instanceof ActivityDefinitionPort) {
        // removing the main edge

        cells = this.getCells(constraintDefinition); // get the remaining branches
        // first remove the one to be removed
        cells.remove(remove);
        // get the first one. we will make this one the main edge now
        ConstraintDefinitionEdge edge = (ConstraintDefinitionEdge) cells.
            iterator().next();

        if (! (edge.getSource() instanceof ActivityDefinitionPort)) { // if the source is lost
          edge.setSource(remove.getSource());
        }
        if (! (edge.getTarget() instanceof ActivityDefinitionPort)) { // if the target is lost
          edge.setTarget(remove.getTarget());
        }
      }
      this.removeCells(new Object[] {remove});
    }
  }

  /**
   *
   * @param activityDefinition ActivityDefinition
   * @return ActivityDefinitionView
   */
  private ActivityDefinitonCell getView(ActivityDefinition activityDefinition) {
    ActivityDefinitonCell view = null;
    List<DefaultGraphCell> cells = this.getCells(activityDefinition);
    if (cells.size() > 0) {
      DefaultGraphCell cell = cells.get(0);
      if (cell instanceof ActivityDefinitonCell) {
        view = (ActivityDefinitonCell) cell;
      }
    }
    return view;
  }

  /**
   * getActivityDefinitionCell
   *
   * @param job ActivityDefinition
   * @return ActivityDefinitionCell
   */
  public ActivityDefinitonCell getActivityDefinitionCell(ActivityDefinition job) {
    List<DefaultGraphCell> cells = this.getCells(job);
    ActivityDefinitonCell jobCell = null;
    if (cells != null) {
      if (cells.size() > 0) {
        DefaultGraphCell cell = cells.get(0);
        if (cell != null) {
          if (cell instanceof ActivityDefinitonCell) {
            jobCell = (ActivityDefinitonCell) cell;
          }
        }
      }
    }
    return jobCell;
  }

  /**
   * getActivityDefinitionCell
   *
   * @param job ActivityDefinition
   * @return ActivityDefinitionCell
   */
  public ConstraintConnector getConnector(ConstraintDefinition constraint) {
    List<DefaultGraphCell> cells = this.getCells(constraint);
    ConstraintConnector connector = null;
    if (cells != null) {
      if (cells.size() > 0) {
        DefaultGraphCell cell = cells.get(0);
        if (cell != null) {
          if (cell instanceof ConstraintConnector) {
            connector = (ConstraintConnector) cell;
          }
        }
      }
    }
    return connector;
  }

  /**
   * activityDefinitionCells
   *
   * @return List
   */
  public List<ActivityDefinitonCell> activityDefinitionCells() {
    List<DVertex> vertexes = this.vertexCells();
    List<ActivityDefinitonCell> cells = new ArrayList<ActivityDefinitonCell> ();
    for (int i = 0; i < vertexes.size(); i++) {
      DVertex vertex = vertexes.get(i);
      if (vertex instanceof ActivityDefinitonCell) {
        cells.add( (ActivityDefinitonCell) vertex);
      }
    }
    return cells;
  }

  /**
   * activityDefinitionCells
   *
   * @return List
   */
  public List<ConstraintConnector> connectorCells() {
    List<ConstraintConnector> cells = new ArrayList<ConstraintConnector> ();
    for (int i = 0; i < getModel().getRootCount(); i++) {
      Object cell = getModel().getRootAt(i);
      if (cell instanceof ConstraintConnector) {
        cells.add( (ConstraintConnector) cell);
      }
    }
    return cells;
  }

  /**
   * refreshCells
   */
  protected void refreshCells() {
    this.clear();
    for (int j = 0; j < model.activityDefinitionsCount(); j++) {
      ActivityDefinition job = model.activityDefinitionAt(j);
      this.addActivityDefinition(job);
    }

    for (int c = 0; c < model.constraintDefinitionsCount(); c++) {
      ConstraintDefinition constraint = model.constraintDefinitionAt(c);
      this.addConstraintDefinition(constraint);

      /*for (int p = 0; p < constraint.parameterCount(); p++) {
        Parameter parameter = constraint.parameterAt(p);

        // take care that we loop from the second branch: int b = 1!
        // this is because the first branch was already added when the constraint was
        // added in the first place
        for (int b = 1; b < parameter.branchesCount(); b++) {
          ParameterBranch branch = parameter.branchAt(b);
          this.addBranch(constraint, (ActivityDefinition) branch.getReal());
        }
             }*/
    }
    this.graph.updateUI();
  }

  /**
   * setBounds
   *
   * @param bounds Rectangle2D
   * @param activityDefinition ActivityDefinition
   */
  public void setBounds(Rectangle2D bounds,
                        ActivityDefinition activityDefinition) {
    ActivityDefinitonCell cell = this.getActivityDefinitionCell(
        activityDefinition);
    if (cell != null) {
      cell.setBounds(bounds);
      this.updateUI();
    }
  }

  /**
   *
   * @param cell DefaultGraphCell
   * @return boolean
   */
  protected boolean activityDefinitionViewClass(DefaultGraphCell cell) {
    return cell instanceof ActivityDefinitonCell;
  }

  /**
   *
   * @param cell DefaultGraphCell
   * @return boolean
   */
  protected boolean constraintDefinitionViewClass(DefaultGraphCell cell) {
    return cell instanceof ConstraintDefinitionEdge;
  }

  /**
   *
   * @param cell DefaultGraphCell
   * @return ActivityDefinition
   */
  protected ActivityDefinition getActivityDefiniton(DefaultGraphCell cell) {
    ActivityDefinition activityDefiniton = null;
    if (cell != null) {
      if (this.activityDefinitionViewClass(cell)) {
        activityDefiniton = ( (ActivityDefinitonCell)
                             cell).getActivityDefinition();
      }
    }
    return activityDefiniton;
  }

}
