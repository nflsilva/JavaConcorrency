package model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Future;

public class Task {

    private List<TaskOperand> operands;
    private Type type;

    public enum Type {
        Add,
        Subtract,
        Multiply,
        Divide
    }

    public Task(List<TaskOperand> operands, Type type){
        this.operands = operands;
        this.type = type;
    }


    public Double compute() throws ArithmeticException{

        switch (this.type){
            case Add:
                return computeAdd();
            case Subtract:
                return computeSubtract();
            case Multiply:
                return computeMultiply();
            case Divide:
                return computeDivide();
            default:
                throw new ArithmeticException("Type unknown.");
        }
    }

    private Double computeAdd(){
        Double result = 0.0;
        for(TaskOperand o : operands){
            result += o.getValue();
        }
        simulateComputingTime();
        return result;
    }
    private Double computeSubtract(){
        Double result = operands.size() > 0 ? operands.get(0).getValue() : 0.0;
        for(int i = 1; i < operands.size(); i++){
            Double o = operands.get(i).getValue();
            result -= o;
        }
        simulateComputingTime();
        return result;
    }
    private Double computeMultiply(){
        Double result = operands.size() > 0 ? operands.get(0).getValue() : 0.0;
        for(int i = 1; i < operands.size(); i++){
            Double o = operands.get(i).getValue();
            result *= o;
        }
        simulateComputingTime();
        return result;
    }
    private Double computeDivide(){
        Double result = operands.size() > 0 ? operands.get(0).getValue() : 0.0;
        for(int i = 1; i < operands.size(); i++){
            Double o = operands.get(i).getValue();
            result /= o;
        }
        simulateComputingTime();
        return result;
    }


    private void simulateComputingTime(){
        try {
            //System.out.println("Computing on thread: " + Thread.currentThread().getId());
            Thread.sleep(1000);
        } catch (InterruptedException ie){ }
    }


}
