

class ConstantOptimization {


    public ConstantOptimization() {

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
        SimpleNode paramList = (SimpleNode) method.jjtGetChild(0);
        SimpleNode body = (SimpleNode) method.jjtGetChild(1);

        executeChildren(body);
    }

    public void execute(SimpleNode node) {
        String nodeName = node.jjtGetName();

        if (nodeName.equals("VarDeclaration"))
            return;

        else if (nodeName.equals("MethodInvocation"))
            return;
        else if (nodeName.equals("Assign"))
            return;
        else if (nodeName.equals("NewObject"))
            return;
        else if (nodeName.equals("NewIntArray"))
            return;
        else if (nodeName.equals("IfStatement"))
            return;
        else if (nodeName.equals("While"))
            return;

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
        else if (nodeName.equals("Integer"))
            return;
        else if (nodeName.equals("Boolean"))
            return;
        else if (nodeName.equals("Identifier"))
            return;
        else if (nodeName.equals("This"))
            return;
        else if (nodeName.equals("Array"))
            return;
        else if (nodeName.equals("Length"))
            return;
    }

    public void executeChildren(SimpleNode node) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++)
            execute((SimpleNode) node.jjtGetChild(i));
    }

    public boolean isArithmetic(SimpleNode node) {
        String nodeName = node.jjtGetName();
        return nodeName.equals("Add") || nodeName.equals("Sub") || nodeName.equals("Mul") || nodeName.equals("Div");
    }

    /**
     * Returns the number of children that are Integers
     */
    public int processArithmeticOperation(SimpleNode node) {
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

            if ((nodeName.equals("Add") || nodeName.equals("Sub")) && (expName.equals("Sub") || expName.equals("Add"))) {
                
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
            else if (nodeName.equals("Mul") && expName.equals("Mul")) {

            }
            else if (nodeName.equals("Mul") && expName.equals("Div")) {

            }
            else if (nodeName.equals("Div") && expName.equals("Mul")) {

            }
            else if (nodeName.equals("Div") && expName.equals("Div")) {

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

        
    public int calculate(SimpleNode operand1, String operation, SimpleNode operand2) {
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
    /**
     * Returns the number of child Integer nodes
     */
    public int processOperation(SimpleNode node) {
        int count = 0;

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) node.jjtGetChild(i);
            execute(child);
            if (child.jjtGetName().equals("Integer"))
                count++;
        }

        return count;
    }
}