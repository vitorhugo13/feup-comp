
class Instruction {

    private int stack;
    private int maxStack;

    public Instruction() {
        System.out.println("");
        System.out.println(" NEW METHOD ");
        System.out.println("");

        this.stack = 0;
        this.maxStack = 0;
    }

    private void updateStack(int value) {
        stack += value;
        if (stack > maxStack)
            maxStack = stack;

        System.out.println("value : " + value);
        System.out.println("stack : " + stack);
        System.out.println("max : " + maxStack);
    }


    // ==========================================
    //                   LIMITS 
    // ==========================================
    
    public String limitStack() {
        return String.format("    .limit stack %d", maxStack);
    }

    public String limitLocals(int locals) {
        return String.format("    .limit locals %d", locals);
    }


    // ==========================================
    //                    IFS
    // ==========================================

    public String if_icmplt(String tag) {
        updateStack(-2);
        return String.format("    if_icmplt %s", tag);
    }

    public String ifeq(String tag) {
        System.out.println("ifeq");
        updateStack(-1);
        return String.format("    ifeq %s", tag);
    }

    public String ifne(String tag) {
        System.out.println("ifne");
        updateStack(-1);
        return String.format("    ifne %s", tag);
    }


    // ==========================================
    //                   FIELDS 
    // ==========================================

    public String getfield(String className, String fieldName, String type) {
        return String.format("    getfield %s/%s %s", className, fieldName, type);
    }

    public String putfield(String className, String fieldName, String type) {
        updateStack(-2);
        return String.format("    putfield %s/%s %s", className, fieldName, type);
    }


    // ==========================================
    //                   LOADS 
    // ==========================================

    public String iload(int value) {
        updateStack(1);
        return String.format("    iload%s%d", value < 4 ? "_" : " ", value);
    }

    public String aload(int value) {
        updateStack(1);
        return String.format("    aload%s%d", value < 4 ? "_" : " ", value);
    }

    public String iaload() {
        updateStack(-1);
        return "    iaload";
    }

    public String aaload() {
        updateStack(-1);
        return "    aaload";
    }

    // ==========================================
    //                   STORES 
    // ==========================================

    public String istore(int value) {
        System.out.println("istore");
        updateStack(-1);
        return String.format("    istore%s%d", value < 4 ? "_" : " ", value);
    }

    public String astore(int value) {
        System.out.println("astore " + value);
        updateStack(-1);
        return String.format("    astore%s%d", value < 4 ? "_" : " ", value);
    }

    public String iastore() {
        updateStack(-3);
        return "    iastore";
    }


    // ==========================================
    //              METHOD INVOCATIONS 
    // ==========================================

    public String invokespecial(String className) {
        System.out.println("invokespecial");
        // updateStack(-numArgs);
        return String.format("    invokespecial %s/<init>()V", className);
    }

    public String invokestatic(String className, String methodName, String argList, int numArgs, String type) {
        System.out.println("invokestatic");
        updateStack(1 - numArgs);
        return String.format("    invokestatic %s/%s(%s)%s", className, methodName, argList, type);
    }

    public String invokevirtual(String className, String methodName, String argList, int numArgs, String type) {
        System.out.println("invokevirtual");
        updateStack(-numArgs);
        return String.format("    invokevirtual %s/%s(%s)%s", className, methodName, argList, type);
    }


    // ==========================================
    //                  ARRAYS
    // ==========================================

    public String newarray() {
        System.out.println("newarray");
        return "    newarray int";
    }

    public String arraylength() {
        return "    arraylength";
    }
    

    // ==========================================
    //                 INTEGERS
    // ==========================================
    
    public String iconst(int value) {
        System.out.println("iconst " + value);
        updateStack(1);
        return String.format("    iconst_%d", value);
    }

    public String bipush(int value) {
        updateStack(1);
        return String.format("    bipush %d", value);
    }

    public String ldc(int value) {
        updateStack(1);
        return String.format("    ldc %d", value);
    }


    // ==========================================
    //                  OPERATORS
    // ==========================================
    
    public String isub() {
        System.out.println("isub");
        updateStack(-1);
        return "    isub";
    }

    public String imul() {
        System.out.println("imul");
        updateStack(-1);
        return "    imul";
    }

    public String ineg() {
        return "    ineg";
    }

    public String idiv() {
        System.out.println("idiv");
        updateStack(-1);
        return "    idiv";
    }

    public String iadd() {
        System.out.println("iadd");
        updateStack(-1);
        return "    iadd";
    }

    public String iand() {
        System.out.println("iand");
        updateStack(-1);
        return "    iand";
    }

    public String ixor() {
        System.out.println("ixor");
        updateStack(-1);
        return "    ixor";
    }


    // ==========================================
    //                  RETURNS
    // ==========================================

    public String _return() {
        return "    return";
    }

    public String ireturn() {
        System.out.println("ireturn");
        updateStack(-1);
        return "    ireturn";
    }

    public String areturn() {
        System.out.println("areturn");
        updateStack(-1);
        return "    areturn";
    }


    // ==========================================
    //                RANDOM STUFF
    // ==========================================

    public String dup() {
        updateStack(1);
        return "    dup";
    }

    public String tag(String tag) {
        return String.format("  %s:", tag);
    }

    public String _goto(String tag) {
        return String.format("    goto %s", tag);
    }

    public String _new(String className) {
        updateStack(1);
        return String.format("    new %s", className);
    }


}