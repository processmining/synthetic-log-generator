package nl.tue.declare.domain.template;

/**
 * <p>
 * Title: DECLARE
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: TU/e
 * </p>
 * 
 * @author Maja Pesic
 * @version 1.0
 */

public class Language extends LanguageGroup {

	public Language(int id, String name) {
		super(id);
		setName(name);
	}

	public static Language unknown() {
		return UnknownLanguage.singleton();
	}

	public ConstraintTemplate createTemplate(int nrParameters) {
		ConstraintTemplate template = new ConstraintTemplate(nextTemplateId(),
				this);
		int asciiCode = Integer.valueOf('A').intValue();
		for (int i = 0; i < nrParameters; i++) {
			char[] asciiChar = { (char) asciiCode };
			template.addParameter(new String(asciiChar));
			asciiCode++;
		}
		return template;
	}

	public LanguageGroup createGroup() {
		return new LanguageGroup(nextTemplateId());
	}

	/**
	 * nextTemplateId
	 * 
	 * @return int
	 */
	private int nextTemplateId() {
		return (getMaxId() + 1);
	}

	public ConstraintTemplate withId(int id) {
		IItem wanted = withId(id);
		ConstraintTemplate template = null;
		if (wanted instanceof ConstraintTemplate) {
			template = (ConstraintTemplate) wanted;
		}
		;
		return template;
	}

	/**
	 * templateExists
	 * 
	 * @param template
	 *            ConstraintTemplate
	 * @return boolean
	 */
	public boolean templateExists(ConstraintTemplate template) {
		return this.exists(template);
	}

	/**
	 * deleteTemplate
	 * 
	 * @param template
	 *            ConstraintTemplate
	 * @return boolean
	 */
	public boolean deleteTemplate(ConstraintTemplate template) {
		boolean canDelete = this.templateExists(template);
		if (canDelete) {
			canDelete = remove(template);
		}
		return canDelete;
	}
}

class UnknownLanguage extends Language {

	private static Language instance = null;

	private UnknownLanguage() {
		super(0, "unknown");
	}

	static Language singleton() {
		if (instance == null) {
			instance = new UnknownLanguage();
		}
		return instance;
	}
}
