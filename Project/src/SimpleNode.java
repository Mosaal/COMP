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
	public String callID;
	public String Op = "";
	public String size = "";
	public String assign = "";
	public boolean digit = false;

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

	public Node[] jjtGetChildren() {
		return children;
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

	public String newPrefix(String prefix, boolean increase) {
		String newPrefix = "";

		if (!increase) {
			for (int i = 0; i < prefix.length() - 1; i++)
				newPrefix += " ";
		} else {
			for (int i = 0; i < prefix.length() + 1; i++)
				newPrefix += " ";
		}

		return newPrefix;
	}

	/* Override this method if you want to customize how the node dumps
     out its children. */

	public void dump(String prefix) {
		//System.out.println(toString(prefix));
		switch (id) {
		case YalToJvmTreeConstants.JJTVOID:
			System.out.println(toString(prefix) + " VOID");
			break;
		case YalToJvmTreeConstants.JJTMODULE:
			System.out.println(toString(prefix) + " \"" + ID + "\"");
			break;
		case YalToJvmTreeConstants.JJTGLOBAL:
			if (assign != "")
				System.out.println(prefix + "[ = ]");
			else
				prefix = newPrefix(prefix, false);
			break;
		case YalToJvmTreeConstants.JJTGLOBALRIGHT:
			if (Op != "" || ID != null)
				System.out.println(newPrefix(prefix, true) + "[ " + Op + ID + " ]");
			break;
		case YalToJvmTreeConstants.JJTFUNCTION:
			System.out.println(toString(prefix) + " \"" + ID + "\"");
			break;
		case YalToJvmTreeConstants.JJTRETURN:
			System.out.println(toString(prefix));
			break;
		case YalToJvmTreeConstants.JJTPARAMS:
			System.out.println(toString(prefix));
			break;
		case YalToJvmTreeConstants.JJTARRAY:
			System.out.println(prefix + "[ " + ID + "[] ]");
			break;
		case YalToJvmTreeConstants.JJTSCALAR:
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
		case YalToJvmTreeConstants.JJTOPERATOR:
			System.out.println(prefix + "[ " + Op + " ]");
			break;
		case YalToJvmTreeConstants.JJTARRAYSIZE:
			System.out.println(prefix + "[ [" + ID + "] ]");
			break;
		case YalToJvmTreeConstants.JJTTERM:
			if (Op != "" || ID != null) {
				SimpleNode temp = (SimpleNode)parent;
				if (temp.Op.equals("+") || temp.Op.equals("-"))
					System.out.println(newPrefix(prefix, true) + "[ " + Op + ID + " ]");
				else if (temp.Op.equals("*") || temp.Op.equals("/") || temp.Op.equals("<<") || temp.Op.equals(">>") || temp.Op.equals(">>>"))
					System.out.println(newPrefix(prefix, true) + "[ " + Op + ID + " ]");
				else if (temp.Op.equals("&") || temp.Op.equals("|") || temp.Op.equals("^"))
					System.out.println(newPrefix(prefix, true) + "[ " + Op + ID + " ]");
				else
					System.out.println(prefix + "[ " + Op + ID + " ]");
			}
			break;
		case YalToJvmTreeConstants.JJTCONDITION:
			System.out.println(prefix + "[ " + ID + " ]");
			break;
		case YalToJvmTreeConstants.JJTWHILE:
			System.out.println(toString(prefix));
			break;
		case YalToJvmTreeConstants.JJTIF:
			System.out.println(toString(prefix));
			break;
		case YalToJvmTreeConstants.JJTCALL:
			if (callID == null)
				System.out.println(toString(prefix) + " \"" + ID + "\"");
			else
				System.out.println(toString(prefix) + " \"" + callID + "\" of \"" + ID + "\"");
			break;
		case YalToJvmTreeConstants.JJTARGUMENT:
			System.out.println(prefix + "[ " + ID + " ]");
			break;
		case YalToJvmTreeConstants.JJTARRAYACCESS:
			SimpleNode temp = (SimpleNode)children[0];
			System.out.println(prefix + "[ " + ID + "[" + temp.ID + "]" + " ]");
			break;
		case YalToJvmTreeConstants.JJTSCALARACCESS:
			if (size == "")
				System.out.println(prefix + "[ " + ID + " ]");
			else
				System.out.println(prefix + "[ " + ID + ".size ]");
			break;
		}

		if (children != null) {
			for (int i = 0; i < children.length; ++i) {
				SimpleNode n = (SimpleNode)children[i];
				if (n != null) {
					n.dump(prefix + " ");
				}
			}
		}
	}

	/**
	 * Method used to process the body of a function, a while loop and an if/else statement
	 */
	public void processBody(Function parentFunction) {		
		for (int i = 0; i < children.length; ++i) {
			SimpleNode child = (SimpleNode)children[i];
			
			switch (child.getId()) {
			case YalToJvmTreeConstants.JJTASSIGNEMENT:
				SimpleNode lhs = (SimpleNode)child.jjtGetChild(0);
				SimpleNode rhs = (SimpleNode)child.jjtGetChild(1);
				
				//Process lhs
				if (lhs.getId() == YalToJvmTreeConstants.JJTARRAYACCESS) {
					SimpleNode index = (SimpleNode)lhs.jjtGetChild(0);
					
					if (index.digit) {
						
					} else {

					}
				} else if (lhs.getId() == YalToJvmTreeConstants.JJTSCALARACCESS) {

				}
				
				//Process rhs
				Node[] rhsChildren = rhs.jjtGetChildren();
				if (rhsChildren.length == 1) {
					SimpleNode rhsChild = (SimpleNode)rhsChildren[0];
					
					if (rhsChild.getId() == YalToJvmTreeConstants.JJTTERM) {
						if (rhsChild.ID != null) {

						} else {
							SimpleNode termChild = (SimpleNode)rhsChild.jjtGetChild(0);
							
							switch (termChild.getId()) {
							case YalToJvmTreeConstants.JJTCALL:
								//set value to variable
								break;
							case YalToJvmTreeConstants.JJTARRAYACCESS:
								//set value to variable
								break;
							case YalToJvmTreeConstants.JJTSCALARACCESS:
								//set value to variable
								break;
							}
						}
					} else if (rhsChild.getId() == YalToJvmTreeConstants.JJTARRAYSIZE) {
						//System.out.println("ArraySize");
					}
				} else if (rhsChildren.length == 2) {
					SimpleNode rhsChildLeft = (SimpleNode)rhsChildren[0];
					SimpleNode rhsChildRight = (SimpleNode)rhsChildren[1];
				}
				break;
			case YalToJvmTreeConstants.JJTWHILE:
				SimpleNode whileCondition = (SimpleNode)child.jjtGetChild(0);
				SimpleNode whileBody = (SimpleNode)child.jjtGetChild(1);
				
				//process condition
				//whileBody.processBody(parentFunction);
				break;
			case YalToJvmTreeConstants.JJTIF:
				Node[] ifChildren = child.jjtGetChildren();
				if (ifChildren.length == 2) {
					SimpleNode ifCondition = (SimpleNode)child.jjtGetChild(0);
					SimpleNode ifBody = (SimpleNode)child.jjtGetChild(1);

					//process condition
					//ifBody.processBody(parentFunction);
				} else if (ifChildren.length == 3) {
					SimpleNode ifCondition = (SimpleNode)child.jjtGetChild(0);
					SimpleNode ifBody = (SimpleNode)child.jjtGetChild(1);
					SimpleNode elseBody = (SimpleNode)child.jjtGetChild(2);

					//process condition
					//ifBody.processBody(parentFunction);
					//elseBody.processBody(parentFunction);
				}
				break;
			case YalToJvmTreeConstants.JJTCALL:
				//process call
				break;
			}
		}
	}

	public int getId() {
		return id;
	}
}

/* JavaCC - OriginalChecksum=45cdfa7b6c656a79cec0656f2db154c7 (do not edit this line) */
