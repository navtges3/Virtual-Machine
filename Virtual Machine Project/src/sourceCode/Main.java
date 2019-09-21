package sourceCode;

public class Main {

	public static int test_passed = 0;

	public static void main(String[] args) {		
		runTests();
	}

	public static J0e desugar(Sexpr se) {

		//number
		if(se instanceof SE_Num)
			return JN(((SE_Num)se).num);
		//negation
		if(se instanceof SE_Cons && 
				((SE_Cons)se).lhs instanceof SE_String &&
				((SE_String)((SE_Cons)se).lhs).str.equals("-") &&
				((SE_Cons)((SE_Cons)se).rhs).rhs instanceof SE_Empty)
			return JM(JN(-1), desugar(((SE_Cons)((SE_Cons)se).rhs).lhs));
		//plus sign
		if(se instanceof SE_Cons && 
				((SE_Cons)se).lhs instanceof SE_String &&
				((SE_String)((SE_Cons)se).lhs).str.equals("+") &&
				(((SE_Cons)se).rhs) instanceof SE_Empty)
			return JN(0);
		//Asterisk
		if(se instanceof SE_Cons && 
				((SE_Cons)se).lhs instanceof SE_String &&
				((SE_String)((SE_Cons)se).lhs).str.equals("*") &&
				(((SE_Cons)se).rhs) instanceof SE_Empty)
			return JN(1);
		//subtraction
		if(se instanceof SE_Cons && 
				((SE_Cons)se).lhs instanceof SE_String &&
				((SE_String)((SE_Cons)se).lhs).str.equals("-") &&
				(((SE_Cons)se).rhs) instanceof SE_Cons)
			return JA(desugar(((SE_Cons)((SE_Cons)se).rhs).lhs), 
					desugar(new SE_Cons(((SE_Cons)se).lhs, ((SE_Cons)((SE_Cons)se).rhs).rhs)));
		//Multiplication
		if(se instanceof SE_Cons && 
				((SE_Cons)se).lhs instanceof SE_String &&
				((SE_String)((SE_Cons)se).lhs).str.equals("*") &&
				(((SE_Cons)se).rhs) instanceof SE_Cons)
			return JM(desugar(((SE_Cons)((SE_Cons)se).rhs).lhs), 
					desugar(new SE_Cons(((SE_Cons)se).lhs, ((SE_Cons)((SE_Cons)se).rhs).rhs)));
		//Addition
		if ( se instanceof SE_Cons
				&& ((SE_Cons)se).lhs instanceof SE_String
				&& ((SE_String)((SE_Cons)se).lhs).str.equals("+")
				&& ((SE_Cons)se).rhs instanceof SE_Cons ) {
			return JA( desugar(((SE_Cons)((SE_Cons)se).rhs).lhs),
					desugar(new SE_Cons(((SE_Cons)se).lhs, ((SE_Cons)((SE_Cons)se).rhs).rhs)) ); }

		//Error Code
		return JN(42069);
	}

	/*********************
	 *                   *
	 * Testing functions *
	 *                   *
	 *********************/
	public static void test(Sexpr se, J0e expected) {
		J0e expr = desugar(se);
		int val = expr.interp();
		
		System.out.println(se.pp() + " desugars to " + expr.pp());
		if(val != expected.interp())
			System.out.println(val + " != expected val of " + expected.interp());
		else {
			test_passed++;
		}
	}

	public static void test_num(Sexpr se, int expected) {
		test(se, JN(expected));
	}

	public static void runTests() {

		test_num(SN(42), 42);
		test_num(SN(7), 7);
		test_num(SA(SN(42),SN(0)), 42);
		test_num(SM(SN(42),SN(0)), 0);
		test_num(SA(SM(SN(42),SN(0)),SN(0)), 0);
		test_num(SA(SM(SN(42),SN(0)),SA(SM(SN(42),SN(0)),SN(0))), 0);

		test_num(SA(SN(42),SN(1)), 43);
		test_num(SM(SN(42),SN(1)), 42);
		test_num(SA(SM(SN(42),SN(1)),SN(1)), 43);
		test_num(SA(SM(SN(42),SN(1)),SA(SM(SN(42),SN(1)),SN(1))), 85);

		test_num(new SE_Cons(new SE_String("+"), new SE_Empty()), 0);
		test_num(new SE_Cons(new SE_String("*"), new SE_Empty()), 1);
		Sexpr three_things =
				new SE_Cons(new SE_Num(1),
						new SE_Cons(new SE_Num(2),
								new SE_Cons(new SE_Num(4),
										new SE_Empty())));
		test_num(new SE_Cons(new SE_String("+"), three_things), 7);
		test_num(new SE_Cons(new SE_String("*"), three_things), 8);

		test_num(new SE_Cons(new SE_String("-"), new SE_Cons(new SE_Num(4), new SE_Empty())), -4);
		test_num(new SE_Cons(new SE_String("-"), new SE_Cons(new SE_Num(4), new SE_Cons(new SE_Num(2), new SE_Empty()))), 2);
		System.out.println(test_passed + " tests passed.");
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
		return new SE_Cons(new SE_String("+"), new SE_Cons(l, new SE_Cons(r, new SE_Empty())));
	}
	//represent mult as an Sexpr
	public static Sexpr SM(Sexpr l, Sexpr r) {
		return new SE_Cons(new SE_String("*"), new SE_Cons(l, new SE_Cons(r, new SE_Empty())));
	}
}
