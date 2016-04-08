import java.util.ArrayList;

/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 6.0 */
/* JavaCCOptions:MULTI=false,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class SimpleNode implements Node {
	protected Node parent;
	protected Node[] children;
	protected int id;
	protected Object value;
	protected YalToJvm parser;

	//added
	public String ID;
	public String Op = "";
	public String assign = "";

	public SimpleNode(int i) {
		id = i;
	}

	public SimpleNode(YalToJvm p, int i) {
		this(i);
		parser = p;
	}

	public void jjtOpen() {
	}

	public void jjtClose() {
	}

	public void jjtSetParent(Node n) { parent = n; }
	public Node jjtGetParent() { return parent; }

	public void jjtAddChild(Node n, int i) {
		if (children == null) {
			children = new Node[i + 1];
		} else if (i >= children.length) {
			Node c[] = new Node[i + 1];
			System.arraycopy(children, 0, c, 0, children.length);
			children = c;
		}
		children[i] = n;
	}

	public Node jjtGetChild(int i) {
		return children[i];
	}

	public int jjtGetNumChildren() {
		return (children == null) ? 0 : children.length;
	}

	public void jjtSetValue(Object value) { this.value = value; }
	public Object jjtGetValue() { return value; }

	/* You can override these two methods in subclasses of SimpleNode to
     customize the way the node appears when the tree is dumped.  If
     your output uses more than one line you should override
     toString(String), otherwise overriding toString() is probably all
     you need to do. */

	public String toString() {
		return YalToJvmTreeConstants.jjtNodeName[id];
	}
	public String toString(String prefix) { return prefix + toString(); }

	/* Override this method if you want to customize how the node dumps
     out its children. */

	public void dump(String prefix) {
		//System.out.println(toString(prefix));
		switch (id) {
		case YalToJvmTreeConstants.JJTMODULE:
			System.out.println(toString(prefix) + " \"" + ID + "\"");
			break;
		case YalToJvmTreeConstants.JJTFUNCTION:
			System.out.println(toString(prefix) + " \"" + ID + "\"");
			break;
		case YalToJvmTreeConstants.JJTPARAMS:
			System.out.println(toString(prefix));
			break;
		case YalToJvmTreeConstants.JJTVAR:
			System.out.println(prefix + "[ " + ID + " ]");
			break;
		case YalToJvmTreeConstants.JJTFUNCTIONBODY:
			System.out.println(toString(prefix));
			break;
		case YalToJvmTreeConstants.JJTWHILEBODY:
			System.out.println(toString(prefix));
			break;
		case YalToJvmTreeConstants.JJTIFBODY:
			System.out.println(toString(prefix));
			break;
		case YalToJvmTreeConstants.JJTELSEBODY:
			System.out.println(toString(prefix));
			break;
		case YalToJvmTreeConstants.JJTASSIGNEMENT:
			System.out.println(prefix + "[ = ]");
			break;
		case YalToJvmTreeConstants.JJTRHS:
			if (Op != "")
				System.out.println(prefix + "[ " + Op + " ]");
			break;
		case YalToJvmTreeConstants.JJTWHILE:
			System.out.println(toString(prefix));
			break;
		case YalToJvmTreeConstants.JJTIF:
			System.out.println(toString(prefix));
			break;
		case YalToJvmTreeConstants.JJTARRAYACCESS:
			System.out.println(prefix + "[ " + ID + " ]");
			break;
		case YalToJvmTreeConstants.JJTSCALARACCESS:
			System.out.println(prefix + "[ " + ID + " ]");
			break;
		case YalToJvmTreeConstants.JJTTERM:
			if (Op != "" || ID != null)
				System.out.println(prefix + "[ " + Op + ID + " ]");
			break;
		case YalToJvmTreeConstants.JJTCONDITION:
			System.out.println(prefix + "[ " + ID + " ]");
			break;
		}

		if (children != null) {
			for (int i = 0; i < children.length; ++i) {
				SimpleNode n = (SimpleNode)children[i];
				if (n != null) {
					n.dump(prefix + "   ");
				}
			}
		}
	}

	public void getFunctions(String prefix) {
		switch (id) {
		case YalToJvmTreeConstants.JJTFUNCTION:
			String name = ID;
			ArrayList<Variable> params = getParams();
			Function f = new Function(name,params);
			YalToJvm.getModule().addFunction(f);
			break;
		default:
			if (children != null) {
				for (int i = 0; i < children.length; ++i) {
					SimpleNode n = (SimpleNode)children[i];
					if (n != null) {
						n.getFunctions(prefix + " ");
					}
				}
			}
		}
	}

	public ArrayList<Variable> getParams(){
		ArrayList<Variable> params = new ArrayList<>();
		SimpleNode paramNode = (SimpleNode)children[0];
		if(paramNode.id == YalToJvmTreeConstants.JJTPARAMS){
			int num = paramNode.jjtGetNumChildren();
			for (int i = 0; i < num; i++) {
				SimpleNode node = (SimpleNode)paramNode.jjtGetChild(i);
				//TODO change this
				params.add(new Scalar(node.ID,1));
			}
		}
		return params;
	}

	public int getId() {
		return id;
	}
}

/* JavaCC - OriginalChecksum=45cdfa7b6c656a79cec0656f2db154c7 (do not edit this line) */
