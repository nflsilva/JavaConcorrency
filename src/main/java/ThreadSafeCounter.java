import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeCounter {

    private int counterMax;
    private int counterCurrent;
    private Lock lock;
    private Lock waitLock;


    public ThreadSafeCounter(int counter) {
        this.counterMax = counter;
        this.counterCurrent = 0;
        this.lock = new ReentrantLock();
        this.waitLock = new ReentrantLock();
    }


    public int incrementOrWait() {
        int result = -1;
        do {
            lock.lock();
            if(counterCurrent == counterMax) {
                // counter is at max
                lock.unlock();
                synchronized (waitLock){
                    try {
                        waitLock.wait();
                    } catch (InterruptedException e) {
                        // woke up
                        continue;
                    }
                }
            }
            else {
                result = counterCurrent;
            }
        } while(result == -1);

        counterCurrent++;

        lock.unlock();
        return result;
    }

    public int decrementOrWait(){
        int result = -1;

        synchronized (waitLock){
            waitLock.notify();
        }

        counterCurrent--;

        return result;
    }



    public int getCounterCurrent() {
        this.lock.lock();
        int returnValue;
        try {
            returnValue = this.counterCurrent;
        }finally {
            this.lock.unlock();
        }
        return returnValue;
    }

    public void increment() {
        this.lock.lock();
        try {
            this.counterCurrent++;
        }finally {
            this.lock.unlock();
        }
    }

    public void decrement() {
        this.lock.lock();
        try {
            this.counterCurrent--;
        }finally {
            this.lock.unlock();
        }
    }

}
