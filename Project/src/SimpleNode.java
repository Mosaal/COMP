/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 6.0 */
/* JavaCCOptions:MULTI=false,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public class SimpleNode implements Node {
	protected Node parent;
	protected Node[] children;
	protected int id;
	protected Object value;
	protected YalToJvm parser;

	// Value stored in each node
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

	public void jjtSetParent(Node n) {
		parent = n;
	}

	public Node jjtGetParent() {
		return parent;
	}

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

	public Node[] jjtGetChildren() {
		return children;
	}

	public void jjtSetValue(Object value) {
		this.value = value;
	}

	public Object jjtGetValue() {
		return value;
	}

	/*
	 * You can override these two methods in subclasses of SimpleNode to
	 * customize the way the node appears when the tree is dumped. If your
	 * output uses more than one line you should override toString(String),
	 * otherwise overriding toString() is probably all you need to do.
	 */
	public String toString() {
		return YalToJvmTreeConstants.jjtNodeName[id];
	}

	public String toString(String prefix) {
		return prefix + toString();
	}

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

	/*
	 * Override this method if you want to customize how the node dumps out its
	 * children.
	 */
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
				SimpleNode arraySize = (SimpleNode) jjtGetChild(0);
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
				System.out.println(
						toString(prefix) + " \"" + separateString(ID)[1] + "\" of \"" + separateString(ID)[0] + "\"");
			else
				System.out.println(toString(prefix) + " \"" + ID + "\"");
			break;
		case YalToJvmTreeConstants.JJTARRAYACCESS:
			SimpleNode index = (SimpleNode) jjtGetChild(0);
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
				SimpleNode n = (SimpleNode) children[i];
				if (n != null) {
					n.dump(prefix + " ");
				}
			}
		}
	}

	public String getRealFunctionName(Node[] args, Function parentFunction) {
		String temp = "";
		
		for (int i = 0; i < args.length; i++) {
			SimpleNode arg = (SimpleNode)args[i];

			if (YalToJvm.getModule().checkGlobalVariable(arg.ID)) {
				if (YalToJvm.getModule().getGlobalVariable(arg.ID) instanceof Scalar) {
					temp += "_s";
					processScalarAccess(arg.ID, parentFunction);
				} else {
					temp += "_a";
					processArrayAccess(arg.ID, "0", parentFunction);
				}
			} else if (parentFunction.checkLocalVariable(arg.ID)) {
				if (parentFunction.getVariable(arg.ID) instanceof Scalar) {
					temp += "_s";
					processScalarAccess(arg.ID, parentFunction);
				} else {
					temp += "_a";
					processArrayAccess(arg.ID, "0", parentFunction);
				}
			} else if (parentFunction.checkParams(arg.ID)) {
				if (parentFunction.getParameter(arg.ID) instanceof Scalar) {
					temp += "_s";
					processScalarAccess(arg.ID, parentFunction);
				} else {
					temp += "_a";
					processArrayAccess(arg.ID, "0", parentFunction);
				}
			} else if (parentFunction.checkReturnVariable(arg.ID)) {
				if (parentFunction.getReturnVar() instanceof Scalar) {
					temp += "_s";
					processScalarAccess(arg.ID, parentFunction);
				} else {
					temp += "_a";
					processArrayAccess(arg.ID, "0", parentFunction);
				}
			} else {
				temp += "_" + arg.ID;
				YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: The variable \"" + arg.ID +"\" hasn't been declared!");
			}
		}
		
		return temp;
	}
	
	public void processArrayAccess(String arrayAccess, String index, Function parentFunction) {
		if (YalToJvm.getModule().checkGlobalVariable(arrayAccess)) {
			if (YalToJvm.getModule().getGlobalVariable(arrayAccess) instanceof Scalar)
				YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: The variable \"" + arrayAccess + "\" is not an array!");
		} else if (parentFunction.checkLocalVariable(arrayAccess)) {
			if (parentFunction.getVariable(arrayAccess) instanceof Scalar)
				YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: The variable \"" + arrayAccess + "\" is not an array!!");
		} else if (parentFunction.checkParams(arrayAccess)) {
			if (parentFunction.getParameter(arrayAccess) instanceof Scalar)
				YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: The variable \"" + arrayAccess + "\" is not an array!!");
		} else if (parentFunction.checkReturnVariable(arrayAccess)) {
			if (parentFunction.getReturnVar() instanceof Scalar)
				YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: The variable \"" + arrayAccess + "\" is not an array!!");
		} else {
			YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: The variable \"" + arrayAccess + "\" hasn't been declared!");
		}

		try {
			Integer.parseInt(index);
		} catch (NumberFormatException e) {
			if (YalToJvm.getModule().checkGlobalVariable(index)) {
				if (YalToJvm.getModule().getGlobalVariable(index) instanceof Array)
					YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: To access the array \"" + arrayAccess + "\" you must use an integer or a scalar variable!");
			} else if (parentFunction.checkLocalVariable(index)) {
				if (parentFunction.getVariable(index) instanceof Array)
					YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: To access the array \"" + arrayAccess + "\" you must use an integer or a scalar variable!");
			} else if (parentFunction.checkParams(index)) {
				if (parentFunction.getParameter(index) instanceof Array)
					YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: To access the array \"" + arrayAccess + "\" you must use an integer or a scalar variable!");
			} else if (parentFunction.checkReturnVariable(index)) {
				if (parentFunction.getReturnVar() instanceof Array)
					YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: To access the array \"" + arrayAccess + "\" you must use an integer or a scalar variable!");
			} else {
				YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: The variable \"" + index + "\" hasn't been declared!");
			}
		}
	}

	public void processScalarAccess(String scalarAccess, Function parentFunction) {
		String var;

		if (dot(scalarAccess)) {
			var = separateString(scalarAccess)[0];

			if (YalToJvm.getModule().checkGlobalVariable(var)) {
				if (YalToJvm.getModule().getGlobalVariable(var) instanceof Scalar)
					YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: The \"size\" property is not avaiable for the variable \"" + var + "\"!");
			} else if (parentFunction.checkLocalVariable(var)) {
				if (parentFunction.getVariable(var) instanceof Scalar)
					YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: The \"size\" property is not avaiable for the variable \"" + var + "\"!");
			} else if (parentFunction.checkParams(var)) {
				if (parentFunction.getParameter(var) instanceof Scalar)
					YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: The \"size\" property is not avaiable for the variable \"" + var + "\"!");
			} else if (parentFunction.checkReturnVariable(var)) {
				if (parentFunction.getReturnVar() instanceof Scalar)
					YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: The \"size\" property is not avaiable for the variable \"" + var + "\"!");
			} else {
				YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: The variable \"" + var + "\" hasn't been declared!");
			}
		} else {
			if (YalToJvm.getModule().checkGlobalVariable(scalarAccess)) {
				if (YalToJvm.getModule().getGlobalVariable(scalarAccess) instanceof Array)
					YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: You can't use the array \"" + scalarAccess + "\" without indicating an index to access!");
			} else if (parentFunction.checkLocalVariable(scalarAccess)) {
				if (parentFunction.getVariable(scalarAccess) instanceof Array)
					YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: You can't use the array \"" + scalarAccess + "\" without indicating an index to access!");
			} else if (parentFunction.checkParams(scalarAccess)) {
				if (parentFunction.getParameter(scalarAccess) instanceof Array)
					YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: You can't use the array \"" + scalarAccess + "\" without indicating an index to access!");
			} else if (parentFunction.checkReturnVariable(scalarAccess)) {
				if (parentFunction.getReturnVar() instanceof Array)
					YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: You can't use the array \"" + scalarAccess + "\" without indicating an index to access!");
			} else {
				YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: The variable \"" + scalarAccess + "\" hasn't been declared!");
			}
		}
	}

	public void processCall(String call, Node[] args, boolean isCondition, Function parentFunction) {
		if (dot(call)) {
//			String lib = separateString(call)[0];
//			String function = separateString(call)[1];
//			
//			System.out.println("Calling method \"" + function + "\" from \"" + lib + "\"");
		} else {
			if (args != null) {
				String fullName = call + "(" + getRealFunctionName(args, parentFunction) + ")";
				
				if (YalToJvm.getModule().functionExists(fullName)) {
					if (isCondition) {
						if (YalToJvm.getModule().getReturnVarFunction(fullName) != null) {
							if (YalToJvm.getModule().getReturnVarFunction(fullName) instanceof Array)
								YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: Function \"" + fullName + "\" returns an array! Invalid call of the method!");
						} else
							YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: Function \"" + fullName + "\" has no return variable! Invalid call of the method!");
					}
				} else
					YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: Function \"" + fullName + "\" hasn't been declared!");
			} else {
				String fullName = call + "()";
				
				if (YalToJvm.getModule().functionExists(fullName)) {
					if (isCondition) {
						if (YalToJvm.getModule().getReturnVarFunction(fullName) != null) {
							if (YalToJvm.getModule().getReturnVarFunction(fullName) instanceof Array)
								YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: Function \"" + fullName + "\" returns an array! Invalid call of the method!");
						} else
							YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: Function \"" + fullName + "\" has no return variable! Invalid call of the method!");
					}
				} else
					YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: Function \"" + fullName + "\" hasn't been declared!");
			}
		}
	}

	/**
	 * Method used to process the attributes of a module
	 */
	public void getAttributes() {
		for (int i = 0; i < children.length; i++) {
			SimpleNode node = (SimpleNode) this.jjtGetChild(i);

			if (node.id == YalToJvmTreeConstants.JJTFUNCTION) // Function found. No more module attributes, stop loop
				break;
			if (node.id == YalToJvmTreeConstants.JJTGLOBAL) { // Attribute found

				int globalChildrenNum = node.jjtGetNumChildren();
				SimpleNode lhs = (SimpleNode) node.jjtGetChild(0);
				String varName = lhs.ID;
				Variable var = new Variable(varName);

				// DECLARATION --> LHS;
				if (globalChildrenNum == 1) {
					var = new Scalar(varName);
					if (!YalToJvm.getModule().addGlobalVariable(var)) {
						// ERROR: Repeated declaration
						YalToJvm.semanticErrorMessages.add("[ Module - " + YalToJvm.getModule().getModuleID() + " ]: Attribute \"" + varName + "\" is already declared!");
					}

					// ASSIGNMENT --> LHS = RHS;
				} else if (globalChildrenNum == 2) {
					SimpleNode rhs = (SimpleNode) node.jjtGetChild(1);
					int rhsChildrenNum = rhs.jjtGetNumChildren();

					// RHS = SCALAR
					if(rhsChildrenNum == 0){
						int value = Integer.parseInt(rhs.ID);
						var = new Scalar(varName, value);

						// RHS = ARRAY
					} else if(rhsChildrenNum == 1){
						SimpleNode arraySize = (SimpleNode) rhs.jjtGetChild(0);
						int size = Integer.parseInt(arraySize.ID);
						var = new Array(varName, size);
					}

					/*
					 * RHS = ATTRIBUTE else { String valueName = valueNode.ID;
					 * Variable var2 = new Variable(valueName);
					 * 
					 * if(YalToJvm.getModule().checkGlobalVariable(valueName) &&
					 * ((YalToJvm.getModule().getGlobalVariable(valueName).
					 * getType().equals("scalar") && ((Scalar)
					 * YalToJvm.getModule().getGlobalVariable(valueName)).
					 * getValue() != null) ||
					 * YalToJvm.getModule().getGlobalVariable(valueName).getType
					 * ().equals("array"))) var2 =
					 * YalToJvm.getModule().getGlobalVariable(valueName); else
					 * YalToJvm.semanticErrorMessages.add("Attribute " +
					 * valueName + " is not assigned to a value!"); // ERROR:
					 * RHS NOT DECLARED/ASSIGNED
					 * 
					 * // SCALAR ATTRIBUTE if (valueNode.id ==
					 * YalToJvmTreeConstants.JJTSCALARACCESS){
					 * if(YalToJvm.getModule().getGlobalVariable(varName).
					 * getType().equals("array"))
					 * YalToJvm.semanticErrorMessages.add(
					 * "Attribute type mismatch! Attribute " + varName +
					 * " is an Array and attribute " + valueName +
					 * " is a Scalar."); else{ int value = ((Scalar)
					 * var2).getValue(); var = new Scalar(varName, value); }
					 * 
					 * // ARRAY ATTRIBUTE } else if (valueNode.id ==
					 * YalToJvmTreeConstants.JJTARRAYACCESS){
					 * if(YalToJvm.getModule().getGlobalVariable(varName).
					 * getType().equals("scalar"))
					 * YalToJvm.semanticErrorMessages.add(
					 * "Attribute type mismatch! Attribute " + varName +
					 * " is an Scalar and attribute " + valueName +
					 * " is a Array."); else{ int size = ((Array)
					 * var2).getSize(); var = new Array(varName, size); } } }
					 */

					if (!YalToJvm.getModule().addGlobalVariable(var)) {
						YalToJvm.semanticErrorMessages.add("[ Module - " + YalToJvm.getModule().getModuleID() + " ]: Attribute \"" + varName + "\" cannot be reassigned!");
					}
				}
			}
		}
	}

	public void processCondition(SimpleNode lhs, SimpleNode rhs, Function parentFunction) {
		if (lhs.getId() == YalToJvmTreeConstants.JJTARRAYACCESS) {
			SimpleNode index = (SimpleNode)lhs.jjtGetChild(0);
			processArrayAccess(lhs.ID, index.ID, parentFunction);
		} else if (lhs.getId() == YalToJvmTreeConstants.JJTSCALARACCESS) {
			processScalarAccess(lhs.ID, parentFunction);
		}

		if (rhs.jjtGetNumChildren() == 1) {
			SimpleNode rhsChild = (SimpleNode)rhs.jjtGetChild(0);

			if (rhsChild.getId() == YalToJvmTreeConstants.JJTTERM) {
				if (rhsChild.ID == null) {
					SimpleNode termChild = (SimpleNode)rhsChild.jjtGetChild(0);

					if (termChild.getId() == YalToJvmTreeConstants.JJTCALL) {
						processCall(termChild.ID, termChild.jjtGetChildren(), true, parentFunction);
					} else if (termChild.getId() == YalToJvmTreeConstants.JJTARRAYACCESS) {
						SimpleNode index = (SimpleNode)termChild.jjtGetChild(0);
						processArrayAccess(termChild.ID, index.ID, parentFunction);
					} else if (termChild.getId() == YalToJvmTreeConstants.JJTSCALARACCESS) {
						processScalarAccess(termChild.ID, parentFunction);
					}
				}
			} else if (rhsChild.getId() == YalToJvmTreeConstants.JJTARRAYSIZE) {
				YalToJvm.semanticErrorMessages.add("[ Function - " + parentFunction + " ]: Invalid placement of TODO!");
			}
		} else if (rhs.jjtGetNumChildren() == 2) {
			SimpleNode termLeft = (SimpleNode)rhs.jjtGetChild(0);
			SimpleNode termRight = (SimpleNode)rhs.jjtGetChild(1);

			if (termLeft.ID == null) {
				SimpleNode termChild = (SimpleNode)termLeft.jjtGetChild(0);

				if (termChild.getId() == YalToJvmTreeConstants.JJTCALL) {
					processCall(termChild.ID, termChild.jjtGetChildren(), true, parentFunction);
				} else if (termChild.getId() == YalToJvmTreeConstants.JJTARRAYACCESS) {
					SimpleNode index = (SimpleNode)termChild.jjtGetChild(0);
					processArrayAccess(termChild.ID, index.ID, parentFunction);
				} else if (termChild.getId() == YalToJvmTreeConstants.JJTSCALARACCESS) {
					processScalarAccess(termChild.ID, parentFunction);
				}
			}

			if (termRight.ID == null) {
				SimpleNode termChild = (SimpleNode)termRight.jjtGetChild(0);

				if (termChild.getId() == YalToJvmTreeConstants.JJTCALL) {
					processCall(termChild.ID, termChild.jjtGetChildren(), true, parentFunction);
				} else if (termChild.getId() == YalToJvmTreeConstants.JJTARRAYACCESS) {
					SimpleNode index = (SimpleNode)termChild.jjtGetChild(0);
					processArrayAccess(termChild.ID, index.ID, parentFunction);
				} else if (termChild.getId() == YalToJvmTreeConstants.JJTSCALARACCESS) {
					processScalarAccess(termChild.ID, parentFunction);
				}
			}
		}
	}

	public void processAssignement(SimpleNode lhs, SimpleNode rhs, Function parentFunction) {
		//VARS
		boolean twoSides = false;
		String lhsId = null;
		String lhsType = null;
		String lhsAccess = null;
		String lhsArrayIndexId = null;
		String lhsArrayAccess = null;

		//LHS
		if(lhs.getId() == YalToJvmTreeConstants.JJTARRAYACCESS) {
			lhsAccess = "array";
			lhsId = lhs.ID;
			SimpleNode arrayIndex = (SimpleNode)(lhs.jjtGetChild(0));
			try{
				Integer.parseInt(arrayIndex.ID);
			} catch (NumberFormatException e) {
				lhsArrayIndexId  = arrayIndex.ID;
			}
		} else if(lhs.getId() == YalToJvmTreeConstants.JJTSCALARACCESS) {
			if(dot(lhs.ID)){
				lhsAccess = "size";
				lhsId = separateString(lhs.ID)[0];
			}else{
				lhsAccess = "scalar";
				lhsId = lhs.ID;
			}
		}

		//RHS 1
		String rhs1Id = null;
		String rhs1Type = null;
		String rhs1Access = null;
		String rhs1ArrayIndexId = null;
		String[] argumentTypes = null;
		SimpleNode rhsChild = (SimpleNode)rhs.jjtGetChild(0);
		if (rhsChild.getId() == YalToJvmTreeConstants.JJTTERM) {
			if (rhsChild.ID != null) {
				rhs1Id = rhsChild.ID;
				rhs1Access = "integer";
			} else {
				SimpleNode termChild = (SimpleNode)rhsChild.jjtGetChild(0);
				if (termChild.getId() == YalToJvmTreeConstants.JJTCALL) {
					rhs1Access = "call";
					rhs1Id = termChild.ID;
					System.out.println(((SimpleNode)termChild.jjtGetChild(0)).ID);
				} else if (termChild.getId() == YalToJvmTreeConstants.JJTARRAYACCESS) {
					rhs1Access = "array";
					rhs1Id = termChild.ID;
					SimpleNode arrayIndex = (SimpleNode)(termChild.jjtGetChild(0));
					try{
						Integer.parseInt(arrayIndex.ID);
					} catch (NumberFormatException e) {
						rhs1ArrayIndexId = arrayIndex.ID;
					}
				} else if (termChild.getId() == YalToJvmTreeConstants.JJTSCALARACCESS) {
					if(dot(termChild.ID)){
						rhs1Access = "size";
						rhs1Id = separateString(termChild.ID)[0];
					}else{
						rhs1Access = "scalar";
						rhs1Id = termChild.ID;
					}
				} 
			}
		} else if (rhsChild.getId() == YalToJvmTreeConstants.JJTARRAYSIZE) {

		}

		// RHS 2
		String rhs2Id = null;
		String rhs2Type = null;
		String rhs2Access = null;
		String rhs2ArrayIndexId = null;
		if (rhs.jjtGetNumChildren() == 2) {
			twoSides = true;
			SimpleNode rhs2Child = (SimpleNode) rhs.jjtGetChild(1);
			if (rhs2Child.getId() == YalToJvmTreeConstants.JJTTERM) {
				if (rhs2Child.ID != null) {
					rhs2Id = rhs2Child.ID;
					rhs2Access = "integer";
				} else {
					SimpleNode termChild = (SimpleNode) rhs2Child.jjtGetChild(0);
					if (termChild.getId() == YalToJvmTreeConstants.JJTCALL) {

					} else if (termChild.getId() == YalToJvmTreeConstants.JJTARRAYACCESS) {
						rhs2Access = "array";
						rhs2Id = termChild.ID;
						rhs2ArrayIndexId = ((SimpleNode) termChild.jjtGetChild(0)).ID;
					} else if (termChild.getId() == YalToJvmTreeConstants.JJTSCALARACCESS) {
						if (dot(termChild.ID)) {
							rhs2Access = "size";
							rhs2Id = separateString(termChild.ID)[0];
						} else {
							rhs2Access = "scalar";
							rhs2Id = termChild.ID;
						}
					}
				}
			} else if (rhs2Child.getId() == YalToJvmTreeConstants.JJTARRAYSIZE) {

			}
		}

		/**********************************
		 * ERROR HANDLING
		 **********************************/
		// LEFT HAND SIDE
		boolean newVariable = false;
		if (!checkVariable(parentFunction, lhsId)) {
			newVariable = true;
			// ERROR: If the left hand side of a new variable is not a scalar
			// type
			if (!lhsAccess.equals("scalar")) {
				YalToJvm.semanticErrorMessages.add("[Function-" + parentFunction + "] "
						+ "Left hand side of assignement for new variable: " + lhsId + " must be of a scalar type");
			}
		} else {
			Variable v = getVariable(parentFunction, lhsId);
			switch (lhsAccess) {
			case "array":
				// ERROR: If the variable on the left hand side is of array
				// access but is not an array type
				if (!v.getType().equals("array")) {
					YalToJvm.semanticErrorMessages.add("[Function-" + parentFunction + "] "
							+ "The variable on the left hand side of assignement for variable: " + lhsId
							+ " is not an array type");
				} else {
					if (lhsArrayIndexId != null) {
						// ERROR: If the index of the left hand side of the
						// assignment does not exist
						if (!checkVariable(parentFunction, lhsArrayIndexId)) {
							YalToJvm.semanticErrorMessages
							.add("[Function-" + parentFunction + "] " + "Variable " + lhsArrayIndexId
									+ " in the index from left hand side of assignement for variable: " + lhsId
									+ " does not exist");
						} else {
							Variable index = getVariable(parentFunction, lhsArrayIndexId);
							// ERROR: If the variable type used for the index is
							// not a scalar
							if (!index.getType().equals("scalar")) {
								YalToJvm.semanticErrorMessages.add("[Function-" + parentFunction + "] "
										+ "The index used for the variable in the left hand for variable: " + lhsId
										+ " is not a scalar");
							}
						}
					}
				}
				break;
			case "scalar":
				// ERROR: If the left hand side variable is not a scalar
				// variable
				if (!v.getType().equals("scalar")) {
					YalToJvm.semanticErrorMessages.add("[Function-" + parentFunction + "] "
							+ "Left hand side of assignement for variable: " + lhsId + " is not a scalar variable");
				}
				break;
			default:
				break;
			}
		}

		// FIRST RIGHT HAND SIDE
		if (!rhs1Access.equals("integer")) {
			// ERROR: If the variable doesn't exist
			if (!checkVariable(parentFunction, rhs1Id)) {
				YalToJvm.semanticErrorMessages.add("[Function-" + parentFunction + "] " + "Variable " + rhs1Id
						+ " from right hand side of assignement for variable: " + lhsId + " does not exist");
			} else {
				rhs1Type = getVariable(parentFunction, rhs1Id).getType();
				if (rhs1Access.equals("array")) {
					// ERROR: If the variable used for the index doesn't exist
					if (rhs1ArrayIndexId != null) {
						if (!checkVariable(parentFunction, rhs1ArrayIndexId)) {
							YalToJvm.semanticErrorMessages.add("[Function-" + parentFunction + "] " + "Variable "
									+ rhs1ArrayIndexId + " from right hand side of assignement for variable: " + lhsId
									+ " does not exist");
						} else {
							Variable v = getVariable(parentFunction, rhs1ArrayIndexId);
							// ERROR: If the variable type used for the index is
							// not a scalar
							if (!v.getType().equals("scalar")) {
								YalToJvm.semanticErrorMessages
								.add("[Function-" + parentFunction + "] " + "The index used for the variable "
										+ rhs1Id + " from right hand side of assignement for variable: " + lhsId
										+ " is not a scalar");
							}
						}
					}
				} else if (rhs1Access.equals("scalar")) {
					// ERROR: Check if the type is consistent with the type of
					// access
					if (rhs1Type.equals("array")) {
						YalToJvm.semanticErrorMessages.add("[Function-" + parentFunction + "] " + "Variable " + rhs1Id
								+ " from right hand side of assignement for variable: " + lhsId
								+ " is not a scalar variable");
					}
				} else if (rhs1Access.equals("size")) {
					Variable v = getVariable(parentFunction, rhs1Id);
					// ERROR: If the variable is not an array type
					if (!v.getType().equals("array")) {
						YalToJvm.semanticErrorMessages.add("[Function-" + parentFunction + "] " + "Variable " + rhs1Id
								+ " from right hand side of assignement for variable: " + lhsId
								+ " is not an array variable");
					}
				}
			}
		}

		// SECOND RIGHT HAND SIDE
		if (twoSides) {
			if (!rhs2Access.equals("integer")) {
				// ERROR: If the variable doesn't exist
				if (!checkVariable(parentFunction, rhs2Id)) {
					YalToJvm.semanticErrorMessages.add("[Function-" + parentFunction + "] " + "Variable " + rhs2Id
							+ " from right hand side of assignement for variable: " + lhsId + " does not exist");
				} else {
					rhs2Type = getVariable(parentFunction, rhs2Id).getType();
					if (rhs2Access.equals("array")) {
						// ERROR: If the variable used for the index doesn't
						// exist
						if (rhs2ArrayIndexId != null) {
							if (!checkVariable(parentFunction, rhs2ArrayIndexId)) {
								YalToJvm.semanticErrorMessages.add("[Function-" + parentFunction + "] " + "Variable "
										+ rhs2ArrayIndexId + " from right hand side of assignement for variable: "
										+ lhsId + " does not exist");
							} else {
								Variable v = getVariable(parentFunction, rhs2ArrayIndexId);
								// ERROR: If the variable type used for the
								// index is not a scalar
								if (!v.getType().equals("scalar")) {
									YalToJvm.semanticErrorMessages.add(
											"[Function-" + parentFunction + "] " + "The index used for the variable "
													+ rhs2Id + " from right hand side of assignement for variable: "
													+ lhsId + " is not a scalar");
								}
							}
						}
					} else if (rhs2Access.equals("scalar")) {
						// ERROR: Check if the type is consistent with the type
						// of access
						if (rhs2Type.equals("array")) {
							YalToJvm.semanticErrorMessages.add("[Function-" + parentFunction + "] " + "Variable "
									+ rhs2Id + " from right hand side of assignement for variable: " + lhsId
									+ " is not a scalar variable");
						}
					} else if (rhs2Access.equals("size")) {
						Variable v = getVariable(parentFunction, rhs2Id);
						// ERROR: If the variable is not an array type
						if (!v.getType().equals("array")) {
							YalToJvm.semanticErrorMessages.add("[Function-" + parentFunction + "] " + "Variable "
									+ rhs2Id + " from right hand side of assignement for variable: " + lhsId
									+ " is not an array variable");
						}
					}
				}
			}
		}

		// Add a new local variable
		if (newVariable) {
			parentFunction.addVariable(new Scalar(lhsId));
		}

	}

	private Variable getVariable(Function f, String id) {
		if (f.getReturnVar() != null && f.checkReturnVariable(id)) {
			return f.getReturnVar();
		} else if (f.checkParams(id)) {
			return f.getParameter(id);
		} else if (f.checkLocalVariable(id)) {
			return f.getVariable(id);
		} else if (YalToJvm.getModule().checkGlobalVariable(id)) {
			return YalToJvm.getModule().getGlobalVariable(id);
		} else {
			return null;
		}
	}

	private boolean checkVariable(Function f, String id) {
		if (f.getReturnVar() != null && f.checkReturnVariable(id)) {
			return true;
		} else if (f.checkParams(id)) {
			return true;
		} else if (f.checkLocalVariable(id)) {
			return true;
		} else if (YalToJvm.getModule().checkGlobalVariable(id)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Method used to process the body of a function, a while loop and an
	 * if/else statement
	 */
	public void processBody(Function parentFunction) {
		for (int i = 0; i < children.length; i++) {
			SimpleNode bodyChild = (SimpleNode) children[i];
			switch (bodyChild.getId()) {
			case YalToJvmTreeConstants.JJTASSIGNEMENT:
				SimpleNode lhs = (SimpleNode)bodyChild.jjtGetChild(0); //ArrayAccess or ScalarAccess
				SimpleNode rhs = (SimpleNode)bodyChild.jjtGetChild(1); //Rhs
				processAssignement(lhs, rhs, parentFunction);
				break;
			case YalToJvmTreeConstants.JJTWHILE:
				SimpleNode whileCondition = (SimpleNode) bodyChild.jjtGetChild(0);
				SimpleNode whileLhs = (SimpleNode) whileCondition.jjtGetChild(0);
				SimpleNode whileRhs = (SimpleNode) whileCondition.jjtGetChild(1);
				processCondition(whileLhs, whileRhs, parentFunction);

				SimpleNode whileBody = (SimpleNode) bodyChild.jjtGetChild(1);
				whileBody.processBody(parentFunction);
				break;
			case YalToJvmTreeConstants.JJTIF:
				SimpleNode ifCondition = (SimpleNode) bodyChild.jjtGetChild(0);
				SimpleNode ifLhs = (SimpleNode) ifCondition.jjtGetChild(0);
				SimpleNode ifRhs = (SimpleNode) ifCondition.jjtGetChild(1);
				processCondition(ifLhs, ifRhs, parentFunction);

				SimpleNode ifBody = (SimpleNode) bodyChild.jjtGetChild(1);
				ifBody.processBody(parentFunction);

				if (bodyChild.jjtGetNumChildren() == 3) {
					SimpleNode elseBody = (SimpleNode) bodyChild.jjtGetChild(2);
					elseBody.processBody(parentFunction);
				}
				break;
			case YalToJvmTreeConstants.JJTCALL:
				processCall(bodyChild.ID, bodyChild.jjtGetChildren(), false, parentFunction);
				break;
			}
		}
	}

	public int getId() {
		return id;
	}
}

/*
 * JavaCC - OriginalChecksum=45cdfa7b6c656a79cec0656f2db154c7 (do not edit this
 * line)
 */
