/** This Class shall be used to store information about each variable */

public class Variable {
	protected String variableID;

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
	
	public String toString(){
		return "  Variable: " + variableID;
	}
	
	public boolean is(String type){
		if(type.equals("Variable"))
			return true;
		else
			return false;
	}
	
}
