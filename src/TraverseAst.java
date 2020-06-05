import java.util.ArrayList;
import descriptors.*;
import java.io.IOException;


@SuppressWarnings({"unchecked"})
public class TraverseAst{
    
    protected SimpleNode root;
    protected SymbolTable symbolTable;
    static private String VOID = "void";
    


    public TraverseAst(SimpleNode root, SymbolTable symbolTable){
        this.root = root;
        this.symbolTable = symbolTable;
    }


    public void execute(Node node) throws IOException{
        if(node.toString().equals("Program")){
            processProgram(node);
        }
        else if(node.toString().equals("Assign")){
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
        else if(Utils.analyzeRegex(node.toString(), "(Class\\[)(.)*(\\])")){
            processClass(node);
        }
        else if(node.toString().equals("Method[main]")){
            processMain(node);
        }
        else if(!node.toString().equals("MethodInvocation") && Utils.analyzeRegex(node.toString(), "(Method\\[)(.)*(\\])")){
            processMethod(node);
        }
        else{
            for (int i = 0; i < node.jjtGetNumChildren(); i++) {
                execute(node.jjtGetChild(i));
            }
        }
    }

    private void processProgram(Node node) throws IOException{

        symbolTable.enterScope(); // Enter Import Scope

        // Hardcoded io.println(int): this is a built in function
        ArrayList<VarDescriptor> params =new ArrayList<>(); 
        params.add(new VarDescriptor("Integer", null));
        MethodDescriptor descriptor = new MethodDescriptor("println", "void", params, true);
        symbolTable.add("io", descriptor, true);

        for (int i = 0; i < node.jjtGetNumChildren()-1; i++) { // Imports
            execute(node.jjtGetChild(i));
        }

        symbolTable.exitScope(); // Leave Import Scope
        execute(node.jjtGetChild(node.jjtGetNumChildren()-1)); // Class
    }

    private void processVarDeclaration(Node node) throws IOException{

        String varType = Utils.parseName(node.jjtGetChild(0).toString());

        if(!varType.equals("int") && !varType.equals("boolean") && !varType.equals("Boolean") && !varType.equals("array") && !varType.equals(this.symbolTable.getClassName()) ){
            
            try{
            ClassDescriptor classDesc = (ClassDescriptor) this.symbolTable.lookup(varType).get(0);
            }
            catch(Exception e){
                int line = ((SimpleNode) node).getCoords().getLine();
                throw new IOException( "Line " + line + ": Type " + varType + " not recognized.");
            }

        }

        String identifier = Utils.parseName(node.jjtGetChild(1).toString());
        VarDescriptor var_descriptor = new VarDescriptor(Utils.parseName(node.jjtGetChild(0).toString()), identifier);
        symbolTable.add(identifier, var_descriptor);
       
    }

    private void processNonStaticImport(Node node){

        MethodDescriptor descriptor;

        switch(node.jjtGetNumChildren()){
            case 1: // Ex.: import List;
                descriptor= new MethodDescriptor(Utils.parseName(node.jjtGetChild(0).toString()), VOID, new ArrayList<>(), false);
                symbolTable.add(Utils.parseName(node.jjtGetChild(0).toString()), descriptor, true);
                return;
            case 2:
                if(Utils.analyzeRegex(node.jjtGetChild(1).toString(), "(Method\\[)(.)*(\\])")){ // Ex: import List.add(int); 

                    ArrayList<VarDescriptor> params = getMethodParams(node.jjtGetChild(1).jjtGetChild(0));
                    String returnType = getMethodReturnType(node.jjtGetChild(1));
                    String name = Utils.parseName(node.jjtGetChild(1).toString());

                    descriptor= new MethodDescriptor(name, returnType, params, false);
                    symbolTable.add(Utils.parseName(node.jjtGetChild(0).toString()), descriptor, true);

                    return;
                }
                else{ // Contructor with params - Ex: import List(int);
                    ArrayList<VarDescriptor> params = getMethodParams(node.jjtGetChild(1));

                    descriptor= new MethodDescriptor(Utils.parseName(node.jjtGetChild(0).toString()), VOID, params, false);
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

        String name = Utils.parseName(node.jjtGetChild(1).toString());
        MethodDescriptor descriptor = new MethodDescriptor(name, returnType, params, true);
        symbolTable.add(Utils.parseName(node.jjtGetChild(0).toString()), descriptor, true);
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

    private void processMethod(Node node) throws IOException{


        ArrayList<VarDescriptor> params = new ArrayList<>();
        Node paramList= node.jjtGetChild(1);
        Node currentNode;
        int localIndex = 1;

        for(int i=0; i<paramList.jjtGetNumChildren(); i++){

            currentNode= paramList.jjtGetChild(i); //VarDeclaration

            String varType = Utils.parseName(currentNode.jjtGetChild(0).toString());
            if(!varType.equals("int") && !varType.equals("boolean") && !varType.equals("Boolean") && !varType.equals("array") && !varType.equals(this.symbolTable.getClassName()) ){

                try{
                ClassDescriptor classDesc = (ClassDescriptor) this.symbolTable.lookup(varType).get(0);
                }
                catch(Exception e){
                    int line = ((SimpleNode) node).getCoords().getLine();
                    throw new IOException( "Line " + line + ": Type " + varType + " not recognized.");
                }

            }

            VarDescriptor varDescriptor =new VarDescriptor(Utils.parseName(currentNode.jjtGetChild(0).toString()), Utils.parseName(currentNode.jjtGetChild(1).toString()), localIndex++);
            params.add(varDescriptor);

        }

        MethodDescriptor methodDescriptor = new MethodDescriptor(Utils.parseName(node.toString()), Utils.parseName(node.jjtGetChild(0).toString()), params, false);
        
        symbolTable.add(this.symbolTable.getClassName(), methodDescriptor, true);
        symbolTable.enterScope();

        for(int i=0; i<params.size(); i++){
            params.get(i).setInitialized(VarDescriptor.INITIALIZATION_TYPE.TRUE);
            symbolTable.add(params.get(i).getIdentifier(), params.get(i));

        }

        for(int i=0; i<node.jjtGetChild(2).jjtGetNumChildren(); i++){
            execute(node.jjtGetChild(2).jjtGetChild(i));
        }

        symbolTable.exitScope();
    }

    private void processMain(Node node) throws IOException{

        ArrayList<VarDescriptor> params = new ArrayList<>();
        Node paramList= node.jjtGetChild(0);
        Node currentNode;
        int localIndex = 0;

        for (int i = 0; i < paramList.jjtGetNumChildren(); i++){
            if (paramList.jjtGetChild(i).toString().equals("VarDeclaration")) {
                currentNode = paramList.jjtGetChild(i);
                VarDescriptor varDescriptor =new VarDescriptor(Utils.parseName(currentNode.jjtGetChild(0).toString()), Utils.parseName(currentNode.jjtGetChild(1).toString()), localIndex++); //tyoe, identifier
                params.add(varDescriptor);
            }
           
        }

        MethodDescriptor methodDescriptor = new MethodDescriptor(Utils.parseName(node.toString()), "void", params, true);
        symbolTable.add(this.symbolTable.getClassName(), methodDescriptor, true);
        symbolTable.enterScope();

        for(int i=0; i<params.size(); i++){
            params.get(i).setInitialized(VarDescriptor.INITIALIZATION_TYPE.TRUE);
            symbolTable.add(params.get(i).getIdentifier(), params.get(i));
           
        }

        for(int i=0; i<node.jjtGetChild(1).jjtGetNumChildren(); i++){
            execute(node.jjtGetChild(1).jjtGetChild(i));
        }

        symbolTable.exitScope();
    }

    private void processClass(Node node) throws IOException{
        symbolTable.enterScope();
        this.symbolTable.setClassName(Utils.parseName(node.toString()));

        if(node.jjtGetNumChildren()!=0){
            if(Utils.analyzeRegex(node.jjtGetChild(0).toString(), "(Extends\\[)(.)*(\\])")){
                ClassDescriptor classDescriptor = new ClassDescriptor(this.symbolTable.getClassName());
                try{
                    ClassDescriptor parentClassDescriptor = (ClassDescriptor) this.symbolTable.lookup(Utils.parseName(node.jjtGetChild(0).toString())).get(0);
                    classDescriptor.setParentClass(parentClassDescriptor);
                    symbolTable.add(this.symbolTable.getClassName(), classDescriptor);
                }
                catch(Exception E){
                    System.out.println("Class " + Utils.parseName(node.jjtGetChild(0).toString()) + " that is extended by class " + this.symbolTable.getClassName() + " is not imported");
                }
                
            }
        }

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            execute(node.jjtGetChild(i));
        }

        symbolTable.exitScope();
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }
}



