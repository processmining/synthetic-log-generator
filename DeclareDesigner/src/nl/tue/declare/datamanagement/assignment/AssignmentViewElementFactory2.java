package nl.tue.declare.datamanagement.assignment;

import nl.tue.declare.datamanagement.XMLBroker;

import org.w3c.dom.Element;
import java.awt.geom.Rectangle2D;
import nl.tue.declare.domain.model.ConstraintDefinition;
import java.util.*;
import nl.tue.declare.domain.model.ActivityDefinition;
import nl.tue.declare.graph.DVertex;
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
public class AssignmentViewElementFactory2  extends AssignmentElementFactory{

     private static final String TAG_GRAPHICAL = "graphical";
     private static final String TAG_GRAPHICAL_CELLS = "activities";
     private static final String TAG_GRAPHICAL_CONNECTORS = "constraints";
     private static final String TAG_BOUND = "cell";
     private static final String TAG_BOUNDS_ID = "id";
     private static final String TAG_BOUNDS_X = "x";
     private static final String TAG_BOUNDS_Y = "y";
     private static final String TAG_BOUNDS_WIDTH = "width";
     private static final String TAG_BOUNDS_HEIGHT = "height";

     /**
      * ElementFactory
      *
      * @param aBroker XMLBroker
      */
     public AssignmentViewElementFactory2(XMLBroker broker) {
       super(broker);
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

       Element element = super.createAssignmentElement(model);
       element.appendChild(this.assignmentGraphicalToElement(view));
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
       Element element = createElement(TAG_GRAPHICAL);
           // create elements for all cells/activities
       Element cellsTag = createElement(TAG_GRAPHICAL_CELLS);
       getAllBounds(cellsTag, new ArrayList<DVertex>(view.activityDefinitionCells()));
       element.appendChild(cellsTag);
           // create elements for all connectors/constraints
       Element connectorsTag = createElement(TAG_GRAPHICAL_CONNECTORS);
       getAllBounds(connectorsTag, new ArrayList<DVertex>(view.connectorCells()));
       element.appendChild(connectorsTag);

       return element;
     }

    private void getAllBounds(Element element, List<DVertex> cells){
      for (int i = 0; i < cells.size(); i++) {
          element.appendChild(boundsToElement(cells.get(i)));
       }
    }

     private Element boundsToElement(DVertex cell) {
       Element element = createElement(TAG_BOUND);

       Rectangle2D bounds = cell.getBounds();

       String x = String.valueOf(bounds.getX());
       String y = String.valueOf(bounds.getY());
       String width = String.valueOf(bounds.getWidth());
       String height = String.valueOf(bounds.getHeight());
       String id = cell.getBase().getIdString();

       this.setAttribute(element, TAG_BOUNDS_ID, id);
       this.setAttribute(element, TAG_BOUNDS_X, x);
       this.setAttribute(element, TAG_BOUNDS_Y, y);
       this.setAttribute(element, TAG_BOUNDS_WIDTH,
                         width);
       this.setAttribute(element, TAG_BOUNDS_HEIGHT,
                         height);
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
       Element assignment = super.getAssignmentElement(element);
       // create the tag for the view / graphical representation
       Element graphTag = getFirstElement(assignment,TAG_GRAPHICAL);
       Element cellsTag = getFirstElement(graphTag,TAG_GRAPHICAL_CELLS);
       Element connectorsTag = getFirstElement(graphTag,TAG_GRAPHICAL_CONNECTORS);

       NodeList cells = cellsTag.getElementsByTagName(TAG_BOUND);
       // loop through the all cells
       for (int i = 0; i < cells.getLength(); i++) {
         Element cell = (Element) cells.item(i);
         // create the element for every cell
         int id = Integer.
             valueOf(cell.getAttribute(TAG_BOUNDS_ID));
         Rectangle2D bounds = elementToBouds(cell);
         ActivityDefinition activityDefinition = model.activityDefinitionWithId(id);
         view.getActivityDefinitionCell(activityDefinition).setBounds(bounds);
       }

       cells = connectorsTag.getElementsByTagName(TAG_BOUND);
       // loop through the all cells
       for (int i = 0; i < cells.getLength(); i++) {
         Element cell = (Element) cells.item(i);
         int id = Integer.
             valueOf(cell.getAttribute(TAG_BOUNDS_ID));
         Rectangle2D bounds = this.elementToBouds(cell);
         ConstraintDefinition constraint = model.constraintWithId(id);
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
       String x = element.getAttribute(TAG_BOUNDS_X);
       String y = element.getAttribute(TAG_BOUNDS_Y);
       String width = element.getAttribute(TAG_BOUNDS_WIDTH);
       String height = element.getAttribute(TAG_BOUNDS_HEIGHT);

       // convert strings to desired types
       double dx = Double.valueOf(x);
       double dy = Double.valueOf(y);
       double dwidth = Double.valueOf(width);
       double dheight = Double.valueOf(height);

       return new Rectangle2D.Double(dx, dy, dwidth, dheight);
    }
}
