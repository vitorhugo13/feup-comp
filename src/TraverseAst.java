import java.util.ArrayList;
import descriptors.*;


@SuppressWarnings({"unchecked"})
public class TraverseAst{
    
    protected SimpleNode root;
    protected SymbolTable symbolTable;
    static private String VOID = "void";


    public TraverseAst(SimpleNode root, SymbolTable symbolTable){
        this.root = root;
        this.symbolTable = symbolTable;
    }


    public void execute(Node node){
        //System.out.println("EXECUTE: " + node.toString());
        if(node.toString().equals("Program")){
            processProgram(node);
        }
        else if(node.toString().equals("VarDeclaration")){
            processVarDeclaration(node);
        }
        else if(node.toString().equals("StaticImport")){
            processStaticImport(node);
        }
        else if(node.toString().equals("NonStaticImport")){
            processNonStaticImport(node);
        }
        else if(node.toString().contains("Class")){
            processClass(node);
        }
        else if(node.toString().equals("Method[main]")){
            processMain(node);
        }
        else if(!node.toString().equals("MethodInvocation") && node.toString().contains("Method")){
            processMethod(node);
        }
        else{
            for (int i = 0; i < node.jjtGetNumChildren(); i++) {
                execute(node.jjtGetChild(i));
            }
        }
    }

    private void processProgram(Node node){

        symbolTable.enterScope(); // Enter Import Scope

        for (int i = 0; i < node.jjtGetNumChildren()-1; i++) { // Imports
            execute(node.jjtGetChild(i));
        }

        symbolTable.exitScope(); // Leave Import Scope
        execute(node.jjtGetChild(node.jjtGetNumChildren()-1)); // Class

    }

    private void processVarDeclaration(Node node) {
        //System.out.println("Node ID: "+ node.getId() + " | " + " TYPE: " + node.toString() + " | "+" VARTYPE: " +node.jjtGetChild(0) + " | " +" Identifier: "+ node.jjtGetChild(1));

        String identifier = Utils.parseName(node.jjtGetChild(1).toString());
        VarDescriptor var_descriptor = new VarDescriptor(Utils.parseName(node.jjtGetChild(0).toString()), identifier);

        symbolTable.add(identifier, var_descriptor);
    }

    private void processNonStaticImport(Node node){

        MethodDescriptor descriptor;

        switch(node.jjtGetNumChildren()){
            case 1: // Ex.: import List;
                descriptor= new MethodDescriptor(Utils.parseName(node.jjtGetChild(0).toString()), VOID, new ArrayList<>());
                symbolTable.add(Utils.parseName(node.jjtGetChild(0).toString()), descriptor, true);
                return;
            case 2:
                if(node.jjtGetChild(1).toString().contains("Method")){ // Ex: import List.add(int);

                    ArrayList<VarDescriptor> params = getMethodParams(node.jjtGetChild(1).jjtGetChild(0));
                    String returnType = getMethodReturnType(node.jjtGetChild(1));
                    String name = Utils.parseName(node.jjtGetChild(0).toString());

                    descriptor= new MethodDescriptor(name, returnType,  params);
                    symbolTable.add(name, descriptor, true);

                    return;
                }
                else{ // Contructor with params - Ex: import List(int);
                    ArrayList<VarDescriptor> params = getMethodParams(node.jjtGetChild(1));

                    descriptor= new MethodDescriptor(Utils.parseName(node.jjtGetChild(0).toString()), VOID, params);
                    symbolTable.add(Utils.parseName(node.jjtGetChild(0).toString()), descriptor, true);

                    return;
                }
        }

    }

    private ArrayList<VarDescriptor> getMethodParams(Node paramList) {
        ArrayList<VarDescriptor> params = new ArrayList<>();

        for (int i = 0; i < paramList.jjtGetNumChildren(); i++) {
            VarDescriptor vd = new VarDescriptor(Utils.parseName(paramList.jjtGetChild(i).toString()), null);
            params.add(vd);
        }
        return params;
    }

    private void processStaticImport(Node node) {

        ArrayList<VarDescriptor> params = getMethodParams(node.jjtGetChild(1).jjtGetChild(0));
        String returnType = getMethodReturnType(node.jjtGetChild(1));


        String name = Utils.parseName(node.jjtGetChild(0).toString());
        MethodDescriptor descriptor = new MethodDescriptor(name, returnType, params);
        symbolTable.add(name, descriptor, true);
        //System.out.println("STATIC IMPORT ID: " + name + "  --- RETURN TYPE: " + returnType + "--- FIRST PARAM: " + params.size());
    }

    private String getMethodReturnType(Node method) {
        String returnType;
        if(method.jjtGetNumChildren() == 2){

            if(method.jjtGetChild(1).jjtGetNumChildren()>0)
                returnType = Utils.parseName(method.jjtGetChild(1).jjtGetChild(0).toString()); //Method->Return->Type
            else
                returnType = VOID;
        }
        else{
            returnType = VOID;
        }
        return returnType;
    }

    private void processMethod(Node node) {


        ArrayList<VarDescriptor> params = new ArrayList<>();
        Node paramList= node.jjtGetChild(1);
        Node currentNode;


        for(int i=0; i<paramList.jjtGetNumChildren(); i++){

            currentNode= paramList.jjtGetChild(i); //VarDeclaration
            VarDescriptor varDescriptor =new VarDescriptor(Utils.parseName(currentNode.jjtGetChild(0).toString()), Utils.parseName(currentNode.jjtGetChild(1).toString()));
            params.add(varDescriptor);

        }

        MethodDescriptor methodDescriptor = new MethodDescriptor(Utils.parseName(node.toString()), Utils.parseName(node.jjtGetChild(0).toString()), params);
        symbolTable.add(Utils.parseName(node.toString()), methodDescriptor);

        symbolTable.enterScope();

        for(int i=0; i<params.size(); i++){
            params.get(i).setInitialized();
            symbolTable.add(params.get(i).getIdentifier(), params.get(i));

        }

        for(int i=0; i<node.jjtGetChild(2).jjtGetNumChildren(); i++){
            execute(node.jjtGetChild(2).jjtGetChild(i));
        }

        symbolTable.exitScope();
    }

    private void processMain(Node node) {

        ArrayList<VarDescriptor> params = new ArrayList<>();
        Node paramList= node.jjtGetChild(0);
        Node currentNode;

        for(int i=0; i<paramList.jjtGetNumChildren(); i++){
            if(paramList.jjtGetChild(i).toString().equals("VarDeclaration")){
                currentNode= paramList.jjtGetChild(i);
                VarDescriptor varDescriptor =new VarDescriptor(Utils.parseName(currentNode.jjtGetChild(0).toString()), Utils.parseName(currentNode.jjtGetChild(1).toString())); //tyoe, identifier
                params.add(varDescriptor);
            }
           
        }

        MethodDescriptor methodDescriptor = new MethodDescriptor(Utils.parseName(node.toString()), null, params);
        symbolTable.add(Utils.parseName(node.toString()), methodDescriptor);

        symbolTable.enterScope();

        for(int i=0; i<params.size(); i++){
            params.get(i).setInitialized();
            symbolTable.add(params.get(i).getIdentifier(), params.get(i));
           
        }

        for(int i=0; i<node.jjtGetChild(1).jjtGetNumChildren(); i++){
            execute(node.jjtGetChild(1).jjtGetChild(i));
        }

        symbolTable.exitScope();
    }

    private void processClass(Node node) {
        symbolTable.enterScope();

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {

            execute(node.jjtGetChild(i));
        }
        symbolTable.exitScope();
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

   
}




