package descriptors;

public class VarDescriptor extends Descriptor {

    protected String dataType;
    protected Boolean isInitialized;
    protected String currValue;
    protected Scope scope;

    public VarDescriptor(String dataType, String identifier){
        this.type=Type.VAR;

        this.identifier = identifier;
        this.dataType = getParsedDataType(dataType);
        this.isInitialized = false;
        this.scope = null;
    }

    public String getDataType(){
        return this.dataType;
    }
    
    public void setDataType(String data){
        this.dataType = getParsedDataType(data);
    }

    public void setIdentifier(String value){
        this.identifier = value;
    }

    public void setInitialized(){
        this.isInitialized = true;
    }

    public void setCurrValue(String value){
        this.currValue = value;
    }

    public Boolean getInitialized(){
       return this.isInitialized;
    }

    public String getCurrValue(){
        return this.currValue;
    }

    public void setScope(Scope scope){
        this.scope = scope;
    }

    public Scope getScope(){
        return this.scope;
    }

}