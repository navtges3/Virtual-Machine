package sourceCode;

public class JPlus implements J0e {
	J0e left;
	J0e right;
	
	//constructor
	public JPlus(J0e l, J0e r) {
		left = l;
		right = r;
	}

	//pretty print
	public String pp() {
		return "( " + left.pp() + " + " + right.pp() + " )";
	}

	//return the addition of the two sides of the expression
	public int interp() {
		return (left.interp() + right.interp());
	}
}
