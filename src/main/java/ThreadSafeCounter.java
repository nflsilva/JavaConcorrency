import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeCounter {

    private int counter;
    private ReadWriteLock lock;

    public ThreadSafeCounter(int counter) {
        this.counter = counter;
        this.lock = new ReentrantReadWriteLock();
    }


    public int getCounter() {
        this.lock.readLock().lock();
        int returnValue;
        try {
            returnValue = this.counter;
        }finally {
            this.lock.readLock().unlock();
        }
        return returnValue;
    }

    public void increment() {
        this.lock.writeLock().lock();
        try {
            this.counter++;
        }finally {
            this.lock.writeLock().unlock();
        }
    }

    public void decrement() {
        this.lock.writeLock().lock();
        try {
            this.counter--;
        }finally {
            this.lock.writeLock().unlock();
        }
    }

}
