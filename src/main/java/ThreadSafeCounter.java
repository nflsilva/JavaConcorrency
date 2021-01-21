import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeCounter {

    private int counter;
    private ReadWriteLock lock;

    public ThreadSafeCounter(int counter) {
        this.counter = counter;
        this.lock = new ReentrantReadWriteLock();
    }


    public int getCounter() {
        return counter;
    }

    public void increment() {
        this.counter++;
    }

    public void decrement() {
        this.counter--;
    }

}
