/* This Class shall be used to store information about each function */

import java.util.*;

public class Function {

	private String functionID;
	private HashMap<String,Variable> variableMap;
	private ArrayList<Variable> variableArray;

	public Function(String functionID) {
		this.functionID = functionID;
		variableArray = new ArrayList<Variable>();
	}

	public int getNumVariable() {
		return variableArray.size();
	}

	public String getFunctionID() {
		return functionID;
	}

	public ArrayList<Variable> getVariableArray() {
		return variableArray;
	}

	@Override
	public boolean equals(Object object) {
		Function function = (Function) object;

		if (functionID == function.getFunctionID() && getNumVariable() == function.getNumVariable())
			return true;

		return false;
	}

	public void addVariable(Variable variable) {
		variableArray.add(variable);
	}

	public void setVariableValue(String variableID, int value) {
		for (Variable array: variableArray) {
			if (array.getVariableID() == variableID)
				if(array instanceof Scalar)
					((Scalar)array).setValue(value);
		}
	}
}
