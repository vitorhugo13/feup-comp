
class Instruction {

    private int stack;
    private int maxStack;

    public Instruction() {
        this.stack = 0;
        this.maxStack = 0;
    }

    private void updateStack(int value) {
        stack += value;
        if (stack > maxStack)
            maxStack = stack;
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
        updateStack(-1);
        return String.format("    ifeq %s", tag);
    }

    public String ifne(String tag) {
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
        updateStack(-1);
        return String.format("    istore%s%d", value < 4 ? "_" : " ", value);
    }

    public String astore(int value) {
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
        // updateStack(-numArgs);
        return String.format("    invokespecial %s/<init>()V", className);
    }

    public String invokestatic(String className, String methodName, String argList, int numArgs, String type) {
        updateStack(1 - numArgs);
        return String.format("    invokestatic %s/%s(%s)%s", className, methodName, argList, type);
    }

    public String invokevirtual(String className, String methodName, String argList, int numArgs, String type) {
        updateStack(-numArgs);
        System.out.println("type : " + type);
        return String.format("    invokevirtual %s/%s(%s)%s", className, methodName, argList, type);
    }


    // ==========================================
    //                  ARRAYS
    // ==========================================

    public String newarray() {
        return "    newarray int";
    }

    public String arraylength() {
        return "    arraylength";
    }
    

    // ==========================================
    //                 INTEGERS
    // ==========================================
    
    public String iconst(int value) {
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
        updateStack(-1);
        return "    isub";
    }

    public String imul() {
        updateStack(-1);
        return "    imul";
    }

    public String ineg() {
        return "    ineg";
    }

    public String idiv() {
        updateStack(-1);
        return "    idiv";
    }

    public String iadd() {
        updateStack(-1);
        return "    iadd";
    }

    public String iand() {
        updateStack(-1);
        return "    iand";
    }

    public String ixor() {
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
        updateStack(-1);
        return "    ireturn";
    }

    public String areturn() {
        updateStack(-1);
        return "    areturn";
    }


    // ==========================================
    //                RANDOM STUFF
    // ==========================================

    public String _pop() {
        updateStack(-1);
        return "    pop";
    }

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