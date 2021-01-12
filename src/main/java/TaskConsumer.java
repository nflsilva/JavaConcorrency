import model.Task;

import java.util.concurrent.Callable;

public class TaskConsumer implements Callable<Double> {


    private TaskBuffer taskBuffer;

    public TaskConsumer(TaskBuffer taskBuffer){
        this.taskBuffer = taskBuffer;
    }

    @Override
    public Double call(){
        Task task = taskBuffer.getTask();
        return task.compute();
    }
}
