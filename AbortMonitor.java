
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class AbortMonitor {

    private ReentrantLock lock;
    private Thread owner;
    private Object currentState;
    private HashMap<Thread, Object> revertStates;
    
    public AbortMonitor(Object obj){
        currentState = obj;
        revertStates = new HashMap<Thread, Object>();
        owner = null;
        lock = new ReentrantLock();
    }

    public void Aquire(){
        lock.lock();
        owner = Thread.currentThread();
//        revertStates.put(owner, deepCopy(currentState));
    }

    public void Release(){
        if(Thread.currentThread().equals(owner)){
        	revertStates.remove(owner);	//get rid of the unused revertState
        	owner = null;
            lock.unlock();
        }
        else
        {
        	throw new IllegalMonitorStateException();
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
                revertStates.put(owner, deepCopy(currentState));
            }
            currentState = obj;
        }
        else
        {
        	throw new IllegalMonitorStateException();
        }
    }

    public Object read(){
        if(Thread.currentThread().equals(owner)){
            if(revertStates.get(owner) == null){
                revertStates.put(owner, deepCopy(currentState));
            }
            return currentState;
        }
        else
        {
        	throw new IllegalMonitorStateException();
        }
    }

    public void Abort(){
        if(Thread.currentThread().equals(owner)){
            if(revertStates.get(owner) != null){
                currentState = revertStates.get(owner);
            }
            Release();
            Thread.currentThread().stop();
        }
        else
        {
        	throw new IllegalMonitorStateException();
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
        else
        {
        	throw new IllegalMonitorStateException();
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
        else
        {
        	throw new IllegalMonitorStateException();
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
        else
        {
        	throw new IllegalMonitorStateException();
        }
    }
    
    private static Object deepCopy(Object orig)
    {
        Object obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }
}
