package nl.tue.declare.datamanagement.assignment;

import nl.tue.declare.graph.DVertex;
import nl.tue.declare.graph.model.ConstraintConnector;
import nl.tue.declare.datamanagement.XMLBroker;

import org.w3c.dom.Element;
import nl.tue.declare.graph.model.ActivityDefinitonCell;
import java.awt.geom.Rectangle2D;
import nl.tue.declare.domain.model.ConstraintDefinition;
import java.util.List;
import nl.tue.declare.domain.model.ActivityDefinition;
import nl.tue.declare.graph.model.AssignmentModelView;
import nl.tue.declare.domain.model.AssignmentModel;
import org.w3c.dom.NodeList;

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
public class AssignmentViewElementFactory
    extends AssignmentElementFactory {

  private static final String TAG_ASSIGNMENT_GRAPHICAL = "graphical";
  private static final String TAG_ASSIGNMENT_GRAPHICAL_CELLS = "cells";
  private static final String TAG_ACTIVITY_DEFINITION_GRAPHICAL = "cell";
  private static final String TAG_ACTIVITY_DEFINITION_GRAPHICAL_ID =
      "activitydefinition";
  private static final String TAG_ACTIVITY_DEFINITION_GRAPHICAL_X = "x";
  private static final String TAG_ACTIVITY_DEFINITION_GRAPHICAL_Y = "y";
  private static final String TAG_ACTIVITY_DEFINITION_GRAPHICAL_WIDTH =
      "width";
  private static final String TAG_ACTIVITY_DEFINITION_GRAPHICAL_HEIGHT =
      "height";

  private static final String TAG_ASSIGNMENT_GRAPHICAL_CONNECTORS =
      "connectors";
  private static final String TAG_CONNECTOR_GRAPHICAL = "cell";
  private static final String TAG_CONSTRAINT_GRAPHICAL_ID =
      "constraintdefinition";
  //private static final String TAG_CONNECTOR_GRAPHICAL_X = "x";
  //private static final String TAG_CONNECTOR_GRAPHICAL_Y = "y";

  /**
   * ElementFactory
   *
   * @param aBroker XMLBroker
   */
  public AssignmentViewElementFactory(XMLBroker aBroker) {
    super(aBroker);
  }

  /**
   * createAssignmentElement
   *
   * @param model model
   * @param view AssignmentModelView
   * @return Element
   */
  public Element createAssignmentElement(AssignmentModel model,
                                         AssignmentModelView view) {

    // *** fill the element with template attributes
    Element element = super.createAssignmentElement(model);
    element.appendChild(this.assignmentGraphicalToElement(view));

    // *** return the template element
    return element;
  }

  /**
   * assignmentGraphicalToElement
   *
   * @param view model
   * @return Element
   */
  private Element assignmentGraphicalToElement(AssignmentModelView view) {
    // create the tag for the view / graphical representation
    Element element = createElement(TAG_ASSIGNMENT_GRAPHICAL);

    Element cellsTag = createElement(TAG_ASSIGNMENT_GRAPHICAL_CELLS);

    List<ActivityDefinitonCell> cells = view.activityDefinitionCells();
    // loop through the all cells
    for (int i = 0; i < cells.size(); i++) {
      // create the element for every cell
      Element cell = this.activityDefinitionCellToElement(cells.get(i));
      // add the element to the view / graphical element
      cellsTag.appendChild(cell);
    }
    element.appendChild(cellsTag);

    Element connectorsTag = createElement(TAG_ASSIGNMENT_GRAPHICAL_CONNECTORS);

    List<ConstraintConnector> connectors = view.connectorCells();
    // loop through the all cells
    for (int i = 0; i < connectors.size(); i++) {
      // create the element for every cell
      Element cell = this.activityDefinitionCellToElement(connectors.get(i));
      // add the element to the view / graphical element
      connectorsTag.appendChild(cell);
    }
    element.appendChild(connectorsTag);

    return element;
  }

  private Element activityDefinitionCellToElement(DVertex cell) {

    Element element = createElement(TAG_ACTIVITY_DEFINITION_GRAPHICAL);

    Rectangle2D bounds = cell.getBounds();

    String x = String.valueOf(bounds.getX());
    String y = String.valueOf(bounds.getY());
    String width = String.valueOf(bounds.getWidth());
    String height = String.valueOf(bounds.getHeight());
    String id = cell.getBase().getIdString();

    this.setAttribute(element, TAG_ACTIVITY_DEFINITION_GRAPHICAL_ID, id);
    this.setAttribute(element, TAG_ACTIVITY_DEFINITION_GRAPHICAL_X, x);
    this.setAttribute(element, TAG_ACTIVITY_DEFINITION_GRAPHICAL_Y, y);
    this.setAttribute(element, TAG_ACTIVITY_DEFINITION_GRAPHICAL_WIDTH,
                      width);
    this.setAttribute(element, TAG_ACTIVITY_DEFINITION_GRAPHICAL_HEIGHT,
                      height);

    //  return the event element
    return element;
  }

  /**
   * elementToAssignmentGraphical
   *
   * @param view model
   * @param model AssignmentModel
   * @param element Element
   */
  public void elementToAssignmentGraphical(AssignmentModelView view,
                                            AssignmentModel model,
                                            Element element) {
    //Element assignment = broker.getFirstElement(element, this.TAG_ASSIGNMENT);
    Element assignment = super.getAssignmentElement(element);
    // create the tag for the view / graphical representation
    Element graphTag = getFirstElement(assignment,TAG_ASSIGNMENT_GRAPHICAL);
    Element cellsTag = getFirstElement(graphTag,TAG_ASSIGNMENT_GRAPHICAL_CELLS);

    NodeList cells = cellsTag.getElementsByTagName(TAG_ACTIVITY_DEFINITION_GRAPHICAL);
    // loop through the all cells
    for (int i = 0; i < cells.getLength(); i++) {
      Element cell = (Element) cells.item(i);
      // create the element for every cell
      String id = cell.getAttribute(TAG_ACTIVITY_DEFINITION_GRAPHICAL_ID);
      ActivityDefinition activityDefinition = model.activityDefinitionWithId(
          Integer.valueOf(id));
      Rectangle2D bounds = this.elementToBouds(cell);
      view.getActivityDefinitionCell(activityDefinition).setBounds(bounds);
    }

    Element connectorsTag=getFirstElement(graphTag,TAG_ASSIGNMENT_GRAPHICAL_CONNECTORS);

    cells = connectorsTag.getElementsByTagName(TAG_CONNECTOR_GRAPHICAL);
    // loop through the all cells
    for (int i = 0; i < cells.getLength(); i++) {
      Element cell = (Element) cells.item(i);
      String id = cell.getAttribute(TAG_CONSTRAINT_GRAPHICAL_ID);
      if (id.equals("")){ // this was an error in previous versions, but I don't want to
                          // waste all old models
        id = cell.getAttribute(TAG_ACTIVITY_DEFINITION_GRAPHICAL_ID);
      }
      Rectangle2D bounds = this.elementToBouds(cell);

      ConstraintDefinition constraint = model.constraintWithId(Integer.
          valueOf(id));
      view.getConnector(constraint).setBounds(bounds);
    }
    view.updateUI();
  }

  /**
   *
   * @param element Element
   * @return Rectangle2D
   */
  private Rectangle2D elementToBouds(Element element) {
    // get all attributes
    String x = element.getAttribute(TAG_ACTIVITY_DEFINITION_GRAPHICAL_X);
    String y = element.getAttribute(TAG_ACTIVITY_DEFINITION_GRAPHICAL_Y);
    String width = element.getAttribute(TAG_ACTIVITY_DEFINITION_GRAPHICAL_WIDTH);
    String height = element.getAttribute(TAG_ACTIVITY_DEFINITION_GRAPHICAL_HEIGHT);

    // convert strings to desired types
    double dx = Double.valueOf(x);
    double dy = Double.valueOf(y);
    double dwidth = Double.valueOf(width);
    double dheight = Double.valueOf(height);

    return new Rectangle2D.Double(dx, dy, dwidth, dheight);
  }
}
