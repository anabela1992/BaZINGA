public class variable{
	private String name;
	private String value;
	private int line;
	private boolean tainted;
	private int vuln;

	public variable(String name, String value, int line, boolean tainted){
		this.name = name;
		this.value = value;
		this.line = line;
		this.tainted = tainted;
		this.vuln = -1;
	}

	public String getName(){
		return this.name;
	}

	public String getValue(){
		return this.value;
	}

	public void setValue(String value){
		this.value = value;
	}

	public void taint(){
		this.tainted = true;
	}

	public void untaint(){
		this.tainted = false;
	}

	public boolean getTainted(){
		return this.tainted;
	}

	public int getLine(){
		return this.line;
	}

	public void setVulnerability(int vuln){
		this.vuln = vuln;
	}

	public int getVulnerability(){
		return this.vuln;
	}
}