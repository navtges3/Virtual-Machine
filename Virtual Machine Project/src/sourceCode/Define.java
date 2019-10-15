package sourceCode;

public class Define {
	lambda fun;
	Jexpr e;
	
	public Define(lambda fun, Jexpr e) {
		this.fun = fun;
		this.e = e;
	}
	
	public String pp() {
		return "define " + fun.pp() + "(" + e.pp() + ")"; 
	}
}
