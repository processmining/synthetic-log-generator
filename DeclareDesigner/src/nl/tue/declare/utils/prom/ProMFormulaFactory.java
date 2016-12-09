package nl.tue.declare.utils.prom;

import ltl2aut.ltl.*;

class ProMFormulaFactory implements FormulaFactory<LTLFormula> {
	

	public ProMFormulaFactory() {
		super();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see ltl2aut.formula.FormulaFactory#Always(ltl2aut.formula.LTLFormula)
	 */
	public LTLFormula Always(LTLFormula f) {
		return create(LTLFormula.ALWAYS, f, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ltl2aut.formula.FormulaFactory#And(ltl2aut.formula.LTLFormula,
	 * ltl2aut.formula.LTLFormula)
	 */
	public LTLFormula And(LTLFormula sx, LTLFormula dx) {
	   return create(LTLFormula.AND, sx, dx, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ltl2aut.formula.FormulaFactory#Eventually(ltl2aut.formula.LTLFormula)
	 */
	public LTLFormula Eventually(LTLFormula f) {
		return create(LTLFormula.EVENTUALLY, f, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ltl2aut.formula.FormulaFactory#False()
	 */
	public LTLFormula False() {
		return create(LTLFormula.FALSE, null, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ltl2aut.formula.FormulaFactory#Implies(ltl2aut.formula.LTLFormula,
	 * ltl2aut.formula.LTLFormula)
	 */
	public LTLFormula Implies(LTLFormula sx, LTLFormula dx) {
		return create(LTLFormula.IMPLIES, sx, dx, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ltl2aut.formula.FormulaFactory#Next(ltl2aut.formula.LTLFormula)
	 */
	public LTLFormula Next(LTLFormula f) {
		return create(LTLFormula.NEXT, f, null, null);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ltl2aut.formula.FormulaFactory#Next(ltl2aut.formula.LTLFormula)
	 */
	public LTLFormula WNext(LTLFormula f) {
		return create(LTLFormula.WNEXT, f, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ltl2aut.formula.FormulaFactory#Not(ltl2aut.formula.LTLFormula)
	 */
	public LTLFormula Not(LTLFormula f) {
				return create(LTLFormula.NOT, f, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ltl2aut.formula.FormulaFactory#Or(ltl2aut.formula.LTLFormula,
	 * ltl2aut.formula.LTLFormula)
	 */
	public LTLFormula Or(LTLFormula sx, LTLFormula dx) {
		return create(LTLFormula.OR, sx, dx, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ltl2aut.formula.FormulaFactory#Proposition(java.lang.String)
	 */
	public LTLFormula Proposition(String name) {
		return create(LTLFormula.PROPOSITION, null, null, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ltl2aut.formula.FormulaFactory#Release(ltl2aut.formula.LTLFormula,
	 * ltl2aut.formula.LTLFormula)
	 */
	public LTLFormula Release(LTLFormula sx, LTLFormula dx) {
		return create(LTLFormula.RELEASE, sx, dx, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ltl2aut.formula.FormulaFactory#True()
	 */
	public LTLFormula True() {
		return create(LTLFormula.TRUE, null, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ltl2aut.formula.FormulaFactory#Until(ltl2aut.formula.LTLFormula,
	 * ltl2aut.formula.LTLFormula)
	 */
	public LTLFormula Until(LTLFormula sx, LTLFormula dx) {
		return create(LTLFormula.UNTIL, sx, dx, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ltl2aut.formula.FormulaFactory#WRelease(ltl2aut.formula.LTLFormula,
	 * ltl2aut.formula.LTLFormula)
	 */
	public LTLFormula WRelease(LTLFormula sx, LTLFormula dx) {
		return create(LTLFormula.WRELEASE, sx, dx, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ltl2aut.formula.FormulaFactory#WUntil(ltl2aut.formula.LTLFormula,
	 * ltl2aut.formula.LTLFormula)
	 */
	public LTLFormula WUntil(LTLFormula sx, LTLFormula dx) {
		return create(LTLFormula.WUNTIL, sx, dx, null);
	}
	
	public LTLFormula create(char c, LTLFormula sx, LTLFormula dx, String n) {
		return new LTLFormula(c, sx, dx, n);
	}
	public LTLFormula Equal(LTLFormula lf, LTLFormula rf) {
			return create(LTLFormula.EQUAL, lf, rf, null);
	}	

}
