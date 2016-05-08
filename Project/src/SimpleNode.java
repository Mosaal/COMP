/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 6.0 */
/* JavaCCOptions:MULTI=false,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class SimpleNode implements Node {
	protected Node parent;
	protected Node[] children;
	protected int id;
	protected Object value;
	protected YalToJvm parser;

	//Value stored in each node
	public String ID;

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

	public boolean dot(String callID) {
		for (int i = 0; i < callID.length(); i++)
			if (callID.charAt(i) == '.')
				return true;
		return false;
	}

	public String[] separateString(String callID) {
		return callID.split("\\.");
	}

	/* Override this method if you want to customize how the node dumps
     out its children. */

	public void dump(String prefix) {
		switch (id) {
		case YalToJvmTreeConstants.JJTMODULE:
			System.out.println(toString(prefix) + " \"" + ID + "\"");
			break;
		case YalToJvmTreeConstants.JJTGLOBAL:
			if (ID != null)
				System.out.println(prefix + "[ " + ID + " ]");
			else
				prefix = newPrefix(prefix, false);
			break;
		case YalToJvmTreeConstants.JJTGLOBALRIGHT:
			if (ID != null)
				System.out.println(prefix + "[ " + ID + " ]");
			else {
				SimpleNode arraySize = (SimpleNode)jjtGetChild(0);
				System.out.println(prefix + "[ [" + arraySize.ID + "] ]");
			}
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
		case YalToJvmTreeConstants.JJTWHILE:
			System.out.println(toString(prefix));
			break;
		case YalToJvmTreeConstants.JJTIF:
			System.out.println(toString(prefix));
			break;
		case YalToJvmTreeConstants.JJTCALL:
			if (dot(ID))
				System.out.println(toString(prefix) + " \"" + separateString(ID)[1] + "\" of \"" + separateString(ID)[0] + "\"");
			else
				System.out.println(toString(prefix) + " \"" + ID + "\"");
			break;
		case YalToJvmTreeConstants.JJTARRAYACCESS:
			SimpleNode index = (SimpleNode)jjtGetChild(0);
			System.out.println(prefix + "[ " + ID + "[" + index.ID + "] ]");
			break;
		default:
			if (id != YalToJvmTreeConstants.JJTARRAYSIZE && id != YalToJvmTreeConstants.JJTINDEX) {
				if (ID != null)
					System.out.println(prefix + "[ " + ID + " ]");
				else
					prefix = newPrefix(prefix, false);
			}
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

	public void processCondition(SimpleNode lhs, SimpleNode rhs, Function parentFunction) {
		if (lhs.getId() == YalToJvmTreeConstants.JJTARRAYACCESS) {

		} else if (lhs.getId() == YalToJvmTreeConstants.JJTSCALARACCESS) {

		}

		if (rhs.jjtGetNumChildren() == 1) {
			SimpleNode rhsChild = (SimpleNode)rhs.jjtGetChild(0);

			if (rhsChild.getId() == YalToJvmTreeConstants.JJTTERM) {

			} else if (rhsChild.getId() == YalToJvmTreeConstants.JJTARRAYSIZE) {

			}
		} else if (rhs.jjtGetNumChildren() == 2) {
			SimpleNode termLeft = (SimpleNode)rhs.jjtGetChild(0);
			SimpleNode termRight = (SimpleNode)rhs.jjtGetChild(1);

			if (termLeft.ID != null) {

			} else {
				SimpleNode termChild = (SimpleNode)termLeft.jjtGetChild(0);

				if (termChild.getId() == YalToJvmTreeConstants.JJTCALL) {

				} else if (termChild.getId() == YalToJvmTreeConstants.JJTARRAYACCESS) {

				} else if (termChild.getId() == YalToJvmTreeConstants.JJTSCALARACCESS) {

				}
			}

			if (termRight.ID != null) {

			} else {
				SimpleNode termChild = (SimpleNode)termRight.jjtGetChild(0);

				if (termChild.getId() == YalToJvmTreeConstants.JJTCALL) {

				} else if (termChild.getId() == YalToJvmTreeConstants.JJTARRAYACCESS) {

				} else if (termChild.getId() == YalToJvmTreeConstants.JJTSCALARACCESS) {

				}
			}
		}
	}

	public void processAssignement(SimpleNode lhs, SimpleNode rhs, Function parentFunction) {
<<<<<<< Updated upstream
		Variable v = new Variable(lhs.ID);
		if(lhs.getId() == YalToJvmTreeConstants.JJTARRAYACCESS) {
			
		}else if(lhs.getId() == YalToJvmTreeConstants.JJTSCALARACCESS) {
			
=======
		if (lhs.getId() == YalToJvmTreeConstants.JJTARRAYACCESS) {

		} else if (lhs.getId() == YalToJvmTreeConstants.JJTSCALARACCESS) {

>>>>>>> Stashed changes
		}
		if (rhs.jjtGetNumChildren() == 1) {
<<<<<<< Updated upstream
			SimpleNode rhsChild = (SimpleNode)jjtGetChild(0);
			
=======
			SimpleNode rhsChild = (SimpleNode)rhs.jjtGetChild(0);

>>>>>>> Stashed changes
			if (rhsChild.getId() == YalToJvmTreeConstants.JJTTERM) {
				
			} else if (rhsChild.getId() == YalToJvmTreeConstants.JJTARRAYSIZE) {
				
			}
		} else if (rhs.jjtGetNumChildren() == 2) {
			SimpleNode termLeft = (SimpleNode)rhs.jjtGetChild(0);
			SimpleNode termRight = (SimpleNode)rhs.jjtGetChild(1);

			if (termLeft.ID != null) {

			} else {
				SimpleNode termChild = (SimpleNode)termLeft.jjtGetChild(0);

				if (termChild.getId() == YalToJvmTreeConstants.JJTCALL) {

				} else if (termChild.getId() == YalToJvmTreeConstants.JJTARRAYACCESS) {
					
				} else if (termChild.getId() == YalToJvmTreeConstants.JJTSCALARACCESS) {
					
				}
			}

			if (termRight.ID != null) {

			} else {
				SimpleNode termChild = (SimpleNode)termRight.jjtGetChild(0);

				if (termChild.getId() == YalToJvmTreeConstants.JJTCALL) {

				} else if (termChild.getId() == YalToJvmTreeConstants.JJTARRAYACCESS) {

				} else if (termChild.getId() == YalToJvmTreeConstants.JJTSCALARACCESS) {

				}
			}
		}
	}

	/**
	 * Method used to process the body of a function, a while loop and an if/else statement
	 */
	public void processBody(Function parentFunction) {
		for (int i = 0; i < children.length; i++) {
			SimpleNode bodyChild = (SimpleNode)children[i];

			switch (bodyChild.getId()) {
			case YalToJvmTreeConstants.JJTASSIGNEMENT:
				SimpleNode lhs = (SimpleNode)bodyChild.jjtGetChild(0); //ArrayAccess or ScalarAccess
				SimpleNode rhs = (SimpleNode)bodyChild.jjtGetChild(1); //Rhs
				processAssignement(lhs, rhs, parentFunction);
				break;
			case YalToJvmTreeConstants.JJTWHILE:
				SimpleNode whileCondition = (SimpleNode)bodyChild.jjtGetChild(0);
				SimpleNode whileLhs = (SimpleNode)whileCondition.jjtGetChild(0);
				SimpleNode whileRhs = (SimpleNode)whileCondition.jjtGetChild(1);
				processCondition(whileLhs, whileRhs, parentFunction);

				SimpleNode whileBody = (SimpleNode)bodyChild.jjtGetChild(1);
				whileBody.processBody(parentFunction);
				break;
			case YalToJvmTreeConstants.JJTIF:
				SimpleNode ifCondition = (SimpleNode)bodyChild.jjtGetChild(0);
				SimpleNode ifLhs = (SimpleNode)ifCondition.jjtGetChild(0);
				SimpleNode ifRhs = (SimpleNode)ifCondition.jjtGetChild(1);
				processCondition(ifLhs, ifRhs, parentFunction);

				SimpleNode ifBody = (SimpleNode)bodyChild.jjtGetChild(1);
				ifBody.processBody(parentFunction);

				if (bodyChild.jjtGetNumChildren() == 3) {
					SimpleNode elseBody = (SimpleNode)bodyChild.jjtGetChild(2);
					elseBody.processBody(parentFunction);
				}
				break;
			case YalToJvmTreeConstants.JJTCALL:
				// check if function exists
				break;
			}
		}
	}

	public int getId() {
		return id;
	}
}

/* JavaCC - OriginalChecksum=45cdfa7b6c656a79cec0656f2db154c7 (do not edit this line) */
