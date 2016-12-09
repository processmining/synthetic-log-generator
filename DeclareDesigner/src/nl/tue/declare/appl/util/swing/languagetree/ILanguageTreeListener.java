package nl.tue.declare.appl.util.swing.languagetree;

import nl.tue.declare.domain.template.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface ILanguageTreeListener {
  /**
   *
   * @param parent Group
   * @return LanguageGroup
   */
  public abstract LanguageGroup addGroup(LanguageGroup parent);

  /**
   *
   * @param group Group
   * @return boolean
   */
  public abstract boolean deleteGroup(LanguageGroup group);

  /**
   *
   * @param group Group
   */
  public abstract void editGroup(LanguageGroup group);

  /**
   *
   * @param parent Group
   * @return ConstraintTemplate
   */
  public abstract ConstraintTemplate addTemplate(LanguageGroup parent,int nrParameters);

  /**
   *
   * @param template Template
   */
  public abstract void editTemplate(ConstraintTemplate template);

  /**
   *
   * @param template Template
   * @return boolean
   */
  public abstract boolean deleteTemplate(ConstraintTemplate template);

  /**
   *
   * @param oldParent LanguageGroup
   * @param node Object
   * @param newParent LanguageGroup
   */
  public void move(LanguageGroup oldParent, IItem node, LanguageGroup newParent);
}
