package descriptors;

public class VarDescriptor extends Descriptor {

    protected String dataType;
    protected String identifier;


    public VarDescriptor(String dataType, String identifier){
        this.type=Type.VAR;

        this.identifier=identifier;
        this.dataType=dataType;

    }

    public String getDataType(){
        return this.dataType;
    }
    
    public void setDataType(String data){
        this.dataType = data;
    }

    public void setIdentifier(String value){
        this.identifier = value;
    }

}