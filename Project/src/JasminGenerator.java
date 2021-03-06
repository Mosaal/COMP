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
		writer.print(".method public static " + f.getFunctionID() + "(");
		
		ArrayList<Variable> params = f.getParameters();
		for (int i = 0; i < params.size(); i++) {
			if(params.get(i).getType().equals("scalar")){
				writer.print("I");
			}else {
				writer.print("[I");
			}
		}
		
		writer.print(")");
		
		livenessAnalysis(f);
		
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
		
		int numLocal = f.getNumParameters() + f.getNumVariable();
		if(f.getReturnVar() != null){
			numLocal++;
		}
		writer.println("\t.limit locals " + numLocal);
		writer.println("\t.limit stack " + 4);
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
				printComment("WHILE");
				node.visited = true;
				f.labelCount++;
				int whileLabel = f.labelCount;
				writer.println("label" + whileLabel + ":");
				f.labelCount++;
				int whileLabel2 = f.labelCount;
				writer.print("\t" + printCondition(f, node));
				writer.println(" label" + whileLabel2);
				
				printCFG(f, node.outs.get(0));
				
				writer.println("\tgoto label" + whileLabel);
				writer.println("label" + whileLabel2 + ":");
				
				if(!node.outs.get(1).type.equals("endif")){
					printCFG(f, node.outs.get(1));
				}
			}
			break;
		case "if":
			printComment("CONDITION");
			node.condInvert = false;
			if(node.outs.size() == 2){
				//If has both inner nodes
				if(!node.outs.get(0).type.equals("endif") && !node.outs.get(1).type.equals("endif")){
					f.labelCount++;
					int iflabel = f.labelCount;
					writer.println("\t" + printCondition(f, node) + " label" + iflabel); //Print condition
					f.labelCount++;
					int iflabel2 = f.labelCount;
					printCFG(f, node.outs.get(0));
					writer.println("\tgoto label" + iflabel2);
					writer.println("label" + iflabel + ":");
					printCFG(f, node.outs.get(1));
					writer.println("label" + iflabel2 + ":");
					printCFG(f, endIfNode);
				}else{
					//If "if" side doesn't exist
					if(node.outs.get(0).type.equals("endif")){
						f.labelCount++;
						int iflabel = f.labelCount;
						writer.println("\t" + printCondition(f, node) + " label" + iflabel); //Print condition
						printCFG(f, node.outs.get(1));
						writer.println("label" + iflabel + ":");
						printCFG(f, node.outs.get(0));
					}else if(node.outs.get(1).type.equals("endif")){
						//CHANGE SIGN!
						node.condInvert = true;
						//If "else" side doesn't exist
						f.labelCount++;
						int iflabel = f.labelCount;
						writer.println("\t" + printCondition(f, node) + " label" + iflabel); //Print condition
						printCFG(f, node.outs.get(0));
						writer.println("label" + iflabel + ":");
						printCFG(f, node.outs.get(1));
					}
				}
			}else{
				//If has no inner nodes
				f.labelCount++;
				int iflabel = f.labelCount;
				writer.println("\t" + printCondition(f, node) + " label " + iflabel);
				writer.println("label" + iflabel + ":");
				printCFG(f, node.outs.get(0));
			}
			break;
		default:
			switch (node.type) {
			case "assignment":
				printAssignment(f, node);
				break;
			case "call":
				printCall(f,node);
				break;
			default:
				break;
			}
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
	
	private static String printCondition(Function f, CFGNode node){
		int numLhs, numRhs1, numRhs2;
		numLhs = f.localVariables.indexOf(node.lhsId);
		numRhs1 = f.localVariables.indexOf(node.rhs1Id);
		numRhs2 = f.localVariables.indexOf(node.rhs2Id);
		//Push rhs1 variable value to stack
		if(node.rhs1Access.equals("integer")){ //INTEGER
			pushInt(node.rhs1Id);
		}else if(node.rhs1Access.equals("scalar")){ //SCALAR
			loadVarScalar(node.rhs1Id, numRhs1, f.getVariableScope(node.rhs1Id));
		}else if(node.rhs1Access.equals("array")){ //ARRAY
			loadVarArray(node.rhs1Id, numRhs1, f.getVariableScope(node.rhs1Id));
			String rhs1ArrayIndexId = node.rhs1ArrayIndexId;
			int rhs1ArrayIndexNum = f.localVariables.indexOf(rhs1ArrayIndexId);
			String rhs1ArrayIndexScope = f.getVariableScope(rhs1ArrayIndexId);
			if(node.rhs1ArrayAccess.equals("integer")){
				pushInt(rhs1ArrayIndexId);
			}else if(node.rhs1ArrayAccess.equals("scalar")){
				loadVarScalar(rhs1ArrayIndexId, rhs1ArrayIndexNum, rhs1ArrayIndexScope);
			}
			writer.println("\tiaload");
		}else if(node.rhs1Access.equals("call")){ //CALL
			String otherModuleName = getFunctionModule(node.rhs1Id);
			String functionName = getFunctionName(node.rhs1Id);
			String fullName;
			if(node.rhs1OtherModule){
				fullName = getFunctionFullNameOtherModule(f,functionName, node.rhs1Args);
			}else{
				fullName = getFunctionFullName(node.rhs1Id,node.rhs1Call);
			}
			
			//Push arguments to stack
			for (int i = 0; i < node.rhs1Args.size(); i++) {
				try {
					Integer.parseInt(node.rhs1Args.get(i));
					pushInt(node.rhs1Args.get(i));
				} catch (NumberFormatException e) {
					Variable var = YalToJvm.getModule().getVariable(f, node.rhs1Args.get(i));
					String scope = f.getVariableScope(node.rhs1Args.get(i));
					int varNum = f.localVariables.indexOf(node.rhs1Args.get(i));
					if(var.getType().equals("scalar")){
						loadVarScalar(node.rhs1Args.get(i), varNum, scope);
					}else if(var.getType().equals("array")){
						loadVarArray(node.rhs1Args.get(i), varNum, scope);
					}
				}
			}
			//Invoke call
			if(otherModuleName != null){
				writer.println("\tinvokestatic " + otherModuleName + "/" + fullName);
			}else{
				writer.println("\tinvokestatic " + moduleName + "/" + fullName);
			}
		}else if(node.rhs1Access.equals("size")){ //SIZE
			loadVarArray(node.rhs1Id, numRhs1, f.getVariableScope(node.rhs1Id));
			writer.println("\tarraylength");
		}
		
		//Push rhs2 variable value to stack
		if(node.twoSides){
			if(node.rhs2Access.equals("integer")){ //INTEGER
				pushInt(node.rhs2Id);
			}else if(node.rhs2Access.equals("scalar")){ //SCALAR
				loadVarScalar(node.rhs2Id, numRhs2, f.getVariableScope(node.rhs2Id));
			}else if(node.rhs2Access.equals("array")){ //ARRAY
				loadVarArray(node.rhs2Id, numRhs2, f.getVariableScope(node.rhs2Id));
				String rhs2ArrayIndexId = node.rhs2ArrayIndexId;
				int rhs2ArrayIndexNum = f.localVariables.indexOf(rhs2ArrayIndexId);
				String rhs2ArrayIndexScope = f.getVariableScope(rhs2ArrayIndexId);
				if(node.rhs2ArrayAccess.equals("integer")){
					pushInt(rhs2ArrayIndexId);
				}else if(node.rhs2ArrayAccess.equals("scalar")){
					loadVarScalar(rhs2ArrayIndexId, rhs2ArrayIndexNum, rhs2ArrayIndexScope);
				}
				writer.println("\tiaload");
			}else if(node.rhs2Access.equals("call")){ //CALL
				String otherModuleName = getFunctionModule(node.rhs2Id);
				String functionName = getFunctionName(node.rhs2Id);
				String fullName;
				if(node.rhs2OtherModule){
					fullName = getFunctionFullNameOtherModule(f,functionName, node.rhs2Args);
				}else{
					fullName = getFunctionFullName(node.rhs2Id,node.rhs2Call);
				}
				
				//Push arguments to stack
				for (int i = 0; i < node.rhs2Args.size(); i++) {
					try {
						Integer.parseInt(node.rhs2Args.get(i));
						pushInt(node.rhs2Args.get(i));
					} catch (NumberFormatException e) {
						Variable var = YalToJvm.getModule().getVariable(f, node.rhs2Args.get(i));
						String scope = f.getVariableScope(node.rhs2Args.get(i));
						int varNum = f.localVariables.indexOf(node.rhs2Args.get(i));
						if(var.getType().equals("scalar")){
							loadVarScalar(node.rhs2Args.get(i), varNum, scope);
						}else if(var.getType().equals("array")){
							loadVarArray(node.rhs2Args.get(i), varNum, scope);
						}
					}
				}
				//Invoke call
				if(otherModuleName != null){
					writer.println("\tinvokestatic " + otherModuleName + "/" + fullName);
				}else{
					writer.println("\tinvokestatic " + moduleName + "/" + fullName);
				}
			}else if(node.rhs2Access.equals("size")){ //SIZE
				loadVarArray(node.rhs2Id, numRhs2, f.getVariableScope(node.rhs2Id));
				writer.println("\tarraylength");
			}
			
			//Make operation
			printOperation(node.condOp);
			
			}
		
			//Get lhs value
			if(node.lhsAccess.equals("scalar")){
				loadVarScalar(node.lhsId, numLhs, f.getVariableScope(node.lhsId));
			}else if(node.lhsAccess.equals("array")){
				loadVarArray(node.lhsId, numLhs, f.getVariableScope(node.lhsId));
				int lhsArrayIndexNum = f.localVariables.indexOf(node.lhsArrayIndexId);
				String lhsArrayIndexScope = f.getVariableScope(node.lhsArrayIndexId);
				if(node.lhsArrayAccess.equals("integer")){
					pushInt(node.lhsArrayIndexId);
				}else if(node.lhsArrayAccess.equals("scalar")){
					loadVarScalar(node.lhsArrayIndexId, lhsArrayIndexNum, lhsArrayIndexScope);
				}
				writer.println("\tiaload");
		}
		
		//Do a condition
		if(node.type.equals("while")){
			return printConditionWhile(node.condSign);
		}else{
			return printConditionJump(node.condSign, node.condInvert);
		}
	}
	
	private static String printConditionWhile(String op){
		String cond = "";
		switch (op) {
		case "<":
			cond = "if_icmple";
			break;
		case "<=":
			cond = "if_icmplt";
			break;
		case ">":
			cond = "if_icmpge";
			break;
		case ">=":
			cond = "if_icmpgt";
			break;
		case "==":
			cond = "if_icmpeq";
			break;
		case "!=":
			cond = "if_icmpne";
			break;
		default:
			break;
		}
		return cond;
	}
	
	private static String printConditionJump(String op, boolean invert){
		String cond = "";
		switch (op) {
		case "<":
			if(invert){
				cond = "if_icmpgt";
			}else{
				cond = "if_icmple";
			}
			break;
		case "<=":
			if(invert){
				cond = "if_icmpge";
			}else{
				cond = "if_icmplt";
			}
			break;
		case ">":
			if(invert){
				cond = "if_icmplt";
			}else{
				cond = "if_icmpge";
			}
			break;
		case ">=":
			if(invert){
				cond = "if_icmple";
			}else{
				cond = "if_icmpgt";
			}
			break;
		case "==":
			if(invert){
				cond = "if_icmpeq"; //IF EQUAL
			}else{
				cond = "if_icmpne"; //IF NOT EQUAL
			}
			break;
		case "!=":
			if(invert){
				cond = "if_icmpne"; //IF NOT EQUAL
			}else{
				cond = "if_icmpeq"; //IF EQUAL
			}
			break;
		default:
			break;
		}
		return cond;
	}
	
	
	private static void printCall(Function f, CFGNode node){
		//Push variables to stack
		/* If we have to support io.class
		String module = getFunctionModule(node.callName);
		if(module.equals("io")){
			//printIO(f, node);
			for (int i = 0; i < node.callArgs.length; i++) {
				System.out.println(node.callArgs[i]);
			}
			return;
		}*/
		
		for (int i = 0; i < node.callArgs.length; i++) {
			try{
				Integer.parseInt(node.callArgs[i]);
				pushInt(node.callArgs[i]);
			}catch(NumberFormatException e){
				Variable var = YalToJvm.getModule().getVariable(f, node.callArgs[i]);
				String scope = f.getVariableScope(node.callArgs[i]);
				int varNum = f.localVariables.indexOf(var.getVariableID());
				if(var.getType().equals("scalar")){
					loadVarScalar(node.callArgs[i], varNum, scope);
				}else if(var.getType().equals("array")){
					loadVarArray(node.callArgs[i], varNum, scope);
				}
			}
		}
		//Invoke function
		Function callFunction = YalToJvm.getModule().getFunctionByID(node.callFullName);
		if(node.dot){
			writer.print("\tinvokestatic /" + node.callName);
		}else{
			writer.print("\tinvokestatic " + moduleName + "/" + node.callName);
		}
		writer.print("(");
		for (int i = 0; i < callFunction.getNumParameters(); i++) {
			Variable var = callFunction.getParameters().get(i);
			if(var.getType().equals("scalar")){
				writer.print("I");
			}else if(var.getType().equals("array")){
				writer.print("[I");
			}
		}
		writer.print(")");
		Variable returnVar = callFunction.getReturnVar();
		if(returnVar == null){
			writer.println("V");
		}else{
			if(returnVar.getType().equals("scalar")){
				writer.print("I");
			}else if(returnVar.getType().equals("array")){
				writer.print("[I");
			}
		}
	}

	
	private static void printAssignment (Function f, CFGNode node) {
		printComment("ASSIGNMENT");
		
		int numLhs, numRhs1, numRhs2;
		numLhs = f.localVariables.indexOf(node.lhsId);
		numRhs1 = f.localVariables.indexOf(node.rhs1Id);
		numRhs2 = f.localVariables.indexOf(node.rhs2Id);
		
		//node.printAssignmentNode();
		
		//Push rhs1 variable value to stack
		if(node.rhs1Access.equals("integer")){ //INTEGER
			pushInt(node.rhs1Id);
			
		}else if(node.rhs1Access.equals("scalar")){ //SCALAR
			loadVarScalar(node.rhs1Id, numRhs1, node.rhs1Scope);
			
		}else if(node.rhs1Access.equals("array")){ //ARRAY
			loadVarArray(node.rhs1Id, numRhs1, node.rhs1Scope);
			String rhs1ArrayIndexId = node.rhs1ArrayIndexId;
			int rhs1ArrayIndexNum = f.localVariables.indexOf(rhs1ArrayIndexId);
			String rhs1ArrayIndexScope = f.getVariableScope(rhs1ArrayIndexId);
			if(node.rhs1ArrayAccess.equals("integer")){
				pushInt(rhs1ArrayIndexId);
			}else if(node.rhs1ArrayAccess.equals("scalar")){
				loadVarScalar(rhs1ArrayIndexId, rhs1ArrayIndexNum, rhs1ArrayIndexScope);
			}
			writer.println("\tiaload");
			
		}else if(node.rhs1Access.equals("call")){ //CALL
			String otherModuleName = getFunctionModule(node.rhs1Id);
			String functionName = getFunctionName(node.rhs1Id);
			String fullName;
			if(node.rhs1OtherModule){
				fullName = getFunctionFullNameOtherModule(f,functionName, node.rhs1Args);
			}else{
				fullName = getFunctionFullName(node.rhs1Id,node.rhs1Call);
			}
			
			//Push arguments to stack
			for (int i = 0; i < node.rhs1Args.size(); i++) {
				try {
					Integer.parseInt(node.rhs1Args.get(i));
					pushInt(node.rhs1Args.get(i));
				} catch (NumberFormatException e) {
					Variable var = YalToJvm.getModule().getVariable(f, node.rhs1Args.get(i));
					String scope = f.getVariableScope(node.rhs1Args.get(i));
					int varNum = f.localVariables.indexOf(node.rhs1Args.get(i));
					if(var.getType().equals("scalar")){
						loadVarScalar(node.rhs1Args.get(i), varNum, scope);
					}else if(var.getType().equals("array")){
						loadVarArray(node.rhs1Args.get(i), varNum, scope);
					}
				}
			}
			//Invoke call
			if(otherModuleName != null){
				writer.println("\tinvokestatic " + otherModuleName + "/" + fullName);
			}else{
				writer.println("\tinvokestatic " + moduleName + "/" + fullName);
			}
			
		}else if(node.rhs1Access.equals("size")){ //SIZE
			loadVarArray(node.rhs1Id, numRhs1, node.rhs1Scope);
			writer.println("\tarraylength");
		}else if(node.rhs1Access.equals("arraysize")){ //ARRAYSIZE(Only in rhs1!)
			pushInt(node.rhs1Id);
		}
		
		//Push rhs2 variable value to stack
		if(node.twoSides){
			if(node.rhs2Access.equals("integer")){ //INTEGER
				pushInt(node.rhs2Id);
				
			}else if(node.rhs2Access.equals("scalar")){ //SCALAR
				loadVarScalar(node.rhs2Id, numRhs2, node.rhs2Scope);
				
			}else if(node.rhs2Access.equals("array")){ //ARRAY
				loadVarArray(node.rhs2Id, numRhs2, node.rhs2Scope);
				String rhs2ArrayIndexId = node.rhs2ArrayIndexId;
				int rhs2ArrayIndexNum = f.localVariables.indexOf(rhs2ArrayIndexId);
				String rhs2ArrayIndexScope = f.getVariableScope(rhs2ArrayIndexId);
				if(node.rhs2ArrayAccess.equals("integer")){
					pushInt(rhs2ArrayIndexId);
				}else if(node.rhs2ArrayAccess.equals("scalar")){
					loadVarScalar(rhs2ArrayIndexId, rhs2ArrayIndexNum, rhs2ArrayIndexScope);
				}
				writer.println("\tiaload");
				
			}else if(node.rhs2Access.equals("call")){ //CALL
				String otherModuleName = getFunctionModule(node.rhs2Id);
				String functionName = getFunctionName(node.rhs2Id);
				String fullName;
				if(node.rhs2OtherModule){
					fullName = getFunctionFullNameOtherModule(f,functionName, node.rhs2Args);
				}else{
					fullName = getFunctionFullName(node.rhs2Id,node.rhs2Call);
				}
				
				//Push arguments to stack
				for (int i = 0; i < node.rhs2Args.size(); i++) {
					try {
						Integer.parseInt(node.rhs2Args.get(i));
						pushInt(node.rhs2Args.get(i));
					} catch (NumberFormatException e) {
						Variable var = YalToJvm.getModule().getVariable(f, node.rhs2Args.get(i));
						String scope = f.getVariableScope(node.rhs2Args.get(i));
						int varNum = f.localVariables.indexOf(node.rhs2Args.get(i));
						if(var.getType().equals("scalar")){
							loadVarScalar(node.rhs2Args.get(i), varNum, scope);
						}else if(var.getType().equals("array")){
							loadVarArray(node.rhs2Args.get(i), varNum, scope);
						}
					}
				}
				//Invoke call
				if(otherModuleName != null){
					writer.println("\tinvokestatic " + otherModuleName + "/" + fullName);
				}else{
					writer.println("\tinvokestatic " + moduleName + "/" + fullName);
				}
				
			}else if(node.rhs2Access.equals("size")){ //SIZE
				loadVarArray(node.rhs2Id, numRhs2, node.rhs2Scope);
				writer.println("\tarraylength");
			}
			
			//Make operation
			printOperation(node.assignementOp);
			
		}
		
		//Assign values in stack to lhs variable
		if(node.lhsAccess.equals("scalar")){
			if(node.rhs1Access.equals("arraysize")){ //new local array
				writer.println("\tnewarray int");
				storeVarArray(node.lhsId, numLhs, node.lhsScope);
			}else{
				storeVarScalar(node.lhsId, numLhs, node.lhsScope);
			}
		}else if(node.lhsAccess.equals("array")){
			loadVarArray(node.lhsId, numLhs, node.lhsScope);
			writer.println("\tswap");
			int lhsArrayIndexNum = f.localVariables.indexOf(node.lhsArrayIndexId);
			String lhsArrayIndexScope = f.getVariableScope(node.lhsArrayIndexId);
			if(node.lhsArrayAccess.equals("integer")){
				pushInt(node.lhsArrayIndexId);
			}else if(node.lhsArrayAccess.equals("scalar")){
				loadVarScalar(node.lhsArrayIndexId, lhsArrayIndexNum, lhsArrayIndexScope);
			}
			writer.println("\tswap");
			writer.println("\tiastore");
		}
		
		printNewLine();
	}

	
	private static void livenessAnalysis (Function f){
		
		// For each node of CFG
		setUsesAndDefs(f);
		
		// Create auxiliary structures for comparison
		f.cfgNodesIns = new ArrayList<ArrayList<String>>();
		f.cfgNodesOuts = new ArrayList<ArrayList<String>>();
		
		// Empty auxiliary structures
		for(int i = 0; i < f.cfgNodes.size(); i++){
			f.cfgNodesIns.add(new ArrayList<String>());
			f.cfgNodesOuts.add(new ArrayList<String>());
		}
		
		do{
			for(int i = 0; i < f.cfgNodes.size(); i++){
				f.cfgNodesIns.set(i, f.cfgNodes.get(i).laIns);
				f.cfgNodesOuts.set(i, f.cfgNodes.get(i).laOuts);
			}
			
			for(int i = f.cfgNodes.size() - 1; i > -1; i--){
				nodeAnalysis(f.cfgNodes.get(i));
			}
			
		}while(compareIterations(f));
		
		liveRange(f);
	}
	
	
	private static void setUsesAndDefs (Function f){
		
		// List with Return and Locals Vars (in this order)
		List<String> varList = f.localVariables.subList(f.getNumParameters(), f.localVariables.size());
		
		for (CFGNode node : f.cfgNodes){
			if (node.type.equals("assignment") || node.type.equals("while") || node.type.equals("if")){
				
				// LHS is Local or Return Var
				if (varList.contains(node.lhsId)){
					
					// Assignment => DEFS
					if (node.type.equals("assignment")){
						setDef(f, node);

					// Contidion => USES
					} else
						setUse(f, node, node.lhsId);
				}
				
				/* USES */
				
				// Variable in LHS array index
				if (node.lhsArrayIndexId != null && varList.contains(node.lhsArrayIndexId))
					setUse(f, node, node.lhsArrayIndexId);
				
				//RHS 1
				if(!node.rhs1Access.equals("integer")){
					if(varList.contains(node.rhs1Id))
						setUse(f, node, node.rhs1Id);
					
					// Local variable as RHS1 array index
					if (node.rhs1ArrayIndexId != null && varList.contains(node.rhs1ArrayIndexId))
						setUse(f, node, node.rhs1ArrayIndexId);
				}
				
				//RHS 2
				if(node.twoSides && !node.rhs2Access.equals("integer") && node.rhs2Id != null){
					
					if(varList.contains(node.rhs2Id))
						setUse(f, node, node.rhs2Id);
					
					// Local variable as RHS2 array index
					if (node.rhs2ArrayIndexId != null && varList.contains(node.rhs2ArrayIndexId))
						setUse(f, node, node.rhs2ArrayIndexId);
				}
				
				for(int j = 0; j < node.rhs1Args.size(); j++){
					try{
						Integer.parseInt(node.rhs1Args.get(j));
					} catch(NumberFormatException e){
						if (varList.contains(node.rhs1Args.get(j)))
							setUse(f, node, node.rhs1Args.get(j));
					}				
				}
				for(int j = 0; j < node.rhs2Args.size(); j++){
					try{
						Integer.parseInt(node.rhs2Args.get(j));
					} catch(NumberFormatException e){
						if (varList.contains(node.rhs2Args.get(j)))
							setUse(f, node, node.rhs2Args.get(j));
					}
				}
				
			} else if(node.type.equals("call")){
				for(int j = 0; j < node.callArgs.length; j++){
					try{
						Integer.parseInt(node.callArgs[j]);
					} catch(NumberFormatException e){
						if (varList.contains(node.callArgs[j]))
							setUse(f, node, node.callArgs[j]);
					}
				}
			} else if(node.type.equals("end") && f.getReturnVar() != null){
				node.uses.add(f.getReturnVar().getVariableID());
			}
		}
	}
	
	private static void setDef (Function f, CFGNode n){
		if (f.checkReturnVariable(n.lhsId)){
			if (!n.defs.contains(f.getReturnVar().getVariableID()))
				n.defs.add(f.getReturnVar().getVariableID());
		} else if (!n.defs.contains(n.lhsId))
			n.defs.add(n.lhsId);
	}
	
	private static void setUse (Function f, CFGNode n, String s){
		if(f.checkReturnVariable(s)){
			if(!n.uses.contains(f.getReturnVar().getVariableID())){
				n.uses.add(f.getReturnVar().getVariableID());
			}
		} else if (!n.uses.contains(s)){
			n.uses.add(s);
		}
	}

	private static boolean compareIterations (Function f){
		for (int i = 0; i < f.cfgNodes.size(); i++){
			if (f.cfgNodes.get(i).type.equals("assignment") || f.cfgNodes.get(i).type.equals("while") || f.cfgNodes.get(i).type.equals("if")){
				if (f.cfgNodesIns.get(i).size() != f.cfgNodes.get(i).laIns.size())
					return true;
				if (f.cfgNodesOuts.get(i).size() != f.cfgNodes.get(i).laOuts.size())
					return true;
			}
		}
		return false;
	}
	
	private static void nodeAnalysis (CFGNode node){
		
		/* node 'OUT' SET */
		// For each successor of node
		for(CFGNode outNode : node.outs){
			
			if(outNode.type.equals("endif")){
				
				// Each sucessor of 'endif' is considered successor of the previous node
				for(int j = 0; j < outNode.outs.size(); j++){
					
					// For each variable in the 'IN' set of the successor
					for(int k = 0; k < outNode.outs.get(j).laIns.size(); k++){
						
						if(!node.laOuts.contains(outNode.outs.get(j).laIns.get(k)))
							node.laOuts.add(outNode.outs.get(j).laIns.get(k));
					}
				}
			}else{
				
				// For each variable in the 'IN' set of the successor
				for(int j = 0; j < outNode.laIns.size(); j++){
				
					if(!node.laOuts.contains(outNode.laIns.get(j)))
					node.laOuts.add(outNode.laIns.get(j));
				}
			}
		}
		
		/* node 'IN' SET */
		for(String useVar : node.uses){
			if(!node.laIns.contains(useVar))
				node.laIns.add(useVar);
		}
		for(String varOutLA : node.laOuts){
			if(!node.laIns.contains(varOutLA) && !node.defs.contains(varOutLA))
				node.laIns.add(varOutLA);
		}
	}
	
	
	private static void liveRange(Function f){
		
		Boolean hasReturn = (f.getReturnVar() != null);
		Boolean checkedReturn = false;
		
		// List with Locals Vars only
		List<String> varList = f.localVariables.subList(hasReturn ? f.getNumParameters() + 1 : f.getNumParameters(), f.localVariables.size());
		
		for(String var : varList){
			startSearch:
			for(int i = 0; i < f.cfgNodesOuts.size(); i++){
				for(String varName : f.cfgNodesOuts.get(i)){
					if(var.equals(varName)){
						f.varStart.put(var, i);
						break startSearch;
					} else if(hasReturn && !checkedReturn && f.getReturnVar().getVariableID().equals(varName)){
						f.varStart.put(f.getReturnVar().getVariableID(), i);
						checkedReturn = true;
					}
				}
			}
			endSearch:
			for(int i = f.cfgNodesIns.size(); i > 0; i--){
				for(String varName : f.cfgNodesIns.get(i-1)){
					if(var.equals(varName)){
						f.varEnd.put(var, i);
						break endSearch;
					}
				}
			}
		}
		if(hasReturn)
			f.varEnd.put(f.getReturnVar().getVariableID(), f.cfgNodeCount);
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
	
	
	private static void pushInt(String n){
		int value = Integer.parseInt(n);
		if(value == -1){
			writer.println("\ticonst_m1");
		}else if(value <= 5 && value >= 0){
			writer.println("\ticonst_" + value);
		}else if(value <= 127 && value >= -128){
			writer.println("\tbipush " + value);
		}else{
			writer.println("\tsipush " + value);
		}
	}
	
	
	private static void loadVarScalar(String id, int varNum, String scope){
		if(scope.equals("global")){
			writer.println("\tgetstatic " + moduleName + "/" + id + " I");
		}else{
			if(varNum <= 3){
				writer.println("\tiload_" + varNum);
			}else{
				writer.println("\tiload " + varNum);
			}
		}
	}
	
	
	private static void loadVarArray(String id, int varNum, String scope){
		if(scope.equals("global")){
			writer.println("\tgetstatic " + moduleName + "/" + id + " [I");
		}else{
			if(varNum <= 3){
				writer.println("\taload_" + varNum);
			}else{
				writer.println("\taload " + varNum);
			}
		}
	}
	
	
	private static void storeVarScalar(String id, int varNum, String scope){
		if(scope.equals("global")){
			writer.println("\tputstatic " + moduleName + "/" + id + " I");
		}else{
			if(varNum <= 3){
				writer.println("\tistore_" + varNum);
			}else{
				writer.println("\tistore " + varNum);
			}
		}
	}
	
	
	private static void storeVarArray(String id, int varNum, String scope){
		if(scope.equals("global")){
			writer.println("\tputstatic " + moduleName + "/" + id + " [I");
		}else{
			if(varNum <= 3){
				writer.println("\tastore_" + varNum);
			}else{
				writer.println("\tastore " + varNum);
			}
		}
	}
	
	
	private static String getFunctionModule(String fullPath){
		String module = null;
		for (int i = fullPath.length()-1; i >= 0; i--) {
			if(fullPath.charAt(i) == '.'){
				module = fullPath.substring(0, i);
			}
		}
		return module;
	}
	
	
	private static String getFunctionName(String fullPath){
		String name = null;
		for (int i = fullPath.length()-1; i >= 0; i--) {
			if(fullPath.charAt(i) == '.'){
				name = fullPath.substring(i+1, fullPath.length());
			}
		}
		return name;
	}
	
	
	private static String getFunctionFullName(String functionName, Function f){
		String fullName = functionName + "(";
		for (int i = 0; i < f.getNumParameters(); i++) {
			if(f.getParameters().get(i).getType().equals("scalar")){
				fullName += "I";
			}else if(f.getParameters().get(i).getType().equals("array")){
				fullName += "[I";
			}
		}
		fullName += ")";
		
		Variable returnVar = f.getReturnVar();
		if(returnVar == null){
			fullName += "V";
		}else{
			Variable var = f.getReturnVar();
			if(var.getType().equals("scalar")){
				fullName += "I";
			}else if(var.getType().equals("array")){
				fullName += "[I";
			}
		}
		return fullName;
	}
	
	
	private static String getFunctionFullNameOtherModule(Function f, String functionName, List<String> args){
		String fullName = functionName + "(";
		for (int i = 0; i < args.size(); i++) {
			try{
				Integer.parseInt(args.get(i));
				fullName += "I";
			}catch(NumberFormatException e){
				Variable var = YalToJvm.getModule().getVariable(f, args.get(i));
				if(var.getType().equals("scalar")){
					fullName += "I";
				}else if(var.getType().equals("array")){
					fullName += "[I";
				}
			}
		}
		fullName += ")";
		fullName += "I";
		return fullName;
	}
	
}
