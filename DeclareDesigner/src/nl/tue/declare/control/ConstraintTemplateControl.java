package nl.tue.declare.control;

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

import nl.tue.declare.datamanagement.*;
import nl.tue.declare.domain.template.*;

public class ConstraintTemplateControl {

  private static ConstraintTemplateControl INSTANCE = null;

  private TemplateBroker templateBroker;
  private ConstraintGroupBroker constrainGroupBroker;

  private List<Language> languages;

  private Collection<ConstraintGroup> groups;

  private ConstraintWarningLevel level;

  private ConstraintTemplateControl() {
    super();
    languages = new ArrayList<Language>();
    groups = new ArrayList<ConstraintGroup>();
    templateBroker = XMLBrokerFactory.newTemplateBroker();
    constrainGroupBroker = XMLBrokerFactory.newConstraintGroupBroker();
    loadLangauges();
    loadGroups();

    loadWarningLevel();
    if (level == null) {
      level = new ConstraintWarningLevel();
    }
  }

  static ConstraintTemplateControl singleton() {
    if (INSTANCE == null) {
      INSTANCE = new ConstraintTemplateControl();
    }
    return INSTANCE;
  }

  public Language createLanguage() {
    return new Language(nextLanguageId(), "");
  }

  public boolean addLanguage(Language lang) {
    boolean ok = this.getLanguage(lang.getName()) == Language.unknown();
    if (ok) {
      ok = languages.add(lang);
    }
    if (ok) {
      templateBroker.addLanguage(lang);
    }
    return ok;
  }

  public boolean deleteLanguage(Language lang) {
    boolean canDelete = languages.remove(lang);
    if (canDelete) {
      canDelete = templateBroker.deleteLanguage(lang);
    }
    return canDelete;
  }

  public Language getLanguage(String name) {
    boolean found = false;
    Language lang = null;
    Iterator<Language> iterator = languages.iterator();
    while (iterator.hasNext() && !found) {
      lang = iterator.next();
      found = name.equals(lang.getName());
    }
    return found ? lang : unknownLanguage();
  }

  public List<Language> getLanguages() {
    return this.languages;
  }

  /**
   * addTemplate
   *
   * @param template ConstraintTemplate
   * @return boolean
   */
  public boolean addTemplate(ConstraintTemplate template, LanguageGroup parent) {
    //boolean canAdd = template.getLanguage().addTemplate(template);
    //boolean canAdd = template.getLanguage().add(template);
    boolean canAdd = parent.add(template);
    if (canAdd) {
      templateBroker.addTemplate(parent, template);
    }
    return canAdd;
  }

  /**
   * deleteTemplate
   *
   * @param template ConstraintTemplate
   * @return boolean
   */
  public boolean deleteTemplate(ConstraintTemplate template) {
    LanguageGroup parent = template.getLanguage().getParent(template);
    boolean canDelete = true;//parent.exists(template); //lang.deleteTemplate(template);
   // if (canDelete) {
      canDelete = templateBroker.deleteTemplate(template, parent);
      parent.remove(template);
    //}
    return canDelete;
  }

  public void loadLangauges() {
    List<Language> list = templateBroker.readLangauges();
    this.languages.clear();
    for (int i = 0; i < list.size(); i++) {
      this.languages.add(list.get(i));
    }
  }

  /**
   * editTemplate
   *
   * @param template ConstraintTemplate
   * @return boolean
   */
  public boolean editTemplate(ConstraintTemplate template) {
    return templateBroker.editTemplate(template,
                                       template.getLanguage().
                                       getParent(template));
  }

  /**
   * nextGroupId()
   *
   * @return int
   */
  private int nextGroupId() {
    int id = 0;
    ConstraintGroup group = null;
    Iterator<ConstraintGroup> it = groups.iterator();
    while (it.hasNext()) {
      group = it.next();
      if (id < group.getId()) {
        id = group.getId();
      }
    }
    return++id;
  }

  /**
   * nextGroupId()
   *
   * @return int
   */
  private int nextLanguageId() {
    int id = 0;
    Language lang = null;
    Iterator<Language> it = languages.iterator();
    while (it.hasNext()) {
      lang = it.next();
      if (id < lang.getId()) {
        id = lang.getId();
      }
    }
    return++id;
  }

  /**
   *
   * @return ConstraintGroup
   */
  public ConstraintGroup createGroup() {
    ConstraintGroup group = new ConstraintGroup(this.nextGroupId());
    return group;
  }

  /**
   *
   * @param group ConstraintGroup
   * @return boolean
   */
  public boolean addGroup(ConstraintGroup group) {
    boolean canAdd = !groups.contains(group);
    if (canAdd) {
      this.groups.add(group);
      this.constrainGroupBroker.addGroup(group);
      return true;
    }
    return canAdd;
  }

  /**
   * editTemplate
   *
   * @param group ConstraintTemplate
   * @return boolean
   */
  public boolean editGroup(ConstraintGroup group) {
    return this.constrainGroupBroker.editGroup(group);
  }

  /**
   *
   * @param group ConstraintGroup
   * @return boolean
   */
  public boolean deleteConstraintGroup(ConstraintGroup group) {
    boolean canRemove = groups.remove(group);
    if (canRemove) {
      this.constrainGroupBroker.deleteGroup(group);
    }
    return canRemove;
  }

  /**
   * loadGroups
   */
  private void loadGroups() {
    Iterator<ConstraintGroup> iterator = constrainGroupBroker.readGroups().
        iterator();
    this.groups.clear();
    while (iterator.hasNext()) {
      this.groups.add(iterator.next());
    }
  }

  private void loadWarningLevel() {
    this.level = constrainGroupBroker.getWarningLevel();
  }

  /**
   *
   * @return int
   */
  public int constraintGroupCount() {
    return groups.size();
  }

  public ConstraintGroup getGroupAt(int index) {
    ConstraintGroup group = null;
    if (index < groups.size()) {
      Iterator<ConstraintGroup> iterator = groups.iterator();
      int i = 0;
      while (iterator.hasNext() && (i++) <= index) {
        group = iterator.next();
      }
    }
    return group;
  }

  /**
   *
   * @return ConstraintWarningLevel
   */
  public ConstraintWarningLevel getConstraintWarningLevel() {
    return this.level;
  }

  public void editConstraintWarningLevel(ConstraintWarningLevel level) {
    this.constrainGroupBroker.writeWarningLevel(level);
  }

  public Language unknownLanguage() {
    return Language.unknown();
  }

  /**
   *
   * @param group ConstraintGroup
   * @param parent LanguageGroup
   * @param language Language
   * @return boolean
   */
  public boolean addLanguageGroup(LanguageGroup group, LanguageGroup parent,
                                  Language language) {
    boolean canAdd = parent.add(group);
    if (canAdd) {
      templateBroker.addGroup(group, parent, language);
    }
    return canAdd;
  }

  /**
   * editlanguageGroup
   *
   * @param group ConstraintTemplate
   * @param language Language
   * @return boolean
   */
  public boolean editLanguageGroup(LanguageGroup group, Language language) {
    return templateBroker.editGroup(group, language);
  }

  /**
   * editlanguageGroup
   *
   * @param group ConstraintTemplate
   * @param language Language
   * @return boolean
   */
  public void deleteLanguageGroup(LanguageGroup group, LanguageGroup parent,
                                  Language language) {
    templateBroker.deleteGroup(group, parent, language);
  }
}
