import java.util.ArrayList;
import java.util.HashSet;

import descriptors.*;

public class InstructionNode {

    protected ArrayList<VarDescriptor> liveIn;
    protected ArrayList<VarDescriptor> liveOut;
    protected VarDescriptor def;
    protected HashSet<VarDescriptor> use;
    protected ArrayList<InstructionNode> successors;

    public InstructionNode(){
        this.liveIn = new ArrayList<>();
        this.liveOut = new ArrayList<>();
        this.use = new HashSet<>();
        this.successors = new ArrayList<>();
    }

    public void setLiveIn(ArrayList<VarDescriptor> liveIn) {
        this.liveIn = liveIn;
    }

    public ArrayList<InstructionNode> getSuccessors() {
        return successors;
    }

    public descriptors.VarDescriptor getDef() {
        return def;
    }

    public ArrayList<VarDescriptor> getLiveIn() {
        return liveIn;
    }

    public ArrayList<VarDescriptor> getLiveOut() {
        return liveOut;
    }

    public void setDef(VarDescriptor def) {
        this.def = def;
    }

    public void setUse(HashSet<VarDescriptor> use) {
        this.use = use;
    }

    public void setLiveOut(ArrayList<VarDescriptor> liveOut) {
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
