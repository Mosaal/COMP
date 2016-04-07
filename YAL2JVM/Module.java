/** This Class shall be used to store information about each module */

import java.util.*;

public class Module {

	private String moduleID;
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

	public boolean checkGlobalVariable(String id){
		return globalVariableMap.containsKey(id);
	}

	public void addFunction(Function function) {
			functionMap.put(function.getFunctionID(),function);
	}

	public void addGlobalVariable(Variable globalVariable) {
			globalVariableMap.put(globalVariable.getVariableID(),globalVariable);
	}

	public Variable getGlobalVariable(String key) {
		if (globalVariableMap.containsKey(key))
			return globalVariableMap.get(key);
		else
			return null;
	}

	public void printSymbolTables(){
		System.out.println("--FUNCTIONS--");
		for(String key : functionMap.keySet()) {
     System.out.println(key + " : " + functionMap.get(key));
	 	}
		System.out.println("--GLOBAL VARIABLES--");
		for(String key : globalVariableMap.keySet()) {
			Variable var = globalVariableMap.get(key);
			System.out.println(var.toString());
		}
	}
}
