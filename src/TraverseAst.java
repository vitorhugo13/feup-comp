import descriptors.*;
class TraverseAst{
    SimpleNode root;
    SymbolTable symbolTable;


    public TraverseAst(SimpleNode root, SymbolTable symbolTable){
        this.root=root;
        this.symbolTable=symbolTable;
    }

    public void execute(Node node){
        if(node.toString().equals("VarDeclaration")){
            System.out.println("Node ID: "+ node.getId() + " TYPE: " + node.toString() + " VARTYPE: " +node.jjtGetChild(0) + " Identifier: "+node.jjtGetChild(1) );
            VarDescriptor descriptor = new VarDescriptor(node.toString());
            symbolTable.add(node.jjtGetChild(1).toString(), descriptor);
            System.out.println("table: "+ symbolTable.toString());
        }
        else if(node.toString().equals("StaticImport")){
            ImportDescriptor descriptor = new ImportDescriptor();
            symbolTable.add(node.toString(), descriptor);
        }

        //System.out.println("Node ID: "+ node.getId() + " TYPE: " + node.toString());
        else{
            for (int i = 0; i < node.jjtGetNumChildren(); i++) {
                execute(node.jjtGetChild(i));
            }
        }
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}
/*
        Program
    StaticImport            Class[HelloWorld]
Identifier  MethodImport

 */
