package model;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TaskOperand {

    private Future<Double> dependency;
    private Double operand;

    public TaskOperand(Future<Double> dependency){
        this.dependency = dependency;
    }
    public TaskOperand(Double operand){
        this.operand = operand;
    }

    public Double getValue(){
        if(operand != null){
            return operand;
        }
        else {
            try {
                return dependency.get();
            }catch (Exception e){
                //Just ignore and return something, for now...
                return 0.0;
            }

        }
    }

}
