package sourceCode;

public interface Context {
	Jexpr plug(Jexpr x);
}

// C
class CHole implements Context {
	// Returns the value x
	public Jexpr plug(Jexpr x) {
		return x;
	}
}

// if C e1 e2
class CIf implements Context {
	Context hole;
	Jexpr trueCase;
	Jexpr falseCase;

	public CIf(Context c, Jexpr lhs, Jexpr rhs) {
		hole = c;
		trueCase = lhs;
		falseCase = rhs;
	}

	// Replaces the hole with the value x and returns a JIf
	public Jexpr plug(Jexpr x) {
		return new JIf(hole.plug(x), trueCase, falseCase);
	}
}

// e1... C e2...
class CApp implements Context {
	Context hole;
	Jexpr lhs;
	Jexpr rhs;

	public CApp(Context c, Jexpr l, Jexpr r) {
		hole = c;
		lhs = l;
		rhs = r;
	}

	// Replaces the hole with the value x and returns the JApp
	public Jexpr plug(Jexpr x) {
		JApp app;

		if(lhs instanceof JCons) {

			//traverse down the expression(list of e's) and add a new JCons to the end
			JCons nav = (JCons)lhs;
			while(!(nav.rhs instanceof JNull)) {
				nav = (JCons)nav.rhs;
			}

			nav.rhs = new JCons(hole.plug(x), rhs);
			app = new JApp(((JCons)lhs).lhs, ((JCons)lhs).rhs);
		}
		else
			app = new JApp(lhs, new JCons(hole.plug(x), rhs));

		return app;
	}
}
