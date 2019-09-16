package sourceCode;

public class Sexprs {
	public Sexprs left = null;
	public Sexprs right = null;
	public String data = "";
	
	public int length() {
		if (this.equals(null))
			return 0;
		if (this.data.isEmpty())
			return 1 + this.right.length();
		else
			return 0;
	}
	
	public boolean isList() {
		if (this.right != null && this.right.data.isEmpty())
			return true;
		
		else
			return false;
	}
}
