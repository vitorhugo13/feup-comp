import descriptors.*;

import java.util.Stack;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Symbol Table
 *
 * This class builds a symbol table that can be used for a semantic analysis or/and to generate code.
 * It will store the symbols in several hash maps representing scopes, connected to each other.
 * There are several extra data structures such as a stack and an ArrayList that are used to make the code cleaner and more efficient.
 */
public class SymbolTable{

    private Stack<MyHashMap> stack;
    private ArrayList<MyHashMap> all_hashes;
    private int posArrayForAnalysis;
    private String className;

    public void setClassName(String name){
        this.className = name;
    }

    public String getClassName(){
        return this.className;
    }

    /**
     * Should be called after every analysis to reset the table.
     */
    public void reset() {
        this.posArrayForAnalysis = 0;
    }

    public SymbolTable() {

        MyHashMap firstHash = new MyHashMap(null);

        stack = new Stack<MyHashMap>();
        all_hashes = new ArrayList<MyHashMap>();
        stack.push(firstHash);
        posArrayForAnalysis=0;

    }

    /**
     * Should be called when building the symbol table to create and enter a new scope.
     */
    public void enterScope() {

        MyHashMap myHash = new MyHashMap(stack.peek());
        stack.push(myHash);
        all_hashes.add(myHash);
       // System.out.println("[SCOPE] Entered scope: " + stack.peek());

    }

    /**
     * After the table being build, this function should be called during an analysis, to enter a new scope.
     */
    public void enterScopeForAnalysis() {
        
        stack.push(all_hashes.get(posArrayForAnalysis));
        posArrayForAnalysis++;
        // System.out.println("[SCOPE] Enter scope for analysis: " + stack.peek());
    }

    /**
     * @return ArrayList of VarDescriptors representing the class attributes
     */
    public ArrayList<VarDescriptor> getClassAtributes(){
        ArrayList<VarDescriptor> variables = new ArrayList<>();
        for(ArrayList<Descriptor> value : all_hashes.get(1).getHash().values()){
            if(value.get(0).getType().equals(Descriptor.Type.VAR)){
                variables.add((VarDescriptor)value.get(0));
            }
        }
        return variables;
    }

    /**
     * Should be called to indicate leaving the current scope, when building the symbol table
     */
    public void exitScope() {
        //System.out.println("[SCOPE] Exit scope: " + stack.peek());
        if (stack.empty()) {
            System.err.println("[ERROR] [SCOPE] existScope: symbol table is empty.");
        }
        
        stack.pop();
    }

    /**
     * Should be called to indicate leaving the current scope, when doing an analysis after the table being built
     */
    public void exitScopeForAnalysis() {
        // System.out.println("[SCOPE] Exit scope for analysis: " + stack.peek());
        stack.pop();
    }

    /**
     * Adds a new symbol to the Symbol Table
     * @param id identifier of the descriptior to be added to the class. This will be used in the lookup function to search
     * @param info descriptor with the symbol information
     */
    public void add(String id, Descriptor info) {
        
        if (stack.empty()) {
            System.err.println("[ADD]: can't add a symbol without a scope.");
        }

        MyHashMap my_hash = stack.peek();

        if(info.getType().equals(Descriptor.Type.VAR)){
            ((VarDescriptor)info).setScope(getCurrentScope());
        }

        if(!info.getType().equals(Descriptor.Type.METHOD)) { // We allow repeated imports for method overloads as they are all in the same scope
            if (stack.peek().exists(id)) {
                System.err.println("[SEMANTIC ERROR] Duplicated variable: " + id + " in: " + my_hash);
                return;
            }
        }
        (stack.peek()).add(id, info);
    }

    /**
     * Adds a new symbol to the Symbol Table
     * should only be used for import statements as imported methods are stored inside the Class descriptors they belong,
     * therefore this will create a class if it doesn't exist already
     * @param id - name of the class to be added
     * @param info -
     * @param isImport - this is only to allow an overlad of the method add
     */
    public void add(String id, MethodDescriptor info, Boolean isImport) {
        if (stack.empty()) {
            System.err.println("[ADD]: can't add a symbol without a scope.");
        }
        if (!stack.peek().exists(id)) {
            ClassDescriptor classDescriptor = new ClassDescriptor(id);
            classDescriptor.addMethod(info);
            (stack.peek()).add(id, classDescriptor);
        }
        else{
            ((ClassDescriptor) stack.peek().getDescriptor(id).get(0)).addMethod(info);
            //System.out.println("Get descriptor: " + ((ClassDescriptor) stack.peek().getDescriptor(id).get(0)).getMethods());
        }

    }


    /**
     * Searches for a symbol in the symbol table
     * @param id - Identifier of the symbol to search
     * @return ArrayList<Descriptor> with the descriptors corresponding to the identifier given.
     * It will look for the closest descriptor:
     * 1. In the current scope
     * 2. In the parent scopes
     * 3. In the import scope
     * 4. If the class expands another, it will look in the methods of that class
     * @throws IOException
     */
    public ArrayList<Descriptor>  lookup(String id) throws IOException {

        if (stack.empty()) {
            System.err.println("[ERROR] [LOOKUP]: symbol table is empty.");
        }

        MyHashMap my_hash = stack.peek();
        do {
            if (my_hash.exists(id)) {
                return my_hash.getDescriptor(id);
            }
            my_hash = my_hash.getFather();
        } while (my_hash != null);

        if(all_hashes.get(0).exists(id)){ //Search imports
            return all_hashes.get(0).getDescriptor(id);
        }
        
        throw new IOException("Variable " + id + " is undefined");
            
    }


    public String toString() {

        String result = "";

        for (int i = stack.size() - 1, j = 0; i >= 0; i--, j++) {
            result += "Scope " + j + ": " + stack.elementAt(i) + "\n";
        }

        return result;
    }

    /**
     * Prints the table in a readable form
     */
    public void print_all(){

        for(int i = 0; i < all_hashes.size(); i++){
            System.out.println("\n======= SCOPE ========");
            all_hashes.get(i).getHash().entrySet().forEach(entry->{
                // System.out.println(entry.getKey());
                if(entry.getValue().get(0).getType().equals(Descriptor.Type.VAR))
                    System.out.println(entry.getKey() + " " + ((VarDescriptor)entry.getValue().get(0)).getDataType() + " " + ((VarDescriptor)entry.getValue().get(0)).getScope());  
                else if(entry.getValue().get(0).getType().equals(Descriptor.Type.CLASS)){
                    if(((ClassDescriptor)entry.getValue().get(0)).getParentClass()!= null)
                        System.out.println(entry.getKey() +" EXTENDS: " + ((ClassDescriptor)entry.getValue().get(0)).getParentClass().getIdentifier() + " " + ((ClassDescriptor)entry.getValue().get(0)).getMethods()); 
                    System.out.println(entry.getKey());

                    ((ClassDescriptor) entry.getValue().get(0)).getMethods().forEach(method ->
                        System.out.println(" " + method.getIdentifier()));
                }
            });

            //  all_hashes.get(i).getHash().entrySet().forEach(entry->{
            //     System.out.println(entry.getKey() + entry.getValue());
                 
            //  });

            // all_hashes.get(i).getHash().entrySet().forEach(entry->{
            //     System.out.println(entry.getKey());
            //     if(entry.getValue().get(0).getType().equals(Descriptor.Type.CLASS))
            //         System.out.println(" " + ((ClassDescriptor)entry.getValue().get(0)).getMethods());  
            //  });
        }
    }

    /**
     *
     * @return the current type of scope
     */
    public Descriptor.Scope getCurrentScope(){
        switch(stack.size()){
            case 1:
                return Descriptor.Scope.IMPORT;
            case 2:
                return Descriptor.Scope.GLOBAL;
            default:
                return Descriptor.Scope.LOCAL;
    
        }
    }
    
}


       
