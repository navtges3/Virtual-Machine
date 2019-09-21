package sourceCode;

public class Main {

	public static int test_passed = 0;

	public static void main(String[] args) {		

	}

	public static J0e desugar(Sexpr se) {

		//number
		if(se instanceof SE_Num)
			return JN(((SE_Num)se).num);
		//negation
		if(se instanceof SE_Cons && 
				((SE_Cons)se).lhs instanceof SE_String &&
				((SE_Cons)se).lhs.equals("-") &&
				((SE_Cons)((SE_Cons)se).rhs).rhs instanceof SE_Empty)
			return JM(JN(-1), desugar(((SE_Cons)((SE_Cons)se).rhs).lhs));
		//plus sign
		if(se instanceof SE_Cons && 
				((SE_Cons)se).lhs instanceof SE_String &&
				((SE_Cons)se).lhs.equals("+") &&
				(((SE_Cons)se).rhs) instanceof SE_Empty)
			return JN(0);
		//Asterisk
		if(se instanceof SE_Cons && 
				((SE_Cons)se).lhs instanceof SE_String &&
				((SE_Cons)se).lhs.equals("*") &&
				(((SE_Cons)se).rhs) instanceof SE_Empty)
			return JN(1);
		//subtraction
		if(se instanceof SE_Cons && 
				((SE_Cons)se).lhs instanceof SE_String &&
				((SE_Cons)se).lhs.equals("-") &&
				(((SE_Cons)se).rhs) instanceof SE_Cons)
			return JA(desugar(((SE_Cons)((SE_Cons)se).rhs).lhs), 
					desugar(new SE_Cons(new SE_String("-"), ((SE_Cons)((SE_Cons)se).rhs).rhs)));
		//Multiplication
		if(se instanceof SE_Cons && 
				((SE_Cons)se).lhs instanceof SE_String &&
				((SE_Cons)se).lhs.equals("*") &&
				(((SE_Cons)se).rhs) instanceof SE_Cons)
			return JM(desugar(((SE_Cons)((SE_Cons)se).rhs).lhs), 
					desugar(new SE_Cons(new SE_String("*"), ((SE_Cons)((SE_Cons)se).rhs).rhs)));
		//Addition
		if(se instanceof SE_Cons && 
				((SE_Cons)se).lhs instanceof SE_String &&
				((SE_Cons)se).lhs.equals("+") &&
				(((SE_Cons)se).rhs) instanceof SE_Cons)
			return JA(desugar(((SE_Cons)((SE_Cons)se).rhs).lhs), 
					desugar(new SE_Cons(new SE_String("+"), ((SE_Cons)((SE_Cons)se).rhs).rhs)));

		//Error Code
		return JN(42069);
	}

	/*********************
	 *                   *
	 * Testing functions *
	 *                   *
	 *********************/
	public static void test(Sexpr se, int expected) {
		J0e expr = desugar(se);

		int val = expr.interp();
		if(val != expected)
			System.out.println(val + " != expected val of " + expected);
		else
			test_passed++;
	}

	/********************
	 *                  *
	 * Helper functions *
	 *                  *
	 ********************/
	//create a JNumber
	public static J0e JN(int n) {
		return new JNumber(n);
	}
	//create a JAdd
	public static J0e JA(J0e l, J0e r) {
		return new JPlus(l, r);
	}
	//create a JMult
	public static J0e JM(J0e l, J0e r) {
		return new JMult(l, r);
	}
	//create a numeric Sexpr
	public static Sexpr SN(int n) {
		return new SE_Num(n);
	}
	//represent add as an Sexpr
	public static Sexpr SA(Sexpr l, Sexpr r) {
		return new SE_Cons(new SE_String("+"), new SE_Cons(l, r));
	}
	//represent mult as an Sexpr
	public static Sexpr SM(Sexpr l, Sexpr r) {
		return new SE_Cons(new SE_String("*"), new SE_Cons(l, r));
	}
}
