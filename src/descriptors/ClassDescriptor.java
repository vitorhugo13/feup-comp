package descriptors;

import java.io.IOException;
import java.util.ArrayList;

public class ClassDescriptor extends Descriptor {
    ArrayList<MethodDescriptor> methods;

    public ClassDescriptor(String identifier){
        this.type=Type.CLASS;
        this.identifier=identifier;
        methods = new ArrayList<>();
    }

    private Boolean existsMethod(String id){
        for(int i=0; i<methods.size();i++){
            if(methods.get(i).getIdentifier().equals(id))
                return true;
        }
        return false;
    }

    public ArrayList<MethodDescriptor> getMethodsMatchingId(String id) throws IOException{
        ArrayList<MethodDescriptor> methodsMatchingId = new ArrayList<>();
        if(existsMethod(id)){
            for(int i=0; i<methods.size();i++){
                if(methods.get(i).getIdentifier().equals(id))
                    methodsMatchingId.add(methods.get(i));
            }
            return methodsMatchingId;
        }
        throw new IOException("No Method in class " + this.identifier + " matches method named " + id);
    }

    public void addMethod(MethodDescriptor method){
        methods.add(method);
    }

    public ArrayList<MethodDescriptor> getMethods(){
        return this.methods;
    }

}