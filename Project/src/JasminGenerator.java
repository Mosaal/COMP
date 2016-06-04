import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.PrinterState;

public class JasminGenerator {
	
	private static final String VOID = "V";
	private static final String SCALARTYPE = "I";
	private static final String ARRAYTYPE = "[I";
	private static final HashMap<String,String> scalarFields = new HashMap<String,String>();
	private static final HashMap<String,String> arrayFields = new HashMap<String,String>();
	private static PrintWriter writer;
	private static SimpleNode node;
	private static Module module;
	private static String moduleName;
	private static int labelWhileCount = 0;
	private static int labelIfCount = 0;
	private static CFGNode endIfNode;
	
	public static void generate(SimpleNode n, Module m){
		node = n;
		module = m;
		moduleName = m.getModuleID();
		try {
			writer = new PrintWriter(moduleName + ".j", "UTF-8");
			initiate();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private static void initiate(){
		printClassHeader();
		printNewLine();
		
		printFields();
		printNewLine();
		
		printLargeComment("CONSTRUCTORS");
		printStaticConstructor();
		printNewLine();
		
		printConstructor();
		printNewLine();
		printNewLine();
		printNewLine();
		printLargeComment("FUNCTIONS");
		
		printFunctions();
	}
	
	private static void printNewLine(){
		writer.println("");
	}
	
	private static void printComment(String comment){
		writer.println("\t; " + comment);
	}
	
	private static void printLargeComment(String comment){
		writer.println(";");
		writer.println("; " + comment);
		writer.println(";");
	}
	
	private static void printClassHeader(){
		writer.println(".source " + moduleName + ".j");
		writer.println(".class public " + moduleName);
		writer.println(".super java/lang/Object");
	}
	
	private static void printStaticConstructor(){
		writer.println(".method static <clinit>()V");
		writer.println("\t.limit stack 2");
		writer.println("\t.limit locals 0");
		printNewLine();
		
		//ARRAY TYPES
		for (Map.Entry<String, String> entry : arrayFields.entrySet() ) {
		    String id = entry.getKey();
		    String size = entry.getValue();
		    writer.println("\tbipush " + size);
		    writer.println("\tnewarray int");
		    writer.println("\tputstatic " + moduleName + "/" + id + " [I");
		    printNewLine();
		}
		
		writer.println("\treturn");
		writer.println(".end method");
	}
	
	private static void printConstructor(){
		writer.println(".method public <init>()V");
		writer.println("\t.limit stack 2");
		writer.println();
		writer.println("\taload 0");
		writer.println("\tinvokenonvirtual java/lang/Object/<init>()V");
		printNewLine();
		
		writer.println("\treturn");
		writer.println(".end method");
	}
	
	private static void printMethodHeader(Function f){
		writer.print(".method public " + f.getFunctionID() + "(");
		
		ArrayList<Variable> params = f.getParameters();
		for (int i = 0; i < params.size(); i++) {
			if(params.get(i).getType().equals("scalar")){
				writer.print("I");
			}else {
				writer.print("[I");
			}
		}
		
		writer.print(")");
		
		//livenessAnalysis(f);
		
		Variable ret = f.getReturnVar();
		if(ret != null){
			if(f.getReturnVar().getType().equals("scalar")){
				writer.println("I");
			}else{
				writer.println("[I");
			}
		}else{
			writer.println("V");
		}
		
		int numLocal = 1+f.getNumParameters()+f.getNumVariable();
		if(f.getReturnVar() != null){
			numLocal++;
		}
		writer.println("\t.limit locals " + numLocal);
		writer.println("\t.limit stack " + 2);
		writer.println();
	}
	
	private static void printBody(Function function, SimpleNode bodyNode){
		for (int i = 0; i < bodyNode.jjtGetNumChildren(); i++) {
			SimpleNode n = (SimpleNode) bodyNode.jjtGetChild(i);
			int id = n.getId();
			
			if (id == YalToJvmTreeConstants.JJTASSIGNEMENT) {
				printAssignment(function, n);
			} else if (id == YalToJvmTreeConstants.JJTWHILE) {
				labelWhileCount++;
				printWhileBlock(function, n);
			} else if (id == YalToJvmTreeConstants.JJTIF) {
				labelIfCount++;
				printIfBlock(function, n);
			} else if (id == YalToJvmTreeConstants.JJTCALL) {
				printCall(function, n);
			}
			
			printNewLine();
		}
	}
	
	private static void printAssignment(Function f, SimpleNode assignNode) {
		String lhsID = ((SimpleNode)assignNode.jjtGetChild(0)).ID;
		Variable lhsVariable = f.getVariableAllScopes(lhsID);
		String lhsType = lhsVariable.getType();
		
		SimpleNode rhsNode = ((SimpleNode)assignNode.jjtGetChild(1));
		String rhs1ID = ((SimpleNode)rhsNode.jjtGetChild(0)).ID;
		//System.out.println(rhs1ID);
		
		/**
		 * All types of assignments
		 * - scalar = scalar;
		 * - scalar = integer
		 * - scalar = array[index]
		 * - scalar = call
		 * - scalar = array.size
		 */
		if(true){
			
		}
	}
	
	private static void printWhileBlock(Function function, SimpleNode whileNode) {
		SimpleNode whileCondition = (SimpleNode) whileNode.jjtGetChild(0);
		SimpleNode whileBody = (SimpleNode) whileNode.jjtGetChild(1);
		
		writer.println("While" + labelWhileCount + ":"); // Start label
		
		String endlabel = "EndWhile" + labelWhileCount;
		
		printCondition(whileCondition, endlabel);
		
		printBody(function, whileBody);
		
		writer.println("\tgoto While" + labelWhileCount);
		writer.println(endlabel+":");
	}
	
	private static void printIfBlock(Function function, SimpleNode ifNode) {
		SimpleNode ifCondition = (SimpleNode) ifNode.jjtGetChild(0);
		SimpleNode ifBody = (SimpleNode) ifNode.jjtGetChild(1);

		String jumplabel, endlabel = "EndIf" + labelIfCount;
		
		jumplabel = (ifNode.jjtGetNumChildren() > 2) ? "ElseBody" + labelIfCount : endlabel;
		
		printCondition(ifCondition, jumplabel);
		printBody(function, ifBody);
		
		if ((ifNode.jjtGetNumChildren() > 2)){
			writer.println("\tgoto "+endlabel);
			SimpleNode elseBody = (SimpleNode) ifNode.jjtGetChild(2);
			writer.println(jumplabel+":");
			printBody(function, elseBody);
		}
		
		writer.println(endlabel+":");
	}
	
	private static String convertFunctionName(String actualName) {
		String temp = "";
		
		if (actualName.equals(temp))
			return temp;
		
		for (int i = 0; i < actualName.length(); i++) {
			if (actualName.charAt(i) == 's')
				temp += SCALARTYPE;
			else if (actualName.charAt(i) == 'a')
				temp += ARRAYTYPE;
		}
		
		return "(" + temp + ")";
	}
	
	private static void printCall(Function function, SimpleNode callNode) {
		if (callNode.dot(callNode.ID)) {
			writer.println("\tinvokestatic " + callNode.separateString(callNode.ID)[0] + "/" + callNode.separateString(callNode.ID)[1] + convertFunctionName(callNode.getRealFunctionName(callNode.jjtGetChildren(), function)) + VOID);
		} else {
			String fullName = callNode.ID + "(" + callNode.getRealFunctionName(callNode.jjtGetChildren(), function) + ")";
			
			if (module.getFunctionByID(fullName).checkReturnVariableType() == null) {
				writer.println("\tinvokestatic " + module.getModuleID() + "/" + callNode.ID + convertFunctionName(callNode.getRealFunctionName(callNode.jjtGetChildren(), function)) + VOID);
			} else if (module.getFunctionByID(fullName).checkReturnVariableType().equals("scalar")) {
				writer.println("\tinvokestatic " + module.getModuleID() + "/" + callNode.ID + convertFunctionName(callNode.getRealFunctionName(callNode.jjtGetChildren(), function)) + SCALARTYPE);
			} else {
				writer.println("\tinvokestatic " + module.getModuleID() + "/" + callNode.ID + convertFunctionName(callNode.getRealFunctionName(callNode.jjtGetChildren(), function)) + ARRAYTYPE);
			}
		}
	}
	
	private static void printCondition(SimpleNode condition, String jumpLabel){
		SimpleNode condLhs = (SimpleNode) condition.jjtGetChild(0);
		SimpleNode condRhs = (SimpleNode) condition.jjtGetChild(1);
		
		if (condLhs.getId() == YalToJvmTreeConstants.JJTSCALAR && Integer.parseInt(condLhs.ID) == 0){
			switch(condition.ID){
			case "<":
				writer.println("\tifle " + jumpLabel); // jump if 0 >= rhs
				break;
			case ">":
				writer.println("\tifge " + jumpLabel); // jump if 0 <= rhs
				break;
			case "<=":
				writer.println("\tifgt " + jumpLabel); // jump if 0 > rhs
				break;
			case ">=":
				writer.println("\tiflt " + jumpLabel); // jump if 0 < rhs
				break;
			case "==":
				writer.println("\tifne " + jumpLabel); // jump if 0 != rhs
				break;
			case "!=":
				writer.println("\tifeq " + jumpLabel); // jump if 0 == rhs
				break;
			default:
				break;
			}
		} else if (condRhs.getId() == YalToJvmTreeConstants.JJTSCALAR && Integer.parseInt(condRhs.ID) == 0){
			switch(condition.ID){
			case "<":
				writer.println("\tifge " + jumpLabel); // jump if lhs >= 0
				break;
			case ">":
				writer.println("\tifle " + jumpLabel); // jump if lhs <= 0
				break;
			case "<=":
				writer.println("\tifgt " + jumpLabel); // jump if lhs > 0
				break;
			case ">=":
				writer.println("\tiflt " + jumpLabel); // jump if lhs < 0
				break;
			case "==":
				writer.println("\tifne " + jumpLabel); // jump if lhs != 0
				break;
			case "!=":
				writer.println("\tifeq " + jumpLabel); // jump if lhs == 0
				break;
			default:
				break;
			}
		} else {
			switch(condition.ID){
			case "<":
				writer.println("\tif_icmpge " + jumpLabel); // jump if lhs >= rhs
				break;
			case ">":
				writer.println("\tif_icmple " + jumpLabel); // jump if lhs <= rhs
				break;
			case "<=":
				writer.println("\tif_icmpgt " + jumpLabel); // jump if lhs > rhs
				break;
			case ">=":
				writer.println("\tif_icmplt " + jumpLabel); // jump if lhs < rhs
				break;
			case "==":
				writer.println("\tif_icmpne " + jumpLabel); // jump if lhs != rhs
				break;
			case "!=":
				writer.println("\tif_icmpeq " + jumpLabel); // jump if lhs == rhs
				break;
			default:
				break;
			}
		}
	}
	
	private static void printMethodFooter(Function f){
		//TODO: Temporary
		Variable ret = f.getReturnVar();
		if(ret != null){
			int localNum = f.localVariables.indexOf(ret.variableID);
			if(ret.getType().equals("scalar")){
				if(localNum <= 3){
					writer.println("\tiload_" + localNum);
				}else{
					writer.println("\tiload " + localNum);
				}
				writer.println("\tireturn ");
			}else if(ret.getType().equals("array")){
				if(localNum <= 3){
					writer.println("\taload_" + localNum);
				}else{
					writer.println("\t");
				}
				writer.println("\tareturn ");
			}
		}else{
			writer.println("\treturn");
		}
		writer.println(".end method");
	}
	
	// .field <access-spec> <field-name> <descriptor> [ = <value> ] <- value doesn't work idkw
	private static void printField(String type, String name, String value){
		String statement = ".field static ";
		statement += name + " ";
		statement += type;
		
		if (value != null && !type.equals(ARRAYTYPE)){
			statement += " = " + value;
		}
		
		writer.println(statement);
	}
	
	private static void printFields(){
		int size = node.jjtGetNumChildren();
		
		for (int i = 0; i < size; i++) {
			SimpleNode n = (SimpleNode)node.jjtGetChild(i);
			if (n.getId() == YalToJvmTreeConstants.JJTGLOBAL){
				String name = null;
				String type = null;
				String value = null;
				SimpleNode lhs = (SimpleNode)n.jjtGetChild(0);
				name = lhs.ID;
				
				if (n.jjtGetNumChildren() == 2) {
					SimpleNode rhs = (SimpleNode)n.jjtGetChild(1);
					
					if (rhs.ID == null) {
						type = ARRAYTYPE;
						SimpleNode arraySize = ((SimpleNode)rhs.jjtGetChild(0));
						
						if (arraySize.ID == null) {
							value = ((SimpleNode)arraySize.jjtGetChild(0)).ID;
						} else {
							value = arraySize.ID;
						}
					} else {
						value = rhs.ID;
						type = SCALARTYPE;
					}
				} else {
					type = SCALARTYPE;
				}
				
				if (type.equals(SCALARTYPE)) {
					scalarFields.put(name, value);
				} else {
					arrayFields.put(name,value);
				}
				
				printField(type,name,value);
			} else {
				i = size;
			}
		}
	}
	
	private static void printFunctions(){
		HashMap<String, Function> map = module.getFunctionMap();
		for (Function f : map.values()) {
		    printMethodHeader(f);
		    //printBody(f, f.getBody());
		    printCFG(f, f.cfgStartNode);
		    printMethodFooter(f);
		    printNewLine();
		}
	}
	

	/**
	 * Analyses the CFG and prints the corresponding
	 * JVM instruction codes
	 * @param f The function of the body
	 * @param node The current node visiting
	 */
	private static void printCFG(Function f, CFGNode node){
		switch (node.type) {
		case "while":
			if(!node.visited){
				node.visited = true;
				f.labelCount++;
				int whileLabel = f.labelCount;
				System.out.println("label" + whileLabel + ":");
				f.labelCount++;
				int whileLabel2 = f.labelCount;
				System.out.print("\t" + node.number + "-WHILE");
				System.out.println(" label" + whileLabel2);
				
				printCFG(f, node.outs.get(0));
				
				System.out.println("\tgoto label" + whileLabel);
				System.out.println("label" + whileLabel2 + ":");
				
				if(!node.outs.get(1).type.equals("endif")){
					printCFG(f, node.outs.get(1));
				}
			}
			break;
		case "if":
			if(node.outs.size() == 2){
				//If has both inner nodes
				if(!node.outs.get(0).type.equals("endif") && !node.outs.get(1).type.equals("endif")){
					f.labelCount++;
					int iflabel = f.labelCount;
					System.out.println("\tIF label" + iflabel);
					f.labelCount++;
					int iflabel2 = f.labelCount;
					printCFG(f, node.outs.get(0));
					System.out.println("\tgoto label" + iflabel2);
					System.out.println("label" + iflabel + ":");
					printCFG(f, node.outs.get(1));
					System.out.println("label" + iflabel2 + ":");
					printCFG(f, endIfNode);
				}else{
					//If "if" side doesn't exist
					if(node.outs.get(0).type.equals("endif")){
						f.labelCount++;
						int iflabel = f.labelCount;
						System.out.println("\tIF label" + iflabel);
						printCFG(f, node.outs.get(1));
						System.out.println("label" + iflabel + ":");
						printCFG(f, node.outs.get(0));
					}else if(node.outs.get(1).type.equals("endif")){
						//TODO: CHANGE SIGN!
						//If "else" side doesn't exist
						f.labelCount++;
						int iflabel = f.labelCount;
						System.out.println("\tIF label" + iflabel);
						printCFG(f, node.outs.get(0));
						System.out.println("label" + iflabel + ":");
						printCFG(f, node.outs.get(1));
					}
				}
			}else{
				//If has no inner nodes
				printCFG(f, node.outs.get(0));
			}
			break;
		default:
			switch (node.type) {
			case "assignment":
				printAssignment(f, node);
				break;
			default:
				break;
			}
			System.out.println("\t" + node.number + "-" + node.type);
			node.visited = true;
			for (int i = 0; i < node.outs.size(); i++) {
				if(!node.outs.get(i).visited){
					if(!node.outs.get(i).type.equals("endif")){
						printCFG(f, node.outs.get(i));
					}else{
						endIfNode = node.outs.get(i);
					}
				}
			}
			break;
		}
	}

	//TODO: Optimize
	private static void printAssignment (Function f, CFGNode node) {
		printComment("ASSIGNMENT");
		
		int numLhs, numRhs1, numRhs2;
		numLhs = f.localVariables.indexOf(node.lhsId);
		numRhs1 = f.localVariables.indexOf(node.rhs1Id);
		
		node.printAssignmentNode();
		
		//Push rhs1 variable value to stack
		if(node.rhs1Access.equals("integer")){
			int rhsValue = Integer.parseInt(node.rhs1Id);
			if(rhsValue <= 127 && rhsValue >= -128){
				writer.println("\tbipush " + rhsValue);
			}else{
				writer.println("\tsipush " + rhsValue);
			}
		}else if(node.rhs1Access.equals("scalar")){
			if(node.lhsScope.equals("global")){
				int rhsValue;
				//TODO:
			}else{
				
			}
		}else if(node.rhs1Access.equals("array")){
			
		}else if(node.rhs1Access.equals("call")){
			
		}
		
		if(node.twoSides){
			//Push rhs2 variable value to stack
			if(node.rhs1Access.equals("integer")){
				int rhs2Value = Integer.parseInt(node.rhs2Id);
				if(rhs2Value <= 127 && rhs2Value >= -128){
					writer.println("\tbipush " + rhs2Value);
				}else{
					writer.println("\tsipush " + rhs2Value);
				}
			}else if(node.rhs1Access.equals("scalar")){
				if(node.lhsScope.equals("global")){
					
				}else{
					
				}
			}else if(node.rhs1Access.equals("array")){
				
			}else if(node.rhs1Access.equals("call")){
				
			}
			
			//Make operation
			printOperation(node.assignementOp);
			
		}
		
		//Assign values in stack to lhs variable
		if(node.lhsScope.equals("global")){
			if(node.lhsAccess.equals("scalar")){
				writer.println("\tputstatic " + moduleName + "/" + node.lhsId + " I");
			}else if(node.lhsAccess.equals("array")){
				//TODO:
			}
		}else{
			if(node.lhsAccess.equals("scalar")){
				if(numLhs <= 3){
					writer.println("\tistore_" + numLhs);
				}else{
					writer.println("\tistore " + numLhs);
				}
			}else if(node.lhsAccess.equals("array")){
				
			}
		}
		
		printNewLine();
	}

	private static void livenessAnalysis (Function f){
		
		setUsesAndDefs(f);
		do{
			for(int i = 0; i < f.cfgNodes.size(); i++){
				f.cfgNodesIns.set(i, f.cfgNodes.get(i).laIns);
				f.cfgNodesOuts.set(i, f.cfgNodes.get(i).laOuts);
			}
			
			for(int i = f.cfgNodes.size() - 1; i > -1; i--){
				nodeAnalysis(f.cfgNodes.get(i));
			}
			
		}while(compareIterations(f));
		

	}
	
	private static void setUsesAndDefs (Function f){
		for (int i = 0; i < f.cfgNodes.size(); i++){
			if (f.cfgNodes.get(i).type.equals("assignment") || f.cfgNodes.get(i).type.equals("while") || f.cfgNodes.get(i).type.equals("if")){
				
				
				// Assignment => DEFS
				if (f.cfgNodes.get(i).type.equals("assignment") && f.checkLocalVariable(f.cfgNodes.get(i).lhsId)
						&& !f.cfgNodes.get(i).defs.contains(f.getVariable(f.cfgNodes.get(i).lhsId))){
					f.cfgNodes.get(i).defs.add(f.getVariable(f.cfgNodes.get(i).lhsId));
				} // Condition => USES
				else if (f.checkLocalVariable(f.cfgNodes.get(i).lhsId) && !f.cfgNodes.get(i).uses.contains(f.getVariable(f.cfgNodes.get(i).lhsId))){
					f.cfgNodes.get(i).uses.add(f.getVariable(f.cfgNodes.get(i).lhsId));
				}
				
				/* USES */
				
				// Local variable as LHS array index
				if (f.cfgNodes.get(i).lhsArrayIndexId != null && f.checkLocalVariable(f.cfgNodes.get(i).lhsArrayIndexId)
						&& !f.cfgNodes.get(i).uses.contains(f.getVariable(f.cfgNodes.get(i).lhsArrayIndexId))){
					f.cfgNodes.get(i).uses.add(f.getVariable(f.cfgNodes.get(i).lhsArrayIndexId));
				}
				
				//RHS 1
				if(f.cfgNodes.get(i).rhs1Id != null && !f.cfgNodes.get(i).rhs1Access.equals("integer")){
					if(f.checkLocalVariable(f.cfgNodes.get(i).rhs1Id)
					&& !f.cfgNodes.get(i).uses.contains(f.getVariable(f.cfgNodes.get(i).rhs1Id))){
						f.cfgNodes.get(i).uses.add(f.getVariable(f.cfgNodes.get(i).rhs1Id));
					}
					
					// Local variable as RHS1 array index
					if (f.cfgNodes.get(i).rhs1ArrayIndexId != null && f.checkLocalVariable(f.cfgNodes.get(i).rhs1ArrayIndexId)
							&& !f.cfgNodes.get(i).uses.contains(f.getVariable(f.cfgNodes.get(i).rhs1ArrayIndexId))){
						f.cfgNodes.get(i).uses.add(f.getVariable(f.cfgNodes.get(i).rhs1ArrayIndexId));
					}
				}
				
				//RHS 2
				if(f.cfgNodes.get(i).twoSides && f.cfgNodes.get(i).rhs2Id != null && !f.cfgNodes.get(i).rhs2Access.equals("integer")){
					if(f.checkLocalVariable(f.cfgNodes.get(i).rhs2Id)
					&& !f.cfgNodes.get(i).uses.contains(f.getVariable(f.cfgNodes.get(i).rhs2Id))){
						f.cfgNodes.get(i).uses.add(f.getVariable(f.cfgNodes.get(i).rhs2Id));
					}
					
					// Local variable as RHS2 array index
					if (f.cfgNodes.get(i).rhs2ArrayIndexId != null && f.checkLocalVariable(f.cfgNodes.get(i).rhs2ArrayIndexId)
							&& !f.cfgNodes.get(i).uses.contains(f.getVariable(f.cfgNodes.get(i).rhs2ArrayIndexId))){
						f.cfgNodes.get(i).uses.add(f.getVariable(f.cfgNodes.get(i).rhs2ArrayIndexId));
					}
				}
			}
		}
	}
	
	private static boolean compareIterations (Function f){
		for (int i = 0; i < f.cfgNodes.size(); i++){
			if (f.cfgNodesIns.get(i).size() != f.cfgNodes.get(i).laIns.size()){
				return true;
			}
			if (f.cfgNodesOuts.get(i).size() != f.cfgNodes.get(i).laOuts.size()){
				return true;
			}
		}
		return false;
	}
	
	private static void nodeAnalysis(CFGNode node){
		
		/* node 'OUT' SET */
		// For each successor of node
		for(int i = 0; i < node.outs.size(); i++){
			
			// For each variable in the 'IN' set of the successor
			for(int j = 0; j < node.outs.get(i).laIns.size(); j++){
				
				if(!node.laOuts.contains(node.outs.get(i).laIns.get(j)))
					node.laOuts.add(node.outs.get(i).laIns.get(j));
			}
		}
		
		/* node 'IN' SET */
		for(int i = 0; i < node.uses.size(); i++){
			if(!node.laIns.contains(node.uses.get(i)))
				node.laIns.add(node.uses.get(i));
		}
		for(int i = 0; i < node.laOuts.size(); i++){
			if(!node.laIns.contains(node.laOuts.get(i)) && !node.defs.contains(node.laOuts.get(i)))
				node.laIns.add(node.laOuts.get(i));
		}
	}
	
	private static void printOperation(String op){
		switch (op) {
		case "+":
			writer.println("\tiadd");
			break;
		case "-":
			writer.println("\tisub");
			break;
		case "*":
			writer.println("\timul");
			break;
		case "/":
			writer.println("\tidiv");
			break;
		case ">>":
			writer.println("\tishr");
			break;
		case "<<":
			writer.println("\tishl");
		break;
		case ">>>":
			writer.println("\tiushr");
			break;
		case "&":
			writer.println("\tiand");
			break;
		case "|":
			writer.println("\tior");
			break;
		case "^":
			writer.println("\tixor");
			break;
		default:
			break;
		}
	}
	
}
