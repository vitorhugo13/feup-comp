package descriptors;

/**
 * Descriptor to be used for variables
 * It stores a var scope in the program, as well as if it is initialize (this will change), current value
 */
public class VarDescriptor extends Descriptor {

    protected String dataType;
    protected INITIALIZATION_TYPE isInitialized;
    protected String currValue;
    protected Scope scope;
    protected int localIndex;

    
    public VarDescriptor(String dataType, String identifier){
        this.type=Type.VAR;

        this.identifier = identifier;
        this.dataType = getParsedDataType(dataType);
        this.isInitialized = INITIALIZATION_TYPE.FALSE;
        this.scope = null;
        this.localIndex = -1;
    }

    public VarDescriptor(String dataType, String identifier, int localIndex) {
        this(dataType, identifier);
        this.localIndex = localIndex;
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

    public void setInitialized(INITIALIZATION_TYPE isInitialized){
        this.isInitialized = isInitialized;
    }

    public void setCurrValue(String value){
        this.currValue = value;
    }

    public INITIALIZATION_TYPE getInitialized(){
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

    public int getLocalIndex() { return this.localIndex; }
    public void setLocalIndex(int localIndex) { this.localIndex = localIndex; }

    /**
     * TRUE if the var is initialized
     * FALSE it it isn't
     * MAYBE if there is a change it may or may not have been initialized (if it is initialized in one branch of a if/while statement for instance)
     */
    public static enum INITIALIZATION_TYPE{
        TRUE,
        FALSE,
        MAYBE
    }
}