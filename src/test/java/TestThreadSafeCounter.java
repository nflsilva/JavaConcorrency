import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

        assertEquals(nTasks, tsc.getCounterCurrent());

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

        assertEquals(-1000, tsc.getCounterCurrent());

    }

    private class SimpleTask implements Callable<Integer> {

        private ThreadSafeCounter counter;

        public SimpleTask(ThreadSafeCounter counter){
            this.counter = counter;
        }

        @Override
        public Integer call() throws Exception {
            return counter.incrementOrWait();
        }
    }

    @Test
    public void testIncrementOrWaitBlocking() throws Exception {

        int nCounter = 5;
        ThreadSafeCounter tsc = new ThreadSafeCounter(5);
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<Future<Integer>> futures = new ArrayList<>();
        for(int i = 0; i < nCounter + 1; i++){
            futures.add(executorService.submit(new SimpleTask(tsc)));
        }

        List<Integer> results = new ArrayList<>();
        for(Future<Integer> f : futures){
            try {
                results.add(f.get(50, TimeUnit.MILLISECONDS));
            }catch (TimeoutException te){
                return;
            }
        }
        fail("Thread did not wait.");

    }
    @Test
    public void testDecrementOrWaitUnblocking() throws Exception {

        int nCounter = 5;
        ThreadSafeCounter tsc = new ThreadSafeCounter(5);
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<Future<Integer>> futures = new ArrayList<>();
        for(int i = 0; i < nCounter; i++){
            futures.add(executorService.submit(new SimpleTask(tsc)));
        }

        // get results
        List<Integer> results = new ArrayList<>();
        for(Future<Integer> f : futures){
            try {
                results.add(f.get(50, TimeUnit.MILLISECONDS));
            }catch (TimeoutException te){
                fail("Thread did block.");
            }
        }

        // sort results and assert order
        results = results.stream().sorted().collect(Collectors.toList());
        for(int i = 0; i < results.size(); i++){
            int r = results.get(i);
            assertEquals("Order was not correct.", i, r);
        }

        // assert unblocking behaviour
        futures.add(executorService.submit(new SimpleTask(tsc)));
        Future<Integer> blockedFuture = futures.get(futures.size()-1);

        try {

            try {
                blockedFuture.get(50, TimeUnit.MILLISECONDS);
            }
            catch (TimeoutException te0){
                // thread is blocked, as expected
                executorService.submit(() -> { tsc.decrementOrWait();});
            }

            int actual = blockedFuture.get(50, TimeUnit.MILLISECONDS);
            assertEquals("Result was not expected.", nCounter -1, actual);

        }catch (TimeoutException te1){
            // the last thread is still blocked
            fail("The last thread did not unblock.");
        }


    }




}
