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

        long startTime = System.currentTimeMillis();

        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);

        //System.out.println("Result: " + finalResult.toString());
        System.out.println("Done!: " + duration + " ms");

    }


}
