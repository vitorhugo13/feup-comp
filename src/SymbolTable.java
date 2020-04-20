import descriptors.*;

import java.util.Stack;
import java.util.HashMap;

@SuppressWarnings({"unchecked"})
public class SymbolTable{

    private Stack stack;

    //stack.peek() retrieves first element at top of the stack

    /** Creates an empty symbol table. */
    public SymbolTable() {

        stack = new Stack<HashMap>();

    }

    // Enters a new scope. 
    // A scope must be entered before anything can be added to the table.
    
    public void enterScope() {
        stack.push(new HashMap<String, Descriptor>());
    }

    /** Exits the most recently entered scope. */
    public void exitScope() {

        if (stack.empty()) {
            System.err.println("existScope: can't remove scope from an empty symbol table.");
        }

        stack.pop();
    }

    /** Adds a new entry to the symbol table.
     *
     * @param id the symbol
     * @param info the data asosciated with id
     * */
    public void add(String id, Descriptor info) {

        if (stack.empty()) {
            System.err.println("ADD: can't add a symbol without a scope.");
        }

        ((HashMap)stack.peek()).put(id, info);
    }

    /**
     * Looks up an item through all scopes of the symbol table.  If
     * found it returns the associated information field, if not it
     * returns NULL.
     *
     * @param sym the symbol
     * @return the info associated with sym, or null if not found
     * */
    public Descriptor lookup(String sym) {

        if (stack.empty()) {
            System.err.println("LOOKUP: no scope in symbol table.");
        }
       
        for (int i = stack.size() - 1; i >= 0; i--) {

            Object info = ((HashMap)stack.elementAt(i)).get(sym);
            if (info != null){
                return (Descriptor) info;
            }
        }

        return null;
    }

    /**
     * Examines the symbol table.  Check the top scope (only) for the
     * symbol SYM.  If found, return the information field.
     * If not return NULL.
     *
     * @param sym the symbol
     * @return the info associated with sym, or null if not found
     * */
    public Object examine(String sym) {

        if (stack.empty()) {
            System.err.println("lookup: no scope in symbol table.");
        }

        return ((HashMap)stack.peek()).get(sym);
    }

    /** Gets the string representation of the symbol table.
     *
     * @return the string rep
     * */
    public String toString() {

        String result = ""; 
        
        for (int i = stack.size() - 1, j = 0; i >= 0; i--, j++) {
            result += "Scope " + j + ": " + stack.elementAt(i) + "\n";
        }

        return result;
    }

}