import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JasminGenerator {
	
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
		
		printConstructor();
		
		printNewLine();
		printFunctions();
	}
	
	private static void printNewLine(){
		writer.println("");
	}
	
	private static void printClassHeader(){
		writer.println(".source " + moduleName + ".j");
		writer.println(".class public " + moduleName);
		writer.println(".super java/lang/Object");
	}
	
	private static void printConstructor(){
		writer.println(".method public <init>()V");
		writer.println("\t.limit stack 2");
		writer.println("\taload 0");
		writer.println("\tinvokenonvirtual java/lang/Object/<init>()V");
		printNewLine();
		
		//SCALAR TYPES
		for (Map.Entry<String, String> entry : scalarFields.entrySet() ) {
		    String id = entry.getKey();
		    String value = entry.getValue();
		    if(value == null){
		   	 value = "0";
		    }
		    writer.println("\taload 0");
		    writer.println("\tldc " + value);
		    writer.println("\tputfield " + moduleName + "/" + id + " I");
		    printNewLine();
		}
		
		//ARRAY TYPES
		for (Map.Entry<String, String> entry : arrayFields.entrySet() ) {
		    String id = entry.getKey();
		    String size = entry.getValue();
		    writer.println("\taload 0");
		    writer.println("\tldc " + size);
		    writer.println("\tnewarray int");
		    writer.println("\tputfield " + moduleName + "/" + id + " [I");
		    printNewLine();
		}
		
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
		
		writer.println("\t.limit locals " + (1+f.getNumParameters()+f.getNumVariable()));
	}
	
	private static void printBody(Function function, SimpleNode bodyNode){
		for (int i = 0; i < bodyNode.jjtGetNumChildren(); i++) {
			SimpleNode n = (SimpleNode) bodyNode.jjtGetChild(i);
			int id = n.getId();
			
			if (id == YalToJvmTreeConstants.JJTASSIGNEMENT) {
				printAssignment(function, n);
			} else if (id == YalToJvmTreeConstants.JJTWHILE) {
				labelWhileCount++;
				printWhileBlock(n);
			} else if (id == YalToJvmTreeConstants.JJTIF) {
				labelIfCount++;
				printIfBlock(function, n);
			} else if (id == YalToJvmTreeConstants.JJTCALL) {
				printCall(n);
			}
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
	
	private static void printWhileBlock(SimpleNode whileNode) {
		SimpleNode whileCondition = (SimpleNode) whileNode.jjtGetChild(0);
		SimpleNode whileBody = (SimpleNode) whileNode.jjtGetChild(1);
		
		writer.println("While" + labelWhileCount + ":"); // Start label
		
		String endlabel = "EndWhile" + labelWhileCount;
		
		printCondition(whileCondition, endlabel);
		
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
	
	private static void printCall(SimpleNode callNode) {
		//TODO
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
	
	private static void printMethodFooter(){
		//TODO: Temporary
		writer.println("\treturn");
		writer.println(".end method");
	}
	
	// .field <access-spec> <field-name> <descriptor> [ = <value> ] <- value doesn't work idkw
	private static void printField(String type, String name, String value){
		String statement = ".field private ";
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
		    printBody(f, f.getBody());
		    printCFG(f, f.cfgStartNode);
		    printMethodFooter();
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
	
}
