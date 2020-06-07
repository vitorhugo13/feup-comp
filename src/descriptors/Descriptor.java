package descriptors;

/**
 * Descriptors are used to store information about symbols
 */
abstract public class Descriptor {

    protected Type type;
    protected String identifier;

    /**
     * Type of descriptors
     */
    public static enum Type{
        CLASS,
        METHOD,
        VAR,
        IMPORT
    }

    /**
     *
     * @param dataType
     * @return a standardized form of the the data type name
     */
    protected String getParsedDataType(String dataType){
        if(dataType.equals("int"))
            return "Integer";
        else if(dataType.equals("array"))
            return "Array";
        else if(dataType.equals("boolean"))
            return "Boolean";
        else
            return dataType;

    }
    public String getIdentifier() {
        return identifier;
    }

    public Type getType(){
        return this.type;
    }

    public static enum Scope{
        LOCAL,
        GLOBAL,
        IMPORT
    }

}