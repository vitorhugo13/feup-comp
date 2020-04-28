import java.util.ArrayList;
import descriptors.*;
import java.io.IOException;

//TODO: this para chamar metodos da propria class
//TODO: import io
//TODO: invoke.add(1,2).printIsto()
//TODO: extends
//TODO: ifs and whiles
//TODO: negações

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
            if (node.toString().equals("StaticImport") || node.toString().equals("NonStaticImport")) { 
            } 
            else if (node.toString().contains("Class") || node.toString().equals("Method[main]") || (!node.toString().equals("MethodInvocation") && node.toString().contains("Method["))) {
                processNewScope(node);
            }
            else if(node.toString().equals("MethodInvocation")){
                processInvocation(node);
            }
            else if (node.toString().equals("Program")) {
                processProgram(node);
            } 
            else if (node.toString().equals("Assign")) {
                processAssign(node);
            } 
            else {
                processChildren(node);
            }
        }catch (Exception e) {
            System.err.println("[SEMANTIC ERROR]: " + e.getMessage());
            e.printStackTrace();
            exceptionCounter++;

            if (exceptionCounter >= MAX_EXCEPTIONS) {
                System.err.println("[PROGRAM TERMINATING] THERE ARE MORE THAN " + MAX_EXCEPTIONS + " SEMANTIC ERRORS.");
                System.exit(0);
            }
        }
    }

    private String processInvocation(Node node) throws IOException{
        String id;
        ClassDescriptor classDescriptor;

        if(node.jjtGetChild(0).equals("This")){
            classDescriptor = (ClassDescriptor) symbolTable.lookup(symbolTable.getClassName()).get(0);
        }
        else{ //io.prinln() ou obj.add();
            id=Utils.parseName(node.jjtGetChild(0).toString());  
            Descriptor descriptor =symbolTable.lookup(id).get(0);
            if(descriptor.getType().equals(Descriptor.Type.CLASS)){
                classDescriptor = (ClassDescriptor) descriptor;
            }
            else{
                String className = ((VarDescriptor) descriptor).getDataType();
                classDescriptor = (ClassDescriptor) symbolTable.lookup(className).get(0);
            }
        }
        

        String methodName = Utils.parseName(node.jjtGetChild(1).toString()); 
        ArrayList<MethodDescriptor> methodDescriptors = classDescriptor.getMethodsMatchingId(methodName);

        //dado que pode ocorrer method overload temos de percorrer a lista de descritores returnados
        //e saber se existe o certo, e se sim processar os args e o return type
        int numArgs = node.jjtGetChild(2).jjtGetNumChildren();
        Boolean correct = true;

        for(int method = 0; method < methodDescriptors.size(); method++){
            MethodDescriptor md = methodDescriptors.get(method);

            if(md.getParameters().size() == numArgs){

                for(int param = 0; param < md.getParameters().size(); param++){    

                    String typeArg = getNodeDataType(node.jjtGetChild(2).jjtGetChild(param)); //tipo de argumento passado
                    System.out.println("TYPE OF ARGUMENT: " + typeArg);
                    String expectedType = md.getParameters().get(param).getDataType(); //tipo de argumento esperado

                    if(!typeArg.equals(expectedType)){
                        correct = false;
                        throw new IOException(Utils.parseName(node.jjtGetChild(2).jjtGetChild(param).toString())+ " does not match type " + expectedType + " in " + methodName);
                    }
                }

                if(correct){
                    return md.getReturnType();
                }

            }
        }

        return "";
        
    }

    private void processAssign(Node node) throws IOException{

        // TODO: Arrays (also need to update symbol table with those), Class types and constructor invocations, sums (a= b+c)

        if(node.jjtGetChild(0).toString().equals("Array") && !node.jjtGetChild(1).toString().contains("Array")){
            processArrayLeft(node);
        }
        else if(node.jjtGetChild(0).toString().equals("Array") && node.jjtGetChild(1).toString().contains("Array")){
            processArrayBoth(node);
        }
        else if(node.jjtGetChild(1).toString().equals("NewIntArray")){
           processInitializeArray(node);
        }
        else if(node.jjtGetChild(1).toString().equals("NewObject")){
            processObject(node);
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

    private void processObject(Node node) throws IOException {

        //TODO: o objeto que está a ser criado tem de ser do mesmo tipo da classe onde está inserido, ou da class a que faz extends ou de uma class qql dos imports
        String obj = Utils.parseName(node.jjtGetChild(1).jjtGetChild(0).toString());
        String id = Utils.parseName(node.jjtGetChild(0).toString());

        VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(id).get(0);
        if(!varDescriptor.getDataType().equals(obj)){
            throw new IOException("Variable " + id + " does not match " + varDescriptor.getDataType());
        }

        varDescriptor.setInitialized();
    }
    private void processInitializeArray(Node node) throws IOException{
        VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(Utils.parseName(node.jjtGetChild(0).toString())).get(0);
        if(!varDescriptor.getDataType().equals("Array")){
            throw new IOException("Variable " + varDescriptor.getIdentifier() + " is not an array. Previously declared as a " + varDescriptor.getDataType());
        }
        if(!getNodeDataType(node.jjtGetChild(1).jjtGetChild(0)).equals(INTEGER)){
            throw new IOException("When initializing array, array size must be an integer");
        }

        varDescriptor.setInitialized();
    }

    private String getNodeDataType(Node node) throws IOException{ 

        /*

        Identifier[a]
        INTEGER[2]
        ADD
            IDENTIFIER
            INTEGER
        ARRAY
            
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
                    throw new IOException("Arithmetic operation must be done with Integer values: variable " + Utils.parseName(node.jjtGetChild(i).toString()) + " is not an Integer");
            }
            return "Integer";
        }
        else if(node.toString().equals("Array")){
            return processArrayRight(node);
        }
        else if(node.toString().equals("MethodInvocation")){

            String tipo = processInvocation(node);
            
            if(tipo.equals("int")){
                tipo = "Integer";
            }

            return tipo;
        }
        else{ // INTEGER[2]
            return Utils.getFirstPartOfName(node.toString());
        }
       
    }

    private String processArrayRight(Node node) throws IOException{

        String id = Utils.parseName(node.jjtGetChild(0).toString());
        VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(id).get(0);

        if(!varDescriptor.getDataType().equals("Array")){
            throw new IOException("Variable " + varDescriptor.getIdentifier() + " of type Array does not match declaration type " + varDescriptor.getDataType());
        }

        String type = getNodeDataType(node.jjtGetChild(1));
        if(!type.equals("Integer")){
            throw new IOException("Index of array " + id + " is not Integer!");
        }

        if(!getNodeDataType(node.jjtGetChild(1)).equals("Integer")){
            throw new IOException("Index of array " + id + " is not Integer!");
        }

        return INTEGER;

    }

    private void processArrayLeft(Node node) throws IOException{

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

    private void processArrayBoth(Node node) throws IOException{

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