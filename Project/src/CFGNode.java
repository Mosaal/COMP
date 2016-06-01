
public class CFGNode {
	public String type;
	public CFGNode[] outs;
	public CFGNode[] ins;
	public boolean twoSides;
	
	/* Assignement or declaration */
	public String lhsId;
	public String lhsAccess;
	public String lhsType;
	public String lhsArrayIndexId;
	public String lhsArrayAccess;
	
}
