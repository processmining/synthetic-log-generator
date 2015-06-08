package nl.tue.declare.domain.organization;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class Administrator
    extends User {
  private static User instance = null;

  private Administrator() {
    super(0);
    setName("administrator");
    setLastName("administrator");
    setUserName("administrator");
    setPassword("administrator");
    //addRole(SuperRole.singleton());
  }

  public static User singleton() {
    if (instance == null) {
      instance = new Administrator();
    }
    return instance;
  }
}
