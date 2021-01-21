import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeCounter {

    private int counter;
    private Lock lock;

    public ThreadSafeCounter(int counter) {
        this.counter = counter;
        this.lock = new ReentrantLock();
    }


    public int getCounter() {
        return counter;
    }

    public void increment() {
        this.lock.lock();
        try {
            this.counter++;
        }finally {
            this.lock.unlock();
        }
    }

    public void decrement() {
        this.lock.lock();
        try {
            this.counter--;
        }finally {
            this.lock.unlock();
        }
    }

}
