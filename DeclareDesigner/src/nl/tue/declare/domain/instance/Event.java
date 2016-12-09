/**
 * 
 */
package nl.tue.declare.domain.instance;

/**
 * @author mpesic
 *
 */

import java.util.*;

import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.organization.*;
import nl.tue.declare.utils.PrettyTime;

public class Event
    implements Cloneable {

  public static final String DOT = ".";
  public static final String WORKITEM_ID = "workItemId";

  protected User user;
  protected ActivityDefinition activity;
  private PrettyTime time;

  public enum Type {STARTED,COMPLETED,CANCELLED;}  
  
  private Type type;
  private HashMap<String, String> attributes;

  /**
   * AbstractEvent
   *
   * @param anUser User
   * @param anJob Job
   */
  public Event(User anUser, ActivityDefinition anJob, Type t) {
	super();
    user = anUser;
    activity = anJob;
    this.time = new PrettyTime();
    type = t;
    this.attributes = new HashMap<String, String>();
  }

  public User getUser() {
    return user;
  }

  public void setUser(User u) {
    user = u;
  }

  /**
   * getJob
   *
   * @return Job
   */
  public ActivityDefinition getActivity() {
    return activity;
  }

  public void setActivity(ActivityDefinition act) {
    this.activity = act;
  }
  
  public Type getType(){
	  return type;
  }

  /**
  *
  * @return PrettyTime
  */
 public PrettyTime getTime() {
   return time;
 }  
  /**
   * clone
   *
   * @throws CloneNotSupportedException
   * @return Object
   */
  public Object clone()  {
	Event e = new Event(this.user, this.activity,this.type);
	e.time.setTime(this.time);
	return e;
  }

  /**
   * equals
   *
   * @param object Object
   * @return boolean
   */
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    if (object.getClass() != this.getClass()) {
      return false;
    }
    if (object instanceof Event) {
      Event compare = (Event) object;
      return activity.equals(compare.activity) && type.equals(compare.type);
    }
    return false;
  }

  /**
   * getProposition
   *
   * @return String
   */
  public String getProposition() {
    if (this.activity != null) {
      return activity.getName() + DOT + type.name();
    }
    return null;
  }

  /**
   * toString
   */
  public String toString() {
    return type.name();
  }


  public static Collection<Event> possibleEvents(Activity activity) {
	  
    Collection<Event> possible = new ArrayList<Event>();
    
    Type[] types = Type.values();
    for (int i = 0; i < types.length; i++){
    	Type t = types[i];
    	possible.add(new Event(new User(0), activity, t));
    }
    return possible;
  }
  
  public void setWorkItem(String a){
	  this.attributes.put(WORKITEM_ID, a);
  }
  
  public HashMap<String, String> getAttributes(){
	  return attributes;
  }
  
}
