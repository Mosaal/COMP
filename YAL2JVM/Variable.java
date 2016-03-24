/* This Class shall be used to store information about each variable */

public class Variable {

	private int value;
	private String variableID;

	public Variable(String variableID) {
		this.variableID = variableID;
	}

	public int getValue() {
		return value;
	}

	public String getVariableID() {
		return variableID;
	}

	public void setValue(int value) {
		this.value = value;
	}
}