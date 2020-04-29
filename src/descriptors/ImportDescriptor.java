package descriptors;

import java.util.ArrayList;

public class ImportDescriptor extends Descriptor{
    ArrayList<String> paramsList;
    String returnType;

    public ImportDescriptor(String identifier, ArrayList<String> paramsList, String returnType){

        this.type=Type.IMPORT;
        this.identifier=identifier;
        this.paramsList=paramsList;
        this.returnType=returnType;
    }

}