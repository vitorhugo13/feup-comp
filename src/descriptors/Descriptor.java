package descriptors;
import java.util.HashMap;

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
        else
            return dataType;

    }
    public String getIdentifier() {
        return identifier;
    }

    public Type getType(){
        return this.type;
    }

}