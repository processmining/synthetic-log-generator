package nl.tue.declare.control;

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

//import java.util.*;

import nl.tue.declare.datamanagement.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.template.*;
import nl.tue.declare.graph.model.*;

public class AssignmentModelControl {

  private final String NEW_MODEL = "new model";

  private static AssignmentModelControl INSTANCE = null;

  private AssignmentModelControl() {
    super();
  }

  static AssignmentModelControl singleton() {
    if (INSTANCE == null) {
      INSTANCE = new AssignmentModelControl();
    }
    return INSTANCE;
  }

  /**
   * createModel
   *
   * @return AssignmentModel
   */
  private AssignmentModel createModel(Language lang) {
    AssignmentModel model = new AssignmentModel(lang);
    model.setName(this.NEW_MODEL);
    return model;
  }

  /**
   * addModel
   *
   * @return AssignmentModel
   */
  public AssignmentModel newModel(Language lang) {
    AssignmentModel model = this.createModel(lang);
    //return models.add(model)? model : null;
    return model;
  }

  /**
   * addActivityDefinition
   *
   * @param model AssignmentModel
   * @return ActivityDefinition
   */
  public ActivityDefinition addActivityDefinition(AssignmentModel model) {
    return model.addActivityDefinition();
  }

  /**
   * addConstraintDefinition
   *
   * @param source ActivityDefinition
   * @param target ActivityDefinition
   * @param template ConstraintTemplate
   * @return ConstraintDefinition
   */
  /* public ConstraintDefinition  addConstraintDefinition(ActivityDefinition source, ActivityDefinition target, ConstraintTemplate template) {
     ConstraintDefinition result = null;
     if ( source.getAssignmentModel() == target.getAssignmentModel() ) {
       AssignmentModel model = source.getAssignmentModel();
           //result = model.addConstraintDefinition(template, source, target);
           //result = model.addConstraintDefinition(template, new ActivityDefinition[] {source, target});
           result = model.addConstraintDefinition(template);
           result.ac
     }
     return result;
   }*/

  /**
   * addConstraintDefinition
   *
   * @param source ActivityDefinition
   * @param template ConstraintTemplate
   * @return ConstraintDefinition
   */
  /*public ConstraintDefinition  addConstraintDefinition(ActivityDefinition source, ConstraintTemplate template) {
    ConstraintDefinition result = null;
    if ( source != null ) {
      AssignmentModel model = source.getAssignmentModel();
          result = model.addConstraintDefinition(template, /*source*/
   /* new ActivityDefinition[] {source});
        }
        return result;
      }*/

   /**
    * editActivityDefinition
    *
    * @param copy ActivityDefinition
    * @return boolean
    */
   public boolean editActivityDefinition(ActivityDefinition copy) {
     return copy.getAssignmentModel().editActivityDefinition(copy);
   }

  /**
   * addAssignmentModel
   */
  public void addAssignmentModel(AssignmentModel model, String path) {
    AssignmentBroker broker = XMLBrokerFactory.newAssignmentBroker(path);
    broker.addAssignment(model /*,path*/);
  }

  /**
   * addAssignmentModel
   */
  public void addAssignmentModelAndView(AssignmentModel model,
                                        AssignmentModelView view, String path) {
    AssignmentViewBroker broker = XMLBrokerFactory.newAssignmentBroker(path);
    broker.addAssignmentAndView(model, view);
  }

  /**
   * addAssignmentModel
   */
  public AssignmentModel getAssignmentModel(String path) {
    AssignmentBroker broker = XMLBrokerFactory.newAssignmentBroker(path);
    return broker.readAssignment( /*path*/);
  }

  /**
   * addAssignmentModel
   */
  public void getAssignmentModelGraphical(AssignmentModel model,
                                          AssignmentModelView view, String path) {
    AssignmentViewBroker broker = XMLBrokerFactory.newAssignmentBroker(path);
    broker.readAssignmentGraphical(model, view /*, path*/);
  }

  /**
   * addAssignmentModel
   */
  public void getAssignmentModelGraphicalFromString(AssignmentModel model,
      AssignmentModelView view, String string) {
    AssignmentViewBroker broker = XMLBrokerFactory.newAssignmentBroker();
    broker.readAssignmentGraphicalFromString(model, view, string);
  }
}
