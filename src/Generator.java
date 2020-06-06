import java.io.PrintWriter;
import java.io.File;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Stack;

import java.util.ArrayList;

import descriptors.Descriptor;
import descriptors.ClassDescriptor;
import descriptors.MethodDescriptor;
import descriptors.VarDescriptor;


class Generator {

    private SymbolTable symbolTable;
    
    private String mainClass;   // main class identifier
    private Instruction instruction;

    private int localIndex;     // index of local variables
    private int maxStackSize;   // maximum size the stack has reached
    private int stackSize;      // current stack size
    
    private int tagIndex;       // less than tags
    private int ifIndex;        // if statement tags
    private int whileIndex;     // while loop tags

    private Stack<Boolean> popScope;


    public Generator(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        
        this.tagIndex = 0;
        this.ifIndex = 0;
        this.whileIndex = 0;

        this.maxStackSize = 0;
        this.stackSize = 0;

        this.popScope = new Stack<Boolean>();
        this.popScope.push(true);
    }

    public void enterPopScope() {
        this.popScope.push(false);
    }

    public void exitPopScope() {
        this.popScope.pop();
    }

    public boolean getPop() {
        return this.popScope.peek();
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
            out.println(Instruction._field(var.getIdentifier(), parseType2(var.getDataType())));
            // out.println(String.format(".field public %s %s", var.getIdentifier(), parseType2(var.getDataType())));
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
        ByteArrayOutputStream tmpStream = new ByteArrayOutputStream();
        PrintWriter tmpOut = new PrintWriter(tmpStream, true);

        instruction = new Instruction();

        symbolTable.enterScopeForAnalysis();

        String name = Utils.parseName(node.toString());
        name = name.equals("main") ? "static main" : name;

        // System.out.println("method : " + name);

        String type = node.jjtGetChild(0).toString().contains("Type") ? parseType(Utils.parseName(node.jjtGetChild(0).toString())) : "V";
        int childNum = type.equals("V") ? 0 : 1;
        
        String params = processParamList(node.jjtGetChild(childNum));
        localIndex = node.jjtGetChild(childNum).jjtGetNumChildren() + childNum;

        Node body = node.jjtGetChild(node.jjtGetNumChildren() - 1); 
        for (int i = 0; i < body.jjtGetNumChildren(); i++) {
            if (body.jjtGetChild(i).toString().equals("VarDeclaration"))
                continue;

            tmpOut.println();
            execute(body.jjtGetChild(i), tmpOut);
        }

        Node lastInstruction = body.jjtGetChild(body.jjtGetNumChildren() - 1);
        tmpOut.println();
        if (lastInstruction.toString().equals("Return")) {
            // System.out.println("in --- Return");
            this.enterPopScope();
            for (int i = 0; i < lastInstruction.jjtGetNumChildren(); i++)
                execute(lastInstruction.jjtGetChild(i), tmpOut);
            this.exitPopScope();
            // System.out.println("out -- Return");
        }
        
        // return
        if (type.equals("I") || type.equals("Z"))
            tmpOut.println(instruction.ireturn());
        else if (type.equals("V"))
            tmpOut.println(instruction._return());
        else 
            tmpOut.println(instruction.areturn());

        out.println();
        out.format(".method public %s(%s)%s\n", name, params, type);
        out.println(instruction.limitStack());
        out.println(instruction.limitLocals(this.localIndex));
        // out.format("    .limit stack %d\n", this.maxStackSize);
        // out.format("    .limit locals %d\n", this.localIndex);

        // write method body
        out.println(tmpStream.toString(StandardCharsets.UTF_8));

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

        // System.out.println("in --- MethodInvocation");
        this.enterPopScope();

        System.out.println(node.jjtGetChild(0).toString() + " " + node.jjtGetChild(1).toString());
        String className = execute(node.jjtGetChild(0), out);
        // System.out.println(className);
        String methodName = Utils.parseName(node.jjtGetChild(1).toString());
        String argList = processArgList(node.jjtGetChild(2), out);
        int numArgs = node.jjtGetChild(2).jjtGetNumChildren();

        ClassDescriptor classDescriptor = null;
        ArrayList<MethodDescriptor> methods = null;
        try {
            classDescriptor = (ClassDescriptor) symbolTable.lookup(className).get(0);
            methods = classDescriptor.getMethodsMatchingId(methodName);
        } catch (Exception e1) {
            try {
                methods = classDescriptor.getParentClass().getMethodsMatchingId(methodName);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        
        MethodDescriptor method = methods.get(0);
        for (int i = 0; i < methods.size(); i++) {
            method = methods.get(i);

            String signature = "";
            for (VarDescriptor var : method.getParameters()) {
                signature += parseType(var.getDataType());
            }

            if (argList.equals(signature))
                break;
        }
        // System.out.println("TYPE : " + method.getReturnType());
        String type = parseType(method.getReturnType());
        boolean isStatic = method.isStatic();

        if (isStatic) 
            out.println(instruction.invokestatic(className, methodName, argList, numArgs, type));
        else
            out.println(instruction.invokevirtual(className, methodName, argList, numArgs, type));
        
        this.exitPopScope();
        // System.out.println("out -- MethodInvocation");
        
        if (this.getPop() && !type.equals("V")) {
            // System.out.println("Popped : " + methodName);
            out.println(instruction._pop());
        }
        
        return type;
    }

    private String processArgList(Node node, PrintWriter out) {
        // System.out.println("in --- ArgList");
        // this.enterPopScope();
        String ret = "";

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            ret += parseType2(execute(node.jjtGetChild(i), out));
        }

        // this.exitPopScope();
        // System.out.println("in --- ArgList");
        return ret;
    }

    private String processConstructorInvocation(Node node, PrintWriter out) {
        String className = processIdentifier(node.jjtGetChild(0), out);

        out.println(instruction._new(className));
        out.println(instruction.dup());
        out.println(instruction.invokespecial(className));

        return className;
    }

    private String processNewIntArray(Node node, PrintWriter out) {
        this.enterPopScope();

        execute(node.jjtGetChild(0), out);
        out.println(instruction.newarray());

        this.exitPopScope();

        return "[I";
    }


    // ==========================================
    //                ASSIGNMENT
    // ==========================================

    public void processAssignment(Node node, PrintWriter out) {
        // System.out.println("in --- Assignment");
        this.enterPopScope();

        String nodeName = node.jjtGetChild(0).toString();
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
            out.println(instruction.aload(0));
            if (nodeName.equals("Array")) {
                out.println(instruction.getfield(mainClass, identifier, parseType2(var.getDataType())));
                execute(node.jjtGetChild(0).jjtGetChild(1), out);
            }
        }
        // local variables
        else {
            if (var.getLocalIndex() == -1)
                var.setLocalIndex(localIndex++);
                
            String type = parseType(var.getDataType());

            if (nodeName.equals("Array")) {
                out.println(load(parseType(var.getDataType()), var.getLocalIndex()));
                execute(node.jjtGetChild(0).jjtGetChild(1), out);        // element index
            }
        }

        // process the right side of the expression, push value to stack
        execute(node.jjtGetChild(1), out);

        if (var.getScope() == Descriptor.Scope.GLOBAL) {
            if (nodeName.equals("Array"))
                out.println(instruction.iastore());
            else
                out.println(instruction.putfield(mainClass, identifier, parseType2(var.getDataType())));
        }
        else {
            if (nodeName.equals("Array"))
                out.println(instruction.iastore());
            else 
                out.println(store(parseType(var.getDataType()), var.getLocalIndex()));
        }

        this.exitPopScope();
        // System.out.println("out -- Assignment");
    }


    // ==========================================
    //             CONTROL STRUCTURES
    // ==========================================

    private void processIf(Node node, PrintWriter out) {
        String elseTag = String.format("else_%d", this.ifIndex);
        String endTag = String.format("endif_%d", this.ifIndex);
        this.ifIndex++;

        // condition
        processCondition(node.jjtGetChild(0), out);
        out.println(instruction.ifeq(elseTag));

        // if
        processScope(node.jjtGetChild(1), out);
        out.println(instruction._goto(endTag));
        
        // else
        out.println(instruction.tag(elseTag));
        processScope(node.jjtGetChild(2), out);

        out.println(instruction.tag(endTag));
    }

    private void processWhile(Node node, PrintWriter out) {
        String startTag = String.format("start_while_%d", this.whileIndex);
        String endTag = String.format("end_while_%d", this.whileIndex);
        this.whileIndex++;

        // condition
        processCondition(node.jjtGetChild(0), out);
        out.println(instruction.ifeq(endTag));
        out.println(instruction.tag(startTag));
        
        // scope
        processScope(node.jjtGetChild(1), out);
        
        // check condition as to minimize the amount of jumps
        processCondition(node.jjtGetChild(0), out);
        out.println(instruction.ifne(startTag));

        out.println(instruction.tag(endTag));
    }

    private void processCondition(Node node, PrintWriter out) {
        // System.out.println("in --- Condition");
        this.enterPopScope();
        execute(node.jjtGetChild(0), out);
        this.exitPopScope();
        // System.out.println("out -- Condition");
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
        // System.out.println("in --- AndOperator");
        this.enterPopScope();
        for (int i = 0; i < node.jjtGetNumChildren(); i++)
            execute(node.jjtGetChild(i), out);
        
        out.println(instruction.iand());

        this.exitPopScope();
        // System.out.println("out -- AndOperator");

        return "I";
    }
    
    private String processLess(Node node, PrintWriter out) {
        // System.out.println("in --- LessOperator");
        this.enterPopScope();

        for (int i = 0; i < node.jjtGetNumChildren(); i++)
            execute(node.jjtGetChild(i), out);

        String tagTrue = String.format("lt_true_%d", tagIndex);
        String tagFalse = String.format("lt_false_%d", tagIndex);
        
        out.println(instruction.if_icmplt(tagTrue));
        out.println(instruction.iconst(0));
        out.println(instruction._goto(tagFalse));
        out.println(instruction.tag(tagTrue));
        out.println(instruction.iconst(1));
        out.println(instruction.tag(tagFalse));

        tagIndex++;

        this.exitPopScope();
        // System.out.println("out -- LessOperator");
        
        return "Z";
    }

    private String processNot(Node node, PrintWriter out) {
        // System.out.println("in --- NotOperator");
        this.enterPopScope();

        execute(node.jjtGetChild(0), out);

        out.println(instruction.iconst(1));
        out.println(instruction.ixor());
        
        this.exitPopScope();
        // System.out.println("out -- NotOperator");
        return "Z";
    }
        
    private String processAdd(Node node, PrintWriter out) {
        // System.out.println("in --- AddOperator");
        this.enterPopScope();

        for (int i = 0; i < node.jjtGetNumChildren(); i++)
            execute(node.jjtGetChild(i), out);
        
        out.println(instruction.iadd());
        
        this.exitPopScope();
        // System.out.println("out -- AddOperator");
        return "I";
    }

    private String processSub(Node node, PrintWriter out) {
        // System.out.println("in --- SubOperator");
        this.enterPopScope();

        for (int i = 0; i < node.jjtGetNumChildren(); i++)
            execute(node.jjtGetChild(i), out);
        
        out.println(instruction.isub());
     
        this.exitPopScope();
        // System.out.println("out -- SubOperator");
        return "I";
    }
    
    private String processMul(Node node, PrintWriter out) {
        // System.out.println("in --- MultOperator");
        this.enterPopScope();
        
        for (int i = 0; i < node.jjtGetNumChildren(); i++)
            execute(node.jjtGetChild(i), out);
        
        out.println(instruction.imul());
     
        this.exitPopScope();
        // System.out.println("out -- MultOperator");
        return "I";
    }

    private String processDiv(Node node, PrintWriter out) {
        // System.out.println("in --- DivOperator");
        this.enterPopScope();

        for (int i = 0; i < node.jjtGetNumChildren(); i++)
            execute(node.jjtGetChild(i), out);
        
        out.println(instruction.idiv());
     
        this.exitPopScope();
        // System.out.println("out -- DivOperator");
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
                out.println(instruction.aload(0));
                out.println(instruction.getfield(mainClass, identifier, parseType2(var.getDataType())));
            }
            // local variables
            else {
                out.println(load(type, var.getLocalIndex()));
            }
        } catch (Exception e) {}
        
        execute(node.jjtGetChild(1), out);

        type = type.substring(1);
        if (type.equals("I") || type.equals("Z"))
            out.println(instruction.iaload());
        else
            out.println(instruction.aaload());
        return type;
    }

    private String processLength(Node node, PrintWriter out) {
        processIdentifier(node.jjtGetChild(0), out);
        out.println(instruction.arraylength());
        return "I";
    }

    private String processInteger(Node node, PrintWriter out) {
        pushInteger(Integer.parseInt(Utils.parseName(node.toString())), out);

        return "I";
    }

    private String processIdentifier(Node node, PrintWriter out) {
        // System.out.println(node.toString());

        String nodeName = Utils.parseName(node.toString());
        Descriptor descriptor = null;
        String type = nodeName;

        try {
            descriptor = symbolTable.lookup(nodeName).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // in case the identifier represents a variable
        // there is the need to load the value onto the stack

        // System.out.println(nodeName);
        // System.out.println(descriptor.getType());
        
        if (descriptor.getType() == Descriptor.Type.VAR) {
            VarDescriptor var = (VarDescriptor) descriptor;
            type = parseType(var.getDataType());
            
            // for global variables AKA class atributes
            if (var.getScope() == Descriptor.Scope.GLOBAL) {
                out.println(instruction.aload(0));
                out.println(instruction.getfield(mainClass, nodeName, parseType2(var.getDataType())));
            }
            // for local variables
            else
                out.println(load(type, var.getLocalIndex()));
        }

        return type;
    }

    private String processBoolean(Node node, PrintWriter out) {
        String value = Utils.parseName(node.toString());
        
        out.println(instruction.iconst(value.equals("true") ? 1 : 0));

        return "Z";
    }

    private String processThis(Node node, PrintWriter out) {
        out.println(instruction.aload(0));

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

    // TODO: IMPORTANT!! handle negative numbers
    private void pushInteger(int value, PrintWriter out) {
        if (value >= 0 && value <= 5)
            out.println(instruction.iconst(value));
        else if (value <= 127)
            out.println(instruction.bipush(value));
        else if (value <= 32767)
            out.println(instruction.sipush(value));
        else
            out.println(instruction.ldc(value));
    }

    private String load(String type, int index) {
        if (type.equals("I") || type.equals("Z"))
            return instruction.iload(index);
        else
            return instruction.aload(index);
    }

    private String store(String type, int index) {
        if (type.equals("I") || type.equals("Z"))
            return instruction.istore(index);
        else 
            return instruction.astore(index);
    }
}
