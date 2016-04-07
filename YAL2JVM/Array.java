/** This Class shall be used to store information about each array type */

public class Array extends Variable {
  private int reference;
  private int[] array;
  private int size;

  public Array(String variableID,int size,int ref) {
    super(variableID);
    array = new int[size];
    reference = ref;
  }

  public int get(int index) {
    return array[index];
  }

  public int getReference(){
    return reference;
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
    if (reference == ((Array) other).getReference())
     return true;
   return false;
  }
}
