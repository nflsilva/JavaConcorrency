package model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Task {

    private int id;
    private List<Double> operands;
    private Type type;

    public enum Type {
        Add,
        Subtract,
        Multiply,
        Divide
    }

    public int getId() {
        return id;
    }

    public Task(int id, List<Double> operands, Type type){
        this.id = id;
        this.operands = operands;
        this.dependencies = dependencies;
        this.type = type;
    }


    public Double compute() throws ArithmeticException{
        switch (this.type){
            case Add:
                return computeAdd(operands);
            case Divide:
                return computeDivide(operands);
            case Multiply:
                return computeMultiply(operands);
            case Subtract:
                return computeSubtract(operands);
            default:
                throw new ArithmeticException("Type unknown.");
        }
    }

    private Double computeAdd(List<Double> operands){
        Double result = 0.0;
        for(Double o : operands){
            result += o;
        }
        return result;
    }
    private Double computeSubtract(List<Double> operands){
        Double result = 0.0;
        for(Double o : operands){
            result -= o;
        }
        return result;
    }
    private Double computeMultiply(List<Double> operands){
        Double result = 1.0;
        for(Double o : operands){
            result *= o;
        }
        return result;
    }
    private Double computeDivide(List<Double> operands){
        Double result = 1.0;
        for(Double o : operands){
            result /= o;
        }
        return result;
    }





}
