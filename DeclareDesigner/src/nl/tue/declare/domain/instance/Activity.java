package nl.tue.declare.domain.instance;

import java.util.*;

import nl.tue.declare.domain.model.*;

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
public class Activity
    extends ActivityDefinition {

  /**
   * Activity
   *
   * @param activityDefinition ActivityDefinition
   * @param anAssignment Assignment
   */
  public Activity(ActivityDefinition activityDefinition,
                  Assignment anAssignment) {
    super(anAssignment, activityDefinition);
    this.createDataModel(activityDefinition);
  }

  public Assignment getAssignment() {
    return (Assignment)super.getAssignmentModel();
  }

  /**
   * clone
   *
   * @return Object
   */
  public Object clone() {
    Activity clone = new Activity(this, this.getAssignment());
    return clone;
  }

 private void createDataModel(ActivityDefinition activityDefinition) {
    Iterator<ActivityDataDefinition> i = activityDefinition.data();
    while (i.hasNext()) {
      ActivityDataDefinition definition = i.next();
      ActivityData data = new ActivityData(definition);
      DataElement element = this.getAssignment().getDataWithId(definition.
          getDataElement().getId());
      data.setDataElement(element);
      this.dataModel.addElement(data);
    }
  }

  protected ActivityDataDefinition cretateDataDefiniton(ActivityDataDefinition
      data) {
    ActivityData activityData = new ActivityData(data);
    return activityData;
  }

  protected ActivityDataDefinition createData(DataElement data) {
    return new ActivityData(dataModel.nextElementId(), data);
  }


}
