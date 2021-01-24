import model.Task;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeBuffer {

    private Task[] taskBuffer;
    private ThreadSafeCounter taskIndex;
    private Lock writeLock;
    private Lock readLock;
    private Lock bufferLock;


    public ThreadSafeBuffer(int capacity){
        taskBuffer = new Task[capacity];
        bufferLock = new ReentrantLock();
        writeLock = new ReentrantLock();
        readLock = new ReentrantLock();
        taskIndex = new ThreadSafeCounter(0);
    }

    public void pushTask(Task task){

        int writeIndex = -1;
        do {
            bufferLock.lock();
            int possibleIndex = taskIndex.getCounterCurrent();
            if(possibleIndex == taskBuffer.length) {
                // has no slot for new tasks
                bufferLock.unlock();
                try {
                    writeLock.wait();
                } catch (InterruptedException e) {
                    // woke up
                    continue;
                }
            }
            else {
                writeIndex = possibleIndex;
            }

        } while (writeIndex == -1);

        // found an available slot
        try {
            taskBuffer[writeIndex] = task;
            taskIndex.increment();
        }
        finally {
            bufferLock.unlock();
            try {
                readLock.notify();
            }
            catch (IllegalMonitorStateException e) {
                // Ignores
            }
        }

    }

    Task popTask(){

        int readIndex = -1;
        do {
            bufferLock.lock();
            int possibleIndex = taskIndex.getCounterCurrent();
            if(possibleIndex > 0) {
                // has no tasks available
                bufferLock.unlock();
                try {
                    readLock.wait();
                } catch (Exception e) {
                    // woke up
                    continue;
                }
            }
            else {
                readIndex = possibleIndex;
            }
        } while(readIndex == -1);

        // found an available task
        Task t = null;
        try {
            taskIndex.decrement();
            t = taskBuffer[taskIndex.getCounterCurrent()];
        }
        finally {
            bufferLock.unlock();
            try {
                writeLock.notify();
            }
            catch (IllegalMonitorStateException e) {
                // Ignores
            }
        }

        return t;
    }


}
