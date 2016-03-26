/* This Class shall be used to store information about each function */

import java.util.*;

public class Function {

	private String functionID;
	private ArrayList<Variable> variableArray;

	public Function(String functionID) {
		this.functionID = functionID;
		variableArray = new ArrayList<Variable>();
	}

	public String getFunctionID() {
		return functionID;
	}

	public ArrayList<Variable> getVariableArray() {
		return variableArray;
	}

	public void addVariable(Variable variable) {
		variableArray.add(variable);
	}

	public void setVariableValue(String variableID, int value) {
		for (Variable array: variableArray) {
			if (array.getVariableID() == variableID)
				array.setValue(value);
		}
	}
}