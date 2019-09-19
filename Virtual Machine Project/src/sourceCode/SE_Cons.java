package sourceCode;

public class SE_Cons implements Sexpr {
	public Sexpr lhs, rhs;
	
	public SE_Cons(Sexpr l, Sexpr r) {
		lhs = l;
		rhs = r;
	}
}
