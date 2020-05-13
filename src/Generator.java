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
    private int tagIndex;
    private String mainClass;

    private int ifIndex;
    private int whileIndex;

    public Generator(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        this.tagIndex = 0;
        this.ifIndex = 0;
        this.whileIndex = 0;
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
        // NEW INT ARRAY
        else if (nodeName.equals("NewIntArray"))
            ret = processNewIntArray(node, out);

        // IF STATEMENT
        else if (nodeName.equals("IfStatement"))
            processIf(node, out);
        // WHILE LOOP
        else if (nodeName.equals("While"))
            processWhile(node, out);

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
        else if (nodeName.equals("Length"))
            ret = processLength(node, out);

        return ret;
    }
    

    // ==========================================
    //             CLASS DECLARATION
    // ==========================================

    private void processClass(Node node, PrintWriter out) {
        symbolTable.enterScopeForAnalysis();
        mainClass = Utils.parseName(node.toString());

        String extension = node.jjtGetChild(0).toString();
        extension = extension.contains("Extends") ? Utils.parseName(extension) : "";

        out.println(String.format(".class public %s", mainClass));
        out.println(String.format(".super %s", extension.equals("") ? "java/lang/Object" : extension));
        out.println();

        ArrayList<VarDescriptor> attributes = symbolTable.getClassAtributes();
        for (int i = attributes.size() - 1; i >= 0; i--) {
            VarDescriptor var = attributes.get(i);
            out.println(String.format(".field public %s %s", var.getIdentifier(), parseType2(var.getDataType())));
        }

        out.println();
        out.println(String.format(".method public <init>()V"));
        out.println(String.format("    aload_0"));
        out.println(String.format("    invokenonvirtual %s/<init>()V", extension.equals("") ? "java/lang/Object" : extension));
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
        out.println(String.format("    .limit stack 99"));
        out.println(String.format("    .limit locals 99"));

        Node body = node.jjtGetChild(node.jjtGetNumChildren() - 1); 
        for (int i = 0; i < body.jjtGetNumChildren(); i++) {
            if (body.jjtGetChild(i).toString().equals("VarDeclaration"))
                continue;

            out.println();
            execute(body.jjtGetChild(i), out);
        }

        Node lastInstruction = body.jjtGetChild(body.jjtGetNumChildren() - 1);
        out.println();
        if (lastInstruction.toString().equals("Return")) {
            for (int i = 0; i < lastInstruction.jjtGetNumChildren(); i++)
                execute(lastInstruction.jjtGetChild(i), out);
        }
        
        out.println(String.format("    %sreturn", (type.equals("V") ? "" : (type.equals("I") || type.equals("Z") ? "i" : "a"))));
        out.println(String.format(".end method"));

        symbolTable.exitScopeForAnalysis();
    }

    private String processParamList(Node node) {
        String paramList = "";
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            paramList += parseType2(Utils.parseName(node.jjtGetChild(i).jjtGetChild(0).toString()));
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
            ret += parseType2(execute(node.jjtGetChild(i), out));
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

    private String processNewIntArray(Node node, PrintWriter out) {
        execute(node.jjtGetChild(0), out);
        out.println(String.format("    newarray int"));

        return "[I";
    }


    // ==========================================
    //                ASSIGNMENT
    // ==========================================

    public void processAssignment(Node node, PrintWriter out) {
        String nodeName = node.jjtGetChild(0).toString();
        String instruction = ""; 
        String identifier = "";

        if (nodeName.equals("Array"))
            identifier = Utils.parseName(node.jjtGetChild(0).jjtGetChild(0).toString());
        else if (nodeName.contains("Identifier"))
            identifier = Utils.parseName(nodeName);

        VarDescriptor var = null;
        try {
            var = (VarDescriptor) symbolTable.lookup(identifier).get(0);
        } catch (Exception e) {}

        // class atributes
        if (var.getScope() == Descriptor.Scope.GLOBAL) {
            out.println("    aload_0");
            if (nodeName.equals("Array")) {
                out.println(String.format("    getfield %s/%s %s", mainClass, identifier, parseType2(var.getDataType())));
                execute(node.jjtGetChild(0).jjtGetChild(1), out);
                instruction = "iastore";
            }
            else
                instruction = String.format("putfield %s/%s %s", mainClass, identifier, parseType2(var.getDataType()));
            
        }
        
        // local variables
        else {
            if (var.getLocalIndex() == -1)
                var.setLocalIndex(localIndex++);
                
            String type = parseType(var.getDataType());

            if (nodeName.equals("Array")) {
                out.println(String.format("    %s", load(parseType(var.getDataType()), var.getLocalIndex())));
                execute(node.jjtGetChild(0).jjtGetChild(1), out);        // element index
                instruction = String.format("iastore");
            }
            else
                instruction = store(type, var.getLocalIndex());
        }

        // process the right side of the expression, push value to stack
        execute(node.jjtGetChild(1), out);

        out.println(String.format("    %s", instruction));
    }


    // ==========================================
    //             CONTROL STRUCTURES
    // ==========================================

    private void processIf(Node node, PrintWriter out) {
        // condition
        processCondition(node.jjtGetChild(0), out);
        out.println(String.format("    ifeq else_%d", this.ifIndex));

        // if
        processScope(node.jjtGetChild(1), out);
        out.println(String.format("    goto endif_%d", this.ifIndex));
        
        // else
        out.println(String.format("else_%d:", this.ifIndex));
        processScope(node.jjtGetChild(2), out);

        out.println(String.format("endif_%d:", this.ifIndex));

        this.ifIndex++;
    }

    private void processWhile(Node node, PrintWriter out) {
        // condition
        processCondition(node.jjtGetChild(0), out);
        out.println(String.format("    ifeq end_while_%d", this.whileIndex));
        out.println(String.format("start_while_%d:", this.whileIndex));
        
        // scope
        processScope(node.jjtGetChild(1), out);
        
        // check condition as to minimize the amount of jumps
        processCondition(node.jjtGetChild(0), out);
        out.println(String.format("    ifne start_while_%d", this.whileIndex));

        out.println(String.format("end_while_%d:", this.whileIndex));

        this.whileIndex++;
    }

    private void processCondition(Node node, PrintWriter out) {
        execute(node.jjtGetChild(0), out);
    }

    private void processScope(Node node, PrintWriter out) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            execute(node.jjtGetChild(i), out);
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
        
        out.println(String.format("    if_icmplt ltTrue%d", tagIndex));
        out.println(String.format("    iconst_0"));
        out.println(String.format("    goto ltFalse%d", tagIndex));
        out.println(String.format("  ltTrue%d:", tagIndex));
        out.println(String.format("    iconst_1"));
        out.println(String.format("  ltFalse%d:", tagIndex));

        tagIndex++;

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
        String identifier = Utils.parseName(node.jjtGetChild(0).toString());
        String type = "";
        try {
            VarDescriptor var = (VarDescriptor) symbolTable.lookup(identifier).get(0);
            type = parseType(var.getDataType());

            // global variables
            if (var.getScope() == Descriptor.Scope.GLOBAL) {
                out.println(String.format("    aload_0"));
                out.println(String.format("    getfield %s/%s %s", mainClass, identifier, parseType2(var.getDataType())));
            }
            // local variables
            else {
                out.println(String.format("    %s", load(type, var.getLocalIndex())));
            }
                // pushInteger(var.getLocalIndex(), out);
        } catch (Exception e) {}
        
        execute(node.jjtGetChild(1), out);

        type = type.substring(1);

        out.println(String.format("    %saload", type.equals("I") || type.equals("Z") ? "i" : "a"));
        return type;
    }

    private String processLength(Node node, PrintWriter out) {
        processIdentifier(node.jjtGetChild(0), out);
        out.println(String.format("    arraylength"));
        return "I";
    }

    private String processInteger(Node node, PrintWriter out) {
        pushInteger(Integer.parseInt(Utils.parseName(node.toString())), out);

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
            type = parseType(var.getDataType());
            
            // for global variables AKA class atributes
            if (var.getScope() == Descriptor.Scope.GLOBAL) {
                out.println(String.format("    aload_0"));
                out.println(String.format("    getfield %s/%s %s", mainClass, nodeName, parseType2(var.getDataType())));
            }
            // for local variables
            else
                out.println(String.format("    %s", load(type, var.getLocalIndex())));
        }

        return type;
    }

    private String processBoolean(Node node, PrintWriter out) {
        String value = Utils.parseName(node.toString());
        
        out.println(String.format("    iconst_%d", value.equals("true") ? 1 : 0));

        return "Z";
    }

    private String processThis(Node node, PrintWriter out) {
        out.println(String.format("    aload_0"));

        return mainClass;
    }


    // ==========================================
    //                 UTILITIES
    // ==========================================

    private String parseType(String type) {
        String ret = "";

        if (type.equals("int") || type.equals("Integer"))
            ret = "I";
        else if (type.equals("boolean") || type.equals("Boolean"))
            ret = "Z";
        else if (type.equals("array") || type.equals("Array"))
            ret = "[I";
        else if (type.equals("stringarray"))
            ret = "[Ljava/lang/String;";
        else if (type.equals("String"))
            ret = "Ljava/lang/String;";
        else if (type.equals("void"))
            ret = "V";
        else 
            ret = type;

        return ret;
    }

    private String parseType2(String type) {
        String ret = "";

        if (type.equals("int") || type.equals("Integer") || type.equals("I"))
            ret = "I";
        else if (type.equals("boolean") || type.equals("Boolean") || type.equals("Z"))
            ret = "Z";
        else if (type.equals("array") || type.equals("Array") || type.equals("[I"))
            ret = "[I";
        else if (type.equals("stringarray") || type.equals("[Ljava/lang/String;"))
            ret = "[Ljava/lang/String;";
        else if (type.equals("String") || type.equals("Ljava/lang/String;"))
            ret = "Ljava/lang/String;";
        else if (type.equals("void") || type.equals("V"))
            ret = "V";
        else 
            ret = "L" + type + ";";

        return ret;
    }


    // ==========================================
    //         OPTIMIZATION UTILITIES
    // ==========================================

    private void pushInteger(int value, PrintWriter out) {
        String instruction = "";

        if (value >= 0 && value <= 5)
            instruction = "iconst_";
        else if (value <= 127)
            instruction = "bipush ";
        else if (value <= 32767)
            instruction = "ldc ";
        
        out.println(String.format("    %s%d", instruction, value));
    }

    private String load(String type, int index) {
        return String.format("%sload%s%d", 
            type.equals("I") || type.equals("Z") ? "i" : "a",
            index < 4 ? "_" : " ",
            index);
    }

    private String store(String type, int index) {
        return String.format("%sstore%s%d", 
            type.equals("I") || type.equals("Z") ? "i" : "a",
            index < 4 ? "_" : " ",
            index);
    }
}

