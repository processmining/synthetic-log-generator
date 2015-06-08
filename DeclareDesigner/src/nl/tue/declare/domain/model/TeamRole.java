package nl.tue.declare.domain.model;

import nl.tue.declare.domain.*;
import nl.tue.declare.domain.organization.*;

public class TeamRole
    extends Base {

  private Role role;
  private String name;

  public TeamRole(int aId, Role role) {
    super(aId);
    this.setName(role.getName());
    this.setRole(role);
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Role getRole() {
    return role;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }

  public Object clone() {
    TeamRole clone = new TeamRole(this.getId(), this.role);
    clone.setName(this.name);
    return clone;
  }

  public boolean authorized(User user) {
    return user.hasRole(this.role);
  }
}
