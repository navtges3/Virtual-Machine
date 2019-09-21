package sourceCode;

public class JMult implements J0e {
	J0e left;
	J0e right;
	
	//constructor
	public JMult(J0e l, J0e r) {
		left = l;
		right = r;
	}

	//pretty print
	public String pp() {
		return "(* " + left.pp() + " " + right.pp() + ")";		
	}

	//return the value of left times right
	public int interp() {
		return (left.interp() * right.interp());
	}
}
