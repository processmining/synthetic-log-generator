package nl.tue.declare.verification;

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
public abstract class VerificationError {

  /**
	 * 
	 */
private static final long serialVersionUID = 6856200236413231770L;
   private ViolationGroup group;

  public VerificationError(ViolationGroup group) {
    super();
    setGroup(group);
  }

  public VerificationError() {
    super();
    setGroup(null);
  }

  public ViolationGroup getGroup() {
    return group;
  }

  public void setGroup(ViolationGroup group) {
    this.group = group;
  }

  public boolean equals(Object obj) {
    boolean eq = this == obj;
    if (!eq) {
      eq = obj.getClass() == this.getClass();
      if (eq && obj instanceof VerificationError) {
        VerificationError err = (VerificationError) obj;
        eq = err.getGroup().equals(this.getGroup());
      }
    }
    return eq;
  }

  public boolean isSuper(VerificationError error) {
    boolean sup = error.getClass() == this.getClass();
    if (sup) {
      sup = this.getGroup().isSuper(error.getGroup());
    }
    return sup;
  }

  public boolean isSub(VerificationError error) {
    if (error.getClass() != this.getClass()) {
      return false;
    }
    if (this.getGroup().isSub(error.getGroup())) {
      return true;
    }
    return false;
  } 
}
