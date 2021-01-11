import model.Task;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class TaskBuffer {

    private Semaphore tasksSemaphore;
    private Queue<Task> buffer;

    public TaskBuffer(int maxTasks){
        tasksSemaphore = new Semaphore(maxTasks);
        buffer = new LinkedList<>();
    }

    public void addTask(Task t){

        try {
            tasksSemaphore.acquire();
            buffer.add(t);
        }catch (InterruptedException iex){

        }
        tasksSemaphore.release();
    }

    public Task getTask(){

        Task r = null;
        try {
            tasksSemaphore.acquire();
            r = buffer.remove();
        }catch (InterruptedException iex){

        }
        tasksSemaphore.release();
        return r;
    }

}
