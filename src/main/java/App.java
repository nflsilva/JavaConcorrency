import model.Task;
import model.TaskOperand;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class App {

    public static void main(String[] args) {


        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Task task = new Task(0, new ArrayList<>(List.of(
                new TaskOperand(12.3),
                new TaskOperand(2.3),
                new TaskOperand(1.2))), Task.Type.Add);

        TaskBuffer tb = new TaskBuffer(10);
        TaskConsumer ts = new TaskConsumer(tb);

        Future<Double> future = executorService.submit(ts);

        System.out.println("Done!");

    }


}
