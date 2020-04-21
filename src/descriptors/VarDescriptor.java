package descriptors;

public class VarDescriptor extends Descriptor {

    protected String dataType;
    protected String value;

    public VarDescriptor(String dataType){
        this.type=Type.VAR;

        this.dataType=dataType;

    }

    public String get_dataType(){
        return this.dataType;
    }

    public String getValue(){
        return this.value;
    }

    public void set_dataType(String data){
        this.dataType = data;
    }

    public void setValue(String value){
        this.value = value;
    }

}