package nl.tue.declare.verification.impl;

import nl.tue.declare.domain.model.*;
import nl.tue.declare.verification.*;

public class DeadActivityError
    extends VerificationError {

  /**
	 * 
	 */
	private static final long serialVersionUID = 4259019084248530949L;
   private ActivityDefinition activity;

  public DeadActivityError(ViolationGroup group, ActivityDefinition activity) {
    super(group);
    this.activity = activity;
  }

  public DeadActivityError(ActivityDefinition activity) {
    this(null, activity);
  }

  public String toString() {
    return "Activity \"" + activity.getName() + "\" is dead";
  }

  public boolean isSuper(VerificationError error) {
    boolean sup = super.isSuper(error);
    if (sup && error instanceof DeadActivityError) {
      DeadActivityError err = (DeadActivityError) error;
      sup = activity.equals(err.activity);
    }
    return sup;
  }

  public boolean isSub(VerificationError error) {
    boolean sub = super.isSub(error);
    if (sub && error instanceof DeadActivityError) {
      DeadActivityError err = (DeadActivityError) error;
      sub = err.activity.equals(activity);
    }
    return sub;
  }

  public boolean equals(Object obj) {
    boolean eq = super.equals(obj);
    if (eq && obj instanceof DeadActivityError) {
      DeadActivityError err = (DeadActivityError) obj;
      eq = this.activity.equals(err.activity);
    }
    return eq;
  }
}
