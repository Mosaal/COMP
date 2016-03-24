/* This Class shall be used to store information about each function */

import java.util.*;

public class Function {

	private String functionID;
	private ArrayList<Variable> variableList;

	public Function(String functionID) {
		this.functionID = functionID;
		variableList = new ArrayList<Variable>();
	}

	public String getFunctionID() {
		return functionID;
	}

	public ArrayList<Variable> getVariableList() {
		return variableList;
	}

	public void addVariable(Variable variable) {
		variableList.add(variable);
	}

	public void setVariableValue(String variableID, int value) {
		for (Variable array: variableList) {
			if (array.getVariableID() == variableID)
				array.setValue(value);
		}
	}
}