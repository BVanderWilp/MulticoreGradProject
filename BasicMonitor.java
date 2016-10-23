
import java.util.concurrent.locks.ReentrantLock;

class BasicMonitor {

    private ReentrantLock lock;
    private Thread owner;
    private Object currentState;
    private Object revertState;

    public BasicMonitor(Object obj){
        currentState = obj;
        revertState = null;
        owner = null;
        lock = new ReentrantLock();
    }

    public void Aquire(){
        lock.lock();
        owner = Thread.currentThread();
    }

    public void Release(){
        if(Thread.currentThread().equals(owner)){
            owner = null;
            revertState = null;
            lock.unlock();
        }
    }

    public void Write(Object obj){
        if(Thread.currentThread().equals(owner)){
            if(revertState == null){
                revertState = currentState;
            }
            currentState = obj;
        }
    }

    public Object read(){
        if(Thread.currentThread().equals(owner)){
            return currentState;
        }
        return null;
    }

    public void Abort(){
        if(Thread.currentThread().equals(owner)){
            if(revertState != null){
                currentState = revertState;
            }
            Release();
        }
    }

    public void Wait(){
        if(Thread.currentThread().equals(owner)){
        }
    }

    public void Notify(){
        if(Thread.currentThread().equals(owner)){
        }
    }

    public void NotifyAll(){
        if(Thread.currentThread().equals(owner)){
        }
    }
}
