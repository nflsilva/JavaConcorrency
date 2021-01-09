import model.Task;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class TestTasks {

    @Test
    public void testAdd(){
        Double expectedResult = 15.8;
        Task task = new Task(0, new ArrayList<>(List.of(12.3, 2.3, 1.2)), Task.Type.Add);
        Double taskResult = task.compute();
        assertEquals(expectedResult, taskResult);
    }

    @Test
    public void testSubtract(){
        Double expectedResult = 8.8;
        Task task = new Task(0, new ArrayList<>(List.of(12.3, 2.3, 1.2)), Task.Type.Subtract);
        Double taskResult = task.compute();
        assertEquals(expectedResult, taskResult);
    }

    @Test
    public void testMultiply(){
        Double expectedResult = 33.948;
        Task task = new Task(0, new ArrayList<>(List.of(12.3, 2.3, 1.2)), Task.Type.Multiply);
        Double taskResult = task.compute();
        assertEquals(expectedResult, taskResult);
    }

    @Test
    public void testDivide(){
        Double expectedResult = 6.15;
        Task task = new Task(0, new ArrayList<>(List.of(12.3, 2.0, 1.0)), Task.Type.Divide);
        Double taskResult = task.compute();
        assertEquals(expectedResult, taskResult);
    }

}
