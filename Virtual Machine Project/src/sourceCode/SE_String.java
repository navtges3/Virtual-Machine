package sourceCode;

public class SE_String implements Sexpr{
	public String str;
	
	public SE_String(String s) {
		str = s;
	}

	public String pp() {
		return str;
	}
}
