//This file has custom stack class which implement stack data structure 
public class CustomStack {
    private final Game.GameState[] stack;
    private int top;
    private final int maxSize;

    // Constructor to initialize the stack with a specific size
    public CustomStack(int size) {
        stack = new Game.GameState[size];  
        top = -1;  
        maxSize = size;
    }

    // insertIntoStack method to insert an item to the top of stack
    public void insertIntoStack(Game.GameState state) {
        if (top < maxSize - 1) {
            top = top + 1;
            stack[top] = state;
        } 
    }

    // removeFromStack method to remove and return the top item from the stack
    public Game.GameState removeFromStack() {
        if (!isStackEmpty()) {
            top = top - 1;
            return stack[top];
        } else {
            return null; 
        }
    }

    // isEmpty method to check if the stack is empty
    public boolean isStackEmpty() {
        return top == -1;
    }
}

