package descriptors;

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

    public ArrayList<MethodDescriptor> getMethods(String id){
        ArrayList<MethodDescriptor> methodsMatchingId = new ArrayList<>();
        if(existsMethod(id)){
            for(int i=0; i<methods.size();i++){
                if(methods.get(i).getIdentifier().equals(id))
                    methodsMatchingId.add(methods.get(i));
            }
            return methodsMatchingId;
        }
        return null;
    }

    public void addMethod(MethodDescriptor method){
        methods.add(method);
    }

}