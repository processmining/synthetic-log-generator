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
public class Control {

  private static Control instance = null;

  private OrganizationControl organization;
  private ConstraintTemplateControl constraintTemplate;
  private AssignmentModelControl assignmentModel;

  private Control() {
    super();
    constraintTemplate = ConstraintTemplateControl.singleton();
    organization = OrganizationControl.singleton();
    assignmentModel = AssignmentModelControl.singleton();
  }

  public OrganizationControl getOrganization() {
    return organization;
  }

  public ConstraintTemplateControl getConstraintTemplate() {
    return constraintTemplate;
  }

  public AssignmentModelControl getAssignmentModel() {
    return assignmentModel;
  }

  public static Control singleton() {
    if (instance == null) {
      instance = new Control();
    }
    return instance;
  }
}
