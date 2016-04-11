/** This Class shall be used to store information about each module */

import java.util.*;

import javax.swing.JViewport;

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
	
	public boolean functionExists(String functionID) {
		return false;
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
						returnVariable = getVariable(n);
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
	
	public Variable getVariable(SimpleNode n){
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
