import java.util.ArrayList;
import descriptors.*;


@SuppressWarnings({"unchecked"})
public class TraverseAst{
    
    protected SimpleNode root;
    protected SymbolTable symbolTable;


    public TraverseAst(SimpleNode root, SymbolTable symbolTable){
        this.root = root;
        this.symbolTable = symbolTable;
    }

    public void execute(Node node){

        //TODO: what is value(?) Shouldn't we keep a variable for name of var and one to keep value when there is an assignment??
        //TODO: check if the variable already exists at stack
        if(node.toString().equals("VarDeclaration")){

            //System.out.println("Node ID: "+ node.getId() + " | " + " TYPE: " + node.toString() + " | "+" VARTYPE: " +node.jjtGetChild(0) + " | " +" Identifier: "+ node.jjtGetChild(1));

            String identifier = parseName(node.jjtGetChild(1).toString());
            VarDescriptor var_descriptor = new VarDescriptor(node.toString(), identifier);

            symbolTable.add(identifier, var_descriptor);

        }
        else if(node.toString().equals("StaticImport")){

            //TODO: do we really need this?
            ImportDescriptor descriptor = new ImportDescriptor();
            System.out.println("STATIC IS: " + node.toString()); //StaticImport
            symbolTable.add(node.toString(), descriptor);

        }
        else if(node.toString().equals("Class")){
            symbolTable.enterScope();
        }
        else if(node.toString().contains("Method")){
            symbolTable.enterScope();
            ArrayList<VarDescriptor> params = new ArrayList<>();
            Node paramList= node.jjtGetChild(1);
            Node currentNode;

            for(int i=0; i<paramList.jjtGetNumChildren(); i++){
                currentNode= paramList.jjtGetChild(i); //VarDeclaration
                VarDescriptor varDescriptor =new VarDescriptor(parseName(currentNode.jjtGetChild(0).toString()), parseName(currentNode.jjtGetChild(1).toString()));
                params.add(varDescriptor);
                symbolTable.add(node.toString(), varDescriptor);
            }

            MethodDescriptor methodDescriptor = new MethodDescriptor(parseName(node.toString()), parseName(node.jjtGetChild(0).toString()), params);
            symbolTable.add(node.toString(), methodDescriptor);
            // TODO: process body
            symbolTable.exitScope();

        }
        else{

            for (int i = 0; i < node.jjtGetNumChildren(); i++) {
                execute(node.jjtGetChild(i));
            }

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




