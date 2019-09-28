package sourceCode;

public interface Jexpr {
	public Jexpr interp();
	public String pp();
	public Boolean isValue();
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
}

class JCons implements Jexpr {
	public Jexpr lhs, rhs;
	public String pp() {
		return "(" + this.lhs.pp() + " " + this.rhs.pp() + ")";
	}
	public JCons(Jexpr l, Jexpr r) {
		this.lhs = l;
		this.rhs = r; 
	}
	public Boolean isValue() {
		return false;
	}
	public Jexpr interp() {
		return new JCons(this.lhs.interp(), this.rhs.interp());
	}
}

class JPrim implements Jexpr {
	public String prim;
	public JPrim(String p) {
		this.prim = p;
	}
	public Boolean isValue() {
		return true;
	}
	public String pp() {
		return "" + this.prim;
	}
	public Jexpr interp() {
		return this;
	}
}

class JNumber implements Jexpr {
	public int num;
	public JNumber(int n) {
		this.num = n;
	}
	public Boolean isValue() {
		return true;
	}
	public String pp() {
		return "" + this.num;
	}
	public Jexpr interp() {
		return this;
	}
}

class JBool implements Jexpr {
	public Boolean val;
	public JBool(Boolean b) {
		this.val = b;
	}
	public Boolean isValue() { 
		return true;
	}
	public String pp() {
		return "" + this.val;
	}
	public Jexpr interp() {
		return this;
	}
}

class JIf implements Jexpr {
	public Jexpr cond, texpr, fexpr;
	public JIf(Jexpr cond, Jexpr tbr, Jexpr fbr) {
		this.cond = cond;
		this.texpr = tbr;
		this.fexpr = fbr;
	}
	public Boolean isValue() {
		return false;
	}
	public String pp() {
		return "(if " + this.cond.pp() + " " + this.texpr.pp() + " " + this.fexpr.pp() + ")";
	}
	public Jexpr interp() {
		Jexpr condv = this.cond.interp();
		if ( condv instanceof JBool
				&& ((JBool)condv).val == false ) {
			return this.fexpr.interp();
		}
		else {
			return this.texpr.interp();
		}
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
		return "(@ " + this.fun.pp() + " " + this.args.pp() + ")";
	}
	public Jexpr interp() {
		Jexpr which_fun = this.fun.interp();
		Jexpr arg_vals = this.args.interp();

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

		return new JNumber(666); 
	}
}