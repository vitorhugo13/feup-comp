import descriptors.*;


@SuppressWarnings({"unchecked"})
public class TraverseAst{
    protected SimpleNode root;
    protected SymbolTable symbolTable;


    public TraverseAst(SimpleNode root, SymbolTable symbolTable){
        this.root = root;
        this.symbolTable = symbolTable;
    }

    public void execute(Node node){

        if(node.toString().equals("VarDeclaration")){

            System.out.println("Node ID: "+ node.getId() + " | " + " TYPE: " + node.toString() + " | "+" VARTYPE: " +node.jjtGetChild(0) + " | " +" Identifier: "+ node.jjtGetChild(1));

            VarDescriptor var_descriptor = new VarDescriptor(node.toString());
            String identifier = parse_varID(node.jjtGetChild(1).toString());

            var_descriptor.setValue(identifier);

            System.out.println("AFTER PARSE: " + identifier);
            symbolTable.add(identifier, var_descriptor);
            System.out.println("table: "+ symbolTable.toString());

        }
        else if(node.toString().equals("StaticImport")){

            ImportDescriptor descriptor = new ImportDescriptor();
            System.out.println("STATIC IS: " + node.toString()); //StaticImport
            symbolTable.add(node.toString(), descriptor);

        }
        else{

            for (int i = 0; i < node.jjtGetNumChildren(); i++) {
                execute(node.jjtGetChild(i));
            }

        }
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public String parse_varID(String name){

        String id ="";
        int state = 0;

        for(int i = 0; i < name.length(); i++){

            switch(state){
                case 0:
                    if(name.charAt(i) == '['){
                        state = 1;
                    }
                    break;
                case 1:

                    if(name.charAt(i) == ']'){
                        state = 2;
                    }
                    else{
                        id = id + name.charAt(i);
                    }
                    break;
                case 2:
                    break;
            }
        }

        return id;
    }
}

