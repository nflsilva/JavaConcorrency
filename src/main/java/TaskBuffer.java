import model.Task;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class TaskBuffer {

    private ExecutorService executorService;
    private Semaphore tasksSemaphore;
    private ConcurrentLinkedQueue<Task> buffer;

    public TaskBuffer(int maxTasks, int maxThreads){
        executorService = Executors.newFixedThreadPool(maxThreads);
        tasksSemaphore = new Semaphore(maxTasks);
        buffer = new ConcurrentLinkedQueue<>();
    }

    public Future<Double> addTask(Task task){
        Future<Double> taskFuture = null;
        try {
            tasksSemaphore.acquire();
            buffer.add(task);
            taskFuture = executorService.submit(new TaskConsumer(this));
        }catch (InterruptedException iex){

        }
        return taskFuture;
    }

    public Task getTask(){
        Task task = buffer.remove();
        tasksSemaphore.release();
        return task;
    }

}
