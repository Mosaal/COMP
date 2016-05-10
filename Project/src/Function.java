/* This Class shall be used to store information about each function */

import java.util.*;

public class Function {

	private SimpleNode body;
	private String functionID;
	private Variable returnVar;
	private ArrayList<Variable> parameters;
	private HashMap<String,Variable> variableMap;

	public Function(String functionID) {
		this.functionID = functionID;
		parameters = new ArrayList<Variable>();
		variableMap = new HashMap<String,Variable>();
	}

	public Function(String id, Variable ret,ArrayList<Variable> p,SimpleNode n){
		functionID = id;
		returnVar = ret;
		parameters = p;
		body = n;
		variableMap = new HashMap<String,Variable>();
	}

	public int getNumVariable() {
		return variableMap.size();
	}

	public String getFunctionID() {
		return functionID;
	}
	
	public Variable getReturnVar(){
		return returnVar;
	}
	
	public ArrayList<Variable> getParameters() {
		return parameters;
	}

	public HashMap<String,Variable> getVariableMap() {
		return variableMap;
	}

	public SimpleNode getBody(){
		return body;
	}

	@Override
	public boolean equals(Object object) {
		Function function = (Function) object;
		if (functionID == function.getFunctionID() && getNumVariable() == function.getNumVariable())
			return true;
		return false;
	}

	public void addVariable(Variable v) {
		variableMap.put(v.getVariableID(),v);
	}
	
	public boolean checkLocalVariable(String id){
		if(variableMap.containsKey(id))
			return true;
		else
			return false;
	}

	public void setVariableValue(String variableID, int value) {
		Variable v = variableMap.get(variableID);
		((Scalar)v).setValue(value);
	}

	public int getNumParameters(){
		return parameters.size();
	}

	public void addParameter(Variable v){
		parameters.add(v);
	}
	
	public Variable getParameter(String id){
		for (int i = 0; i < parameters.size(); i++) {
			if(parameters.get(i).getVariableID().equals(id)){
				return parameters.get(i);
			}
		}
		return null;
	}

	public String toString(){
		String s = functionID;
		s += "(";
		for (int i = 0; i < parameters.size(); i++) {
			if(parameters.get(i) instanceof Scalar)
				s += "_s";
			else if(parameters.get(i) instanceof Array)
				s += "_a";
		}
		s += ")";
		return s;
	}
	
	public boolean checkParams(String varID) {
		for (int i = 0; i < parameters.size(); i++)
			if (parameters.get(i).getVariableID().equals(varID))
				return true;
		return false;
	}
	
	public void printData(){
		System.out.println("  PARAMETERS:");
		for (int i = 0; i < parameters.size(); i++) {
			System.out.println("  " + parameters.get(i));
		}
		System.out.println("  LOCAL:");
		for(String key : variableMap.keySet()) {
			Variable v = variableMap.get(key);
			System.out.println("  " + v);
	 	}
	}
}
