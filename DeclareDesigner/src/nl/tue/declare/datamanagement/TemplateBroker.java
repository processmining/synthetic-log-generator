package nl.tue.declare.datamanagement;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: Interface for a template broker. When you implement a
 * template broker, you should implement the TemplateBroker interface. </p>
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

public interface TemplateBroker {
  /**
   * Adds a template to the data warehouse.
   *
   * @param template template that should be added.
   */
  public void addTemplate(LanguageGroup parent, ConstraintTemplate template);

  /**
   * Edits/alters a template in the data warehouse.
   *
   * @param template template that should be edited
   * @return true if the template was succesfuly edited
   */
  public boolean editTemplate(ConstraintTemplate template, LanguageGroup parent);

  /**
   * Deletes a template from the data warehouse.
   *
   * @param template template that should be deleted
   * @return true if the template was succesfuly deleted
   */
  public boolean deleteTemplate(ConstraintTemplate template,
                                LanguageGroup parent);

  /**
   * Reads all templates from the data warehouse.
   *
   * @return a list containing templates that were read from the data warehouse
   */
  //public List<ConstraintTemplate> readTemplates();


  /**
   * Reads all templates from the data warehouse.
   *
   * @return a list containing templates that were read from the data warehouse
   */
  public List<Language> readLangauges();

  /**
   * Adds a template to the data warehouse.
   *
   * @param template template that should be added.
   */
  public void addLanguage(Language language);

  public boolean deleteLanguage(Language language);

  public void addGroup(LanguageGroup group, LanguageGroup parent,
                       Language language);

  public boolean editGroup(LanguageGroup group, Language language);

  public void deleteGroup(LanguageGroup group, LanguageGroup parent,
                          Language language);

}
