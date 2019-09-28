package sourceCode;

public interface Context {
	Jexpr plug(Jexpr x);
}

// C
class C_Hole implements Context {
	// Returns the value x
	public Jexpr plug(Jexpr x) {
		return x;
	}
}

// if C e1 e2
class C_If0 implements Context {
	Context hole;
	Jexpr trueCase;
	Jexpr falseCase;

	public C_If0(Jexpr lhs, Jexpr rhs) {
		hole = new C_Hole();
		trueCase = lhs;
		falseCase = rhs;
	}

	// Replaces the hole with the value x and returns a JIf
	public Jexpr plug(Jexpr x) {
		return new JIf(hole.plug(x), trueCase, falseCase);
	}
}

// if e1 C e2
class C_If1 implements Context {
	Jexpr condition;
	Context hole;
	Jexpr falseCase;

	public C_If1(Jexpr cond, Jexpr rhs) {
		hole = new C_Hole();
		condition = cond;
		falseCase = rhs;
	}

	// Replaces the hole with the value x and returns a JIf
	public Jexpr plug(Jexpr x) {
		return new JIf(condition, hole.plug(x), falseCase);
	}
}

// if e1 e2 C
class C_If2 implements Context {

	Jexpr condition;
	Jexpr trueCase;
	Context hole;

	public C_If2(Jexpr cond, Jexpr lhs) {
		hole = new C_Hole();
		trueCase = lhs;
		condition = cond;
	}

	// Replaces the hole with the value x and returns a JIf
	public Jexpr plug(Jexpr x) {
		return new JIf(condition, trueCase, hole.plug(x));
	}
}

// e1... C e2...
class C_App implements Context {
	Context hole;
	Jexpr lhs;
	Jexpr rhs;

	public C_App(Jexpr l, Jexpr r) {
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
