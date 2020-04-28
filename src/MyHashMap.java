import descriptors.*;
import java.util.HashMap;
import java.util.ArrayList;

public class MyHashMap{
    
    private HashMap<String, ArrayList<Descriptor>> myHash;
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
        if(exists(s)) {
            ArrayList descriptors = myHash.get(s);
            descriptors.add(d);
            this.myHash.put(s, descriptors);
        }
        else{
            ArrayList descriptors = new ArrayList<>();
            descriptors.add(d);
            this.myHash.put(s, descriptors);
        }
    }

    public Boolean exists(String s){   
        return myHash.containsKey(s);
    }

    public ArrayList<Descriptor> getDescriptor(String s){

        if(exists(s))
            return myHash.get(s);

        return null;
    }

    public HashMap<String, ArrayList<Descriptor>> getHash(){
        return this.myHash;
    }
}