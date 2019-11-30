package sourceCode;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static int test_passed = 0;
	public static HashMap<String, Define> sigMap = new HashMap<String, Define>();

	public static void main(String[] args) throws IOException {
		Jexpr e = JA(JM(JN(2), JN(4)), JN(8));
		System.out.println("CC0: " + CC0.interp(e).pp());
		System.out.println("Big: " + e.interp().pp());
		emit(e);

		test_j3();
		runTests();
	}

	public static void emit(Jexpr e) throws IOException {
		FileWriter fileWriter = new FileWriter("ll.c");
		PrintWriter printWriter = new PrintWriter(fileWriter);

		printWriter.printf("#include " + '"' + "Source.c" + '"' + ";\n");
		printWriter.printf("#include <stdio.h>;\n");
		printWriter.printf("int main(int argc, char* argv[]) {\n");

		if(e instanceof JApp) {
			printWriter.printf("expr* o = " + printJApp(e) + ";\n");
		}
		else if (e instanceof JIf) {
			printWriter.printf("expr* o = " + printJIf(e) + ";\n");
		}
		else if (e instanceof JNumber) {
			printWriter.printf("expr* o = " + printJNum(e) + ";\n");
		}
		else if (e instanceof JBool) {
			printWriter.printf("expr* o = " + printJBool(e) + ";\n");
		}
		else if (e instanceof lambda) {
			printWriter.printf("expr* o = " + printLambda(e) + ";\n");
		}
		printWriter.printf("return 0;\n");
		printWriter.printf("}");

		printWriter.close();
	}

	private static String printLambda(Jexpr e) {
		String output = "make_lambda(";

		output = output.concat(printCons(((lambda)e).vars));
		output = output.concat(")");
		return output;
	}

	private static String printCons(Jexpr e) {
		String output = "";

		if(e instanceof JCons) {
			if(((JCons)e).lhs instanceof JIf) {
				output = output.concat(printJIf(((JCons)e).lhs));
			}
			else if (((JCons)e).lhs instanceof JApp) {
				output = output.concat(printJApp(((JCons)e).lhs));
			}
			else if (((JCons)e).lhs instanceof JBool) {
				output = output.concat(printJBool(((JCons)e).lhs));
			}
			else if (((JCons)e).lhs instanceof JNumber) {
				output = output.concat(printJNum(((JCons)e).lhs));
			}
			else if (((JCons)e).lhs instanceof JVar) {
				output = output.concat("make_var(" + ((JVar)((JCons)e).lhs).name + ")");
			}
		}

		output = output.concat(printCons(((JCons)e).rhs));
		return output;
	}

	private static String printJApp(Jexpr e) {
		String output = "make_app(";
		//fun
		output = output.concat("make_prim(" + ((JPrim)((JApp)e).fun).prim + "), ");

		//arg1
		if(((JCons)((JApp)e).args).lhs instanceof JIf)
			output = output.concat(printJIf(((JCons)((JApp)e).args).lhs));
		else if(((JCons)((JApp)e).args).lhs instanceof JApp)
			output = output.concat(printJApp(((JCons)((JApp)e).args).lhs));
		else if(((JCons)((JApp)e).args).lhs instanceof JBool)
			output = output.concat(printJBool(((JCons)((JApp)e).args).lhs));
		else if(((JCons)((JApp)e).args).lhs instanceof JNumber)
			output = output.concat(printJNum(((JCons)((JApp)e).args).lhs));

		output = output.concat(", ");

		//arg2
		if(((JCons)((JCons)((JApp)e).args).rhs).lhs instanceof JIf)
			output = output.concat(printJIf(((JCons)((JCons)((JApp)e).args).rhs).lhs));
		else if(((JCons)((JCons)((JApp)e).args).rhs).lhs instanceof JApp)
			output = output.concat(printJApp(((JCons)((JCons)((JApp)e).args).rhs).lhs));
		else if(((JCons)((JCons)((JApp)e).args).rhs).lhs instanceof JBool)
			output = output.concat(printJBool(((JCons)((JCons)((JApp)e).args).rhs).lhs));
		else if(((JCons)((JCons)((JApp)e).args).rhs).lhs instanceof JNumber) {
			output = output.concat(printJNum(((JCons)((JCons)((JApp)e).args).rhs).lhs));
		}
		output = output.concat(")");
		System.out.println(output);
		return output;
	}

	private static String printJIf(Jexpr e) {
		String output = "make_if(";
		//cond
		if(((JIf)e).cond instanceof JIf)
			output = output.concat(printJIf(((JIf)e).cond) + ", ");
		else if(((JIf)e).cond instanceof JApp)
			output = output.concat(printJApp(((JIf)e).cond) + ", ");
		else if(((JIf)e).cond instanceof JBool)
			output = output.concat(printJBool(((JIf)e).cond) + ", ");
		else if(((JIf)e).cond instanceof JNumber)
			output = output.concat(printJNum(((JIf)e).cond) + ", ");

		//true
		if(((JIf)e).texpr instanceof JIf)
			output = output.concat(printJIf(((JIf)e).texpr) + ", ");
		else if(((JIf)e).texpr instanceof JApp)
			output = output.concat(printJApp(((JIf)e).texpr) + ", ");
		else if(((JIf)e).texpr instanceof JBool)
			output = output.concat(printJBool(((JIf)e).texpr) + ", ");
		else if(((JIf)e).texpr instanceof JNumber)
			output = output.concat(printJNum(((JIf)e).texpr) + ", ");

		//false
		if(((JIf)e).fexpr instanceof JIf)
			output = output.concat(printJIf(((JIf)e).fexpr) + ")");
		else if(((JIf)e).fexpr instanceof JApp)
			output = output.concat(printJApp(((JIf)e).fexpr) + ")");
		else if(((JIf)e).fexpr instanceof JBool)
			output = output.concat(printJBool(((JIf)e).fexpr) + ")");
		else if(((JIf)e).fexpr instanceof JNumber)
			output = output.concat(printJNum(((JIf)e).fexpr) + ")");
		return output;
	}

	private static String printJBool(Jexpr e) {
		return "make_bool(" + ((JBool)e).val + ")";
	}

	private static String printJNum(Jexpr e) {
		return "make_num(" + ((JNumber)e).num + ")";
	}

	// Used to convert from S-expressions to J-expressions
	public static Jexpr desugar(Sexpr se) {

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

		//Application
		if(se instanceof SE_Cons &&
				((SE_Cons)se).lhs instanceof SE_String &&
				((SE_Cons)se).rhs instanceof SE_Cons &&
				((SE_Cons)((SE_Cons)se).rhs).rhs instanceof SE_Cons &&
				((SE_Cons)((SE_Cons)((SE_Cons)se).rhs).rhs).rhs instanceof SE_Empty)
			return new JApp(new JPrim(((SE_String)((SE_Cons)se).lhs).str), 
					new JCons(desugar(((SE_Cons)((SE_Cons)se).rhs).lhs), 
							new JCons(desugar(((SE_Cons)((SE_Cons)((SE_Cons)se).rhs).rhs).lhs), new JNull())));
		//if
		if ( se instanceof SE_Cons
				&& ((SE_Cons)se).lhs instanceof SE_String
				&& ((SE_String)((SE_Cons)se).lhs).str.equals("if")
				&& ((SE_Cons)se).rhs instanceof SE_Cons
				&& ((SE_Cons)((SE_Cons)se).rhs).rhs instanceof SE_Cons
				&& ((SE_Cons)((SE_Cons)((SE_Cons)se).rhs).rhs).rhs instanceof SE_Cons
				&& ((SE_Cons)((SE_Cons)((SE_Cons)((SE_Cons)se).rhs).rhs).rhs).rhs instanceof SE_Empty )
			return new JIf( desugar(((SE_Cons)((SE_Cons)se).rhs).lhs),
					desugar(((SE_Cons)((SE_Cons)((SE_Cons)se).rhs).rhs).lhs),
					desugar(((SE_Cons)((SE_Cons)((SE_Cons)((SE_Cons)se).rhs).rhs).rhs).lhs) );

		//lambda
		if(se instanceof SE_Cons
				&& ((SE_Cons)se).lhs instanceof SE_String
				&& ((SE_String)((SE_Cons)se).lhs).str.equals("let")
				&& ((SE_Cons)se).rhs instanceof SE_Cons)
			return new lambda(((SE_String)((SE_Cons)((SE_Cons)se).rhs).lhs).str,
					desugar(((SE_Cons)((SE_Cons)((SE_Cons)se).rhs).rhs).lhs), 
					desugar(((SE_Cons)((SE_Cons)((SE_Cons)((SE_Cons)se).rhs).rhs).rhs).lhs));


		//Error Code
		return JN(42069);
	}

	/*********************
	 *                   *
	 * Testing functions *
	 *                   *
	 *********************/
	public static void lambdaFactorial() {
		Jexpr fac = new lambda("fac", new JCons(new JVar("n"), new JNull()), //function
				new JIf(new lambda("zero?", new JCons(new JVar("n"), new JNull()), new JIf(new JVar("n"), new JBool(true), new JBool(false))), // is zero?
						new lambda("one", new JCons(new JVar("f"), new JCons(new JVar("x"), new JNull())), new JCons(new JVar("x"), new JNull())), //one
						new lambda("mult", new JCons(new JVar("n"), new JCons(new JVar("m"), new JNull()), new JApp(new JPrim("*"), new JCons(new JVar("n"), new JCons(new JVar("m"), new JNull())))))
	}
	
	public static void test(Sexpr se, Jexpr expected) {
		Jexpr expr = desugar(se);
		Jexpr val = expr.interp();

		System.out.println(se.pp() + " desugars to " + expr.pp());
		if(!val.pp().equals(expected.interp().pp()))
			System.out.println(val.pp() + " != expected val of " + expected.interp().pp());
		else {
			test_passed++;
		}
	}

	public static void test_num(Sexpr se, int expected) {
		test(se, JN(expected));
	}

	public static void test_j3() {
		Jexpr e1 = JA(JN(7), new JVar("x"));
		Jexpr e2 = JM(JN(7), new JVar("x"));
		Jexpr e3 = JA(new JVar("x"), JN(7));
		Jexpr e4 = JM(new JVar("x"), JN(7));

		Jexpr function = new lambda("func", new JCons(new JVar("x"), new JNull()), e1);
		Jexpr result = function.subst(new JVar("x"), JN(5));
		if(result.interp().pp().equals("12"))
			test_passed++;

		function = new lambda("func", new JCons(new JVar("x"), new JNull()), e2);
		result = function.subst(new JVar("x"), JN(5));
		if(result.interp().pp().equals("35"))
			test_passed++;
		
		function = new lambda("func", new JCons(new JVar("x"), new JNull()), function);
		result = function.subst(new JVar("x"), JN(5));
		if(result.interp().pp().equals("35"))
			test_passed++;
		
		function = new lambda("func", new JCons(new JVar("x"), new JNull()), e3);
		result = function.subst(new JVar("x"), JN(5));
		if(result.interp().pp().equals("12"))
			test_passed++;

		function = new lambda("func", new JCons(new JVar("x"), new JNull()), e4);
		result = function.subst(new JVar("x"), JN(5));
		if(result.interp().pp().equals("35"))
			test_passed++;
		
		function = new lambda("func", new JCons(new JVar("x"), new JNull()), function);
		result = function.subst(new JVar("x"), JN(5));
		if(result.interp().pp().equals("35"))
			test_passed++;
		
		Sexpr se = Slambda(new SE_String("new func"), new SE_Empty(), SA(SN(3), SN(5)));
		Jexpr test = desugar(se);
		if(test.subst(null, null).interp().pp().equals("8"))
			test_passed++;

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

		test(new SE_Cons(new SE_String("=="), new SE_Cons(new SE_Num(4), new SE_Cons(new SE_Num(2), new SE_Empty()))), new JBool(false));
		test(new SE_Cons(new SE_String("=="), new SE_Cons(new SE_Num(4), new SE_Cons(new SE_Num(4), new SE_Empty()))), new JBool(true));

		test(SApp("==", new SE_Num(4), new SE_Num(4)), new JBool(true));
		test(SIf(SApp("==", new SE_Num(4), new SE_Num(4)), new SE_Num(5), new SE_Num(6)), JN(5));
		test(SIf(SApp("==", new SE_Num(4), new SE_Num(2)), new SE_Num(5), new SE_Num(6)), JN(6));

		System.out.println(test_passed + " tests passed.");
	}

	/********************
	 *                  *
	 * Helper functions *
	 *                  *
	 ********************/
	//create a JNumber
	public static Jexpr JN(int n) {
		return new JNumber(n);
	}
	//create a JAdd
	public static Jexpr JA(Jexpr l, Jexpr r) {
		return new JApp(new JPrim("+"), new JCons(l, new JCons(r, new JNull())));
	}
	//create a JMult
	public static Jexpr JM(Jexpr l, Jexpr r) {
		return new JApp(new JPrim("*"), new JCons(l, new JCons(r, new JNull())));
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
	//represent a JApp as a Sexpr
	public static Sexpr SApp(String op, Sexpr l, Sexpr r) {
		return new SE_Cons(new SE_String(op), new SE_Cons(l, new SE_Cons(r, new SE_Empty())));
	}
	//represent a JIf as a Sexpr
	public static Sexpr SIf(Sexpr c, Sexpr l, Sexpr r) {
		return new SE_Cons(new SE_String("if"), new SE_Cons(c, new SE_Cons(l, new SE_Cons(r, new SE_Empty()))));
	}
	//represent a lambda as a Sexpr
	public static Sexpr Slambda(Sexpr name, Sexpr e1, Sexpr e2) {
		return new SE_Cons(new SE_String("let"), new SE_Cons(name, new SE_Cons(e1, new SE_Cons(e2, new SE_Empty()))));
	}
}