package sourceCode;

public class SE_Num implements Sexpr {
	public int num;

	public SE_Num(int n) {
		num = n;
	}

	public String pp() {
		return Integer.toString(num);
	}
}
