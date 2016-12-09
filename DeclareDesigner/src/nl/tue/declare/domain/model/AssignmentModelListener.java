package nl.tue.declare.domain.model;

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
public interface AssignmentModelListener {

  /**
   * addActivityDefinition
   *
   * @param activityDefinition ActivityDefinition
   */
  public void addActivityDefinition(ActivityDefinition activityDefinition);

  /**
   * updateActivityDefinition
   *
   * @param activityDefinition ActivityDefinition
   */
  public void updateActivityDefinition(ActivityDefinition activityDefinition);

  /**
   * deleteActivityDefinition
   *
   * @param activityDefinition ActivityDefinition
   */
  public void deleteActivityDefinition(ActivityDefinition activityDefinition);

  /**
   * addConstraintDefinition
   *
   * @param constraintDefinition ConstraintDefinition
   */
  public void addConstraintDefinition(ConstraintDefinition constraintDefinition);

  /**
   * updateConstraintDefinition
   *
   * @param constraintDefinition ConstraintDefinition
   */
  public void updateConstraintDefinition(ConstraintDefinition
                                         constraintDefinition);

  /**
   * deleteConstraintDefiniton
   *
   * @param constraintDefinition ConstraintDefinition
   */
  public void deleteConstraintDefiniton(ConstraintDefinition
                                        constraintDefinition);

  /**
   * addBranch
   *
   * @param constraintDefinition ConstraintDefinition
   * @param activityDefinition ActivityDefinition
   */
  // public void addBranch(ConstraintDefinition constraintDefinition, ActivityDefinition activityDefinition);

  /**
   * removeBranch
   *
   * @param constraintDefinition ConstraintDefinition
   * @param activityDefinition ActivityDefinition
   */
  public void deleteBranch(ConstraintDefinition constraintDefinition,
                           ActivityDefinition activityDefinition);

}
