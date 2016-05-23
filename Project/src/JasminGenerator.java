

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JasminGenerator {
	private static final String SCALARTYPE = "I";
	private static final String ARRAYTYPE = "[I";
	private static final HashMap<String, String> scalarFields = new HashMap<String,String>();
	private static final HashMap<String, String> arrayFields = new HashMap<String,String>();
	private static PrintWriter writer;
	private static SimpleNode node;
	private static Module module;
	private static String moduleName;
	
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
		writer.println("\t.limit locals " + (f.getNumParameters()+1));
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
		if(value != null && !type.equals(ARRAYTYPE)){
			statement += " = " + value;
		}
		writer.println(statement);
	}
	
	private static void printFields(){
		int size = node.jjtGetNumChildren();
		int i;
		for (i = 0; i < size; i++) {
			SimpleNode n = (SimpleNode)node.jjtGetChild(i);
			if(n.getId() == YalToJvmTreeConstants.JJTGLOBAL){
				String name = null;
				String type = null;
				String value = null;
				SimpleNode lhs = (SimpleNode)n.jjtGetChild(0);
				name = lhs.ID;
				if(n.jjtGetNumChildren() == 2){
					SimpleNode rhs = (SimpleNode)n.jjtGetChild(1);
					if(rhs.ID == null){
						type = ARRAYTYPE;
						SimpleNode arraySize = ((SimpleNode)rhs.jjtGetChild(0));
						if(arraySize.ID == null){
							value = ((SimpleNode)arraySize.jjtGetChild(0)).ID;
						}else{
							value = arraySize.ID;
						}
					}else{
						value = rhs.ID;
						type = SCALARTYPE;
					}
				}else{
					type = SCALARTYPE;
				}
				if(type.equals(SCALARTYPE)){
					scalarFields.put(name, value);
				}else {
					arrayFields.put(name,value);
				}
				printField(type,name,value);
			}else{
				i = size;
			}
		}
	}
	
	private static void printFunctions(){
		HashMap<String, Function> map = module.getFunctionMap();
		for (Function f : map.values()) {
		    printMethodHeader(f);
		    printMethodFooter();
		}
	}
	
}
