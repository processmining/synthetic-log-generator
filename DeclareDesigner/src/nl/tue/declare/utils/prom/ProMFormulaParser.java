package nl.tue.declare.utils.prom;

import ltl2aut.ltl.*;

class ProMFormulaParser extends Parser<LTLFormula>{

	public ProMFormulaParser(String ltl) {
		super(ltl, new ProMFormulaFactory());
	}

}
