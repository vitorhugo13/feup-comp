import java.io.PrintWriter;
import java.io.File;


class Generator {

    private SymbolTable symbolTable;

    public Generator(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
    
    public void generate(Node root, String filename) {
        File file = new File(filename + ".j");
        try {
            PrintWriter out = new PrintWriter(filename + ".j", "UTF-8");

            execute(root, out);

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void execute(Node node, PrintWriter out) {

        String nodeName = node.toString();
        if (nodeName.equals("Program")) {
            processProgram(node, out);
        }
        else if (nodeName.contains("Class")) {
            processClass(node, out);
        }
        else if (nodeName.equals("VarDeclaration")) {
            processVarDeclaration(node, out);
        }
        else if (nodeName.contains("MethodInvocation")) {
            processMethodInvocation(node, out);
        }
        else if (nodeName.contains("Method")) {
            processMethod(node, out);
        }

        // OPERATORS
        else if (nodeName.equals("And")) {
            processAnd(node, out);
        }
        else if (nodeName.equals("Less")) {
            processLess(node, out);
        }
        else if (nodeName.equals("Add")) {
            processAdd(node, out);
        }
        else if (nodeName.equals("Sub")) {
            processSub(node, out);
        }
        else if (nodeName.equals("Mul")) {
            processMul(node, out);
        }
        else if (nodeName.equals("Div")) {
            processDiv(node, out);
        }
        else if (nodeName.equals("Not")) {
            processNot(node, out);
        }
    }

    private void processProgram(Node node, PrintWriter out) {
        execute(node.jjtGetChild(node.jjtGetNumChildren() - 1), out);   // Class
    }

    private void processClass(Node node, PrintWriter out) {
        symbolTable.enterScopeForAnalysis();                        // Enter Import Scope

        // TODO: the vardeclarations are in the symbol table

        // TODO: support extends
        out.println(String.format(".class public %s", Utils.parseName(node.toString())));
        out.println(String.format(".super java/lang/Object"));

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            execute(node.jjtGetChild(i), out);
        }

        symbolTable.exitScopeForAnalysis();                             // Leave Import Scope
    }

    private void processVarDeclaration(Node node, PrintWriter out) {
        // FIXME: the inClassScope method may change
        // for class fields
        if (symbolTable.inClassScope()) {
            out.println(String.format(".field public %s %s", 
                Utils.parseName(node.jjtGetChild(1).toString()), 
                getDescriptor(Utils.parseName(node.jjtGetChild(0).toString()))
                ));
        }
        // for method variables and parameters
        else {

        }
    }

    private void processMethod(Node node, PrintWriter out) {
        symbolTable.enterScopeForAnalysis();                        // Enter Import Scope

        String name = Utils.parseName(node.toString());
        name = name.equals("main") ? "static main" : name;
        // TODO: access symbol table to get return type and params

        out.println(String.format(".method public %s(%s)%s", name, "TODO", "TODO"));
        out.println(String.format(".limit_stack 99"));
        out.println(String.format(".limit_locals 99"));

        // TODO: deal with the parameters according to the information from the symbol table

        // TODO: the vardeclarations are in the symbol table

        // TODO: according to the return type decide the return directive
        // areturn -> return an object
        // ireturn -> return an integer
        // return -> return void

        out.println(".end method");

        symbolTable.exitScopeForAnalysis();                         // Leave Import Scope
    }

    private void processMethodInvocation(Node node, PrintWriter out) {
        // TODO:
    }


    private String getDescriptor(String type) {
        String type = Utils.parseName(node.toString());

        if (type.equals("int"))
            return "I";
        else if (type.equals("boolean"))
            return "Z";
        else if (type.equals("void"))
            return "V";
        // FIXME: this is not a generic approach to the array problem
        // bidimensional arrays are not supported by this
        // hint: use the symbol table
        else if (type.equals("array"))
            return "[I";
        else if (type.equals("stringarray"))
            return "[L/java/lang/String";
        
        return "";
    }
}