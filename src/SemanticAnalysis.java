import java.util.ArrayList;
import descriptors.*;

class SemanticAnalysis{
    private SymbolTable symbolTable;
    private int exceptionCounter;
    static private String VOID = "void";

    public SemanticAnalysis(SymbolTable symbolTable){
        this.symbolTable = symbolTable;
        this.exceptionCounter = 0;
    }

    public void execute(Node node){
        //System.out.println("EXECUTE: " + node.toString());
        if(node.toString().equals("StaticImport")
            || node.toString().equals("NonStaticImport")){
        }
        else if(node.toString().contains("Class")
                || node.toString().equals("Method[main]")
                || (!node.toString().equals("MethodInvocation")
                    && node.toString().contains("Method"))){
           // System.out.println("New Scope: " + node.toString());
            processNewScope(node);
        }
        else if (node.toString().equals("Program")){
            processProgram(node);
        }
        else{
            processChildren(node);
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