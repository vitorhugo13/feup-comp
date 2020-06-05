

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
        // method.
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
        else if (nodeName.equals("And"))
            return;
        else if (nodeName.equals("Less"))
            return;
        else if (nodeName.equals("Add"))
            return;
        else if (nodeName.equals("Sub"))
            return;
        else if (nodeName.equals("Mul"))
            return;
        else if (nodeName.equals("Div"))
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
    
}