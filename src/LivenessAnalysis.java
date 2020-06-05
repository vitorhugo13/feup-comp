import descriptors.ClassDescriptor;
import descriptors.Descriptor;
import descriptors.MethodDescriptor;
import descriptors.VarDescriptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class LivenessAnalysis {
    HashMap<Integer, InstructionNode> instructionHashMap;
    SymbolTable symbolTable;

    public LivenessAnalysis(SymbolTable symbolTable){
        this.symbolTable = symbolTable;
    }

    public void traverseTree(Node node){

        if (node.toString().equals("StaticImport") || node.toString().equals("NonStaticImport")) {
        }
        else if(node.toString().equals("Method[main]")){
            processNewScope(node);
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



    private HashSet<String> processIfStatement(Node node)throws IOException{

    }

    private String processStmtAssign(Node node, HashMap<String, VarDescriptor.INITIALIZATION_TYPE> changedToTrue) throws IOException{

    }

    private void processWhile(Node node)throws IOException{


    }

    private String processInvocation(Node node) throws IOException{


    }

    private String getSignatures(ClassDescriptor classDescriptor){

    }

    private String compareArgsAndParams(Node node, ClassDescriptor classDescriptor) throws IOException {

    }


    private void processAssign(Node node) throws IOException{

    }

    private void processObject(Node node) throws IOException {

    }

    private String processNewObjectMethodInvoke(Node node) throws IOException {

    }



    private void processInitializeArray(Node node) throws IOException{

    }

    private String getNodeDataType(Node node) throws IOException{

    }

    private String processArrayRight(Node node) throws IOException{
    }

    private void processArrayLeft(Node node) throws IOException{

    }

    private void processArrayBoth(Node node) throws IOException{

    }

    private void processProgram(Node node) throws IOException{

    }


    private void processNewScope(Node node) throws IOException{

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


}
