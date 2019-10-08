package sourceCode;

public class CC0 {
	
	public static Jexpr interp(Jexpr e) {
		state s = inject(e);
		while(s.e.isValue() == false && !(s.E instanceof CHole)) {
			s = step(s);
			s.e = s.e.step();
		}
		return extract(s);
	}
	
	public static state inject(Jexpr e) {
		Context E = new CHole();
		return new state(e, E);
	}
	
	public static Jexpr extract(state s) {
		return s.E.plug(s.e);
	}
	
	public static state step(state s) {
		if(s.e instanceof JIf)
			return new state(((JIf)s.e).cond, new CIf(new CHole(), ((JIf)s.e).texpr, ((JIf)s.e).fexpr));
		if(s.e instanceof JBool && ((JBool)s.e).val == true && s.E instanceof CIf)
			return new state(((CIf)s.E).trueCase, new CHole());
		if(s.e instanceof JBool && ((JBool)s.e).val == false && s.E instanceof CIf)
			return new state(((CIf)s.E).falseCase, new CHole());
		
		if(s.e instanceof JApp)
			return new state(((JCons)((JApp)s.e).args).lhs, new CApp(new CHole(), ((JApp)s.e).fun, new JNull(), ((JCons)((JCons)((JApp)s.e).args).rhs).lhs));
		if(s.e.isValue() && s.E instanceof CApp && ((CApp)s.E).lhs instanceof JNull)
			return new state(((CApp)s.E).rhs, new CApp(new CHole(), ((JApp)s.e).fun, s.e, new JNull()));
		if(s.e.isValue() && s.E instanceof CApp && ((CApp)s.E).rhs instanceof JNull)
			return new state(delta(s.E.plug(s.e)), new CHole());
			
		return new state(new JNumber(6969), new CHole());
	}
	
	private static Jexpr delta(Jexpr e) {
		Jexpr fun = ((JApp)e).fun;
		Jexpr args = ((JApp)e).args;
		
		String p = ((JPrim)fun).prim;
		int lhs = ((JNumber)((JCons)args).lhs).num;
		int rhs = ((JNumber)((JCons)((JCons)args).rhs).lhs).num;
		if ( p.equals("+") ) { return new JNumber(lhs + rhs); }
		if ( p.equals("*") ) { return new JNumber(lhs * rhs); }
		if ( p.equals("/") ) { return new JNumber(lhs / rhs); }
		if ( p.equals("-") ) { return new JNumber(lhs - rhs); }
		if ( p.equals("<") ) { return new JBool(lhs < rhs); }
		if ( p.equals("<=") ){ return new JBool(lhs <= rhs); }
		if ( p.equals("==") ){ return new JBool(lhs == rhs); }
		if ( p.equals(">") ) { return new JBool(lhs > rhs); }
		if ( p.equals(">=") ){ return new JBool(lhs >= rhs); }
		if ( p.equals("!=") ){ return new JBool(lhs != rhs); }
		
		return new JNumber(6969);
	}
}

class state {
	Jexpr e;
	Context E;
	public state(Jexpr e, Context E) {
		this.e = e;
		this.E = E;
	}
}