
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

class BasicMonitor {

    private ReentrantLock lock;
    private Thread owner;
    private Object currentState;
    private HashMap<Thread, Object> revertStates;

    public BasicMonitor(Object obj){
        currentState = obj;
        revertStates = new HashMap<Thread, Object>();
        owner = null;
        lock = new ReentrantLock();
    }

    public void Aquire(){
        lock.lock();
        owner = Thread.currentThread();
        revertStates.put(owner, null);
    }

    public void Release(){
        if(Thread.currentThread().equals(owner)){
        	revertStates.remove(owner);	//get rid of the unused revertState
        	owner = null;
            lock.unlock();
        }
    }

    /**
     * Update the object being monitored to be obj
     * If this is the first time this Thread has written
     * to this object since the last time it acquired the monitor,
     * store the old state of the object in case of abort.
     * @param obj
     */
    public void Write(Object obj){
        if(Thread.currentThread().equals(owner)){
            if(revertStates.get(owner) == null){
                revertStates.put(owner, currentState);
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
            if(revertStates.get(owner) != null){
                currentState = revertStates.get(owner);
            }
            Release();
            Thread.currentThread().stop();
        }
    }

    /**
     * Release the lock until notified
     * @throws Exception 
     */
    public void Wait() throws Exception{

        if(Thread.currentThread().equals(owner)){
        	//syncrhonized block to utilize standard wait/notify
        	//choice of "lock" as the synchronizing object is arbitrary
        	synchronized(lock)
        	{
	        	//release, but keep state
	        	owner = null;
	        	lock.unlock();
	        	//wait
	        	lock.wait();
	        	//wake up and regain ownership
	        	lock.lock();
	        	owner = Thread.currentThread();	//synchronized block prevents race condition
        	}
        }
    }

    /**
     * Wake one thread that is waiting
     */
    public void Notify(){
        if(Thread.currentThread().equals(owner)){
        	synchronized(lock)
        	{
        		lock.notify();
        	}
        }
    }

    /**
     * Wake all threads that are waiting
     */
    public void NotifyAll(){
        if(Thread.currentThread().equals(owner)){
        	synchronized(lock)
        	{
        		lock.notifyAll();
        	}
        }
    }
}