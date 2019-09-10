package sourceCode;

public class JNumber implements J0e {
	int n;
	
	//constructor
	public JNumber(int _n) {
		n = _n;
	}

	//pretty print
	public String pp() {
		return Integer.toString(n);
	}

	//returns the value of the number
	public int interp() {		
		return n;
	}
}
