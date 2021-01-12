import model.Task;
import model.TaskOperand;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestConsumerBuffer {


    @Test
    public void testDependencies(){
        /*
        Computing:
        [5*(2+4) + 4*(5+7)] / [5*(2+4) + 4*(5+7)] = 1
        nTasks = 11

        ----- Phase 0 -----
        task00 = (2+4)
        task01 = (5+7)
        task02 = (2+4)
        task03 = (5+7)

        ----- Phase 1 -----
        task10 = 5*task00
        task11 = 4*task01
        task12 = 5*task02
        task13 = 4*task03

        ----- Phase 2 -----
        task20 = task10 + task11
        task21 = task12 + task13

        ----- Phase 3 -----
        task30 = task20 + task21
        */

        TaskBuffer taskBuffer = new TaskBuffer(20, 4);

        Task task00 = new Task(new ArrayList<>(List.of(new TaskOperand(2.0), new TaskOperand(4.0))), Task.Type.Add);
        Future<Double> task00Future = taskBuffer.addTask(task00);
        Task task01 = new Task(new ArrayList<>(List.of(new TaskOperand(5.0), new TaskOperand(7.0))), Task.Type.Add);
        Future<Double> task01Future = taskBuffer.addTask(task01);
        Task task02 = new Task(new ArrayList<>(List.of(new TaskOperand(2.0), new TaskOperand(4.0))), Task.Type.Add);
        Future<Double> task02Future = taskBuffer.addTask(task02);
        Task task03 = new Task(new ArrayList<>(List.of(new TaskOperand(5.0), new TaskOperand(7.0))), Task.Type.Add);
        Future<Double> task03Future = taskBuffer.addTask(task03);

        Task task10 = new Task(new ArrayList<>(List.of(new TaskOperand(5.0), new TaskOperand(task00Future))), Task.Type.Multiply);
        Future<Double> task10Future = taskBuffer.addTask(task10);
        Task task11 = new Task(new ArrayList<>(List.of(new TaskOperand(4.0), new TaskOperand(task01Future))), Task.Type.Multiply);
        Future<Double> task11Future = taskBuffer.addTask(task11);
        Task task12 = new Task(new ArrayList<>(List.of(new TaskOperand(5.0), new TaskOperand(task02Future))), Task.Type.Multiply);
        Future<Double> task12Future = taskBuffer.addTask(task12);
        Task task13 = new Task(new ArrayList<>(List.of(new TaskOperand(4.0), new TaskOperand(task03Future))), Task.Type.Multiply);
        Future<Double> task13Future = taskBuffer.addTask(task13);

        Task task20 = new Task(new ArrayList<>(List.of(new TaskOperand(task10Future), new TaskOperand(task11Future))), Task.Type.Add);
        Future<Double> task20Future = taskBuffer.addTask(task20);
        Task task21 = new Task(new ArrayList<>(List.of(new TaskOperand(task12Future), new TaskOperand(task13Future))), Task.Type.Add);
        Future<Double> task21Future = taskBuffer.addTask(task21);

        Task task30 = new Task(new ArrayList<>(List.of(new TaskOperand(task20Future), new TaskOperand(task21Future))), Task.Type.Divide);
        Future<Double> task30Future = taskBuffer.addTask(task30);
        try {
            Double taskResult = task30Future.get();
            Double expectedResult = 1.0;
            assertEquals(taskResult, expectedResult);
        } catch (InterruptedException e) {
            fail();
        } catch (ExecutionException e) {
            fail();
        }


    }

}
