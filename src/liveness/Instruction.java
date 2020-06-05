package liveness;

import java.utils.ArrayList;
import descriptors.*;

public class Instruction{

    ArrayList<VarDescriptor> liveIn;
    ArrayList<VarDescriptor> liveOut;
    ArrayList<VarDescriptor> def;
    ArrayList<VarDescriptor> use;
    ArrayList<Instruction> successors;

    public Instruction(){
        this.liveIn = new ArrayList<>();
        this.liveOut = new ArrayList<>();
        this.def = new ArrayList<>();
        this.use = new ArrayList<>();
        this.successors = new ArrayList<>();
    }
}