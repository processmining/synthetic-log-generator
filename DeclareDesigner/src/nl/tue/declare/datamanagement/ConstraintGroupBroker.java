package nl.tue.declare.datamanagement;

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
import java.util.*;

import nl.tue.declare.domain.template.*;

public interface ConstraintGroupBroker {
  /**
   * Adds a group to the data warehouse.
   *
   * @param ConstraintGroup group that should be added.
   */
  public void addGroup(ConstraintGroup group);

  /**
   * Edits/alters a group in the data warehouse.
   *
   * @param ConstraintGroup group that should be edited
   * @return true if the group was succesfuly edited
   */
  public boolean editGroup(ConstraintGroup group);

  /**
   * Deletes a group from the data warehouse.
   *
   * @param ConstraintGroup group that should be deleted
   * @return true if the group was succesfuly deleted
   */
  public boolean deleteGroup(ConstraintGroup group);

  /**
   * Reads all groups from the data warehouse.
   *
   * @return a list containing groups that were read from the data warehouse
   */
  public Collection<ConstraintGroup> readGroups();

  /**
   * Stors inforamtion about the currnt warning level in the data warehouse.
   * @param level ConstraintWarningLevel
   */
  public void writeWarningLevel(ConstraintWarningLevel level);

  /**
   * Retreives the warning level from the data warehouse.
   * @return ConstraintWarningLevel
   */
  public ConstraintWarningLevel getWarningLevel();

}
