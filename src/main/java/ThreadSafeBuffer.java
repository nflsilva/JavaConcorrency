import model.Task;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeBuffer {

    private Task[] taskBuffer;
    private int taskIndex;
    private Lock bufferLock;

    public ThreadSafeBuffer(int capacity){
        taskBuffer = new Task[capacity];
        bufferLock = new ReentrantLock();
        taskIndex = 0;
    }

    public void pushTask(Task task){
        bufferLock.lock();
        if(taskIndex < taskBuffer.length){
            taskBuffer[taskIndex] = task;
            taskIndex++;
        }
        bufferLock.unlock();
    }

     Task popTask(){
        Task t = null;
        bufferLock.lock();
        try {
            if(taskIndex > 0){
                taskIndex--;
                t = taskBuffer[taskIndex];
            }
        }
        finally {
            bufferLock.unlock();
        }

        return t;
    }


}
