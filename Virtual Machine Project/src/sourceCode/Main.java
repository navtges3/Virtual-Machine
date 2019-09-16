package sourceCode;

public class Main {

	public static void main(String[] args) {
		Test_Suite suite = new Test_Suite();
		System.out.println("Tests Passed: " + suite.run());
		
		Sexprs test = new Sexprs();
		test.left = new Sexprs();
		test.left.data = "-";
		
		test.right = new Sexprs();
		test.right.left = new Sexprs();
		test.right.left.data = "4";
		test.right.right = new Sexprs();
		test.right.right.data = "6";
		
		J0e result = desugar(test);
		System.out.println("Desugar pp test 1 of " + result.pp() + " = " + result.interp());
	}
	
	public static J0e desugar(Sexprs se) {
		
		//is a number
		if(se.isList() == false && !se.data.isEmpty()) {
			System.out.println(se.data);
			int num = Integer.parseInt(se.data);
			System.out.println(num);
			return new JNumber(num);
		}
		//Subtraction
		if(se.left.data.equals("-") && se.length() > 1) {
			Sexprs r = new Sexprs();
			r.left = new Sexprs();
			r.left.data = "-";
			r.right = se.right.right;
			return new JPlus(desugar(se.right.left), desugar(r));
		}
		
		//Negative expression
		if(se.left.data.equals("-"))
			return new JMult(new JNumber(-1), desugar(se.right));
		
		//Single plus sign
		if(se.left.data.equals("+") && se.right == null)
			return new JNumber(0);
		
		//Single mult sign
		if(se.left.data.equals("*") && se.right == null)
			return new JNumber(1);
		
		//Addition
		if(se.left.data.equals("+")) {
			Sexprs r = new Sexprs();
			r.left = new Sexprs();
			r.left.data = "+";
			r.right = se.right.right;
			return new JPlus(desugar(se.right.left), desugar(r));
		}
		
		//Multiplication
		if(se.left.data.equals("*")) {
			Sexprs r = new Sexprs();
			r.left = new Sexprs();
			r.left.data = "*";
			r.right = se.right.right;
			return new JMult(desugar(se.right.left), desugar(r));
		}
		
		return null;
	}

}
