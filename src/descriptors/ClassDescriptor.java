package descriptors;

public class ClassDescriptor extends Descriptor {

    public ClassDescriptor(String identifier){
        this.type=Type.CLASS;
        this.identifier=identifier;
    }

}