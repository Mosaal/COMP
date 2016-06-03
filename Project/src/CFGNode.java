import java.util.ArrayList;
import java.util.List;


public class CFGNode {
	/* Meta data */
	public String type;
	public List<CFGNode> outs;
	public List<CFGNode> ins;
	public static int cfgNodeCount;
	public int number;
	
	/* Assignement or declaration */
	public boolean twoSides;
	
	public String lhsId;
	public String lhsAccess;
	public String lhsType;
	public String lhsArrayIndexId;
	public String lhsArrayAccess;
	
	public String rhs1Id;
	public String rhs1Access;
	public String rhs1Type;
	public String rhs1ArrayIndexId;
	public String rhs1ArrayAccess;
	
	public String rhs2Id;
	public String rhs2Access;
	public String rhs2Type;
	public String rhs2ArrayIndexId;
	public String rhs2ArrayAccess;
	
	public boolean newVar;
	
	/* Conditions */
	
	/* Call */
	
	/* For printing purposes */
	public boolean visited;
	
	
	
	public CFGNode (String t, Function f) {
		type = t;
		outs = new ArrayList<CFGNode>();
		ins = new ArrayList<CFGNode>();
		if(!t.equals("endif")){
			number = cfgNodeCount;
			cfgNodeCount++;
			f.cfgNodes.add(this);
		}else{
			number = -1;
		}
	}
	
}
