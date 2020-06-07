package descriptors;

import java.util.ArrayList;

/**
 * Descriptor for methods that saves the method type, if it is static, return type and it's return parameters
 */
public class MethodDescriptor extends Descriptor {

    protected String identifier;
    protected String returnType;
    protected ArrayList<VarDescriptor> parameters;
    protected boolean isStatic;

    public MethodDescriptor(String identifier, String returnType, ArrayList<VarDescriptor> parameters, boolean isStatic){
        this.type=Type.METHOD;

        this.returnType=getParsedDataType(returnType);
        this.parameters=parameters;
        this.identifier=identifier;
        this.isStatic = isStatic;

    }

    public boolean isStatic() { return this.isStatic; }

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