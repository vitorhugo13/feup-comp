class VarInfo {

    private String type;            // int or boolean
    private Object value;
    private boolean local;          // true if local, false if argument or field
    private boolean constant;

    public VarInfo(String type, boolean local, boolean constant) {
        this.type = type;
        this.value = null;
        this.local = local;
        this.constant = constant;
    }

    public VarInfo(String type, Object value, boolean local, boolean constant) {
        this.type = type;
        this.value = value;
        this.local = local;
        this.constant = constant;
    }

    public String getType() {
        return this.type;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean getLocal() {
        return this.local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    public boolean getConstant() {
        return this.constant;
    }
}