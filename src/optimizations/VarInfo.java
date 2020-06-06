class VarInfo {

    private String type;    // int or boolean
    private Object value;

    public VarInfo(String type) {
        this.type = type;
        this.value = null;
    }

    public VarInfo(String type, Object value) {
        this.type = type;
        this.value = value;
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
}