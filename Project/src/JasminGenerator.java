

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class JasminGenerator {
	private static PrintWriter writer;
	private static SimpleNode node;
	private static String module;
	
	public static void generate(String m, SimpleNode n){
		node = n;
		module = m;
		try {
			writer = new PrintWriter(module + ".j", "UTF-8");
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
		printSuperInit();
	}
	
	private static void printNewLine(){
		writer.println("");
	}
	
	private static void printClassHeader(){
		writer.println(".class public " + module);
		writer.println(".super java/lang/Object");
	}
	
	private static void printSuperInit(){
		writer.println(".method public <init>()V");
		writer.println("\taload_0");
      writer.println("\tinvokenonvirtual java/lang/Object/<init>()V");
      writer.println("\treturn");
      writer.println(".end method");
	}
	
	private static void printMethodHeader(){
		
	}
	
private static void printMethodFooter(){
		
	}
	
}
