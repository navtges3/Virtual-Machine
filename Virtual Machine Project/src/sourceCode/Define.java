package sourceCode;

public class Define {
	Jexpr fun;
	Jexpr params;
	Jexpr e;
	
	public Define(Jexpr fun, Jexpr params, Jexpr e) {
		this.fun = fun;
		this.params = params;
		this.e = e;
	}
	
	public String pp() {
		return "define (" + fun.pp() + " " + params.pp() + ") (" + e.pp() + ")"; 
	}
}
