
public class BasicMonitorMain1 {
	
	/**
	 * Acquires lock, writes foo, waits, then aborts
	 * @author Brad
	 *
	 */
	public static class ThreadA extends Thread{
		public BasicMonitor myMonitor;
		public void run(){
			myMonitor.Aquire();
			myMonitor.Write("foo");
			System.out.println("tA has written: " + myMonitor.read());
			try {
				myMonitor.Wait();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("tA has back from wait, tB aborted: " + myMonitor.read());
			myMonitor.Abort();
			System.out.println("tA ABORTED. YOU SHOULD NOT SEE THIS MESSAGE");
		}
	}
	
	/**
	 * Acquires lock, writes bar, notifies, then aborts
	 * @author Brad
	 *
	 */
	public static class ThreadB extends Thread{
		public BasicMonitor myMonitor;
		public void run(){
			myMonitor.Aquire();
			myMonitor.Write("bar");
			System.out.println("tB has written: " + myMonitor.read());
			myMonitor.Notify();
			myMonitor.Abort();
			System.out.println("tB ABORTED. YOU SHOULD NOT SEE THIS MESSAGE");
			
		}
	}
	
	public static void main(String...args) throws InterruptedException
	{
		String foo = "";
		BasicMonitor myMonitor = new BasicMonitor(foo);
		System.out.println("String begins as: " + myMonitor.read());
		ThreadA tA = new ThreadA();
		ThreadB tB = new ThreadB();
		tA.myMonitor = myMonitor;
		tB.myMonitor = myMonitor;
		tA.start();
		//make sure first thread acquires lock first
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tB.start();
		tA.join();
		System.out.println("Final result after tA aborts: " + myMonitor.read());
	}
}
