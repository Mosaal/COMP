import java.util.ArrayList;
import java.util.List;


public class CFGNode {
	/* Meta data */
	public String type;
	public List<CFGNode> outs;
	public List<CFGNode> ins;
	public List<Variable> laIns;
	public List<Variable> laOuts;
	public static int cfgNodeCount;
	public int number;
	
	/* Assignement or declaration */
	public boolean twoSides;
	public String lhsId;
	public String lhsAccess;
	public String lhsType;
	public String lhsArrayIndexId;
	public String lhsArrayAccess;
	public boolean newVar;
	
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
	
	@Override
    public boolean equals(Object obj) {
		if (!(obj instanceof CFGNode))
			return false;
		else
			return ((CFGNode)obj).number == this.number;
	}	
}
