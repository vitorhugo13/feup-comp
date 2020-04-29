import java.io.PrintWriter;
import java.io.File;

import java.util.ArrayList;

import descriptors.Descriptor;
import descriptors.ClassDescriptor;
import descriptors.MethodDescriptor;
import descriptors.VarDescriptor;


class Generator {

    private SymbolTable symbolTable;
    private int localIndex;
    private String mainClass;

    public Generator(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
    
    public void generate(Node root, String filename) {
        File file = new File(filename + ".j");
        try {
            PrintWriter out = new PrintWriter(filename + ".j", "UTF-8");
            
            symbolTable.enterScopeForAnalysis();
            symbolTable.exitScopeForAnalysis();

            processClass(root.jjtGetChild(root.jjtGetNumChildren() - 1), out);

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String execute(Node node, PrintWriter out) {

        String nodeName = node.toString();
        String ret = "";

        // METHOD INVOCATION
        if (nodeName.contains("MethodInvocation"))
            ret = processMethodInvocation(node, out);

        // ASSIGNMENT
        else if (nodeName.equals("Assign"))
            processAssignment(node, out);

        // NEW OBJECT
        else if (nodeName.equals("NewObject"))
            ret = processConstructorInvocation(node, out);

        // OPERATORS
        else if (nodeName.equals("And"))
            ret = processAnd(node, out);
        else if (nodeName.equals("Less"))
            ret = processLess(node, out);
        else if (nodeName.equals("Add"))
            ret = processAdd(node, out);
        else if (nodeName.equals("Sub"))
            ret = processSub(node, out);
        else if (nodeName.equals("Mul"))
            ret = processMul(node, out);
        else if (nodeName.equals("Div"))
            ret = processDiv(node, out);
        else if (nodeName.equals("Not"))
            ret = processNot(node, out);

        // TERMINALS
        else if (nodeName.contains("Integer"))
            ret = processInteger(node, out);
        else if (nodeName.contains("Boolean"))
            ret = processBoolean(node, out);
        else if (nodeName.contains("Identifier"))
            ret = processIdentifier(node, out);
        else if (nodeName.equals("This"))
            ret = processThis(node, out);
        else if (nodeName.equals("Array"))
            ret = processArray(node, out);

        return ret;
    }
    

    // ==========================================
    //             CLASS DECLARATION
    // ==========================================

    private void processClass(Node node, PrintWriter out) {
        symbolTable.enterScopeForAnalysis();
        mainClass = Utils.parseName(node.toString());

        // TODO: support extends
        out.println(String.format(".class public %s", mainClass));
        out.println(String.format(".super java/lang/Object"));
        out.println();
        
        for (VarDescriptor var : symbolTable.getClassAtributes())
            out.println(String.format(".field public %s %s", var.getIdentifier(), parseType(var.getDataType())));

        // TODO: support extension
        out.println(String.format(".method public<init>()V"));
        out.println(String.format("    aload 0"));
        out.println(String.format("    invokenonvirtual java/lang/Object/<init>()V"));
        out.println(String.format("    return"));
        out.println(String.format(".end method"));

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            Node child = node.jjtGetChild(i);
            String childName = child.toString();

            if (childName.contains("Method"))            
                processMethod(child, out);
        }

        symbolTable.exitScopeForAnalysis();
    }


    // ==========================================
    //             METHOD DECLARATION
    // ==========================================

    private void processMethod(Node node, PrintWriter out) {
        symbolTable.enterScopeForAnalysis();

        String name = Utils.parseName(node.toString());
        name = name.equals("main") ? "static main" : name;

        String type = node.jjtGetChild(0).toString().contains("Type") ? parseType(Utils.parseName(node.jjtGetChild(0).toString())) : "V";
        int childNum = type.equals("V") ? 0 : 1;
        
        String params = processParamList(node.jjtGetChild(childNum));
        localIndex = node.jjtGetChild(childNum).jjtGetNumChildren() + childNum;

        out.println();
        out.println(String.format(".method public %s(%s)%s", name, params, type));
        out.println(String.format("    .limit_stack 99"));
        out.println(String.format("    .limit_locals 99"));

        Node body = node.jjtGetChild(node.jjtGetNumChildren() - 1); 
        for (int i = 0; i < body.jjtGetNumChildren(); i++) {
            if (body.jjtGetChild(i).toString().equals("VarDeclaration"))
                continue;

            out.println();
            execute(body.jjtGetChild(i), out);
        }

        Node lastInstruction = body.jjtGetChild(body.jjtGetNumChildren() - 1);
        if (lastInstruction.toString().equals("Return")) {
            for (int i = 0; i < lastInstruction.jjtGetNumChildren(); i++)
                execute(lastInstruction.jjtGetChild(i), out);
        }
        
        out.println(String.format("    %sreturn", (type.equals("V") ? "" : (type.equals("I") ? "i" : "a"))));
        out.println(String.format(".end method"));

        symbolTable.exitScopeForAnalysis();
    }

    private String processParamList(Node node) {
        String paramList = "";
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            paramList += parseType(Utils.parseName(node.jjtGetChild(i).jjtGetChild(0).toString()));
        }
        return paramList;
    }


    // ==========================================
    //            METHOD INVOCATIONS
    // ==========================================

    private String processMethodInvocation(Node node, PrintWriter out) {

        String className = execute(node.jjtGetChild(0), out);
        String methodName = Utils.parseName(node.jjtGetChild(1).toString());
        String argList = processArgList(node.jjtGetChild(2), out);
        String type = "";
        boolean isStatic = false;

        try {
            ClassDescriptor classDescriptor = (ClassDescriptor) symbolTable.lookup(className).get(0);

            ArrayList<MethodDescriptor> methods = classDescriptor.getMethodsMatchingId(methodName);
            MethodDescriptor method = methods.get(0);

            for (int i = 1; i < methods.size(); i++) {
                String signature = "";
                for (VarDescriptor var : method.getParameters()) {
                    signature += parseType(var.getDataType());
                }

                if (argList.equals(signature))
                    break;
            }

            type = parseType(method.getReturnType());
            isStatic = method.isStatic();
        } catch (Exception e) {}
        
        if (isStatic) 
            out.println(String.format("    invokestatic %s/%s(%s)%s", className, methodName, argList, type));
        else
            out.println(String.format("    invokevirtual %s/%s(%s)%s", className, methodName, argList, type));

        return type;
    }

    private String processArgList(Node node, PrintWriter out) {
        String ret = "";

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            ret += parseType(execute(node.jjtGetChild(i), out));
        }

        return ret;
    }

    private String processConstructorInvocation(Node node, PrintWriter out) {

        String className = processIdentifier(node.jjtGetChild(0), out);

        out.println(String.format("    new %s", className));
        out.println(String.format("    dup"));
        out.println(String.format("    invokespecial %s/<init>()V", className)); 

        return "V";
    }


    // ==========================================
    //                ASSIGNMENT
    // ==========================================

    public void processAssignment(Node node, PrintWriter out) {

        // get the right part of the assignment
        execute(node.jjtGetChild(1), out);

        String nodeName = node.jjtGetChild(0).toString();
        String identifier = ""

        if (nodeName.contains("Array")) {
            Node array = node.jjtGetChild(0);
            identifier = Utils.parseName(array.jjtGetChild(0));
            // push the reference onto the stack
            execute(array.jjtGetChild(1), out);
        }
        else if (nodeName.contains("Identifier")) {
            identifier = Utils.parseName(nodeName);
        }

        VarDescriptor var = null;
        try {
            var = (VarDescriptor) symbolTable.lookup(identifier).get(0);
        } catch (Exception e) {}

        if (var.getScope() == Descriptor.Scope.GLOBAL) {
            out.println(String.format("    putfield %s/%s", mainClass, varName));
        }
        else {
            if (var.getLocalIndex() == -1)
                var.setLocalIndex(localIndex++);
                
            String type = parseType(var.getDataType());

            else if (type.equals("[I"))
                out.println(String.format("    iastore"));
            else
                out.println(String.format("    %sstore %d", type.equals("I") ? "i" : "a", var.getLocalIndex()));
        }
    }


    // ==========================================
    //                 OPERATORS
    // ==========================================

    private String processAnd(Node node, PrintWriter out) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++)
            execute(node.jjtGetChild(i), out);
        
        out.println(String.format("    iand"));

        return "I";
    }
    
    private String processLess(Node node, PrintWriter out) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++)
            execute(node.jjtGetChild(i), out);
        
        out.println(String.format("    iflt ltTrue"));
        out.println(String.format("    iconst_0"));
        out.println(String.format("    goto ltFalse"));
        out.println(String.format("  ltTrue:"));
        out.println(String.format("    iconst_1"));
        out.println(String.format("  ltFalse:"));
        
        return "Z";
    }

    private String processNot(Node node, PrintWriter out) {
        execute(node.jjtGetChild(0), out);

        out.println(String.format("    iconst_1"));
        out.println(String.format("    ixor"));

        return "Z";
    }
        
    private String processAdd(Node node, PrintWriter out) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++)
            execute(node.jjtGetChild(i), out);
        
        out.println(String.format("    iadd"));
        
        return "I";
    }

    private String processSub(Node node, PrintWriter out) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++)
            execute(node.jjtGetChild(i), out);
        
        out.println(String.format("    isub"));
     
        return "I";
    }
    
    private String processMul(Node node, PrintWriter out) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++)
            execute(node.jjtGetChild(i), out);
        
        out.println(String.format("    imul"));
     
        return "I";
    }

    private String processDiv(Node node, PrintWriter out) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++)
            execute(node.jjtGetChild(i), out);
        
        out.println(String.format("    idiv"));
     
        return "I";
    }
    

    // ==========================================
    //                 TERMINALS
    // ==========================================

    private String processArray(Node node, PrintWriter out) {

        try {
            VarDescriptor var = (VarDescriptor) symbolTable.lookup(node.jjtGetChild(0)).get(0);
            local = var.getLocalIndex();
            // push the array reference
            // push the index
        } catch (Exception e) {}
        
        // array ref -> Utils.parseName(node.jjtGetChild(1).toString());
        // array index -> var.getLocalIndex();
        out.println(String.format("    iaload"))

        return "[I";
    }

    private String processInteger(Node node, PrintWriter out) {
        String value = Utils.parseName(node.toString());
        int val = Integer.parseInt(value);
        String instruction = "";

        if (val >= 0 && val <= 5)
            instruction = "iconst_";
        else if (val <= 127)
            instruction = "bipush ";
        else if (val <= 32767)
            instruction = "ldc ";
        
        out.println(String.format("    %s%s", instruction, value));

        return "I";
    }

    private String processIdentifier(Node node, PrintWriter out) {
        String nodeName = Utils.parseName(node.toString());
        Descriptor descriptor = null;
        String type = nodeName;

        try {
            descriptor = symbolTable.lookup(nodeName).get(0);
        } catch (Exception e) {}

        // in case the identifier represents a variable
        // there is the need to load the value onto the stack
        if (descriptor.getType() == Descriptor.Type.VAR) {
            VarDescriptor var = (VarDescriptor) descriptor;
            
            // for global variables AKA class atributes
            if (var.getScope() == Descriptor.Scope.GLOBAL) {
                out.println(String.format("    getfield %s/%s", mainClass, nodeName));
            }
            // for local variables
            else {
                out.println(String.format("    %sload %d", type.equals("I") ? "i" : "a", var.getLocalIndex()));
            }

            type = parseType(var.getDataType());
        }

        return type;
    }

    private String processBoolean(Node node, PrintWriter out) {
        String value = Utils.parseName(node.toString());
        String instruction = "";

        if (value.equals("true"))
            instruction = "iconst_1";
        else
            instruction = "iconst_0";

        out.println(String.format("    %s", instruction));

        return "Z";
    }

    private String processThis(Node node, PrintWriter out) {
        out.println(String.format("    aload 0"));

        return mainClass;
    }


    // ==========================================
    //                 UTILITIES
    // ==========================================

    private String parseType(String type) {
        String ret = "";

        if (type.equals("int") || type.equals("Integer"))
            ret = "I";
        else if (type.equals("boolean"))
            ret = "Z";
        else if (type.equals("array") || type.equals("Array"))
            ret = "[I";
        else if (type.equals("stringarray"))
            ret = "[L/java/lang/String;";
        else if (type.equals("String"))
            ret = "L/java/lang/String;";
        else if (type.equals("void"))
            ret = "V";
        else 
            ret = type;

        return ret;
    }
}

