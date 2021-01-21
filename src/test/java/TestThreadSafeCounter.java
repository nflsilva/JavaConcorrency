import model.Task;
import model.TaskOperand;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class TestThreadSafeCounter {


    @Test
    public void testConcurrency() throws Exception {

        int nTasks = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ThreadSafeCounter tsc = new ThreadSafeCounter(0);

        for(int i = 0; i < nTasks; i++){
            executorService.submit(() -> {tsc.increment();});
        }

        executorService.shutdown();

        assertEquals(nTasks-1, tsc.getCounter());

    }

}
