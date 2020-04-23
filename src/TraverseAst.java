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

    //TODO: handle String[] args at method void main
    public void execute(Node node){

        if(node.toString().equals("VarDeclaration")){
            processVarDeclaration(node);
        }
        else if(node.toString().equals("StaticImport")){
            processStaticImport(node);
        }
        else if(node.toString().equals("NonStaticImport")){
            processNonStaticImport(node);
        }
        else if(node.toString().equals("Class")){
            processClass(node);
        }
        else if(node.toString().equals("Method[main]")){
            processMain(node);
        }
        else if(node.toString().contains("Method")){
            processMethod(node);
        }
        else{
            for (int i = 0; i < node.jjtGetNumChildren(); i++) {
                execute(node.jjtGetChild(i));
            }
        }
    }

    private void processVarDeclaration(Node node) {
        //System.out.println("Node ID: "+ node.getId() + " | " + " TYPE: " + node.toString() + " | "+" VARTYPE: " +node.jjtGetChild(0) + " | " +" Identifier: "+ node.jjtGetChild(1));

        String identifier = parseName(node.jjtGetChild(1).toString());
        VarDescriptor var_descriptor = new VarDescriptor(node.toString(), identifier);

        symbolTable.add(identifier, var_descriptor);
    }

    private void processNonStaticImport(Node node){
        symbolTable.enterScope();
        ImportDescriptor descriptor;
        switch(node.jjtGetNumChildren()){
            case 1: // Ex.: import List;
                descriptor= new ImportDescriptor(parseName(node.jjtGetChild(0).toString()), new ArrayList<>(), VOID);
                symbolTable.add(parseName(node.jjtGetChild(0).toString()), descriptor);
                //System.out.println("1 IMPORT ID: " + parseName(node.jjtGetChild(0).toString()));
                symbolTable.exitScope();
                return;
            case 2:
                if(node.jjtGetChild(1).toString().contains("Method")){ // Ex: import List.add(int);
                    ArrayList<String> params = getMethodParams(node.jjtGetChild(1).jjtGetChild(0));
                    String returnType = getMethodReturnType(node.jjtGetChild(1));
                    String name = parseName(node.jjtGetChild(0).toString())+ "." + parseName(node.jjtGetChild(1).toString());
                    descriptor= new ImportDescriptor(parseName(name), params, returnType);
                    symbolTable.add(name, descriptor);
                    //System.out.println("2 IMPORT ID: " + name + "  --- RETURN TYPE: " + returnType + "--- FIRST PARAM: " + params.size());
                    symbolTable.exitScope();
                    return;
                }
                else{ // Contructor with params - Ex: import List(int);
                    ArrayList<String> params = getMethodParams(node.jjtGetChild(1));
                    descriptor= new ImportDescriptor(parseName(node.jjtGetChild(0).toString()), params, VOID);
                    symbolTable.add(parseName(node.jjtGetChild(0).toString()), descriptor);
                    //System.out.println("3 IMPORT ID: " + parseName(node.jjtGetChild(0).toString())  + "--- FIRST PARAM: " + params.size());
                    symbolTable.exitScope();
                    return;
                }
        }

    }

    private ArrayList<String> getMethodParams(Node paramList) {
        ArrayList<String> params = new ArrayList<>();

        for (int i = 0; i < paramList.jjtGetNumChildren(); i++) {
            params.add(parseName(paramList.jjtGetChild(i).toString()));
        }
        return params;
    }

    private void processStaticImport(Node node) {
        symbolTable.enterScope();

        /*
        StaticImport = node
            Identifier[io]
            Method[println]
                ParamList
                    Type[int]
                    Type[String]
                Return 
                    Type[int]
        */
        //TODO: what to do if we have 'import static Map;'

        ArrayList<String> params = getMethodParams(node.jjtGetChild(1).jjtGetChild(0));
        String returnType = getMethodReturnType(node.jjtGetChild(1));


        String name = parseName(node.jjtGetChild(0).toString())+ "." + parseName(node.jjtGetChild(1).toString());
        ImportDescriptor descriptor = new ImportDescriptor(name, params, returnType);
        symbolTable.add(name, descriptor);
        //System.out.println("STATIC IMPORT ID: " + name + "  --- RETURN TYPE: " + returnType + "--- FIRST PARAM: " + params.size());
    
        
        symbolTable.exitScope();
            
    }

    private String getMethodReturnType(Node method) {
        String returnType;
        if(method.jjtGetNumChildren() == 2){

            if(method.jjtGetChild(1).jjtGetNumChildren()>0)
                returnType = parseName(method.jjtGetChild(1).jjtGetChild(0).toString()); //Method->Return->Type
            else
                returnType = VOID;
        }
        else{
            returnType = VOID;
        }
        return returnType;
    }

    private void processMethod(Node node) {
        symbolTable.enterScope();
        ArrayList<VarDescriptor> params = new ArrayList<>();
        Node paramList= node.jjtGetChild(1);
        Node currentNode;

        for(int i=0; i<paramList.jjtGetNumChildren(); i++){
            currentNode= paramList.jjtGetChild(i); //VarDeclaration
            VarDescriptor varDescriptor =new VarDescriptor(parseName(currentNode.jjtGetChild(0).toString()), parseName(currentNode.jjtGetChild(1).toString()));
            params.add(varDescriptor);
            symbolTable.add(parseName(currentNode.jjtGetChild(1).toString()), varDescriptor);
        }

        MethodDescriptor methodDescriptor = new MethodDescriptor(parseName(node.toString()), parseName(node.jjtGetChild(0).toString()), params);
        symbolTable.add(node.toString(), methodDescriptor);

        for(int i=0; i<node.jjtGetChild(2).jjtGetNumChildren(); i++){
            execute(node.jjtGetChild(2).jjtGetChild(i));
        }

        symbolTable.exitScope();
    }

    private void processMain(Node node) {
        symbolTable.enterScope();
        ArrayList<VarDescriptor> params = new ArrayList<>();
        Node paramList= node.jjtGetChild(0);
        Node currentNode;

        for(int i=0; i<paramList.jjtGetNumChildren(); i++){
            currentNode= paramList.jjtGetChild(i); //VarDeclaration
            VarDescriptor varDescriptor =new VarDescriptor(parseName(currentNode.jjtGetChild(0).toString()), parseName(currentNode.jjtGetChild(1).toString()));
            params.add(varDescriptor);
            symbolTable.add(node.toString(), varDescriptor);
        }

        MethodDescriptor methodDescriptor = new MethodDescriptor(parseName(node.toString()), null, params);
        symbolTable.add(node.toString(), methodDescriptor);

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




