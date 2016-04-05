/** This Class shall be used to store information about each variable */

public class Variable {
	private String variableID;

	public Variable(String id) {
		variableID = id;
	}

	public String getVariableID() {
		return variableID;
	}

	@Override
	public boolean equals(Object object) {
		Variable variable = (Variable) object;
		if (variableID.equals(variable.getVariableID()))
			return true;
		return false;
	}
}
