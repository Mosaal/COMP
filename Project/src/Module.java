/** This Class shall be used to store information about each module */

import java.util.*;

public class Module {

	private String moduleID;
	private HashMap<String,Function> functionMap;
	private HashMap<String,Variable> globalVariableMap;
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

	public boolean addFunction(Function function) {
		String key = function.toString();
		if (!functionMap.containsKey(key)){
			functionMap.put(key,function);
			return true;
		} else {
			YalToJvm.semanticErrorMessages.add("Function " + key + " already exists");
			return false;
		}
	}
	
	public HashMap<String, Function> getFunctionMap(){
		return functionMap;
	}

	public boolean addGlobalVariable(Variable globalVariable) {
		if (!globalVariableMap.containsKey(globalVariable.getVariableID())){
			globalVariableMap.put(globalVariable.getVariableID(),globalVariable);
			return true;
		} else
			return false;
	}

	public void replaceGlobalVariable(Variable globalVariable) {
		globalVariableMap.put(globalVariable.getVariableID(),globalVariable);
	}
	
	public boolean checkGlobalVariable(String id) {
		return globalVariableMap.containsKey(id);
	}
	
	public Variable getGlobalVariable(String id){
		return globalVariableMap.get(id);
	}

	/**
	* Prints the functions and variables symbol functions
	*/
	public void printSymbolTables(){
		System.out.println("--FUNCTIONS--");
		for(String key : functionMap.keySet()) {
			Function f = functionMap.get(key);
			System.out.println(f);
			f.printData();
	 	}
		System.out.println("--GLOBAL VARIABLES--");
		for(String key : globalVariableMap.keySet()) {
			Variable var = globalVariableMap.get(key);
			System.out.println(var.toString());
		}
	}

	/**
	* Analyses the body of each function to check if there are any semantic errors
	*/
	public void processFunctions(){
		for (String id : functionMap.keySet()) {
			Function f = functionMap.get(id);
			
			//Semantic analysis and CFG building
			f.cfgStartNode = new CFGNode("start",f);
			CFGNode lastNode = functionMap.get(id).getBody().processBody(f,f.cfgStartNode);
			CFGNode endNode = new CFGNode("end",f);
			lastNode.outs.add(endNode);
			endNode.ins.add(lastNode);
			
			//Printing jasmin instructions
			f.fillLocalVariables();
		}
	}

	public boolean functionExists(String functionID) {
		return functionMap.containsKey(functionID);
	}
	
	public boolean functionExistsOnlyName(String functionName){
		for (String key : functionMap.keySet()) {
			if(getFunctionName(key).equals(functionName)){
				return true;
			}
		}
		return false;
	}
	
	private String getFunctionName(String functionID){
		String name = "";
		for (int i = 0; i < functionID.length(); i++) {
			if(functionID.charAt(i) == '('){
				break;
			}else{
				name += functionID.charAt(i);
			}
		}
		return name;
	}
	
	private void processFunctions(String id, List<String> args){
		boolean passed = false;
		for (String key : functionMap.keySet()) {
			if(getFunctionName(key).equals(id)){
				Function f = functionMap.get(key);
				if(f.getNumParameters() == args.size()){
					for (int i = 0; i < args.size(); i++) {
						//Check if variable exist and get its type
						String varId = args.get(i);
					}
				}
			}
		}
	}
	
	private boolean checkVariable(Function f, String id) {
		if (f.getReturnVar() != null && f.checkReturnVariable(id)) {
			return true;
		} else if (f.checkParams(id)) {
			return true;
		} else if (f.checkLocalVariable(id)) {
			return true;
		} else if (checkGlobalVariable(id)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Variable getVariable(Function f, String id) {
		if (f.getReturnVar() != null && f.checkReturnVariable(id)) {
			return f.getReturnVar();
		} else if (f.checkParams(id)) {
			return f.getParameter(id);
		} else if (f.checkLocalVariable(id)) {
			return f.getVariable(id);
		} else if (checkGlobalVariable(id)) {
			return getGlobalVariable(id);
		} else {
			return null;
		}
	}
	
	public Function getFunctionByID(String functionID) {
		return functionMap.get(functionID);
	}

	public Variable getReturnVarFunction(String functionID) {
		return functionMap.get(functionID).getReturnVar();
	}

	public void getAttributes() {
		int num = root.jjtGetNumChildren();
		for (int i = 0; i < num; i++) {
			SimpleNode node = (SimpleNode)root.jjtGetChild(i);
			if (node.getId() == YalToJvmTreeConstants.JJTGLOBAL) {
			}
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
				ArrayList<Variable> params = new ArrayList<>();
				Variable returnVariable = null;
				int num2 = node.jjtGetNumChildren();
				for (int j = 0; j < num2; j++) {
					SimpleNode n = (SimpleNode)node.jjtGetChild(j);
					int id = n.getId();
					if(id == YalToJvmTreeConstants.JJTRETURN){
						returnVariable = getReturnVariable(n);
					}else if (id == YalToJvmTreeConstants.JJTPARAMS) {
						params = getParams(n,node.ID, returnVariable);
					}
				}
				String name = node.ID;
				Function f = new Function(name,returnVariable,params,(SimpleNode)node.jjtGetChild(node.jjtGetNumChildren()-1));
				addFunction(f);
			}
		}
	}

	/**
	*	Gets the return variable of a function
	*/
	public Variable getReturnVariable(SimpleNode n){
		Variable var = null;
		if(n.getId() == YalToJvmTreeConstants.JJTRETURN){
			SimpleNode retVarNode = (SimpleNode)n.jjtGetChild(0);
			int returnId = retVarNode.getId();
			if(returnId == YalToJvmTreeConstants.JJTARRAY){
				var = new Array(retVarNode.ID);
			}else if(returnId == YalToJvmTreeConstants.JJTSCALAR){
				var = new Scalar(retVarNode.ID);
			}
		}
		return var;
	}

	/**
	*	Gets the parameters of the function and checks for any semantic
	*/
	public ArrayList<Variable> getParams(SimpleNode node, String functionId, Variable returnVar){
		ArrayList<Variable> params = new ArrayList<>();
		HashSet<String> set = new HashSet<>();
		int num = node.jjtGetNumChildren();
		for (int i = 0; i < num; i++) {
			SimpleNode n = (SimpleNode)node.jjtGetChild(i);
			if(n.getId() == YalToJvmTreeConstants.JJTSCALAR){
				params.add(new Scalar(n.ID));
			} else if(n.getId() == YalToJvmTreeConstants.JJTARRAY){
				params.add(new Array(n.ID));
			}
			if(set.contains(n.ID)){
				YalToJvm.semanticErrorMessages.add("Function " + functionId + " has parameter: " + n.ID + " duplicated");
			}else if(returnVar != null && n.ID.equals(returnVar.getVariableID())){
				YalToJvm.semanticErrorMessages.add("Function " + functionId + " has parameter: " + n.ID + " with the same ID as the return variable");
			}else {
				set.add(n.ID);
			}
		}
		return params;
	}

}
