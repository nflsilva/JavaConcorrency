import model.Task;
import model.TaskOperand;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;

public class TestThreadSafeBuffer {

    @Test
    public void testBufferPutGet() throws Exception {


        ThreadSafeBuffer taskBuffer = new ThreadSafeBuffer(10);

        Task t0 = new Task(new ArrayList<>(List.of(new TaskOperand(0.0))), Task.Type.Add);
        Task t1 = new Task(new ArrayList<>(List.of(new TaskOperand(0.0))), Task.Type.Add);

        taskBuffer.pushTask(t0);
        taskBuffer.pushTask(t1);

        Task t11 = taskBuffer.popTask();
        Task t00 = taskBuffer.popTask();

        assertEquals(t0, t00);
        assertEquals(t1, t11);

    }

    private class SimpleTaskProducer implements Runnable {

        private int type;
        private ThreadSafeBuffer taskBuffer;

        public SimpleTaskProducer(int type, ThreadSafeBuffer taskBuffer) {
            this.type = type;
            this.taskBuffer = taskBuffer;
        }

        @Override
        public void run() {
            switch (type){
                case 0:
                    taskBuffer.pushTask(new Task(new ArrayList<>(List.of(new TaskOperand(3.0), new TaskOperand(5.0))), Task.Type.Multiply));
                    break;
                case 1:
                    taskBuffer.pushTask(new Task(new ArrayList<>(List.of(new TaskOperand(2.0), new TaskOperand(10.0))), Task.Type.Multiply));
                    break;
                case 2:
                    taskBuffer.pushTask(new Task(new ArrayList<>(List.of(new TaskOperand(6.0), new TaskOperand(1.0))), Task.Type.Multiply));
                    break;
            }
        }
    }
    private class SimpleTaskConsumer implements Callable<Double> {

        private ThreadSafeBuffer taskBuffer;

        public SimpleTaskConsumer(ThreadSafeBuffer taskBuffer){
            this.taskBuffer = taskBuffer;
        }

        @Override
        public Double call() throws Exception {
            Task t = taskBuffer.popTask();
            if(t != null)
                return t.call();
            else
                return 0.0;
        }
    }

    @Test
    public void testBufferGetConcurrent() throws Exception {

        /*
        Computing:
        r = 3 * 5 + 2 * 10 + 6 * 1 + 3 * 5 + 2 * 10 + 6 * 1
        r must be 82
         */

        int nWorkers = 2;
        ThreadSafeBuffer taskBuffer = new ThreadSafeBuffer(10);
        ExecutorService executorService = Executors.newFixedThreadPool(nWorkers);

        List<Task> tasks = new ArrayList<>(List.of(
                new Task(new ArrayList<>(List.of(new TaskOperand(3.0), new TaskOperand(5.0))), Task.Type.Multiply),
                new Task(new ArrayList<>(List.of(new TaskOperand(2.0), new TaskOperand(10.0))), Task.Type.Multiply),
                new Task(new ArrayList<>(List.of(new TaskOperand(6.0), new TaskOperand(1.0))), Task.Type.Multiply),

                new Task(new ArrayList<>(List.of(new TaskOperand(3.0), new TaskOperand(5.0))), Task.Type.Multiply),
                new Task(new ArrayList<>(List.of(new TaskOperand(2.0), new TaskOperand(10.0))), Task.Type.Multiply),
                new Task(new ArrayList<>(List.of(new TaskOperand(6.0), new TaskOperand(1.0))), Task.Type.Multiply)
        ));

        for(Task t : tasks)
            taskBuffer.pushTask(t);


        List<SimpleTaskConsumer> consumers = new ArrayList<>();
        for(int i = 0; i < tasks.size(); i++)
            consumers.add(new SimpleTaskConsumer(taskBuffer));


        List<Future<Double>> futures = executorService.invokeAll(consumers);


        Double r = 0.0;
        for(Future<Double> result : futures)
            r += result.get();


        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals((Double)82.0, r);

    }

    @Test
    public void testBufferPutConcurrent() throws Exception {

        /*
        Computing:
        r = (3 * 5 + 2 * 10 + 6 * 1) * nCycles
        r must be 41 * nCycles
         */

        int nCycles = 10;
        int nProducers = 5;

        ThreadSafeBuffer taskBuffer = new ThreadSafeBuffer(50);
        ExecutorService producersExecService = Executors.newFixedThreadPool(nProducers);

        // Setup Consumers and Producers
        List<Future<Double>> futures = new ArrayList<>();
        for(int i = 0; i < nCycles * 3; i++) {
            producersExecService.submit(new SimpleTaskProducer(i % 3, taskBuffer)); }

        producersExecService.shutdown();
        producersExecService.awaitTermination(2, TimeUnit.SECONDS);

        Double r = 0.0;
        for(int i = 0; i < nCycles * 3; i++)
            r += taskBuffer.popTask().call();

        assertEquals((Double)(41.0 * nCycles), r);

    }






}
