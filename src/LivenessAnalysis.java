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
        this.instructionIndex = -1;
    }


    //TODO: work with scopes should be a good option when an assignment is complicated
    public void execute(Node node) throws IOException{

        //System.out.println("Node name: " + node.toString());

        if(node.toString().equals("Program")){
            processProgram(node);
        }
        else if(node.toString().equals("Assign")){
            processAssign(node);
        }
        else if(node.toString().equals("IfStatement")){
            processIfStmt(node);
        }
        else if(node.toString().equals("While")){
            processWhile(node);
        }
        else if(node.toString().equals("Return")){
            processReturn(node);
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

    private void processWhile(Node node) throws IOException{
        //condition
        updateIndex();

        System.out.println("============================");
        System.out.println("index: " + instructionIndex);
        InstructionNode instructionCondition = new InstructionNode();

        HashSet<VarDescriptor> usedVariables = getUsedVariables(node.jjtGetChild(0).jjtGetChild(0));

        if(usedVariables.size() > 0)
            System.out.println("Used Variables");

        for(VarDescriptor var : usedVariables){
            System.out.println("Var: " + var.getIdentifier());
        }
        instructionCondition.setUse(usedVariables);
        instructionHashMap.put(instructionIndex, instructionCondition);

        for(int i = 1; i < node.jjtGetNumChildren();i++){
            execute(node.jjtGetChild(i));
        }
    }

    private void processIfStmt(Node node) throws IOException{

        //condition
        updateIndex();

        System.out.println("============================");
        System.out.println("index: " + instructionIndex);
        InstructionNode instructionCondition = new InstructionNode();

        HashSet<VarDescriptor> usedVariables1 = getUsedVariables(node.jjtGetChild(0).jjtGetChild(0));

        if(usedVariables1.size() > 0)
            System.out.println("Used Variables");

        for(VarDescriptor var : usedVariables1){
            System.out.println("Var: " + var.getIdentifier());
        }
        instructionCondition.setUse(usedVariables1);
        instructionHashMap.put(instructionIndex, instructionCondition);

        for(int i = 1; i < node.jjtGetNumChildren();i++){
            execute(node.jjtGetChild(i));
        }
    }

    private void processReturn(Node node) throws IOException{

        updateIndex();

        System.out.println("============================");
        System.out.println("index: " + instructionIndex);
        InstructionNode instructionNode = new InstructionNode();

        HashSet<VarDescriptor> usedVariables = getUsedVariables(node.jjtGetChild(0));

        if(usedVariables.size() > 0)
            System.out.println("Used Variables");

        for(VarDescriptor var : usedVariables){
            System.out.println("Var: " + var.getIdentifier());
        }

        instructionNode.setUse(usedVariables);
        instructionHashMap.put(instructionIndex, instructionNode);

    }

    private void processAssign(Node node) throws IOException{

        updateIndex();

        System.out.println("============================");
        System.out.println("index: " + instructionIndex);
        InstructionNode instructionNode = new InstructionNode();
        HashSet<VarDescriptor> usedLeftVariables = new HashSet<>();

        //LeftSide
        if(node.jjtGetChild(0).toString().equals("Array")){
            usedLeftVariables = getUsedVariables(node.jjtGetChild(0));
        }
        else{
            String varDefiniton = Utils.parseName(node.jjtGetChild(0).toString());
            VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(varDefiniton).get(0);
            instructionNode.setDef(varDescriptor);
            
            System.out.println("Name of def: " + varDefiniton);
        }

        
        //RightSide
        HashSet<VarDescriptor> usedVariables = getUsedVariables(node.jjtGetChild(1));
        usedVariables.addAll(usedLeftVariables);

        if(usedVariables.size() > 0)
            System.out.println("Used Variables");

        for(VarDescriptor var : usedVariables){
            System.out.println("Var: " + var.getIdentifier());
        }

        instructionNode.setUse(usedVariables);
        instructionHashMap.put(instructionIndex, instructionNode);
    }

    private HashSet<VarDescriptor> getUsedVariables(Node node) throws IOException{

        HashSet<VarDescriptor> usedVariables = new HashSet<>();
        if(Utils.analyzeRegex(node.toString(), "(Identifier\\[)(.)*(\\])")){ //IDENTIFIER[a]
            VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(Utils.parseName(node.toString())).get(0);
            usedVariables.add(varDescriptor);
        }
        else if(node.toString().equals("NewIntArray")){
            for(int i = 0; i < node.jjtGetNumChildren(); i++){
                usedVariables.addAll(getUsedVariables(node.jjtGetChild(i)));
            }
        }
        else if(node.toString().equals("Add") || node.toString().equals("Sub") || node.toString().equals("Div") || node.toString().equals("Mul") || node.toString().equals("Less") || node.toString().equals("Not") || node.toString().equals("And")){
            for(int i = 0; i < node.jjtGetNumChildren(); i++){
                usedVariables.addAll(getUsedVariables(node.jjtGetChild(i)));
            }
        }
        else if(node.toString().equals("Array")){
            VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(Utils.parseName(node.jjtGetChild(0).toString())).get(0);
            usedVariables.add(varDescriptor);
            for(int i = 0; i < node.jjtGetChild(1).jjtGetNumChildren(); i++){
                usedVariables.addAll(getUsedVariables(node.jjtGetChild(1).jjtGetChild(i)));
            }
        }
        else if(node.toString().equals("This")){
            
        }
        else if(node.toString().equals("MethodInvocation")){
            try{
                VarDescriptor varDescriptor = (VarDescriptor) symbolTable.lookup(Utils.parseName(node.jjtGetChild(0).toString())).get(0);
                usedVariables.add(varDescriptor);
            }catch(Exception e){

            }

            for(int i = 0; i < node.jjtGetChild(2).jjtGetNumChildren(); i++){
                usedVariables.addAll(getUsedVariables(node.jjtGetChild(2).jjtGetChild(i)));
            }
        }
        else{ // INTEGER[2]

        }

        return usedVariables;
    }

    private void processMethod(Node node) throws IOException{
        symbolTable.enterScopeForAnalysis();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            execute(node.jjtGetChild(i));
        }
        symbolTable.exitScopeForAnalysis();
    }

    private void processMain(Node node) throws IOException{
        symbolTable.enterScopeForAnalysis();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            execute(node.jjtGetChild(i));
        }
        symbolTable.exitScopeForAnalysis();
    }

    private void processClass(Node node) throws IOException{
        symbolTable.enterScopeForAnalysis(); //Scope Imports does not matter for this analysis
        symbolTable.exitScopeForAnalysis();


        symbolTable.enterScopeForAnalysis();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            execute(node.jjtGetChild(i));
        }
        symbolTable.exitScopeForAnalysis();
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    private void updateIndex(){
        this.instructionIndex = this.instructionIndex + 1;
    }

    public void printInstructionHash(){
        System.out.println("\n=============Instruction state ==============");
        for(int i = 0; i < instructionHashMap.size(); i++){
            System.out.println(instructionHashMap.get(i));
        }
        System.out.println("\n");

    }
    
    public void calculateLivenessAnalysis(){
        HashSet<VarDescriptor> newLiveIn = new HashSet();
        HashSet<VarDescriptor> newLiveOut = new HashSet();
        HashSet<VarDescriptor> outNotDef = new HashSet();

        boolean isFinished;

        do{
            isFinished = true;
            for(int i=1; i<=instructionHashMap.size(); i++){

                newLiveIn = instructionHashMap.get(i).getLiveIn();
                newLiveOut = instructionHashMap.get(i).getLiveOut();

                //Update Live In
                instructionHashMap.get(i).getLiveIn().addAll(instructionHashMap.get(i).getUse());
                outNotDef = new HashSet<>(instructionHashMap.get(i).getLiveOut());
                outNotDef.remove(instructionHashMap.get(i).getDef());
                instructionHashMap.get(i).getLiveIn().addAll(outNotDef);

                //Update Live Out
                /*
                for(InstructionNode instructionNode : instructionHashMap.get(i).getSuccessors()){
                    instructionHashMap.get(i).getLiveOut().addAll(instructionNode.getLiveIn());
                }
                 */

                //TODO: uncomment the previous lines
                if(i==instructionHashMap.size())
                    instructionHashMap.get(i).getLiveOut().addAll(instructionHashMap.get(1).getLiveIn());
                else
                    instructionHashMap.get(i).getLiveOut().addAll(instructionHashMap.get(i+1).getLiveIn());

                if(!(newLiveIn.equals(instructionHashMap.get(i).getLiveIn()) && newLiveOut.equals(instructionHashMap.get(i).getLiveOut()))){
                    isFinished=false;
                }
            }
        }
        while(!isFinished);

        for(int i=1; i<=instructionHashMap.size(); i++){

            System.out.println("Live in: ");
            for(VarDescriptor var: instructionHashMap.get(i).getLiveIn()){
                System.out.println(var.getIdentifier());
            }
            System.out.println("Live out: " + instructionHashMap.get(i).getLiveOut());
            for(VarDescriptor var: instructionHashMap.get(i).getLiveOut()){
                System.out.println(var.getIdentifier());
            }

        }
    }
}
