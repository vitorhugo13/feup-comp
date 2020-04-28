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

    private Boolean possibleToOverload(MethodDescriptor method){

        Boolean argsAllSame = true;
        ArrayList<VarDescriptor> method_parameters = method.getParameters();
        ArrayList<VarDescriptor> parameters = new ArrayList<>();

        for(int i = 0; i < this.methods.size(); i++){
          

            if(this.methods.get(i).getIdentifier().equals(method.getIdentifier())){
                argsAllSame = true;
                parameters = this.methods.get(i).getParameters();
                
                if(method_parameters.size() == parameters.size()){
                
                    for(int j = 0; j < parameters.size(); j++){
                        if(!parameters.get(j).getDataType().equals(method_parameters.get(j).getDataType())){
                            argsAllSame = false;
                        }
                    }
                
                    if(argsAllSame){
                        return false;
                    }
                }
            }
        }

        return true;
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

    //TODO: dar throw de um warning/exception se ocorrer overload
    public void addMethod(MethodDescriptor method){

        if(possibleToOverload(method))
            methods.add(method);
           
    }

    public ArrayList<MethodDescriptor> getMethods(){
        return this.methods;
    }

}