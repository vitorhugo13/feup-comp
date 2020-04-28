package descriptors;

abstract public class Descriptor {

    protected Type type;
    protected String identifier;

    public static enum Type{
        CLASS,
        METHOD,
        VAR,
        IMPORT
    }

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