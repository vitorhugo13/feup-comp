package descriptors;

abstract public class Descriptor {
    protected Type type;
    protected String identifier;

    enum Type{
        CLASS,
        METHOD,
        VAR,
        IMPORT
    }

    public Type getType(){
        return this.type;
    }

}