import descriptors.*;

import java.util.Stack;
import java.util.ArrayList;
import java.io.IOException;


public class SymbolTable{

    private Stack<MyHashMap> stack;
    private ArrayList<MyHashMap> all_hashes;
    private int posArrayForAnalysis;

    public SymbolTable() {

        MyHashMap firstHash = new MyHashMap(null);

        stack = new Stack<MyHashMap>();
        all_hashes = new ArrayList<MyHashMap>();
        stack.push(firstHash);
        posArrayForAnalysis=0;

    }

    
    public void enterScope() {

        MyHashMap myHash = new MyHashMap(stack.peek());
        stack.push(myHash);
        all_hashes.add(myHash);
       // System.out.println("[SCOPE] Entered scope: " + stack.peek());

    }

    public void enterScopeForAnalysis() {
        
        stack.push(all_hashes.get(posArrayForAnalysis));
        posArrayForAnalysis++;
        System.out.println("[SCOPE] Enter scope for analysis: " + stack.peek());
    }

    public void exitScope() {
        //System.out.println("[SCOPE] Exit scope: " + stack.peek());
        if (stack.empty()) {
            System.err.println("[ERROR] [SCOPE] existScope: symbol table is empty.");
        }

        stack.pop();
    }

    public void exitScopeForAnalysis() {
        System.out.println("[SCOPE] Exit scope for analysis: " + stack.peek());
        stack.pop();
    }


    public void add(String id, Descriptor info) {
        
        if (stack.empty()) {
            System.err.println("[ADD]: can't add a symbol without a scope.");
        }

        MyHashMap my_hash = stack.peek();

        if(!info.getType().equals(Descriptor.Type.METHOD)) { // We allow repeated imports for method overloads as they are all in the same scope
            if (stack.peek().exists(id)) {
                System.err.println("[SEMANTIC ERROR] Duplicated variable: " + id + " in: " + my_hash);
                return;
            }
        }
        (stack.peek()).add(id, info);
    }

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
            System.out.println("Get descriptor: " + ((ClassDescriptor) stack.peek().getDescriptor(id).get(0)).getMethods());
        }

    }



    //TODO: search methods in import
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
        throw new IOException("Variable " + id + " was not declared");
            
    }


    public String toString() {

        String result = "";

        for (int i = stack.size() - 1, j = 0; i >= 0; i--, j++) {
            result += "Scope " + j + ": " + stack.elementAt(i) + "\n";
        }

        return result;
    }

    public void print_all(){

        for(int i = 0; i < all_hashes.size(); i++){
            System.out.println("\n======= SCOPE ========");
            all_hashes.get(i).getHash().entrySet().forEach(entry->{
                System.out.println(entry.getKey());
                if(entry.getValue().get(0).getType().equals(Descriptor.Type.VAR))
                    System.out.println(" " + ((VarDescriptor)entry.getValue().get(0)).getDataType());  
             });
        }
    }

        

}

       
