import java.io.PrintWriter;
import java.io.File;


class Generator {

    private SymbolTable symbolTable;
    private int localIndex;

    public Generator(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
    
    public void generate(Node root, String filename) {
        File file = new File(filename + ".j");
        try {
            PrintWriter out = new PrintWriter(filename + ".j", "UTF-8");

            execute(root.jjtGetChild(root.jjtGetNumChildren() - 1), out);

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void execute(Node node, PrintWriter out) {

        String nodeName = node.toString();

        // CLASS DECLARATION
        else if (nodeName.contains("Class"))
            processClass(node, out);

        // METHOD INVOCATION
        else if (nodeName.contains("MethodInvocation"))
        processMethodInvocation(node, out);
        
        // METHOD DECLARATION
        else if (nodeName.contains("Method"))
            processMethod(node, out);

        else if (nodeName.equals("Assign")) {
            processAssignment(node, out);
        }

        else if (nodeName.equals("NewObject")) {
            processConstructorInvocation(node, out);
        }

        // OPERATORS
        // else if (nodeName.equals("And"))
        //     processAnd(node, out);
        // else if (nodeName.equals("Less"))
        //     processLess(node, out);
        // else if (nodeName.equals("Add"))
        //     processAdd(node, out);
        // else if (nodeName.equals("Sub"))
        //     processSub(node, out);
        // else if (nodeName.equals("Mul"))
        //     processMul(node, out);
        // else if (nodeName.equals("Div"))
        //     processDiv(node, out);
        // else if (nodeName.equals("Not"))
        //     processNot(node, out);

        // TERMINALS
        else if (nodeName.contains("Integer"))
            processInteger(node, out);
        else if (nodeName.contains("Boolean"))
            processBoolean(node, out);
        else if (nodeName.contains("Identifier"))
            processIdentifier(node, out);
    }
    

    // ==========================================
    //             CLASS DECLARATION
    // ==========================================

    private void processClass(Node node, PrintWriter out) {
        symbolTable.enterScopeForAnalysis();                        // Enter Import Scope

        // TODO: support extends
        out.println(String.format(".class public %s", Utils.parseName(node.toString())));
        out.println(String.format(".super java/lang/Object"));

        // TODO: get the attributes from the table
        // for each
            // out.println(String.format(".field public %s %s", name, type));

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            execute(node.jjtGetChild(i), out);
        }

        symbolTable.exitScopeForAnalysis();                             // Leave Import Scope
    }

    private void processMethod(Node node, PrintWriter out) {
        symbolTable.enterScopeForAnalysis();

        String name = Utils.parseName(node.toString());
        name = name.equals("main") ? "static main" : name;

        // FIXME: hardcoded for now, change for lookup
        String params = "II";
        String type = "V";
        localIndex = 1;
        
        out.println(String.format(".method public %s(%s)%s", name, params, type));
        out.println(String.format(".limit_stack 99"));
        out.println(String.format(".limit_locals 99"));

        Node body = node.jjtGetChild(node.jjtGetNumChildren() - 1); 
        for (int i = 0; i < body.jjtGetNumChildren(); i++) {
            execute(body.jjtGetChild(i), out);
        }

        Node lastInstruction = body.jjtGetChild(body.jjtGetNumChildren() - 1);
        if (lastInstruction.toString().equals("Return")) {
            for (int i = 0; i < lastInstruction.jjtGetNumChildren(); i++)
                execute(lastInstruction.jjtGetChild(i), out);
        }
        
        out.println(String.format("%sreturn"), (type.equals("V") ? "" : (type.equals("I") ? "i" : "a")));
        out.println(String.format(".end method"));

        symbolTable.exitScopeForAnalysis();
    }

    private void processMethodInvocation(Node node, PrintWriter out) {
        // TODO:

        // load the object to invoke the method

        // handle the arguments
        
        execute(node.jjtGetChild(2), out);

        // get the information about the method from the table
        // if the method is static
            // out.println(String.format("invokestatic %s(%s)%s", name, paramlist, returntype));
        // else
            // out.println(String.format("invokevirtual %s(%s)%s", name, paramList, returnType));
    }

    private void processConstructorInvocation(Node node, PrintWriter out) {

        // get the full name of the class

        out.println(String.format("invokespecial %s/<init>()V", name));
    }

    public void processAssignment(Node node, PrintWriter out) {
        String identifier = Utils.parseName(node.jjtGetChild(0).toString());
        // lookup the identifier on the table
        // get the type of the var

        // execute(node.jjtGetChild(1), out);

        // if the var is a field
            // out.println(String.formater("putfield %s/%s", className, fieldName));
        // else
            // out.println(String.format("%s %d", type.equals("I") ? "istore" : "astore", localIndex++));
            // associate the localIndex with the var identifier
            // two options ? add to symbol table : create a new hashmap
    }





    // ==========================================
    //                 TERMINALS
    // ==========================================

    private void processInteger(Node node, PrintWriter out) {
        String value = Utils.parseName(node.toString());
        int val = Integer.parseInt(value);
        String instruction = "";

        if (val >= 0 && val <= 5)
            instruction = "iconst_";
        else if (val <= 127)
            instruction = "bipush ";
        else if (val <= 32767)
            instruction = "ldc ";
        
        out.println(String.format("%s%s", instruction, value));
    }

    private void processIdentifier(Node node, PrintWriter out) {
        String varName = Utils.parseName(node.toString());

        // check the symbol table
        // get the var type
        
        // if it is a field
            // get the class name
            // out.println(String.format("getfield %s/%s", className, varName));
        // else
            // get the local index from the symbol table
            // out.println(String.format("%sload %d", type.equals("I") ? "i" : "a", index));
    }

    private void processBoolean(Node node, PrintWriter out) {
        String value = Utils.parseName(node.toString());
        String instruction = "";

        if (value.equals("true"))
            instruction = "iconst_1";
        else
            instruction = "iconst_0";

        out.println(instruction);
    }

}