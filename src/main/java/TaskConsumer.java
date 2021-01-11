import java.util.concurrent.Callable;

public class TaskConsumer implements Callable<Double> {


    private TaskBuffer buffer;

    public TaskConsumer(TaskBuffer tf){
        buffer = tf;
    }

    @Override
    public Double call(){
        return 0.0;
    }
}
