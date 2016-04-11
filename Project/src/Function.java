/* This Class shall be used to store information about each function */

import java.util.*;

public class Function {

	private String functionID;
	private HashMap<String,Variable> variableMap;
	private ArrayList<Variable> parameters;
	private Variable returnVar;
	private SimpleNode body;

	public Function(String functionID) {
		this.functionID = functionID;
		parameters = new ArrayList<Variable>();
		variableMap = new HashMap<String,Variable>();
	}

	public Function(String id, ArrayList<Variable> p,SimpleNode n){
		functionID = id;
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

}
