abstract public class Descriptor {
    //General
    protected Type type;

    enum Type{
        CLASS,
        METHOD,
        VAR,
        IMPORT
    }

}