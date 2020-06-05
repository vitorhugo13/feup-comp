import descriptors.ClassDescriptor;
import descriptors.Descriptor;
import descriptors.MethodDescriptor;
import descriptors.VarDescriptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class LivenessAnalysis {

    private HashMap<Integer, InstructionNode> instructionHashMap;
    private SymbolTable symbolTable;
    private int instructionIndex;

    public LivenessAnalysis(SymbolTable symbolTable){
        this.symbolTable = symbolTable;
        this.instructionHashMap = new HashMap<>();
        this.instructionIndex = 0;
    }


    //TODO: work with scopes should be a good option when an assignment is complicated
    public void execute(Node node) throws IOException{

        System.out.println("Node name: " + node.toString());

        if(node.toString().equals("Program")){
            processProgram(node);
        }
        else if(node.toString().equals("Assign")){
            processAssign(node);
        }
        else if(node.toString().equals("VarDeclaration")){
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
        execute(node.jjtGetChild(node.jjtGetNumChildren()-1)); 
    }

  
    private void processAssign(Node node) throws IOException{
        updateIndex();
        InstructionNode instructionNode = new InstructionNode();
        String varDefiniton = Utils.parseName(node.jjtGetChild(0).toString());

        VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(varDefiniton).get(0);
        instructionNode.setDef(varDescriptor);

        System.out.println("============================\n");
        System.out.println("index: " + instructionIndex);
        System.out.println("Name of def: " + varDefiniton);

        //TODO: process right side of assignment to complete instructionNode
        instructionHashMap.put(instructionIndex, instructionNode);
    }



    private void processMethod(Node node) throws IOException{
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            execute(node.jjtGetChild(i));
        }
    }

    private void processMain(Node node) throws IOException{
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            execute(node.jjtGetChild(i));
        }
    }

    private void processClass(Node node) throws IOException{
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            execute(node.jjtGetChild(i));
        }
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    private void updateIndex(){
        this.instructionIndex = this.instructionIndex + 1;
    }
}
