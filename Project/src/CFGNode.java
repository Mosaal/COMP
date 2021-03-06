import java.util.ArrayList;
import java.util.List;


public class CFGNode {
	/* Meta data */
	public String type; //assignment, while, if, endif, call, start, end
	public ArrayList<CFGNode> outs = new ArrayList<CFGNode>();
	public ArrayList<CFGNode> ins = new ArrayList<CFGNode>();
	public ArrayList<String> uses = new ArrayList<String>();
	public ArrayList<String> defs = new ArrayList<String>();
	public ArrayList<String> laIns = new ArrayList<String>();
	public ArrayList<String> laOuts = new ArrayList<String>();
	public static int cfgNodeCount;
	public int number;
	
	/* Assignement or declaration */
	public boolean twoSides;
	public String assignementOp;
	
	public String lhsId;
	public String lhsScope;
	public String lhsAccess;
	public String lhsType;
	public String lhsArrayIndexId;
	public String lhsArrayAccess;
	
	public String rhs1Id;
	public String rhs1Scope;
	public String rhs1Access;
	public String rhs1Type;
	public String rhs1ArrayIndexId;
	public String rhs1ArrayAccess;
	public List<String> rhs1Args;
	public Function rhs1Call;
	public boolean rhs1OtherModule;
	
	public String rhs2Id;
	public String rhs2Scope;
	public String rhs2Access;
	public String rhs2Type;
	public String rhs2ArrayIndexId;
	public String rhs2ArrayAccess;
	public List<String> rhs2Args;
	public Function rhs2Call;
	public boolean rhs2OtherModule;
	
	public String callModule;
	public String callFuntion;
	public String callFullName;
	public String[] callParams;
	
	public boolean newVar;
	
	/* Conditions */
	public String condOp;
	public String condSign;
	public boolean condInvert;
	
	/* Call */
	public boolean dot;
	public String callName;
	public String[] callArgs;
	
	/* For printing purposes */
	public boolean visited;
	
	
	
	public CFGNode (String t, Function f) {
		type = t;
		outs = new ArrayList<CFGNode>();
		ins = new ArrayList<CFGNode>();
		rhs1Args = new ArrayList<String>();
		rhs2Args = new ArrayList<String>();
		number = f.cfgNodeCount;
		f.cfgNodeCount++;
		f.cfgNodes.add(this);
	}
	
	@Override
    public boolean equals(Object obj) {
		if (!(obj instanceof CFGNode))
			return false;
		else
			return ((CFGNode)obj).number == this.number;
	}
	
	public void printAssignmentNode(){
		System.out.println("LHS:");
		System.out.println("lhsId - " + lhsId);
		System.out.println("lhsScope - " + lhsScope);
		System.out.println("lhsAccess - " + lhsAccess);
		System.out.println("lhsType - " + lhsType);
		System.out.println("lhsArrayIndexId - " + lhsArrayIndexId);
		System.out.println("lhsArrayAccess - " + lhsArrayAccess);
		System.out.println("---------------------------------");
		System.out.println("RHS1:");
		System.out.println("rhs1Id - " + rhs1Id);
		System.out.println("rhs1Scope - " + rhs1Scope);
		System.out.println("rhs1Access - " + rhs1Access);
		System.out.println("rhs1Type - " + rhs1Type);
		System.out.println("rhs1ArrayIndexId - " + rhs1ArrayIndexId);
		System.out.println("rhs1ArrayAccess - " + rhs1ArrayAccess);
		if(twoSides){
			System.out.println("---------------------------------");
			System.out.println("RHS2:");
			System.out.println("rhs2Id - " + rhs2Id);
			System.out.println("rhs2Scope - " + rhs2Scope);
			System.out.println("rhs2Access - " + rhs2Access);
			System.out.println("rhs2Type - " + rhs2Type);
			System.out.println("rhs2ArrayIndexId - " + rhs2ArrayIndexId);
			System.out.println("rhs2ArrayAccess - " + rhs2ArrayAccess);
			System.out.println("---------------------------------");
		}
		System.out.println("");
	}
}
