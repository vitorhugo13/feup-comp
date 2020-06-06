import java.util.HashMap;


class ConstantOptimization {

    private HashMap<String, VarInfo> vars;

    public ConstantOptimization() {
        this.vars = new HashMap<String, VarInfo>();
    }

    public void init(SimpleNode root) {
        SimpleNode classNode = (SimpleNode) root.jjtGetChild(root.jjtGetNumChildren() - 1);

        for (int i = 0; i < classNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) classNode.jjtGetChild(i);
            if (!child.jjtGetName().equals("Method"))
                continue;
            optimize(child);
        }
    }

    public void optimize(SimpleNode method) {
        this.vars = new HashMap<String, VarInfo>();

        SimpleNode paramList, body;
        if (((String) method.jjtGetValue()).equals("main")) {
            paramList = (SimpleNode) method.jjtGetChild(0);
            body = (SimpleNode) method.jjtGetChild(1);
        }
        else {
            paramList = (SimpleNode) method.jjtGetChild(1);
            body = (SimpleNode) method.jjtGetChild(2);
        }

        // TODO: process ParamList


        // process Body
        for (int i = 0; i < body.jjtGetNumChildren(); ) {
            SimpleNode child = (SimpleNode) body.jjtGetChild(i);
            String childName = child.jjtGetName();

            if (childName.equals("VarDeclaration")) {
                if (processVarDeclaration(child)) {
                    body.jjtRemoveChild(i);
                    continue;
                }
            }
            else if (childName.equals("Assign")) {
                if (processAssignment(child)) {
                    body.jjtRemoveChild(i);
                    continue;
                }
                else {
                    String varName = (String) ((SimpleNode) child.jjtGetChild(0)).jjtGetValue();
                    if (varName != null && vars.containsKey(varName)) {
                        SimpleNode declaration = new SimpleNode(ParserTreeConstants.JJTVARDECLARATION, body);
                        SimpleNode type = new SimpleNode(ParserTreeConstants.JJTTYPE, vars.get(varName).getType(), declaration);
                        SimpleNode identifier = new SimpleNode(ParserTreeConstants.JJTIDENTIFIER, varName, declaration);
                        
                        declaration.jjtAppendChild(type);
                        declaration.jjtAppendChild(identifier);
                        
                        body.jjtAddChildAt(declaration, 0);
                        vars.remove(varName);
                        i++;
                    }
                }
            }
            else {
                execute(child);
            }

            i++;
        }

    }

    public void execute(SimpleNode node) {
        String nodeName = node.jjtGetName();

        if (nodeName.equals("MethodInvocation"))
            processMethodInvocation(node);
        // else if (nodeName.equals("NewObject"))
            // return;
        else if (nodeName.equals("NewIntArray"))
            executeChildren(node);
        else if (nodeName.equals("IfStatement"))
            return;
        else if (nodeName.equals("While"))
            return;
        else if (nodeName.equals("Return"))
            executeChildren(node);

        // OPERATORS
        else if (isArithmetic(node))    // +, -, * and /
            processArithmeticOperation(node);
        else if (nodeName.equals("And"))
            return;
        else if (nodeName.equals("Less"))
            return;
        else if (nodeName.equals("Not"))
            return;

        // TERMINALS
        // else if (nodeName.equals("Integer"))
        //     return;
        // else if (nodeName.equals("Boolean"))
        //     return;
        else if (nodeName.equals("Identifier"))
            processIdentifier(node);
        // else if (nodeName.equals("This"))
        //     return;
        else if (nodeName.equals("Array"))
            processArray(node);
        // else if (nodeName.equals("Length"))
        //     return;
    }

    private void executeChildren(SimpleNode node) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++)
            execute((SimpleNode) node.jjtGetChild(i));
    }

    private void processMethodInvocation(SimpleNode node) {
        SimpleNode argList = (SimpleNode) node.jjtGetChild(node.jjtGetNumChildren() - 1);
        System.out.println(argList);
        executeChildren(argList);
    }

    private void processArray(SimpleNode node) {
        execute((SimpleNode) node.jjtGetChild(1));
    }

    private boolean processVarDeclaration(SimpleNode node) {
        String type = (String) ((SimpleNode) node.jjtGetChild(0)).jjtGetValue();

        if (!type.equals("int") && !type.equals("boolean"))
            return false;

        String name = (String) ((SimpleNode) node.jjtGetChild(1)).jjtGetValue();

        vars.put(name, new VarInfo(type));

        return true;
    }

    private boolean processAssignment(SimpleNode node) {
        String nodeType = ((SimpleNode) node.jjtGetChild(0)).jjtGetName();
        
        if (!nodeType.equals("Identifier")) {
            execute((SimpleNode) node.jjtGetChild(0));
            return false;
        }

        String identifier = (String) ((SimpleNode) node.jjtGetChild(0)).jjtGetValue();
        
        for (int i = 1; i < node.jjtGetNumChildren(); i++){
            SimpleNode child = (SimpleNode) node.jjtGetChild(i);
            execute(child);
        }
        
        String expression = ((SimpleNode) node.jjtGetChild(1)).jjtGetName();
        if (!expression.equals("Integer") && !expression.equals("Boolean"))
            return false;

        Object value = ((SimpleNode) node.jjtGetChild(1)).jjtGetValue();
        VarInfo info = vars.get(identifier);
        info.setValue(value);
        vars.replace(identifier, info);
            
        return true;
    }

    private boolean isArithmetic(SimpleNode node) {
        String nodeName = node.jjtGetName();
        return nodeName.equals("Add") || nodeName.equals("Sub") || nodeName.equals("Mul") || nodeName.equals("Div");
    }

    /**
     * Returns the number of children that are Integers
     */
    private int processArithmeticOperation(SimpleNode node) {
        int count = 0;
        int index = 0;
        int child_count = 0;

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) node.jjtGetChild(i);

            if (isArithmetic(child))
                child_count = processArithmeticOperation(child);
            else
                execute(child);
            
            if (child.jjtGetName().equals("Integer")) {
                index = i;
                count++;
            }
        }

        if (count == 0) {
            // checks if expression is an arithmetic expression with one integer child
            if (child_count < 1) {
                return 1;
            }

            // TODO: 
            // ((a + 2) + b) + 1

            return 0;
        }
        // in case the outer expression has an integer
        // example:  (a + 2) + 1 -> a + 3
        else if (count == 1) {
            // checks if expression is an arithmetic expression with one integer child
            if (child_count < 1) {
                return 1;
            }

            SimpleNode integer = (SimpleNode) node.jjtGetChild(index);
            SimpleNode expression = (SimpleNode) node.jjtGetChild((index + 1) % 2);

            String nodeName = node.jjtGetName();
            String expName = expression.jjtGetName();

            if (((nodeName.equals("Add") || nodeName.equals("Sub")) && expName.equals("Add"))
                || (nodeName.equals("Mul") && expName.equals("Mul"))) {

                SimpleNode[] children = new SimpleNode[2];
                children[0] = (SimpleNode) expression.jjtGetChild(0);
                children[1] = (SimpleNode) expression.jjtGetChild(1);
                
                index = children[0].jjtGetName().equals("Integer") ? 0 : 1;
                children[index].jjtSetValue(calculate(children[index], nodeName, integer));
                children[index].jjtSetParent(node);
                node.jjtAddChild(children[index], index);
                
                index = (index + 1) % 2;
                children[index].jjtSetParent(node);
                node.jjtAddChild(children[index], index);

                node.setId(expression.getId());
            }
            else if ((nodeName.equals("Add") || nodeName.equals("Sub")) && expName.equals("Sub")) {

            }
            else if (nodeName.equals("Mul") && expName.equals("Div")) {
                // TODO:
            }
            else if (nodeName.equals("Div") && expName.equals("Mul")) {
                // TODO:
            }
            else if (nodeName.equals("Div") && expName.equals("Div")) {
                // TODO:
            }

            return 1;
        }
        // in case both children are integers
        // example: 3 + 4 -> 7
        else if (count == 2) {
            int value = calculate((SimpleNode) node.jjtGetChild(0), node.jjtGetName(), (SimpleNode) node.jjtGetChild(1));
            node.jjtSetValue(value);
            node.setId(ParserTreeConstants.JJTINTEGER);     // change the node type to "Integer"
            node.jjtRemoveChildren();                       // remove the node children
            return 0;
        }

        return 0;
    }

    private void processIdentifier(SimpleNode node) {
        String varName = (String) node.jjtGetValue();

        if (!vars.containsKey(varName))
            return;
        
        VarInfo info = vars.get(varName);
        if (info.getType().equals("int")) {
            node.setId(ParserTreeConstants.JJTINTEGER);
            node.jjtSetValue((Integer) info.getValue());
        }
        else if (info.getType().equals("boolean")) {
            node.setId(ParserTreeConstants.JJTBOOLEAN);
            node.jjtSetValue((Boolean) info.getValue());
        }
    }
        
    private int calculate(SimpleNode operand1, String operation, SimpleNode operand2) {
        int value1 = (Integer) operand1.jjtGetValue();
        int value2 = (Integer) operand2.jjtGetValue();

        int value = 0;

        if (operation.equals("Add"))
            value = value1 + value2;
        else if (operation.equals("Sub"))
            value = value1 - value2;
        else if (operation.equals("Mul"))
            value = value1 * value2;
        else if (operation.equals("Div"))
            value = value1 / value2;
        
        return value;
    }
}