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

	public boolean addFunction(Function function) {
		String key = function.toString();
		if (!functionMap.containsKey(key)){
			functionMap.put(key,function);
			return true;
		}else{
			YalToJvm.semanticErrorMessages.add("Function " + key + "already exists");
			return false;
		}
	}

	public boolean addGlobalVariable(Variable globalVariable) {
		if (!globalVariableMap.containsKey(globalVariable.getVariableID())){
			globalVariableMap.put(globalVariable.getVariableID(),globalVariable);
			return true;
		}else
			return false;
	}

	public void printSymbolTables(){
		System.out.println("--FUNCTIONS--");
		for(String key : functionMap.keySet()) {
			System.out.println(functionMap.get(key));
	 	}
		System.out.println("--GLOBAL VARIABLES--");
		for(String key : globalVariableMap.keySet()) {
			Variable var = globalVariableMap.get(key);
			System.out.println(var.toString());
		}
	}

}
