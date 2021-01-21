import model.Task;
import model.TaskOperand;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class TestThreadSafeCounter {


    @Test
    public void testIncrement() throws Exception {

        int nTasks = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ThreadSafeCounter tsc = new ThreadSafeCounter(0);

        for(int i = 0; i < nTasks; i++){
            executorService.submit(tsc::increment);
        }

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(nTasks, tsc.getCounter());

    }

    @Test
    public void testDecrement() throws Exception {

        int nTasks = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ThreadSafeCounter tsc = new ThreadSafeCounter(nTasks);

        for(int i = 0; i < nTasks; i++){
            executorService.submit(() -> {tsc.decrement();});
        }

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(0, tsc.getCounter());

    }

}
