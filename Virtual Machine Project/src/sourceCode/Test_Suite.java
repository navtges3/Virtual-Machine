package sourceCode;

public class Test_Suite {

	//Runs through all tests and returns the number of tests completed successfully
	public int run() {
		int numPassed = 0;
		if(JMultInterp())
			numPassed++;

		if(JMultPP())
			numPassed++;

		if(JNumberInterp())
			numPassed++;

		if(JNumberPP())
			numPassed++;

		if(JPlusInterp())
			numPassed++;

		if(JPlusPP())
			numPassed++;

		if(JMultJNumberInterp())
			numPassed++;

		if(JPlusJNumberInterp())
			numPassed++;

		if(JMult2PP())
			numPassed++;
		
		if(JMult2Interp())
			numPassed++;
		
		if(JPlus2PP())
			numPassed++;
		
		if(JPlus2Interp())
			numPassed++;

		return numPassed;
	}

	private boolean JMultInterp() {
		boolean passed = false;		
		JNumber num1 = new JNumber(3);
		JNumber num2 = new JNumber(4);
		JMult mult = new JMult(num1, num2);

		if(mult.interp() == (3 * 4))
			passed = true;
		return passed;
	}

	private boolean JMultPP() {
		boolean passed = false;		
		JNumber num1 = new JNumber(3);
		JNumber num2 = new JNumber(4);
		JMult mult = new JMult(num1, num2);

		if(mult.pp().equals("( 3 * 4 )"))
			passed = true;
		return passed;
	}

	private boolean JNumberInterp() {
		boolean passed = false;
		JNumber num = new JNumber(5);

		if(num.interp() == 5)
			passed = true;
		return passed;
	}

	private boolean JNumberPP() {
		boolean passed = false;
		JNumber num = new JNumber(5);

		if(num.pp().equals("5"))
			passed = true;
		return passed;	}

	private boolean JPlusInterp() {
		boolean passed = false;		
		JNumber num1 = new JNumber(3);
		JNumber num2 = new JNumber(4);
		JPlus plus = new JPlus(num1, num2);

		if(plus.interp() == (3 + 4))
			passed = true;
		return passed;
	}

	private boolean JPlusPP() {
		boolean passed = false;		
		JNumber num1 = new JNumber(3);
		JNumber num2 = new JNumber(4);
		JPlus plus = new JPlus(num1, num2);

		if(plus.pp().equals("( 3 + 4 )"))
			passed = true;
		return passed;
	}

	private boolean JMultJNumberInterp() {
		boolean passed = false;
		JNumber num1 = new JNumber(3);
		JNumber num2 = new JNumber(4);
		JMult mult = new JMult(num1, num2);

		if(mult.interp() == (num1.interp() * num2.interp()))
			passed = true;
		return passed;
	}

	private boolean JPlusJNumberInterp() {
		boolean passed = false;		
		JNumber num1 = new JNumber(3);
		JNumber num2 = new JNumber(4);
		JPlus plus = new JPlus(num1, num2);

		if(plus.interp() == (num1.interp() + num2.interp()))
			passed = true;
		return passed;
	}

	private boolean JMult2PP() {
		boolean passed = false;		
		JNumber num1 = new JNumber(3);
		JNumber num2 = new JNumber(4);
		JMult mult = new JMult(num1, num2);
		JMult mult2 = new JMult(num2, mult);

		if(mult2.pp().equals("( 4 * ( 3 * 4 ) )"))
			passed = true;
		return passed;
	}

	private boolean JMult2Interp() {
		boolean passed = false;		
		JNumber num1 = new JNumber(3);
		JNumber num2 = new JNumber(4);
		JMult mult = new JMult(num1, num2);
		JMult mult2 = new JMult(num2, mult);

		if(mult2.interp() == ( 4 * ( 3 * 4 ) ))
			passed = true;
		return passed;
	}
	
	private boolean JPlus2PP() {
		boolean passed = false;		
		JNumber num1 = new JNumber(3);
		JNumber num2 = new JNumber(4);
		JPlus plus = new JPlus(num1, num2);
		JPlus plus2 = new JPlus(num2, plus);

		if(plus2.pp().equals("( 4 + ( 3 + 4 ) )"))
			passed = true;
		return passed;
	}

	private boolean JPlus2Interp() {
		boolean passed = false;		
		JNumber num1 = new JNumber(3);
		JNumber num2 = new JNumber(4);
		JPlus plus = new JPlus(num1, num2);
		JPlus plus2 = new JPlus(num2, plus);

		if(plus2.interp() == ( 4 + ( 3 + 4 ) ))
			passed = true;
		return passed;
	}
}
