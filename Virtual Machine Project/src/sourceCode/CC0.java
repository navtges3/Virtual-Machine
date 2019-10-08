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
		System.out.println("~~~" + s.e.pp());

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

		if(s.e instanceof JBool && ((JBool)s.e).val == true && last instanceof CIf) {
			Jexpr nexte = new JNull();
			Context temp = s.E;
			Context next = new CHole();

			nexte = ((CIf)last).trueCase;

			//find the end of the list again
			if(s.E instanceof CApp) {
				next = ((CApp)s.E).hole;
			}
			else if(s.E instanceof CIf) {
				next = ((CIf)s.E).hole;
			}
			while(!(next instanceof CHole)) {
				temp = next;
				if(temp instanceof CApp)
					next = ((CApp)temp).hole;
				else if(temp instanceof CIf)
					next = ((CIf)temp).hole;
			}
			last = temp;
			return new state(nexte, s.E);
		}
		if(s.e instanceof JBool && ((JBool)s.e).val == false && last instanceof CIf) {
			Jexpr nexte = new JNull();
			Context temp = s.E;
			Context next = new CHole();

			nexte = ((CIf)last).falseCase;

			//find the end of the list again
			if(s.E instanceof CApp) {
				next = ((CApp)s.E).hole;
			}
			else if(s.E instanceof CIf) {
				next = ((CIf)s.E).hole;
			}
			while(!(next instanceof CHole)) {
				temp = next;
				if(temp instanceof CApp)
					next = ((CApp)temp).hole;
				else if(temp instanceof CIf)
					next = ((CIf)temp).hole;
			}
			last = temp;
			return new state(nexte, s.E);
		}

		if(s.e instanceof JApp) {
			System.out.println("New JApp");
			if(last instanceof CHole) {
				System.out.println("1");
				last = new CApp(new CHole(), ((JApp)s.e).fun, new JNull(), ((JCons)((JCons)((JApp)s.e).args).rhs).lhs);
				return new state(((JCons)((JApp)s.e).args).lhs, last);
			}
			else if(last instanceof CIf){
				System.out.println("2");
				((CIf)last).hole = new CApp(new CHole(), ((JApp)s.e).fun, new JNull(), ((JCons)((JCons)((JApp)s.e).args).rhs).lhs);
				last = ((CIf)last).hole;
				return new state(((JCons)((JApp)s.e).args).lhs, s.E);
			}
			else if(last instanceof CApp){
				System.out.println("3");
				System.out.println(((JCons)((JCons)((JApp)s.e).args).rhs).lhs.pp());
				
				((CApp)last).hole = new CApp(new CHole(), ((JApp)s.e).fun, new JNull(), ((JCons)((JCons)((JApp)s.e).args).rhs).lhs);
				last = ((CApp)last).hole;
				System.out.println(((JCons)((JApp)s.e).args).lhs.pp());
				return new state(((JCons)((JApp)s.e).args).lhs, s.E);
			}
		}
		if(s.e.isValue() && last instanceof CApp && ((CApp)last).lhs instanceof JNull) {
			System.out.println("Hole first");
			Jexpr r = ((CApp)last).rhs;
			System.out.println(r.pp());
			((CApp)last).hole = new CApp(new CHole(), ((CApp)last).fun, s.e, new JNull());
			last = ((CApp)last).hole;
			return new state(r, s.E);
		}
		if(s.e.isValue() && last instanceof CApp && ((CApp)last).rhs instanceof JNull) {
			Jexpr nexte = new JNull();
			Context temp = s.E;
			Context next = new CHole();

			System.out.println(s.e.pp());
			System.out.println(last.plug(s.e).pp());
			nexte = delta(last.plug(s.e));
			System.out.println(nexte.pp());
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//find the end of the list again
			if(s.E instanceof CApp) {
				next = ((CApp)s.E).hole;
			}
			else if(s.E instanceof CIf) {
				next = ((CIf)s.E).hole;
			}
			while(!(next instanceof CHole)) {
				temp = next;
				if(temp instanceof CApp)
					next = ((CApp)temp).hole;
				else if(temp instanceof CIf)
					next = ((CIf)temp).hole;
			}
			temp = new CHole();
			//find the end of the list again
			if(s.E instanceof CApp) {
				next = ((CApp)s.E).hole;
			}
			else if(s.E instanceof CIf) {
				next = ((CIf)s.E).hole;
			}
			while(!(next instanceof CHole)) {
				temp = next;
				if(temp instanceof CApp)
					next = ((CApp)temp).hole;
				else if(temp instanceof CIf)
					next = ((CIf)temp).hole;
			}
			last = temp;
			return new state(nexte, s.E);
		}

		return new state(new JNumber(6969), new CHole());
	}

	private static Jexpr delta(Jexpr e) {
		System.out.println("Delta: " + e.pp());
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