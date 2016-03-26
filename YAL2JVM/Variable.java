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

	@Override
	public boolean equals(Object object) {
		Variable variable = (Variable) object;

		if (variableID == variable.getVariableID())
			return true;

		return false;
	}

	public void setValue(int value) {
		this.value = value;
	}
}