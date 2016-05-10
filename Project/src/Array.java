/** This Class shall be used to store information about each array type */

public class Array extends Variable {
	private int ref;
	private int size;
	private int[] array;
	public static int REFERENCE;

	public Array(String variableID) {
		super(variableID);
		array = new int[0];
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
	
	public String toString(){
		String s = variableID + " = [";
		for (int i = 0; i < array.length; i++) {
			s += array[i];
			if(i < array.length-1)
				s += ", ";
		}
		s += "]"; 
		return s;
	}
}
