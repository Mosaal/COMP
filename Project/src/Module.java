/** This Class shall be used to store information about each module */

import java.util.*;

public class Module {

	private String moduleID;
	private HashMap<String,Function> functionMap;
	private HashMap<String,Variable> globalVariableMap;
	private Variable ret;
	private SimpleNode root;

	public Module(String id, SimpleNode r) {
		moduleID = id;
		functionMap = new HashMap<String,Function>();
		globalVariableMap = new HashMap<String,Variable>();
		root = r;
	}

	public String getModuleID() {
		return moduleID;
	}

	public HashMap<String,Function> getFunctionMap() {
		return functionMap;
	}

	public HashMap<String,Variable> getGlobalVariableMap() {
		return globalVariableMap;
	}

	public boolean addFunction(Function function) {
		String key = function.toString();
		if (!functionMap.containsKey(key)){
			functionMap.put(key,function);
			return true;
		}else{
			YalToJvm.semanticErrorMessages.add("Function " + key + " already exists");
			return false;
		}
	}

	public boolean addGlobalVariable(Variable globalVariable) {
		if (!globalVariableMap.containsKey(globalVariable.getVariableID())){
			globalVariableMap.put(globalVariable.getVariableID(),globalVariable);
			return true;
		} else
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

	public void processFunctions(){
		for (String id : functionMap.keySet()) {
			functionMap.get(id).getBody().processBody(functionMap.get(id));
		}
	}

	/**
	 * Analyses the AST tree and looks for functions.
	 * Adds them in the symbol table and returns any
	 * kind of semantic error message
	 */
	public void getFunctions() {
		int num = root.jjtGetNumChildren();
		for (int i = 0; i < num; i++) {
			SimpleNode node = (SimpleNode)root.jjtGetChild(i);
			if(node.getId() == YalToJvmTreeConstants.JJTFUNCTION){
				ArrayList<Variable> params = getParams(node);
				String name = node.ID;
				Function f = new Function(name,params,(SimpleNode)node.jjtGetChild(node.jjtGetNumChildren()-1));
				addFunction(f);
			}
		}
	}

	public ArrayList<Variable> getParams(SimpleNode node){
		int num = node.jjtGetNumChildren();
		ArrayList<Variable> params = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			SimpleNode n = (SimpleNode)node.jjtGetChild(i);
			if(n.getId() == YalToJvmTreeConstants.JJTPARAMS){
				getVariables(n,params);
			}else if(n.getId() == YalToJvmTreeConstants.JJTARRAY){
				
			}else if(n.getId() == YalToJvmTreeConstants.JJTSCALAR){
				
			}
		}
		return params;
	}

	public void getVariables(SimpleNode node, ArrayList<Variable> params){
		int num = node.jjtGetNumChildren();
		for (int i = 0; i < num; i++) {
			SimpleNode n = (SimpleNode)node.jjtGetChild(i);
			if(n.getId() == YalToJvmTreeConstants.JJTSCALAR){
				params.add(new Scalar(node.ID));
			} else if(n.getId() == YalToJvmTreeConstants.JJTARRAY){
				params.add(new Array(node.ID));
			}
		}
	}

}
