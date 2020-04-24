import java.util.ArrayList;
import descriptors.*;
import java.io.IOException;


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
                System.err.println("THERE ARE MORE THAN " + MAX_EXCEPTIONS + " SEMANTIC ERRORS. PROGRAM TERMINATING...");
                System.exit(0);
            }
        }
    }

    private void processAssign(Node node) throws IOException{
        symbolTable.lookup(parseName(node.jjtGetChild(0).toString()), Descriptor.Type.VAR);
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


    public String parseName(String name){

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