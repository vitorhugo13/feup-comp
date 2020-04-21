package descriptors;

import java.util.ArrayList;

public class MethodDescriptor extends Descriptor {

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

    public ArrayList<VarDescriptor> getParameters(){
        return this.parameters;
    }
    
}