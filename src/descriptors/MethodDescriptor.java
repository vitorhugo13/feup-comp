package descriptors;

import java.util.ArrayList;

public class MethodDescriptor extends Descriptor {

    protected String returnType;
    protected ArrayList<Descriptor> parameters;

    public MethodDescriptor(String returnType, ArrayList<Descriptor> parameters){
        this.type=Type.METHOD;

        this.returnType=returnType;
        this.parameters=parameters;

    }

    public String get_returnType(){
        return this.returnType;
    }

    public ArrayList<Descriptor> get_parameters(){
        return this.parameters;
    }
    
}