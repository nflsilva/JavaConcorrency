import model.Task;
import model.TaskOperand;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class App {

    public static void main(String[] args) throws ExecutionException, InterruptedException {


        ExecutorService executorService = Executors.newFixedThreadPool(10);
        TaskBuffer tb = new TaskBuffer(10);

        Task task0 = new Task(0, new ArrayList<>(List.of(
                new TaskOperand(12.3),
                new TaskOperand(2.3),
                new TaskOperand(1.2))), Task.Type.Add);

        tb.addTask(task0);
        Future<Double> task0Future = executorService.submit(new TaskConsumer(tb));

        Task task1 = new Task(1, new ArrayList<>(List.of(
                new TaskOperand(100.0),
                new TaskOperand(task0Future))), Task.Type.Add);
        tb.addTask(task1);

        Future<Double> task1Future = executorService.submit(new TaskConsumer(tb));

        Double result = task1Future.get();

        System.out.println("Result: " + result.toString());
        System.out.println("Done!");

    }


}
