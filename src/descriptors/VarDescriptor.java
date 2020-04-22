package descriptors;

public class VarDescriptor extends Descriptor {

    protected String dataType;

    public VarDescriptor(String dataType, String identifier){
        this.type=Type.VAR;

        //this.identifier=identifier;
        this.dataType=dataType;

    }

    public String getDataType(){
        return this.dataType;
    }
    
    public void setDataType(String data){
        this.dataType = data;
    }

    // public String getIdentifier(){
    //     return this.identifier;
    // }


    // public void setIdentifier(String value){
    //     this.identifier = value;
    // }

}