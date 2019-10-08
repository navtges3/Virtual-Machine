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
	Jexpr fun;
	Jexpr lhs;
	Jexpr rhs;

	public CApp(Context c, Jexpr fun, Jexpr l, Jexpr r) {
		hole = c;
		this.fun = fun;
		lhs = l;
		rhs = r;
	}

	// Replaces the hole with the value x and returns the JApp
	public Jexpr plug(Jexpr x) {
		if(lhs instanceof JNull)
			return new JApp(fun, new JCons(x, new JCons(rhs, new JNull())));
		else
			return new JApp(fun, new JCons(lhs, new JCons(x, new JNull())));
	}
}
