/**
 *
 */

package nl.tue.declare.domain.model;

import java.util.*;

import nl.tue.declare.domain.template.*;

public class AssignmentModel {

  private Language language;
  private List<ActivityDefinition> activities;
  private List<ConstraintDefinition> constraints;
  private AssignmentModelListener listener;

  private String name;

  private TeamModel teamModel;
  private AssignmentDataModel dataModel;

  private HashMap<String, String> attributes;

  /**
   * AssignmentModel
   *
   * @param lang Language
   */
  public AssignmentModel(Language lang) {
    super();
    language = lang;
    name = "test";
    activities = Collections.synchronizedList(new ArrayList<ActivityDefinition>());
    constraints = Collections.synchronizedList(new ArrayList<ConstraintDefinition>());
    teamModel = new TeamModel(this);
    dataModel = new AssignmentDataModel(this);
    attributes = new HashMap<String, String>();
    listener = null;
  }

  public AssignmentModel(AssignmentModel model) {
    super();
    this.language = model.language;
    activities = Collections.synchronizedList(new ArrayList<ActivityDefinition>());
    constraints = Collections.synchronizedList(new ArrayList< ConstraintDefinition>());
    teamModel = model.teamModel;
    dataModel = new AssignmentDataModel(this);
    this.listener = null;
    this.name = model.name;
    attributes = new HashMap<String, String>();
    this.attributes.putAll(model.attributes);
    createDataFields(model);
    createActivities(model);
    createConstraints(model);
  }

  public Object clone() {
    AssignmentModel clone = new AssignmentModel(this.language);
    clone.name = this.name;
    synchronized (activities) {
      Iterator<ActivityDefinition> ia = activities.iterator();
      while (ia.hasNext()) {
        clone.addActivityDefinition(ia.next());
      }
    }
    synchronized (constraints) {
      Iterator<ConstraintDefinition> ic = constraints.iterator();
      while (ic.hasNext()) {
        try {
          clone.addConstraintDefiniton(ic.next());
        }
        catch (Exception ex) {
        }
      }
    }
    clone.teamModel = (TeamModel) teamModel.clone();
    dataModel = (AssignmentDataModel) dataModel.clone();
    clone.attributes.putAll(this.attributes);
    return clone;
  }
  
  public Iterable<ConstraintDefinition> getConstraintDefinitions(){
	  return constraints;
  }

  /**
   * addactivityDefinition
   *
   * @return activityDefinition
   */
  public ActivityDefinition addActivityDefinition() {
    ActivityDefinition activityDefinition = new ActivityDefinition(this.
        nextActivityDefinitionId(), this);
    activityDefinition.setName("activity " + activityDefinition.getId());
    if (this.addActivityDefinition(activityDefinition)) {
      return activityDefinition;
    }
    return null;
  }

  private void createActivities(AssignmentModel assignmentModel) {
    for (int i = 0; i < assignmentModel.activityDefinitionsCount(); i++) {
      ActivityDefinition activityDefinition = assignmentModel.
          activityDefinitionAt(i);
      this.addActivityDefinition(activityDefinition);
    }
  }

  /**
   * addactivityDefinition
   *
   * @return activityDefinition
   * @param activityDefinition ActivityDefinition
   */
  protected boolean addActivityDefinition(ActivityDefinition activityDefinition) {
    if (activities.add(activityDefinition)) {
      if (listener != null) {
        listener.addActivityDefinition(activityDefinition);
      }
      return true;
    }
    return false;
  }

  /**
   * addactivityDefinition
   *
   * @return ActivityDefinition
   * @param id int
   */
  public ActivityDefinition addActivityDefinition(int id) {
    ActivityDefinition anActivityDefinition = new ActivityDefinition(id, this);
    anActivityDefinition.setName("job " + anActivityDefinition.getId());
    if (activities.add(anActivityDefinition)) {
      if (listener != null) {
        listener.addActivityDefinition(anActivityDefinition);
      }
      return anActivityDefinition;
    }
    return null;
  }

  /**
   * ActivityDefinitionExists
   *
   * @param anActivityDefinition ActivityDefinition
   * @return boolean
   */
  public boolean ActivityDefinitionExists(ActivityDefinition
                                          anActivityDefinition) {
    return this.activities.contains(anActivityDefinition);
  }

  /**
   * deleteActivityDefinition
   *
   * @param anActivityDefinition ActivityDefinition
   * @return boolean
   */
  public List<Object> deleteActivityDefinition(ActivityDefinition anActivityDefinition) {
    List<Object> list = new ArrayList<Object>();
    if (ActivityDefinitionExists(anActivityDefinition)) {
      if (activities.remove(anActivityDefinition)) {
        if (listener != null) {
          listener.deleteActivityDefinition(anActivityDefinition);
        }
        list.add(anActivityDefinition);
        list.addAll(deleteConstraintDefinitions(anActivityDefinition));
      }
    }
    return list;
  }

  /**
   * constraintExists
   *
   * @param constraint LTLConstraintDefinition
   * @return boolean
   */
  public boolean constraintDefinitionExists(ConstraintDefinition constraint) {
    return this.constraints.contains(constraint);
  }

  /**
   * deleteConstraint
   *
   * @param constraint LTLConstraintDefinition
   * @return boolean
   */
  public List<Object> deleteConstraintDefinition(ConstraintDefinition constraint) {
    List<Object> list = new ArrayList<Object>();
    if (constraintDefinitionExists(constraint)) {
      if (constraints.remove(constraint)) {
        if (listener != null) {
          listener.deleteConstraintDefiniton(constraint);
        }
        list.add(constraint);
      }
    }
    return list;
  }

  /**
   * jobsCount
   *
   * @return int
   */
  public int activityDefinitionsCount() {
    return activities.size();
  }

  /**
   * activityDefinitionAt
   *
   * @param anIndex int
   * @return ActivityDefinition
   */
  public ActivityDefinition activityDefinitionAt(int anIndex) {
    ActivityDefinition activityDefinition = null;
    if (anIndex < this.activityDefinitionsCount()) {
      activityDefinition = activities.get(anIndex);
    }
    return activityDefinition;
  }
  
  public Iterable<ActivityDefinition> getActivityDefinitions() {
    return activities;
  }

  /**
   * nextTemplateId
   *
   * @return int
   */
  private int nextActivityDefinitionId() {
    int id = 0;
    ActivityDefinition activityDefinition = null;
    synchronized (activities) {
      Iterator<ActivityDefinition> it = this.activities.iterator();
      while (it.hasNext()) {
        activityDefinition = it.next();
        if (id < activityDefinition.getId()) {
          id = activityDefinition.getId();
        }
      }
    }
    return++id;
  }

  /**
   * nextTemplateId
   *
   * @return int
   */
  private int nextConstraintDefinitionId() {
    int id = 0;
    ConstraintDefinition constraintDefinition = null;
    synchronized (constraints) {
      Iterator<ConstraintDefinition> it = this.constraints.iterator();
      while (it.hasNext()) {
        constraintDefinition = it.next();
        if (id < constraintDefinition.getId()) {
          id = constraintDefinition.getId();
        }
      }
    }
    return++id;
  }

  /**
   * constraintsCount
   *
   * @return int
   */
  public int constraintDefinitionsCount() {
    return this.constraints.size();
  }

  /**
   * constraintAt
   *
   * @param anIndex int
   * @return ConstraintDefinition
   */
  public ConstraintDefinition constraintDefinitionAt(int anIndex) {
    ConstraintDefinition constraintDefinition = null;
    if (anIndex < this.constraintDefinitionsCount()) {
      constraintDefinition = constraints.get(anIndex);
    }
    return constraintDefinition;
  }

  /**
   *
   * @param id int
   * @return Constraint
   */
  public ConstraintDefinition constraintWithId(int id) {
    ConstraintDefinition constraint = null;
    boolean found = false;
    synchronized (constraints) {
      Iterator<ConstraintDefinition> iterator = this.constraints.iterator();
      while (iterator.hasNext() && !found) {
        constraint = iterator.next();
        found = constraint.getId() == id;
      }
    }
    return found ? constraint : null;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  /**
   * Adds a binary constraint defintion.
   *
   * @param template ConstraintTemplate
   * @return ConstraintDefinition
   */
  public ConstraintDefinition createConstraintDefinition(ConstraintTemplate
      template) {
	  try{
    return new ConstraintDefinition(nextConstraintDefinitionId(), this,
                                    template);
    } catch (Throwable t){
    	t.printStackTrace();
    	return null;
    }
  }

  /**
   * Adds a unary constraint defintion.
   *
   * @param constraint int
   * @return ConstraintDefinition / /* public ConstraintDefinition
    *   addConstraintDefinition(int id, ConstraintTemplate template,
    *   ActivityDefinition param) { ConstraintDefinition constraint = null; if
    *   (template != null && param != null) { if (template.isUnary() && param !=
    *   null) { constraint = new ConstraintDefinition(id, this, template);
    *   constraint.addParameter(param); } } if (constraint != null) if
    *   (this.addConstraintDefiniton(constraint)) { return constraint; } return
    *   null; }
    */
   public boolean addConstraintDefiniton(ConstraintDefinition constraint) {
     if (constraint != null) {
       if (!constraintDefinitionExists(constraint)) {
         constraints.add(constraint);
         if (listener != null) {
           listener.addConstraintDefinition(constraint);
         }
         return true;
       }
     }
     return false;
   }

  /**
   * createActivities
   *
   * @param assignmentModel AssignmentModel
   */
  private void createConstraints(AssignmentModel assignmentModel) {
    for (int i = 0; i < assignmentModel.constraintDefinitionsCount(); i++) {
      ConstraintDefinition constraintDefinition = assignmentModel.
          constraintDefinitionAt(
              i);
      this.addConstraintDefiniton(constraintDefinition);
    }
  }

  /**
   * deleteConstraintDefinitions
   *
   * @param descripton ActivityDefinition
   * @return List
   */
  private List<Object> deleteConstraintDefinitions(ActivityDefinition descripton) {
    List<Object> list = new ArrayList<Object>();
    List<ConstraintDefinition> temp = new ArrayList<ConstraintDefinition>();
    for (int i = 0; i < this.constraintDefinitionsCount(); i++) {
      ConstraintDefinition constraint = constraintDefinitionAt(i);
      if (constraint.dependsOn(descripton)) {
        temp.add(constraint);
      }
    }
    for (int i = 0; i < temp.size(); i++) {
      list.addAll(deleteConstraintDefinition(temp.get(i)));
    }
    return list;
  }

  /**
   * addListener
   *
   * @param aListener AssignmentModelListener
   */
  public void addListener(AssignmentModelListener aListener) {
    listener = aListener;
  }

  /**
   * editActivityDefinition
   *
   * @param activityDefinition activityDefinition
   * @return boolean
   */
  public boolean editActivityDefinition(ActivityDefinition activityDefinition) {
    boolean ok = true;
    int count = activityDefinitionsCount();
    int i = 0;
    ActivityDefinition curr = null;
    // *** search for all job descriptions that have the user-name like the copy object
    while ( (i < count) && ok) {
      curr = activityDefinitionAt(i++);
      if (activityDefinition.getName().equals(curr.getName())) {
        // *** allow for the update only if the new job description-name is not alreday
        //     assigned to another job description (a job description with different id)
        ok = activityDefinition.getId() == curr.getId();
      }
    }
    if (ok && (listener != null)) {
      listener.updateActivityDefinition(activityDefinition);
    }
    return ok;
  }

  /**
   * editConstraintDefinition
   *
   * @param constraint ConstraintDefinition
   * @return boolean
   */
  public boolean editConstraintDefinition(ConstraintDefinition constraint) {
    if (listener != null) {
      listener.updateConstraintDefinition(constraint);
    }
    return true;

  }

  /**
   *
   * @param name String
   * @return ActivityDefinition
   */
  public ActivityDefinition activityDefinitionWithName(String name) {
    boolean found = false;
    ActivityDefinition job = null;
    synchronized (activities) {
      Iterator<ActivityDefinition> iterator = this.activities.
          listIterator();
      while (iterator.hasNext() && !found) {
        job = iterator.next();
        found = job.getName().equals(name);
      }
    }
    return found ? job : null;
  }

  /**
   *
   * @param id int
   * @return ActivityDefinition
   */
  public ActivityDefinition activityDefinitionWithId(int id) {
    boolean found = false;
    ActivityDefinition job = null;
    synchronized (activities) {
      Iterator<ActivityDefinition> iterator = this.activities.
          listIterator();
      while (iterator.hasNext() && !found) {
        job = iterator.next();
        found = job.getId() == id;
      }
    }
    return found ? job : null;
  }

  /**
   *
   * @return TeamModel
   */
  public TeamModel getTeamModel() {
    return this.teamModel;
  }

  /**
   * add
   *
   * @param element DataElement
   * @return boolean
   */
  public boolean addData(DataElement element) {
    return dataModel.add(element);
  }

  public int getDataCount() {
    return dataModel.getSize();
  }

  public DataElement dataAt(int index) {
    return dataModel.get(index);
  }

  private void createDataFields(AssignmentModel assignmentModel) {
    for (int i = 0; i < assignmentModel.getDataCount(); i++) {
      DataElement element = assignmentModel.dataAt(i);
      addData(element);
    }
  }

  public boolean assignedToActivityDefiniton(DataElement element) {
    boolean found = false;
    synchronized (activities) {
      Iterator<ActivityDefinition> iterator = this.activities.
          iterator();
      while (iterator.hasNext() && !found) {
        found = iterator.next().contains(element);
      }
    }
    return found;
  }

  public boolean assignedToActivityDefiniton(TeamRole teamRole) {
    boolean found = false;
    synchronized (activities) {
      Iterator<ActivityDefinition> iterator = this.activities.
          iterator();
      while (iterator.hasNext() && !found) {
        found = iterator.next().getAuthorization().authorized(teamRole);
      }
    }
    return found;
  }

  public boolean deleteDataElement(DataElement element) {
    boolean ok = !this.assignedToActivityDefiniton(element);
    if (ok) {
      ok = this.dataModel.delete(element);
    }
    return ok;
  }

  public boolean deleteTeamRole(TeamRole teamRole) {
    boolean ok = !this.assignedToActivityDefiniton(teamRole);
    if (ok) {
      ok = this.teamModel.delete(teamRole);
    }
    return ok;
  }
  
  public Language getLanguage() {
    return language;
  }

  public HashMap<String, String> getAttributes(){
     return this.attributes;
  }

  public DataElement createDataElement() {
    return dataModel.createDataElement();
  }

  /**
   * getId
   *
   * @param anId int
   * @return DataElement
   */
  public DataElement getDataWithId(int anId) {
    return dataModel.getId(anId);
  }

  /**
   * add
   *
   * @param Id DataElement
   * @param name String
   * @param type Type
   * @param initial String
   * @return boolean
   */
  public DataElement addData(int Id, String name, DataElement.Type type,
                             String initial) {
    return dataModel.add(Id, name, type, initial);
  }

  /**
   *
   * @return Iterator
   */
  public Iterator<ActivityDefinition> getConstrainedActivities(){
    List<ActivityDefinition> result = new ArrayList<ActivityDefinition>();
    Iterator<ActivityDefinition> act = activities.iterator();
    while (act.hasNext()){
      ActivityDefinition activity = act.next();
      Iterator<ConstraintDefinition> constr = constraints.iterator();
      boolean found = false;
      while (constr.hasNext() && !found){
        ConstraintDefinition constraint = constr.next();
        found = constraint.dependsOn(activity);
      }
      if (found)
        result.add(activity);
    }
    return result.iterator();
  }
  
  public String toString(){
	return name;
	  
  }
 }
