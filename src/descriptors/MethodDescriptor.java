package descriptors;

import java.util.ArrayList;

public class MethodDescriptor extends Descriptor {

    protected String identifier;
    protected String returnType;
    protected ArrayList<VarDescriptor> parameters;

    public MethodDescriptor(String identifier, String returnType, ArrayList<VarDescriptor> parameters){
        this.type=Type.METHOD;

        this.returnType=returnType;
        this.parameters=parameters;
        this.identifier=identifier;

    }

    public String getReturnType(){
        return this.returnType;
    }

    public int getNumParameters(){
        if(this.identifier.equals("main")){
            return parameters.size();
        }
        return parameters.size()+1;
    }

    public ArrayList<VarDescriptor> getParameters(){
        return this.parameters;
    }

    public String getIdentifier(){
        return this.identifier;
    }
    
}