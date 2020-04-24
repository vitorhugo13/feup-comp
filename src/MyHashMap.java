import descriptors.*;
import java.util.HashMap;
import java.util.ArrayList;

public class MyHashMap{

    //TODO: deal with overload of functions (aka, change descriptor to ArrayList<Descriptor>);
    
    private HashMap<String, Descriptor> myHash;
    private MyHashMap fatherHash;
    private MyHashMap childHash;

    public MyHashMap(MyHashMap father){

        myHash = new HashMap<>();
        this.fatherHash = father;

    }

    public MyHashMap getFather(){
        return this.fatherHash;
    }

    public MyHashMap getChild(){
        return this.childHash;
    }

    public void setChild(MyHashMap child){
        this.childHash = child;
    }

    public void add(String s, Descriptor d){
        //TODO
        if(exists(s)) {
            this.myHash.put(s, d);
        }
        else{

            this.myHash.put(s, d);
        }
    }

    public Boolean exists(String s){   
        return myHash.containsKey(s);
    }

    public Descriptor getDescriptor(String s){

        if(exists(s))
            return myHash.get(s);

        return null;
    }

    public HashMap<String, Descriptor> getHash(){
        return this.myHash;
    }
}