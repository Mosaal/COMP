/* This Class shall be used to store information about each module */

import java.util.*;

public class Module {

	private String moduleID;
	private ArrayList<Function> functionArray;
	private ArrayList<Variable> globalVariableArray;

	public Module(String moduleID) {
		this.moduleID = moduleID;
		functionArray = new ArrayList<Function>();
		globalVariableArray = new ArrayList<Variable>();
	}

	public String getModuleID() {
		return moduleID;
	}

	public ArrayList<Function> getFunctionArray() {
		return functionArray;
	}

	public ArrayList<Variable> getGlobalVariableArray() {
		return globalVariableArray;
	}

	public void addFunction(Function function) {
		functionArray.add(function);
	}

	public void addGlobalVariable(Variable globalVariable) {
		globalVariableArray.add(globalVariable);
	}
}