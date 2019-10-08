package sourceCode;

public class CC0 {
	private static Context last;

	public static Jexpr interp(Jexpr e) {
		state s = inject(e);
		last = s.E;
		s = step(s);
		while(s.e.isValue() == false || !(s.E instanceof CHole)) {
			s = step(s);
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
		// JIf
		if(s.e instanceof JIf) {
			if(last instanceof CHole) {
				last = new CIf(new CHole(), ((JIf)s.e).texpr, ((JIf)s.e).fexpr);
				return new state(((JIf)s.e).cond, last);
			}
			else if(last instanceof CIf){
				((CIf)last).hole = new CIf(new CHole(), ((JIf)s.e).texpr, ((JIf)s.e).fexpr);
				last = ((CIf)last).hole;
				return new state(((JIf)s.e).cond, s.E);
			}
			else if(last instanceof CApp){
				((CApp)last).hole = new CIf(new CHole(), ((JIf)s.e).texpr, ((JIf)s.e).fexpr);
				last = ((CApp)last).hole;
				return new state(((JIf)s.e).cond, s.E);
			}
		}

		// JIf true
		if(s.e instanceof JBool && ((JBool)s.e).val == true && last instanceof CIf) {
			Jexpr nexte = new JNull();
			Context prev = null;
			Context temp = s.E;
			Context next = new CHole();

			nexte = ((CIf)last).trueCase;

			if(s.E == last)
				s.E = new CHole();
			else {
				if(s.E instanceof CApp)
					next = ((CApp)s.E).hole;
				else
					next = ((CIf)s.E).hole;
				while(!(next instanceof CHole)) {
					prev = temp;
					temp = next;
					if(temp instanceof CApp)
						next = ((CApp)temp).hole;
					else
						next = ((CIf)temp).hole;
				}
				last = prev;
				if(last instanceof CApp)
					((CApp)last).hole = new CHole();
				else
					((CIf)last).hole = new CHole();
			}
			return new state(nexte, s.E);
		}
		//JIf false
		if(s.e instanceof JBool && ((JBool)s.e).val == false && last instanceof CIf) {
			Jexpr nexte = new JNull();
			Context prev = null;
			Context temp = s.E;
			Context next = new CHole();

			nexte = ((CIf)last).falseCase;

			if(s.E == last)
				s.E = new CHole();
			else {
				if(s.E instanceof CApp)
					next = ((CApp)s.E).hole;
				else
					next = ((CIf)s.E).hole;
				while(!(next instanceof CHole)) {
					prev = temp;
					temp = next;
					if(temp instanceof CApp)
						next = ((CApp)temp).hole;
					else
						next = ((CIf)temp).hole;
				}
				last = prev;
				if(last instanceof CApp)
					((CApp)last).hole = new CHole();
				else
					((CIf)last).hole = new CHole();
			}
			return new state(nexte, s.E);
		}

		//JApp
		if(s.e instanceof JApp) {
			if(last instanceof CHole) {
				last = new CApp(new CHole(), ((JApp)s.e).fun, new JNull(), ((JCons)((JCons)((JApp)s.e).args).rhs).lhs);
				return new state(((JCons)((JApp)s.e).args).lhs, last);
			}
			else if(last instanceof CIf){
				((CIf)last).hole = new CApp(new CHole(), ((JApp)s.e).fun, new JNull(), ((JCons)((JCons)((JApp)s.e).args).rhs).lhs);
				last = ((CIf)last).hole;
				return new state(((JCons)((JApp)s.e).args).lhs, s.E);
			}
			else if(last instanceof CApp){
				((CApp)last).hole = new CApp(new CHole(), ((JApp)s.e).fun, new JNull(), ((JCons)((JCons)((JApp)s.e).args).rhs).lhs);
				last = ((CApp)last).hole;
				return new state(((JCons)((JApp)s.e).args).lhs, s.E);
			}
		}
		//JApp hole first
		if(s.e.isValue() && last instanceof CApp && ((CApp)last).lhs instanceof JNull) {
			Jexpr r = ((CApp)last).rhs;
			((CApp)last).lhs = s.e;
			((CApp)last).rhs = new JNull();
			return new state(r, s.E);
		}
		//JApp hole second
		if(s.e.isValue() && last instanceof CApp && ((CApp)last).rhs instanceof JNull) {
			Jexpr nexte = new JNull();
			Context prev = null;
			Context temp = s.E;
			Context next = new CHole();

			nexte = delta(last.plug(s.e));

			// fix the stack
			if(s.E == last)
				s.E = new CHole();
			else {
				if(s.E instanceof CApp)
					next = ((CApp)s.E).hole;
				else
					next = ((CIf)s.E).hole;
				while(!(next instanceof CHole)) {
					prev = temp;
					temp = next;
					if(temp instanceof CApp)
						next = ((CApp)temp).hole;
					else
						next = ((CIf)temp).hole;
				}
				last = prev;
				if(last instanceof CApp)
					((CApp)last).hole = new CHole();
				else
					((CIf)last).hole = new CHole();
			}
			return new state(nexte, s.E);
		}

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