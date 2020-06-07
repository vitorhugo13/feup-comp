package descriptors;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Descriptor for classes.
 * Stores: class methods and the class parent if the class extends another
 */
public class ClassDescriptor extends Descriptor {
    ArrayList<MethodDescriptor> methods;
    ClassDescriptor parentClass;

    public ClassDescriptor(String identifier){
        this.type=Type.CLASS;
        this.identifier=identifier;
        methods = new ArrayList<>();
        parentClass = null;
    }

    public void setParentClass(ClassDescriptor classDescriptor){
        this.parentClass = classDescriptor;
    }

    public ClassDescriptor getParentClass(){
        return this.parentClass;
    }

    private Boolean existsMethod(String id){
        for(int i=0; i<methods.size();i++){
            if(methods.get(i).getIdentifier().equals(id))
                return true;
        }

        return false;
    }

    /**
     *
     * @param method - method to check if it is possible to overload
     * @return false if there exists a method within the class with the same nubmer and type of parameters
     */
    private Boolean possibleToOverload(MethodDescriptor method){

        Boolean argsAllSame = true;
        ArrayList<VarDescriptor> method_parameters = method.getParameters();
        ArrayList<VarDescriptor> parameters = new ArrayList<>();

        for(int i = 0; i < this.methods.size(); i++){
          

            if(this.methods.get(i).getIdentifier().equals(method.getIdentifier())){
                argsAllSame = true;
                parameters = this.methods.get(i).getParameters();
                
                if(method_parameters.size() == parameters.size()){
                
                    for(int j = 0; j < parameters.size(); j++){
                        if(!parameters.get(j).getDataType().equals(method_parameters.get(j).getDataType())){
                            argsAllSame = false;
                        }
                    }
                
                    if(argsAllSame){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     *
     * @param id - identifier of the method to search
     * @return an Array list of all methods matching the identifier provided
     * @throws IOException - if no method with the same name exists in class
     */
    public ArrayList<MethodDescriptor> getMethodsMatchingId(String id) throws IOException{
        ArrayList<MethodDescriptor> methodsMatchingId = new ArrayList<>();
        if(existsMethod(id)){
            for(int i=0; i<methods.size();i++){
                if(methods.get(i).getIdentifier().equals(id))
                    methodsMatchingId.add(methods.get(i));
            }
            return methodsMatchingId;
        }
        throw new IOException("No Method in class " + this.identifier + " matches method named " + id);
    }

    /**
     *
     * @param method to add to the class.
     *               Only adds the method a method with this identifier doesn't exist already or if it is a possible overload
     */
    public void addMethod(MethodDescriptor method){

        if(possibleToOverload(method))
            methods.add(method);           
    }

    public ArrayList<MethodDescriptor> getMethods(){
        return this.methods;
    }

}