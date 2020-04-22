import descriptors.*;

import java.util.Stack;
import java.util.ArrayList;


MyHashTab-> Father

HashMap<a, int>


public class SymbolTable{

    private Stack<MyHashMap> stack;
    private ArrayList<MyHashMap> all_hashes;
 
    public SymbolTable() {

        MyHashMap firstHash = new MyHashMap(null);

        stack = new Stack<MyHashMap>();
        all_hashes = new ArrayList<MyHashMap>();

        all_hashes.add(firstHash);
        stack.push(firstHash);

    }

    
    public void enterScope() {

        MyHashMap myHash = new MyHashMap(stack.peek());
        stack.push(myHash);
        all_hashes.add(myHash);
        System.out.println("Entered scope: " + stack.peek());

    }

    public void exitScope() {
        System.out.println("Exit scope: " + stack.peek());

        if (stack.empty()) {
            System.err.println("existScope: can't remove scope from an empty symbol table.");
        }

        stack.pop();
    }


    public void add(String id, Descriptor info) {

        if (stack.empty()) {
            System.err.println("ADD: can't add a symbol without a scope.");
        }

        MyHashMap my_hash = stack.peek();

        // do{

        //     if(my_hash.exists(id)){
        //         System.err.println("Duplicated variable: " + id + " in: " + my_hash);
        //         return;
        //     }

        //     my_hash = my_hash.getFather();

        // }while(my_hash != null);
        
        if(stack.peek().exists(id)){
            System.err.println("Duplicated variable: " + id + " in: " + my_hash);
            return;
        }

        //System.out.println("Added var: " + id + " to table: " + stack.peek());
        (stack.peek()).add(id, info);

    }

  
    public Descriptor lookup(String sym) {

        if (stack.empty()) {
            System.err.println("LOOKUP: no scope in symbol table.");
        }

        for(int i = 0; i < all_hashes.size(); i++){
            if(all_hashes.get(i).getDescriptor(sym) != null){
                return all_hashes.get(i).getDescriptor(sym);
            }
        }
        

        return null;
    }

    public String toString() {

        String result = "";

        for (int i = stack.size() - 1, j = 0; i >= 0; i--, j++) {
            result += "Scope " + j + ": " + stack.elementAt(i) + "\n";
        }

        return result;
    }

}




       
