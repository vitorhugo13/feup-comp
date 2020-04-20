package descriptors;

public class VarDescriptor extends Descriptor {
    protected String dataType;
    protected String initialValue;

    public VarDescriptor(String dataType, String initialValue){
        this.type=Type.VAR;

        this.dataType=dataType;
        this.initialValue=initialValue;

    }

}