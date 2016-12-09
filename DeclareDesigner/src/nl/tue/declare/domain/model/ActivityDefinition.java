/* Generated by Together */

package nl.tue.declare.domain.model;

import java.util.*;

import nl.tue.declare.domain.*;
import yawlservice.ExternalCase;

public class ActivityDefinition
    extends Base {

  protected static final String DURATION = "duration";

  private AssignmentModel assignmentModel;

  private String name;

  private Authorization authorization;

  protected ActivityDataModel dataModel;

   /*
    * externalSpecification and externalDecomposition are used for launching a
    * YAWL case for this activity (instead letting DECLARE users execute the
    * activity in DECLARE WORKLIST). externalSpecification is the specificationId
    * in a YAWL model (e.g., "test.ywl"). externalDecomposition is the decompositionId
    * of the net in the same YALW model (e.g., "main").
    *
    * externalSpecification and externalDecomposition are optional fields.
    * if externalSpecification and/or externalDecomposition are null, then this activity
    * is not forwarded to YAWL
    */
  protected ExternalCase externalCase = null;
  protected boolean external;

  /**
   * ActivityDefinition
   *
   * @param anId int
   * @param model AssignmentModel
   */
  public ActivityDefinition(int anId, AssignmentModel model) {
    super(anId);
    this.name = "";
    this.assignmentModel = model;
    this.setExternal(false);
    this.authorization = new Authorization(this);
    this.dataModel = new ActivityDataModel(this);

  }

  /**
   * ActivityDefinition
   *
   * @param anName String
   * @param anId int
   * @param model AssignmentModel
   */
  public ActivityDefinition(String anName, int anId, AssignmentModel model) {
    this(anId, model);
    this.name = anName;
  }

  /**
   * ActivityDefinition
   *
   * @param model AssignmentModel
   * @param definition int
   */
  public ActivityDefinition(AssignmentModel model,
                            ActivityDefinition definition) {
    super(definition);
    this.name = definition.getName();
    this.assignmentModel = model;
    this.setExternal(definition.isExternal());
    if (definition.externalCase != null){
      this.externalCase = (ExternalCase) definition.externalCase.clone();
    }
    this.authorization = definition.authorization;
    this.dataModel = new ActivityDataModel(this);
  }

  protected ActivityDataDefinition cretateDataDefiniton(ActivityDataDefinition
      data) {
    return (ActivityDataDefinition) data.clone();
  }

  /**
   * equals
   *
   * @param anObject Object
   * @return boolean
   */
  public boolean equals(Object anObject) {
    boolean result = false;
    if (anObject != null) {
      if (anObject instanceof ActivityDefinition) {
        ActivityDefinition anActivityDefinition = (ActivityDefinition) anObject;
        result = ( (anActivityDefinition.getId() == this.getId()) ||
                  (anActivityDefinition.getName().equals(this.getName())));
      }
    }
    return result;
  }
  
  
  public int hashCode(){
	  return this.getId();
  }

  /**
   * toString
   *
   * @return String
   */
  public String toString() {
    return name;
  }

  /**
   * clone
   *
   * @return Object
   */
  public Object clone() {
    ActivityDefinition clone = new ActivityDefinition(this.getName(),
        this.getId(), this.assignmentModel);
    clone.dataModel = (ActivityDataModel)this.dataModel.clone();
    clone.authorization = (Authorization)this.authorization.clone();
    clone.setExternal(this.isExternal());
    if (this.externalCase != null){
      clone.externalCase = (ExternalCase)this.externalCase.clone();
    }

    return clone;
  }

  /**
   * setName
   *
   * @param name String
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * getName
   *
   * @return String
   */
  public String getName() {
    return name;
  }

  public Authorization getAuthorization() {
    return authorization;
  }

  /**
   * getModel
   *
   * @return AssignmentModel
   */
  public AssignmentModel getAssignmentModel() {
    return this.assignmentModel;
  }

  /**
   * setModel
   *
   * @param model AssignmentModel
   */
  protected void setAssignmentModel(AssignmentModel model) {
    this.assignmentModel = model;
  }

  public void setExternal(boolean external) {
    if ((!this.external) && external){
      this.externalCase = new ExternalCase();
    };
    if (!external){
      this.externalCase = null;
    }
     this.external = external;
  }

  public boolean isExternal(){
    return external;
  }


  public ExternalCase getExternalCase(){
    return this.externalCase;
  }

  public ActivityDataDefinition addDataElement(DataElement data) {
    ActivityDataDefinition element = null;
    if (!dataModel.contains(data)) {
      element = createData(data);
      dataModel.add(element);
    }
    return element;
  }

  protected ActivityDataDefinition createData(DataElement data) {
    return new ActivityDataDefinition(dataModel.nextElementId(), data);
  }

  public int dataCount() {
    return dataModel.count();
  }

  public Iterator<ActivityDataDefinition> data() {
    return dataModel.elements();
  }

  boolean contains(DataElement element) {
    return dataModel.contains(element);
  }

  public Object[] availableDataElements() {
    return dataModel.availableDataElements();
  }

  boolean add(ActivityDataDefinition element) {
    return dataModel.add(element);
  }

  public void remove(ActivityDataDefinition element) {
    dataModel.remove(element);
  }
}
