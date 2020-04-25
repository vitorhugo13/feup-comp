import java.util.ArrayList;
import descriptors.*;
import java.io.IOException;

//TODO: string[] args do method main DONE, I GUESS
//TODO: get data type
//TODO: arrays(ESQ, DIREITA, AMBOS)

class SemanticAnalysis{
    static private String VOID = "void";
    static private int MAX_EXCEPTIONS = 10;
    private SymbolTable symbolTable;
    private int exceptionCounter;




    public SemanticAnalysis(SymbolTable symbolTable){
        this.symbolTable = symbolTable;
        this.exceptionCounter = 0;
    }

    public void execute(Node node){
        //System.out.println("EXECUTE: " + node.toString());
        try {
            if (node.toString().equals("StaticImport")
                    || node.toString().equals("NonStaticImport")) {
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
        }
        catch (Exception e) {
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
        if(node.jjtGetChild(0).equals("Array")){
            processArray(node);
        }
        else{

            VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(Utils.parseName(node.jjtGetChild(0).toString()), Descriptor.Type.VAR).get(0);
            String dataType= Utils.getFirstPartOfName(node.jjtGetChild(1).toString());

            if(dataType.equals(varDescriptor.getDataType())){
                varDescriptor.setInitialized();
                varDescriptor.setCurrValue(Utils.parseName(node.jjtGetChild(1).toString()));
            }
            else{
                throw new IOException("Variable " + varDescriptor.getIdentifier() + " of type " + dataType + " does not match declaration type " + varDescriptor.getDataType());
            }
        }
    }

    private void processArray(Node node) throws IOException{

        String id = Utils.parseName(node.jjtGetChild(0).jjtGetChild(0).toString());
        VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(id, Descriptor.Type.VAR).get(0);
        if(!varDescriptor.getDataType().equals("Array")){
            throw new IOException("Variable " + varDescriptor.getIdentifier() + " of type Array does not match declaration type " + varDescriptor.getDataType());
        }

        String index_dataType = Utils.getFirstPartOfName(node.jjtGetChild(0).jjtGetChild(1).toString()); //index of array data type

        if(index_dataType.equals("Identifier")){
            VarDescriptor index_varDescriptor = (VarDescriptor) symbolTable.lookup(index_dataType, Descriptor.Type.VAR).get(0);
            if(!index_varDescriptor.getDataType().equals("Integer")){
                throw new IOException("Index of array " + id + " is not Integer!");
            }
        }
        else if(index_dataType.equals("Identifier")){

        }
        else{
            throw new IOException("Index of array " + id + " is not Integer!");
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