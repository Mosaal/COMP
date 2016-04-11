/** This Class shall be used to store information about each array type */

public class Array extends Variable {
	private int ref;
	private int size;
	private int[] array;
	public static int REFERENCE;

	public Array(String variableID) {
		super(variableID);
		ref = REFERENCE;
		REFERENCE++;
	}

	public Array(String variableID,int size) {
		super(variableID);
		array = new int[size];
		ref = REFERENCE;
		REFERENCE++;
	}

	public int get(int index) {
		return array[index];
	}

	public int getReference(){
		return ref;
	}

	public int getSize(){
		return size;
	}

	public void setArray(int[] a){
		array = a;
	}

	public void set(int index, int value){
		array[index] = value;
	}

	@Override
	public boolean equals(Object other) {
		if (ref == ((Array)other).getReference())
			return true;
		return false;
	}
}
