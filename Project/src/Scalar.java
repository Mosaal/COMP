/** This Class shall be used to store information about each Integer type */

public class Scalar extends Variable {
	private int value;

	public Scalar(String id, int v) {
		super(id);
		value = v;
	}

	public Scalar(String id){
		super(id);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int v) {
		value = v;
	}

	@Override
	public boolean equals(Object other) {
		if (value == ((Scalar)other).getValue())
			return true;
		return false;
	}
	
	public String toString(){
		return variableID + " = " + value;
	}
}
