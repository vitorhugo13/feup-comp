package descriptors;

public class VarDescriptor extends Descriptor {

    protected String dataType;
    protected String initialValue;

    public VarDescriptor(String dataType, String initialValue){
        this.type=Type.VAR;

        this.dataType=dataType;
        this.initialValue=initialValue;

    }

    public String get_dataType(){
        return this.dataType;
    }

    public String get_initialValue(){
        return this.initialValue;
    }

    public void set_dataType(String data){
        this.dataType = data;
    }

    public void set_initialValue(String value){
        this.initialValue = value;
    }

}