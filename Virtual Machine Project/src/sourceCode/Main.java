package sourceCode;

public class Main {

	public static int test_passed = 0;

	public static void main(String[] args) {		
		
	}

	public static J0e desugar(Sexpr se) {

		return null;
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
	public J0e JN(int n) {
		return new JNumber(n);
	}
	//create a JAdd
	public J0e JA(J0e l, J0e r) {
		return new JPlus(l, r);
	}
	//create a JMult
	public J0e JM(J0e l, J0e r) {
		return new JMult(l, r);
	}
	//create a numeric Sexpr
	public Sexpr SN(int n) {
		return new SE_Num(n);
	}
	//represent add as an Sexpr
	public Sexpr SA(Sexpr l, Sexpr r) {
		return new SE_Cons(new SE_String("+"), new SE_Cons(l, r));
	}
	//represent mult as an Sexpr
	public Sexpr SM(Sexpr l, Sexpr r) {
		return new SE_Cons(new SE_String("*"), new SE_Cons(l, r));
	}
}
