package sourceCode;

public interface Jexpr {
	public Jexpr interp();
	public String pp();
	public Boolean isValue();
	public Jexpr step();
	public Jexpr subst(JVar x, Jexpr v);
}

class JFun implements Jexpr {
	public String Name;
	public JFun(String Name) {
		this.Name = Name;
	}
	public Jexpr interp() {
		return this;
	}
	public String pp() {
		return "" + Name;
	}
	public Boolean isValue() {
		return true;
	}
	public Jexpr step() {
		return this;
	}
	public Jexpr subst(JVar x, Jexpr v) {
		return this;
	}
}

class JVar implements Jexpr {
	public String name;
	public JVar(String name) {
		this.name = name;
	}
	public Jexpr interp() {
		return this;
	}
	public String pp() {
		return "" + name;
	}
	public Boolean isValue() {
		return false;
	}
	public Jexpr step() {
		return this;
	}
	public Jexpr subst(JVar x, Jexpr v) {
		if(this == x)
			return v;
		else
			return this;
	}
	}

class JNull implements Jexpr {
	public String pp() { 
		return "";
	}
	public JNull() {

	}
	public Boolean isValue() {
		return true;
	}
	public Jexpr interp() {
		return this;
	}
	public Jexpr step() {
		return this;
	}
	public Jexpr subst(JVar x, Jexpr v) {
		return this;
	}
}

class JCons implements Jexpr {
	public Jexpr lhs, rhs;
	public String pp() {
		return "(" + lhs.pp() + " " + rhs.pp() + ")";
	}
	public JCons(Jexpr l, Jexpr r) {
		lhs = l;
		rhs = r; 
	}
	public Boolean isValue() {
		return false;
	}
	public Jexpr interp() {
		return new JCons(this.lhs.interp(), this.rhs.interp());
	}
	public Jexpr step() {
		return lhs.step();
	}
	public Jexpr subst(JVar x, Jexpr v) {
		return new JCons(lhs.subst(x, v), rhs.subst(x, v));
	}
}

class JPrim implements Jexpr {
	public String prim;
	public JPrim(String p) {
		prim = p;
	}
	public Boolean isValue() {
		return true;
	}
	public String pp() {
		return "" + prim;
	}
	public Jexpr interp() {
		return this;
	}
	public Jexpr step() {
		return this;
	}
	public Jexpr subst(JVar x, Jexpr v) {
		return this;
	}
}

class JNumber implements Jexpr {
	public int num;
	public JNumber(int n) {
		num = n;
	}
	public Boolean isValue() {
		return true;
	}
	public String pp() {
		return "" + num;
	}
	public Jexpr interp() {
		return this;
	}
	public Jexpr step() {
		return this;
	}
	public Jexpr subst(JVar x, Jexpr v) {
		return this;
	}
}

class JBool implements Jexpr {
	public Boolean val;
	public JBool(Boolean b) {
		val = b;
	}
	public Boolean isValue() { 
		return true;
	}
	public String pp() {
		return "" + val;
	}
	public Jexpr interp() {
		return this;
	}
	public Jexpr step() {
		return this;
	}
	public Jexpr subst(JVar x, Jexpr v) {
		return this;
	}
}

class JIf implements Jexpr {
	public Jexpr cond, texpr, fexpr;
	public JIf(Jexpr cond, Jexpr tbr, Jexpr fbr) {
		this.cond = cond;
		texpr = tbr;
		fexpr = fbr;
	}
	public Boolean isValue() {
		return false;
	}
	public String pp() {
		return "(if " + cond.pp() + " " + texpr.pp() + " " + fexpr.pp() + ")";
	}
	public Jexpr interp() {
		Jexpr condv = this.cond.interp();
		if ( condv instanceof JBool
				&& ((JBool)condv).val == false ) {
			return fexpr.interp();
		}
		else {
			return texpr.interp();
		}
	}
	public Jexpr step() {
		if(cond instanceof JBool) {
			if(((JBool)cond).val == true)
				return texpr;
			else
				return fexpr;
		}
		else {
			Jexpr newCond = cond.step();
			cond = newCond;
			return this;
		}
	}
	public Jexpr subst(JVar x, Jexpr v) {
		return new JIf(cond.subst(x, v), texpr.subst(x, v), fexpr.subst(x, v));
	}
}

class JApp implements Jexpr {
	public Jexpr fun, args;
	public JApp(Jexpr fun, Jexpr args) {
		this.fun = fun;
		this.args = args;
	}
	public Boolean isValue() {
		return false;
	}
	public String pp() {
		return "(@ " + fun.pp() + " " + args.pp() + ")";
	}
	public Jexpr interp() {
		Jexpr which_fun = fun.interp();
		Jexpr arg_vals = args.interp();

		String p = ((JPrim)which_fun).prim;
		int lhs = ((JNumber)((JCons)arg_vals).lhs).num;
		int rhs = ((JNumber)((JCons)((JCons)arg_vals).rhs).lhs).num;
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
	public Jexpr step() {
		if(!(((JCons)args).lhs.isValue())) {
			((JCons)args).lhs = ((JCons)args).lhs.step();
			return this;
		}
		else if(!(((JCons)((JCons)args).rhs).lhs.isValue())) {
			((JCons)((JCons)args).rhs).lhs = ((JCons)((JCons)args).rhs).lhs.step();
			return this;
		}
		
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
	public Jexpr subst(JVar x, Jexpr v) {
		return new JApp(fun.subst(x, v), args.subst(x, v));
	}
}