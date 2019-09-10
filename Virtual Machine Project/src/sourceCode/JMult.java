package sourceCode;

public class JMult implements J0e {
	J0e left;
	J0e right;
	
	public JMult(J0e l, J0e r) {
		left = l;
		right = r;
	}

	public String pp() {
		return "( " + left.pp() + " * " + right.pp() + " )";		
	}
}
