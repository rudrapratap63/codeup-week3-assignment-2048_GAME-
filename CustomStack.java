public class CustomStack {
    private final Game2048.GameState[] stack;
    private int top;
    private final int maxSize;
    Constant constant = new Constant();

    // Constructor to initialize the stack with a specific size
    public CustomStack(int size) {
        stack = new Game2048.GameState[size];  
        top = -1;  
        maxSize = size;
    }

    // Push method to add an item to the stack
    public void push(Game2048.GameState state) {
        if (top < maxSize - 1) {
            stack[++top] = state;
        } else {
            System.out.println(constant.STACK_OVERFLOW_ERROR);
        }
    }

    // Pop method to remove and return the top item from the stack
    public Game2048.GameState pop() {
        if (!isEmpty()) {
            return stack[top--];
        } else {
            System.out.println(constant.STACK_OVERFLOW_ERROR);
            return null; 
        }
    }

    // isEmpty method to check if the stack is empty
    public boolean isEmpty() {
        return top == -1;
    }
}

