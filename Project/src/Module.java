/** This Class shall be used to store information about each module */

import java.util.*;

public class Module {

	private String moduleID;
	private ArrayList<Function> functionArray;
	private HashMap<String,Function> functionMap;
	private HashMap<String,Variable> globalVariableMap;

	public Module(String id) {
		moduleID = id;
		functionMap = new HashMap<>();
		globalVariableMap = new HashMap<>();
	}

	public String getModuleID() {
		return moduleID;
	}

	public HashMap<String,Function> getFunctionArray() {
		return functionMap;
	}

	public HashMap<String,Variable> getGlobalVariableArray() {
		return globalVariableMap;
	}

	public void addFunction(Function function) {
		if (!functionMap.containsKey(function.getFunctionID()))
			functionMap.put(function.getFunctionID(),function);
	}

	public void addGlobalVariable(Variable globalVariable) {
		if (!globalVariableMap.containsKey(globalVariable.getVariableID()))
			globalVariableMap.put(globalVariable.getVariableID(),globalVariable);
	}
}
