import model.Task;

import java.util.concurrent.Callable;

public class TaskConsumer implements Callable<Double> {


    private TaskBuffer buffer;

    public TaskConsumer(TaskBuffer tf){
        buffer = tf;
    }

    @Override
    public Double call(){
        Task task = buffer.getTask();
        return task.compute();
    }
}
