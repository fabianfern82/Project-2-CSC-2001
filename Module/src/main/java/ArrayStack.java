public class ArrayStack {

    //Private variables for keeping track of each element and where the stack is
    private String[] elements;
    private int count;

    public ArrayStack() {
        elements = new String[4];
        count = 0;
    }

    //Returns an empty stack
    public static ArrayStack empty_stack() {
        return new ArrayStack();
    }

    //Takes a String and adds to the stack
    public void push(String s) {
        if (count >= elements.length) {
            String[] larger = new String[elements.length * 2];

            for(int i = 0; i < count; i++) {
                larger[i] = elements[i];
            }
            elements = larger;
        }
        elements[count++] = s;
    }

    //Grabs the element at the top to return and remove it
    public String pop() {
        if (is_empty()) {
            throw new IndexOutOfBoundsException("There is no element to pop.");
        }
        count--;
        String value = elements[count];
        elements[count] = null;
        return value;
    }

    //Returns the top element
    public String peek() {
        if (is_empty()) {
            throw new IndexOutOfBoundsException("There is no element to peek");
        }
        return elements[count - 1];
    }

    //Checks to see if the stack is empty or not
    public boolean is_empty() {
        return count == 0;
    }

    //Returns the size of the stack
    public int size() {
        return count;
    }




}
