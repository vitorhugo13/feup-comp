import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

import java.io.IOException;

import descriptors.*;


class SemanticAnalysis{
    static private String VOID = "void";
    static String INTEGER = "Integer";
    static private int MAX_EXCEPTIONS = 10;
    private SymbolTable symbolTable;
    private int exceptionCounter;
    private boolean isStatic;



    public SemanticAnalysis(SymbolTable symbolTable){
        this.symbolTable = symbolTable;
        this.exceptionCounter = 0;
        this.isStatic = false;
    }

    public void execute(Node node) throws IOException{
        
       // try {
            if (node.toString().equals("StaticImport") || node.toString().equals("NonStaticImport")) { 
            } 
            else if(node.toString().equals("Method[main]")){
                this.isStatic = true;
                processNewScope(node);
                this.isStatic = false;

            }
            else if (Utils.analyzeRegex(node.toString(), "(Class\\[)(.)*(\\])") || (!node.toString().equals("MethodInvocation") && Utils.analyzeRegex(node.toString(), "(Method\\[)(.)*(\\])"))) {   
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
            else if(node.toString().equals("Length")){
                processLength(node);
            }
            else if(node.toString().equals("IfStatement")){
                HashSet<String> toInit = processIfStatement(node);
            
                VarDescriptor varDescriptor;
                for(String var:toInit){
                    varDescriptor = (VarDescriptor) symbolTable.lookup(var).get(0);
                    varDescriptor.setInitialized(VarDescriptor.INITIALIZATION_TYPE.TRUE);
                }
            }
            else if(node.toString().equals("While")){
                processWhile(node);
            }
            else {
                processChildren(node);
            }
    }

    private void processLength(Node node) throws IOException{
        String type = getNodeDataType(node.jjtGetChild(0));

        if(!type.equals("Array") && !type.equals("stringarray")){

            int line = ((SimpleNode) node).getCoords().getLine();
            throw new IOException( "Line " + line + ": It is not possible to invoke length of a non array object.");
        }
    }
    private Boolean processReturnType(Node node) throws IOException{

        String expected = Utils.parseName(node.jjtGetChild(0).toString());
        String actual = "";

        if(expected.equals("int")){
            expected = "Integer";
        }
        else if(expected.equals("array")){
            expected = "Array";
        }
        else if(expected.equals("boolean")){
            expected = "Boolean";
        }

        Node body = node.jjtGetChild(2);

        for(int i = 0; i < body.jjtGetNumChildren(); i++){
            if(body.jjtGetChild(i).toString().equals("Return")){
                actual = getNodeDataType(body.jjtGetChild(i).jjtGetChild(0));
                if(actual.equals(expected)){
                    return true;
                }
            }    
        }

        return false;
    }

    private HashSet<String> processIfStatement(Node node)throws IOException{

        HashSet<String> initializedVarIf = new HashSet<>();
        HashSet<String> initializedVarElse = new HashSet<>();
        HashSet<String> initializedReturn = new HashSet<>();
        HashMap<String, VarDescriptor.INITIALIZATION_TYPE> changedToTrue = new HashMap<>();
        Node condition = node.jjtGetChild(0);

        /*IF CONDITION */
        if(!getNodeDataType(condition.jjtGetChild(0)).equals("Boolean")){
            int line = ((SimpleNode) node).getCoords().getLine();
            throw new IOException("Line " + line + ": If Statement must have a boolean condition");
        }

        /*IF SCOPE */
        for(int i = 0; i < node.jjtGetChild(1).jjtGetNumChildren(); i++){

            if(node.jjtGetChild(1).jjtGetChild(i).toString().equals("Assign")){
                String variableIf = processStmtAssign(node.jjtGetChild(1).jjtGetChild(i), changedToTrue);
                if(!variableIf.equals(""))
                    initializedVarIf.add(variableIf);
            }
            else if(node.jjtGetChild(1).jjtGetChild(i).toString().equals("IfStatement")){
                HashSet<String> childHashSet = processIfStatement(node.jjtGetChild(1).jjtGetChild(i));

                for(String var:childHashSet){
                    initializedVarIf.add(var);
                }
            }
            else{
                execute(node.jjtGetChild(1).jjtGetChild(i));
            }

        }

        VarDescriptor varDescriptor;
        for(HashMap.Entry<String, VarDescriptor.INITIALIZATION_TYPE> var:changedToTrue.entrySet()){
            varDescriptor = (VarDescriptor) symbolTable.lookup(var.getKey()).get(0);
            varDescriptor.setInitialized(var.getValue());
        }

        changedToTrue.clear(); //prepare for new scope: else

        /*ELSE SCOPE */
        for(int j = 0; j < node.jjtGetChild(2).jjtGetNumChildren(); j++){

            if(node.jjtGetChild(2).jjtGetChild(j).toString().equals("Assign")){
                String variableElse = processStmtAssign(node.jjtGetChild(2).jjtGetChild(j), changedToTrue);
                if(!variableElse.equals(""))
                    initializedVarElse.add(variableElse);
            }
            else if(node.jjtGetChild(2).jjtGetChild(j).toString().equals("IfStatement")){
                HashSet<String> childHashSet = processIfStatement(node.jjtGetChild(2).jjtGetChild(j));

                for(String var:childHashSet){
                    initializedVarElse.add(var);
                }
            }
            else{
                execute(node.jjtGetChild(2).jjtGetChild(j));                
            }
        }

        for(HashMap.Entry<String, VarDescriptor.INITIALIZATION_TYPE> var:changedToTrue.entrySet()){
            varDescriptor = (VarDescriptor) symbolTable.lookup(var.getKey()).get(0);
            varDescriptor.setInitialized(var.getValue());
        }

        /*compare both hashSets */
        for(String varIF:initializedVarIf){
            if(initializedVarElse.contains(varIF)){
                initializedReturn.add(varIF);
            }
            else{
                varDescriptor = (VarDescriptor) symbolTable.lookup(varIF).get(0);
                varDescriptor.setInitialized(VarDescriptor.INITIALIZATION_TYPE.MAYBE);
                // System.out.println("[WARNING]: Variable " + varIF + " may not have been initialized.");
            }
        }

        for(String varElse:initializedVarElse){
            if(!initializedVarIf.contains(varElse)){
                varDescriptor = (VarDescriptor) symbolTable.lookup(varElse).get(0);
                varDescriptor.setInitialized(VarDescriptor.INITIALIZATION_TYPE.MAYBE);
                // System.out.println("[WARNING]: Variable " + varElse + " may not have been initialized.");  
            }
        }

        return initializedReturn;

    }

    private String processStmtAssign(Node node, HashMap<String, VarDescriptor.INITIALIZATION_TYPE> changedToTrue) throws IOException{
        VarDescriptor varDescriptor;
        if(node.jjtGetChild(0).toString().equals("Array")){
            varDescriptor = (VarDescriptor) symbolTable.lookup(Utils.parseName(node.jjtGetChild(0).jjtGetChild(0).toString())).get(0);
        }
        else{
            varDescriptor = (VarDescriptor) symbolTable.lookup(Utils.parseName(node.jjtGetChild(0).toString())).get(0);
        }
        VarDescriptor.INITIALIZATION_TYPE wasInitializedBefore = varDescriptor.getInitialized();
        processAssign(node);
        if(wasInitializedBefore.equals(VarDescriptor.INITIALIZATION_TYPE.FALSE) || wasInitializedBefore.equals(VarDescriptor.INITIALIZATION_TYPE.MAYBE)){
            changedToTrue.put(varDescriptor.getIdentifier(), wasInitializedBefore);
            return varDescriptor.getIdentifier();
        }
        return "";
        
    }

    private void processWhile(Node node)throws IOException{

        Node condition = node.jjtGetChild(0);
        VarDescriptor varDescriptor;

        if(!getNodeDataType(condition.jjtGetChild(0)).equals("Boolean")){
            System.out.println("WHILE: Condition is not boolean");
        }

        for(int i = 0; i < node.jjtGetChild(1).jjtGetNumChildren(); i++){
            if(node.jjtGetChild(1).jjtGetChild(i).toString().equals("Assign")){
                if(node.jjtGetChild(1).jjtGetChild(i).jjtGetChild(0).toString().equals("Array")){
                    varDescriptor = (VarDescriptor) symbolTable.lookup(Utils.parseName(node.jjtGetChild(1).jjtGetChild(i).jjtGetChild(0).jjtGetChild(0).toString())).get(0);
                }
                else{
                    varDescriptor = (VarDescriptor) symbolTable.lookup(Utils.parseName(node.jjtGetChild(1).jjtGetChild(i).jjtGetChild(0).toString())).get(0);
                }
                VarDescriptor.INITIALIZATION_TYPE assignmentStateBeforeWhile = varDescriptor.getInitialized();
                
                processAssign(node.jjtGetChild(1).jjtGetChild(i));
                
                if(!(assignmentStateBeforeWhile.equals(VarDescriptor.INITIALIZATION_TYPE.TRUE)))
                    varDescriptor.setInitialized(VarDescriptor.INITIALIZATION_TYPE.MAYBE);
            }
            else if(node.jjtGetChild(1).jjtGetChild(i).toString().equals("IfStatement")){
                HashSet<String> toInit = processIfStatement(node.jjtGetChild(1).jjtGetChild(i));
        
                for(String var:toInit){
                    varDescriptor = (VarDescriptor) symbolTable.lookup(var).get(0);
                    varDescriptor.setInitialized(VarDescriptor.INITIALIZATION_TYPE.MAYBE);
                }
            }
            else{
                execute(node.jjtGetChild(1).jjtGetChild(i));
            }
        }
    }

    private String processInvocation(Node node) throws IOException{
        String id;
        ClassDescriptor classDescriptor;
        String methodName = Utils.parseName(node.jjtGetChild(1).toString());
 
        if(node.jjtGetChild(0).toString().equals("This")){
            if(isStatic){
                int line = ((SimpleNode) node).getCoords().getLine();
                throw new IOException("Line" + line + ": Cannot use 'this' in a static method.");
            }
            classDescriptor = (ClassDescriptor) symbolTable.lookup(symbolTable.getClassName()).get(0);
        }
        else if(node.jjtGetChild(0).toString().equals("MethodInvocation")){

            String type= processInvocation(node.jjtGetChild(0));
            if(type.equals("Integer") || type.equals("boolean")){
                int line = ((SimpleNode) node).getCoords().getLine();
                throw new IOException( "Line " + line + ": Method Invocation: method cannot be invoked for primitive types");
            }
            classDescriptor = (ClassDescriptor) symbolTable.lookup(type).get(0);

        }
        else if(node.jjtGetChild(0).toString().equals("NewObject")){

            String type = processNewObjectMethodInvoke(node.jjtGetChild(0));

            if(type.equals("Integer") || type.equals("boolean")){
                int line = ((SimpleNode) node).getCoords().getLine();
                throw new IOException( "Line " + line + ": Method Invocation: method cannot be invoked for primitive types");
            }
            classDescriptor = (ClassDescriptor) symbolTable.lookup(type).get(0);

        }
        else{ //io.prinln() ou obj.add(); VITORHUGO

            id = Utils.parseName(node.jjtGetChild(0).toString());  
            Descriptor descriptor2 =symbolTable.lookup(id).get(0);

            String idType = "";
            if(!descriptor2.getType().equals(Descriptor.Type.CLASS)){
                idType = ((VarDescriptor) descriptor2).getDataType();
            }
            

            if(!idType.equals("Integer") && !idType.equals("Boolean") && !idType.equals("boolean")){

                Descriptor descriptor =symbolTable.lookup(id).get(0);
                if(descriptor.getType().equals(Descriptor.Type.CLASS)){
                    classDescriptor = (ClassDescriptor) descriptor;
                }
                else{
                    VarDescriptor.INITIALIZATION_TYPE wasInitializedBefore = ((VarDescriptor) descriptor).getInitialized();
                    if(wasInitializedBefore.equals(VarDescriptor.INITIALIZATION_TYPE.FALSE)){
                        int line = ((SimpleNode) node).getCoords().getLine();
                        throw new IOException( "Line " + line + ": Variable " + ((VarDescriptor) descriptor).getIdentifier() + " was not initialized");
                    }
                    else if(wasInitializedBefore.equals(VarDescriptor.INITIALIZATION_TYPE.MAYBE)){
                        int line = ((SimpleNode) node).getCoords().getLine();
                        System.out.println("[WARNING]: Line " + line +": Variable " + ((VarDescriptor) descriptor).getIdentifier() + " may not have been initialized.");
                    }
            
                    
                    
                    String className = ((VarDescriptor) descriptor).getDataType();

                    classDescriptor = (ClassDescriptor) symbolTable.lookup(className).get(0);
                }

            }
            else{
                int line = ((SimpleNode) node).getCoords().getLine();
                throw new IOException( "Line " + line + ": Method Invocation: method cannot be invoked for primitive types");
            }
        }
        
        String returnValue;

        try{
            returnValue = compareArgsAndParams(node, classDescriptor);
            if(!returnValue.equals("error")){
                return returnValue; //Method exists in the class
            }

        }
        catch(Exception e){ //Method does not exist in first class but can still exist in parent
        }

        if(isStatic && node.jjtGetChild(0).toString().equals("This")){
            int line = ((SimpleNode) node).getCoords().getLine();
            throw new IOException( "Line " + line + ": Cannot use 'this' in a static method.");
        }

        if(node.jjtGetChild(0).toString().equals("This") || classDescriptor.getIdentifier().equals(this.symbolTable.getClassName())){

            if(classDescriptor.getParentClass() != null){
                returnValue = compareArgsAndParams(node, classDescriptor.getParentClass());


                if(!returnValue.equals("error")){
                    return returnValue; //Method exists in parent class
                }
                else{

                    int line = ((SimpleNode) node).getCoords().getLine();

                    for(int i = 0; i < node.jjtGetChild(2).jjtGetNumChildren(); i++){
                        if(!Utils.getFirstPartOfName(node.jjtGetChild(2).jjtGetChild(i).toString()).equals("Integer") && !Utils.getFirstPartOfName(node.jjtGetChild(2).jjtGetChild(i).toString()).equals("Boolean")){
                            String variableID = Utils.parseName(node.jjtGetChild(2).jjtGetChild(i).toString());
                            VarDescriptor vd = (VarDescriptor) symbolTable.lookup(variableID).get(0);
                            if(vd.getInitialized().equals(VarDescriptor.INITIALIZATION_TYPE.FALSE)){
                                throw new IOException("Line " + line + ": Variable " + variableID + " not initialized");
                            }
                            else if(vd.getInitialized().equals(VarDescriptor.INITIALIZATION_TYPE.MAYBE)){
                                throw new IOException("[WARNING]: Line " + line + ": Variable " + variableID + " may not be initialized");
                            }
                        }
                    }

                    String signatures = getSignatures(classDescriptor);
                    throw new IOException( "Line " + line + ": No signature of method " + methodName + " matches list of arguments given." + signatures + "\n");
                }
            }
            else{

                int line = ((SimpleNode) node).getCoords().getLine();

                for(int i = 0; i < node.jjtGetChild(2).jjtGetNumChildren(); i++){
                    if(!Utils.getFirstPartOfName(node.jjtGetChild(2).jjtGetChild(i).toString()).equals("Integer") && !Utils.getFirstPartOfName(node.jjtGetChild(2).jjtGetChild(i).toString()).equals("Boolean")){
                        String variableID = Utils.parseName(node.jjtGetChild(2).jjtGetChild(i).toString());
                        VarDescriptor vd = (VarDescriptor) symbolTable.lookup(variableID).get(0);
                        if(vd.getInitialized().equals(VarDescriptor.INITIALIZATION_TYPE.FALSE)){
                            throw new IOException("Line " + line + ": Variable " + variableID + " not initialized");
                        }
                        else if(vd.getInitialized().equals(VarDescriptor.INITIALIZATION_TYPE.MAYBE)){
                            throw new IOException("[WARNING]: Line " + line + ": Variable " + variableID + " may not be initialized");
                        }
                    }
                }

                String signatures = getSignatures(classDescriptor);
                throw new IOException( "Line " + line + ": No signature of method " + methodName + " matches list of arguments given." + signatures + "\n");
            }
        } 
        else{

            int line = ((SimpleNode) node).getCoords().getLine();

            for(int i = 0; i < node.jjtGetChild(2).jjtGetNumChildren(); i++){

                if(!Utils.getFirstPartOfName(node.jjtGetChild(2).jjtGetChild(i).toString()).equals("Integer")&& !Utils.getFirstPartOfName(node.jjtGetChild(2).jjtGetChild(i).toString()).equals("Boolean")){
                    String variableID = Utils.parseName(node.jjtGetChild(2).jjtGetChild(i).toString());
                    VarDescriptor vd = (VarDescriptor) symbolTable.lookup(variableID).get(0);
                    if(vd.getInitialized().equals(VarDescriptor.INITIALIZATION_TYPE.FALSE)){
                        throw new IOException("Line " + line + ": Variable " + variableID + " not initialized");
                    }
                    else if(vd.getInitialized().equals(VarDescriptor.INITIALIZATION_TYPE.MAYBE)){
                        throw new IOException("[WARNING]: Line " + line + ": Variable " + variableID + " may not be initialized");
                    }
                }
                
            }

            String signatures = getSignatures(classDescriptor);
            throw new IOException( "Line " + line + ": No signature of method " + methodName + " matches list of arguments given." + signatures + "\n");
        }

    }

    private String getSignatures(ClassDescriptor classDescriptor){
        String signaturesSupported = "";
        int numberSupported = classDescriptor.getMethods().size();

        if(numberSupported > 0){
            signaturesSupported = " \n\t\t  Methods supported for " + classDescriptor.getIdentifier() + ":";

            for(int k = 0; k < numberSupported; k++){

                String returnType = classDescriptor.getMethods().get(k).getReturnType();
                String methName = classDescriptor.getMethods().get(k).getIdentifier();

                signaturesSupported = signaturesSupported + "\n\t\t\t " + returnType + " " + methName +"(";

                for(int l = 0; l < classDescriptor.getMethods().get(k).getParameters().size(); l++){
                    if(l != 0 )
                        signaturesSupported = signaturesSupported + ", " + classDescriptor.getMethods().get(k).getParameters().get(l).getDataType();
                    else
                        signaturesSupported = signaturesSupported + classDescriptor.getMethods().get(k).getParameters().get(l).getDataType();
                }
                signaturesSupported = signaturesSupported + ")";
            }
        }
        else{
            signaturesSupported = " \n\t    t No methods supported for " + classDescriptor.getIdentifier() + ".";
        }

        return signaturesSupported;
    }

    private String compareArgsAndParams(Node node, ClassDescriptor classDescriptor) throws IOException {
        
        String methodName = Utils.parseName(node.jjtGetChild(1).toString());
        ArrayList<MethodDescriptor> methodDescriptors = classDescriptor.getMethodsMatchingId(methodName);
        int numArgs = node.jjtGetChild(2).jjtGetNumChildren();
        Boolean correct = true;

        for(int method = 0; method < methodDescriptors.size(); method++){
            correct = true;
            MethodDescriptor md = methodDescriptors.get(method);
            if(md.getParameters().size() == numArgs){
                for(int param = 0; param < md.getParameters().size(); param++){
                    
                    String typeArg = getNodeDataType(node.jjtGetChild(2).jjtGetChild(param)); //tipo de argumento passado
                    String expectedType = md.getParameters().get(param).getDataType(); //tipo de argumento esperado

                    if(!typeArg.equals(expectedType)){
                        correct = false;
                    }
                }

                if(correct){
                    return md.getReturnType();
                }
            }
        }

        return "error";
    }


    private void processAssign(Node node) throws IOException{

        if(node.jjtGetChild(0).toString().equals("Array") && !Utils.analyzeRegex(node.jjtGetChild(1).toString(), "(Array\\[)(.)*(\\])")){ 
            processArrayLeft(node);
        }
        else if(node.jjtGetChild(0).toString().equals("Array") && Utils.analyzeRegex(node.jjtGetChild(1).toString(), "(Array\\[)(.)*(\\])")){
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
                    varDescriptor.setInitialized(VarDescriptor.INITIALIZATION_TYPE.TRUE);
                    varDescriptor.setCurrValue(Utils.parseName(node.jjtGetChild(1).toString()));
            }
            else{
                int line = ((SimpleNode) node).getCoords().getLine();
                throw new IOException( "Line " + line + ": Variable " + varDescriptor.getIdentifier() + " of type " + dataType + " does not match declaration type " + varDescriptor.getDataType());
            }
        }
    }

    private void processObject(Node node) throws IOException {

        String obj = Utils.parseName(node.jjtGetChild(1).jjtGetChild(0).toString());
        String id = Utils.parseName(node.jjtGetChild(0).toString());

        VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(id).get(0);
        if(!varDescriptor.getDataType().equals(obj)){
            try{
            ClassDescriptor classDescriptor = (ClassDescriptor) symbolTable.lookup(obj).get(0);
            if(obj.equals(classDescriptor.getParentClass().getIdentifier())){ // Extends: child class is initialized with constructor from parent
                varDescriptor.setDataType(classDescriptor.getParentClass().getIdentifier());
            }
            }
            catch(Exception e){
                int line = ((SimpleNode) node).getCoords().getLine();
                throw new IOException( "Line " + line + ": Variable " + id + " does not match " + varDescriptor.getDataType());
            }
            // if(obj.equals(classDescriptor.getParentClass().getIdentifier())){ // Extends: child class is initialized with constructor from parent
            //     varDescriptor.setDataType(classDescriptor.getParentClass().getIdentifier());
            // }
            // else{
            //     throw new IOException("Variable " + id + " does not match " + varDescriptor.getDataType());
            // }
        }

        varDescriptor.setInitialized(VarDescriptor.INITIALIZATION_TYPE.TRUE);

    }

    private String processNewObjectMethodInvoke(Node node) throws IOException {

        String id = Utils.parseName(node.jjtGetChild(0).toString());
        ClassDescriptor classDescriptor = (ClassDescriptor) symbolTable.lookup(id).get(0);

        return classDescriptor.getIdentifier();
    
    }



    private void processInitializeArray(Node node) throws IOException{
        VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(Utils.parseName(node.jjtGetChild(0).toString())).get(0);
        if(!varDescriptor.getDataType().equals("Array")){
            int line = ((SimpleNode) node).getCoords().getLine();
            throw new IOException( "Line " + line + ": Variable " + varDescriptor.getIdentifier() + " is not an array. Previously declared as a " + varDescriptor.getDataType());
        }
        if(!getNodeDataType(node.jjtGetChild(1).jjtGetChild(0)).equals(INTEGER)){
            int line = ((SimpleNode) node).getCoords().getLine();
            throw new IOException( "Line " + line + ": When initializing array, array size must be an integer");
        }

        varDescriptor.setInitialized(VarDescriptor.INITIALIZATION_TYPE.TRUE);

    }

    private String getNodeDataType(Node node) throws IOException{ 

        if(Utils.analyzeRegex(node.toString(), "(Identifier\\[)(.)*(\\])")){ //IDENTIFIER[a]
            VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(Utils.parseName(node.toString())).get(0);
            if(varDescriptor.getInitialized().equals(VarDescriptor.INITIALIZATION_TYPE.FALSE)){
                int line = ((SimpleNode) node).getCoords().getLine();
                throw new IOException( "Line " + line + ": Variable " + varDescriptor.getIdentifier() + " was not initialized");
            }
            else if(varDescriptor.getInitialized().equals(VarDescriptor.INITIALIZATION_TYPE.MAYBE)){
                int line = ((SimpleNode) node).getCoords().getLine();
                System.out.println("[WARNING]: Line " + line +": Variable "+ varDescriptor.getIdentifier() + " may not have been initialized.");
            }
            return varDescriptor.getDataType();
        }
        else if(node.toString().equals("Add") || node.toString().equals("Sub") || node.toString().equals("Div") || node.toString().equals("Mul")){
            for(int i = 0; i < node.jjtGetNumChildren(); i++){
                if(!getNodeDataType(node.jjtGetChild(i)).equals(INTEGER)){
                    int line = ((SimpleNode) node).getCoords().getLine();
                    throw new IOException( "Line " + line + ": Arithmetic operation must be done with Integer values ");
                }
            }
            return "Integer";
        }
        else if(node.toString().equals("Length")){
            processLength(node);
            return "Integer";
        }
        else if(node.toString().equals("Less")){
            for(int i = 0; i < node.jjtGetNumChildren(); i++){
                if(!getNodeDataType(node.jjtGetChild(i)).equals("Integer")){
                    int line = ((SimpleNode) node).getCoords().getLine();
                    throw new IOException( "Line " + line + ": <(LESS) operation must be done with Integer values");
                }
            }
            return "Boolean";
        }
        else if(node.toString().equals("Not")){
            for(int i = 0; i < node.jjtGetNumChildren(); i++){
                if(!getNodeDataType(node.jjtGetChild(i)).equals("Boolean")){
                    int line = ((SimpleNode) node).getCoords().getLine();
                    throw new IOException( "Line " + line + ": !(NOT) operation must be done with Boolean values");
                }
            }
            return "Boolean";

        }
        else if(node.toString().equals("And")){
            for(int i = 0; i < node.jjtGetNumChildren(); i++){
                if(!getNodeDataType(node.jjtGetChild(i)).equals("Boolean")){
                    int line = ((SimpleNode) node).getCoords().getLine();
                    throw new IOException( "Line " + line + ": &&(AND) operation must be done with Boolean values");
                }
            }
            return "Boolean";
        }
        else if(node.toString().equals("Array")){
            return processArrayRight(node);
        }
        else if(node.toString().equals("This")){
            if(isStatic){
                int line = ((SimpleNode) node).getCoords().getLine();
                throw new IOException( "Line " + line + ": Cannot use 'this' in a static method.");
            }
            return this.symbolTable.getClassName();
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

        if(!varDescriptor.getDataType().equals("Array") && !varDescriptor.getDataType().equals("stringarray")){
            int line = ((SimpleNode) node).getCoords().getLine();
            throw new IOException( "Line " + line + ": Variable " + varDescriptor.getIdentifier() + " of type Array does not match declaration type " + varDescriptor.getDataType());
        }


        String type = getNodeDataType(node.jjtGetChild(1));

        if(!type.equals("Integer")){
            int line = ((SimpleNode) node).getCoords().getLine();
            throw new IOException( "Line " + line + ": Index of array " + id + " is not Integer!");
        }

        if(!getNodeDataType(node.jjtGetChild(1)).equals("Integer")){
            int line = ((SimpleNode) node).getCoords().getLine();
            throw new IOException( "Line " + line + ": Index of array " + id + " is not Integer!");
        }

        if(varDescriptor.getInitialized().equals(VarDescriptor.INITIALIZATION_TYPE.FALSE)){
            int line = ((SimpleNode) node).getCoords().getLine();
            throw new IOException( "Line " + line + ": Array " + id + " is not initialized");
        }
        else if(varDescriptor.getInitialized().equals(VarDescriptor.INITIALIZATION_TYPE.MAYBE)){
            int line = ((SimpleNode) node).getCoords().getLine();
            System.out.println("[WARNING]: Line " + line +": Variable " + varDescriptor.getIdentifier() + " may not have been initialized.");
        }
    
        if(varDescriptor.getDataType().equals("stringarray")){
            return "String";
        }
        else{
            return INTEGER;
        }

    }

    private void processArrayLeft(Node node) throws IOException{

        String id = Utils.parseName(node.jjtGetChild(0).jjtGetChild(0).toString());
        VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(id).get(0);

        if(!varDescriptor.getDataType().equals("Array")){
            int line = ((SimpleNode) node).getCoords().getLine();
            throw new IOException( "Line " + line + ": Variable " + varDescriptor.getIdentifier() + " of type Array does not match declaration type " + varDescriptor.getDataType());
        }

        String type = getNodeDataType(node.jjtGetChild(0).jjtGetChild(1));
        if(!type.equals("Integer")){
            int line = ((SimpleNode) node).getCoords().getLine();
            throw new IOException( "Line " + line + ": Index of array " + id + " is not Integer!");
        }

        if(!getNodeDataType(node.jjtGetChild(1)).equals("Integer")){
            int line = ((SimpleNode) node).getCoords().getLine();
            throw new IOException( "Line " + line + ": Index of array " + id + " is not Integer!");
        }

    }

    private void processArrayBoth(Node node) throws IOException{

        String id = Utils.parseName(node.jjtGetChild(0).jjtGetChild(0).toString());
        VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(id).get(0);

        if(!varDescriptor.getDataType().equals("Array")){
            int line = ((SimpleNode) node).getCoords().getLine();
            throw new IOException( "Line " + line + ": Variable " + varDescriptor.getIdentifier() + " of type Array does not match declaration type " + varDescriptor.getDataType());
        }

        String type = getNodeDataType(node.jjtGetChild(0).jjtGetChild(1));
        if(!type.equals("Integer")){
            int line = ((SimpleNode) node).getCoords().getLine();
            throw new IOException( "Line " + line + ": Index of array " + id + " is not Integer!");
        }
        //   ==============

        String id2 = Utils.parseName(node.jjtGetChild(1).jjtGetChild(0).toString());
        VarDescriptor varDescriptor2 = (VarDescriptor) symbolTable.lookup(id2).get(0);

        if(!varDescriptor2.getDataType().equals("Array")){
            int line = ((SimpleNode) node).getCoords().getLine();
            throw new IOException( "Line " + line + ": Variable " + varDescriptor2.getIdentifier() + " of type Array does not match declaration type " + varDescriptor2.getDataType());
        }

        String type2 = getNodeDataType(node.jjtGetChild(1).jjtGetChild(1));
        if(!type2.equals("Integer")){
            int line = ((SimpleNode) node).getCoords().getLine();
            throw new IOException( "Line " + line + ": Index of array " + id2 + " is not Integer!");
        }

    }

    private void processProgram(Node node) throws IOException{
        symbolTable.enterScopeForAnalysis(); // Enter Import Scope
        for (int i = 0; i < node.jjtGetNumChildren()-1; i++) { // Imports
            execute(node.jjtGetChild(i));
        }
        symbolTable.exitScopeForAnalysis(); // Leave Import Scope
        execute(node.jjtGetChild(node.jjtGetNumChildren()-1)); // Class

    }


    private void processNewScope(Node node) throws IOException{
        symbolTable.enterScopeForAnalysis();
        
        processChildren(node);
        if(!node.toString().equals("MethodInvocation") && Utils.analyzeRegex(node.toString(), "(Method\\[)(.)*(\\])") && !node.toString().equals("Method[main]")){
            if(!processReturnType(node)){
                int line = ((SimpleNode) node).getCoords().getLine();
                throw new IOException( "Line " + line + ": Method " + Utils.parseName(node.toString()) + " does not return expected type " + Utils.parseName(node.jjtGetChild(0).toString()));
            }
        }
        symbolTable.exitScopeForAnalysis();
    }

    private void processChildren(Node node) throws IOException{
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            execute(node.jjtGetChild(i));
        }
    }


    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }
}
