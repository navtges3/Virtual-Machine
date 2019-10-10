package sourceCode;

public class Define {
	JFun fun;
	Jexpr e;
	
	public Define(JFun fun, Jexpr e) {
		this.fun = fun;
		this.e = e;
	}
	
	public String pp() {
		return "define " + fun.pp() + "(" + e.pp() + ")"; 
	}
}
