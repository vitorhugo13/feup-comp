import java.util.ArrayList;
import java.util.HashSet;

import descriptors.*;

public class InstructionNode {

    protected HashSet<VarDescriptor> liveIn;
    protected HashSet<VarDescriptor> liveOut;
    protected VarDescriptor def;
    protected HashSet<VarDescriptor> use;
    protected ArrayList<InstructionNode> successors;

    public InstructionNode(){
        this.liveIn = new HashSet<>();
        this.liveOut = new HashSet<>();
        this.use = new HashSet<>();
        this.successors = new ArrayList<>();
    }

    public void setLiveIn(HashSet<VarDescriptor> liveIn) {
        this.liveIn = liveIn;
    }

    public ArrayList<InstructionNode> getSuccessors() {
        return successors;
    }

    public descriptors.VarDescriptor getDef() {
        return def;
    }

    public HashSet<VarDescriptor> getLiveIn() {
        return liveIn;
    }

    public HashSet<VarDescriptor> getLiveOut() {
        return liveOut;
    }

    public void setDef(VarDescriptor def) {
        this.def = def;
    }

    public void setUse(HashSet<VarDescriptor> use) {
        this.use = use;
    }

    public void setLiveOut(HashSet<VarDescriptor> liveOut) {
        this.liveOut = liveOut;
    }

    public HashSet<VarDescriptor> getUse() {
        return use;
    }

    public void setSuccessors(ArrayList<InstructionNode> successors) {
        this.successors = successors;
    }

    public void addUse(VarDescriptor use){
        this.use.add(use);
    }
}
