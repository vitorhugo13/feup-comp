import java.util.ArrayList;
import descriptors.*;
import java.io.IOException;

//TODO: string[] args do method main DONE, I GUESS
//TODO: arrays(ESQ, DIREITA, AMBOS)


class SemanticAnalysis{
    static private String VOID = "void";
    static String INTEGER = "Integer";
    static private int MAX_EXCEPTIONS = 10;
    private SymbolTable symbolTable;
    private int exceptionCounter;



    public SemanticAnalysis(SymbolTable symbolTable){
        this.symbolTable = symbolTable;
        this.exceptionCounter = 0;
    }

    public void execute(Node node){

        try {

            if (node.toString().equals("StaticImport")
                    || node.toString().equals("NonStaticImport")) { //TODO: é suposto este 1º if nao ter nenhum condição?
            } else if (node.toString().contains("Class")
                    || node.toString().equals("Method[main]")
                    || (!node.toString().equals("MethodInvocation")
                    && node.toString().contains("Method"))) {
                processNewScope(node);
            } else if (node.toString().equals("Program")) {
                processProgram(node);
            } else if (node.toString().equals("Assign")) {
                processAssign(node);
            } else {
                processChildren(node);
            }

        }catch (Exception e) {

            System.err.println("[SEMANTIC ERROR]: " + e.getMessage());
            exceptionCounter++;

            if (exceptionCounter >= MAX_EXCEPTIONS) {
                System.err.println("[PROGRAM TERMINATING] THERE ARE MORE THAN " + MAX_EXCEPTIONS + " SEMANTIC ERRORS.");
                System.exit(0);
            }
        }
    }

    private void processAssign(Node node) throws IOException{

        // TODO: Arrays (also need to update symbol table with those), Class types and constructor invocations, sums (a= b+c)

        if(node.jjtGetChild(0).equals("Array") && !node.jjtGetChild(1).equals("Array")){
            processArrayLeft(node);
        }
        else if(!node.jjtGetChild(0).equals("Array") && node.jjtGetChild(1).equals("Array")){
            processArrayRight(node);
        }
        else if(node.jjtGetChild(0).equals("Array") && node.jjtGetChild(1).equals("Array")){
            processArrayBoth(node);
        }
        else if(node.jjtGetChild(1).toString().equals("NewIntArray")){
           processInitializeArray(node);
        }
        else{
            VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(Utils.parseName(node.jjtGetChild(0).toString())).get(0);
            String dataType = getNodeDataType(node.jjtGetChild(1));
            if(dataType.equals(varDescriptor.getDataType())){
                varDescriptor.setInitialized();
                varDescriptor.setCurrValue(Utils.parseName(node.jjtGetChild(1).toString()));
            }
            else{
                throw new IOException("Variable " + varDescriptor.getIdentifier() + " of type " + dataType + " does not match declaration type " + varDescriptor.getDataType());
            }
        }
    }

    private void processInitializeArray(Node node) throws IOException{
        VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(Utils.parseName(node.jjtGetChild(0).toString())).get(0);
        if(!varDescriptor.getDataType().equals("Array")){
            throw new IOException("Variable " + varDescriptor.getIdentifier() + " is not an array. Previously declared as a " + varDescriptor.getDataType());
        }
        if(!getNodeDataType(node.jjtGetChild(1).jjtGetChild(0)).equals(INTEGER)){
            throw new IOException("When initializing array, array size must be an integer");
        }
        
        

    }
    private String getNodeDataType(Node node) throws IOException{ 
        /*
        Identifier[a]
        INTEGER[2]
        ADD
            IDENTIFIER
            INTEGER
            */
        if(node.toString().contains("Identifier")){ //IDENTIFIER[a]
            VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(Utils.parseName(node.toString())).get(0);
            if(!varDescriptor.getInitialized())
                throw new IOException("Variable " + varDescriptor.getIdentifier() + " is not initialized");
            return varDescriptor.getDataType();
        }
        else if(node.toString().equals("Add") || node.toString().equals("Sub") || node.toString().equals("Div") || node.toString().equals("Mul")){
            for(int i= 0; i<node.jjtGetNumChildren();i++){
                if(!getNodeDataType(node.jjtGetChild(i)).equals(INTEGER))
                    throw new IOException("Arithmetic operation must be done with Integer values");
            }
            return "Integer";
        }
        else{ // INTEGER[2]
            return Utils.getFirstPartOfName(node.toString());
        }
      
        
    }

    private void processArrayLeft(Node node) throws IOException{

        System.out.println("ARRAY LEFT");

        String id = Utils.parseName(node.jjtGetChild(0).jjtGetChild(0).toString());
        VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(id).get(0);

        if(!varDescriptor.getDataType().equals("Array")){
            throw new IOException("Variable " + varDescriptor.getIdentifier() + " of type Array does not match declaration type " + varDescriptor.getDataType());
        }

        String type = getNodeDataType(node.jjtGetChild(0).jjtGetChild(1));
        if(!type.equals("Integer")){
            throw new IOException("Index of array " + id + " is not Integer!");
        }

        if(!getNodeDataType(node.jjtGetChild(1)).equals("Integer")){
            throw new IOException("Index of array " + id + " is not Integer!");
        }

    }

    private void processArrayRight(Node node) throws IOException{

        System.out.println("ARRAY RIGHT");

        String id = Utils.parseName(node.jjtGetChild(0).toString());
        VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(id).get(0);

        if(!getNodeDataType(node.jjtGetChild(0)).equals("Integer")){
            throw new IOException( id + " is not Integer!");
        }

     //   ==============

        String id2 = Utils.parseName(node.jjtGetChild(1).jjtGetChild(0).toString());
        VarDescriptor varDescriptor2 = (VarDescriptor) symbolTable.lookup(id2).get(0);

        if(!varDescriptor2.getDataType().equals("Array")){
            throw new IOException("Variable " + varDescriptor2.getIdentifier() + " of type Array does not match declaration type " + varDescriptor2.getDataType());
        }

        String type = getNodeDataType(node.jjtGetChild(1).jjtGetChild(1));
        if(!type.equals("Integer")){
            throw new IOException("Index of array " + id2 + " is not Integer!");
        }

    }

    private void processArrayBoth(Node node) throws IOException{

        System.out.println("ARRAY BOTH");
    

        String id = Utils.parseName(node.jjtGetChild(0).jjtGetChild(0).toString());
        VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(id).get(0);

        if(!varDescriptor.getDataType().equals("Array")){
            throw new IOException("Variable " + varDescriptor.getIdentifier() + " of type Array does not match declaration type " + varDescriptor.getDataType());
        }

        String type = getNodeDataType(node.jjtGetChild(0).jjtGetChild(1));
        if(!type.equals("Integer")){
            throw new IOException("Index of array " + id + " is not Integer!");
        }
        //   ==============

        String id2 = Utils.parseName(node.jjtGetChild(1).jjtGetChild(0).toString());
        VarDescriptor varDescriptor2 = (VarDescriptor) symbolTable.lookup(id2).get(0);

        if(!varDescriptor2.getDataType().equals("Array")){
            throw new IOException("Variable " + varDescriptor2.getIdentifier() + " of type Array does not match declaration type " + varDescriptor2.getDataType());
        }

        String type2 = getNodeDataType(node.jjtGetChild(1).jjtGetChild(1));
        if(!type2.equals("Integer")){
            throw new IOException("Index of array " + id2 + " is not Integer!");
        }

    }

    private void processProgram(Node node){
        symbolTable.enterScopeForAnalysis(); // Enter Import Scope
        for (int i = 0; i < node.jjtGetNumChildren()-1; i++) { // Imports
            execute(node.jjtGetChild(i));
        }
        symbolTable.exitScopeForAnalysis(); // Leave Import Scope
        execute(node.jjtGetChild(node.jjtGetNumChildren()-1)); // Class

    }


    private void processNewScope(Node node) {
        symbolTable.enterScopeForAnalysis();
        processChildren(node);
        symbolTable.exitScopeForAnalysis();
    }

    private void processChildren(Node node) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            execute(node.jjtGetChild(i));
        }
    }


    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }
}