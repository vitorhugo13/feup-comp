package descriptors;

abstract public class Descriptor {
    protected Type type;

    enum Type{
        CLASS,
        METHOD,
        VAR,
        IMPORT
    }

    public Type get_type(){
        return this.type;
    }

}