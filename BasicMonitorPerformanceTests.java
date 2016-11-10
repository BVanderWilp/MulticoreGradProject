import java.util.ArrayList;

public class BasicMonitorPerformanceTests {

	public static class SmallAbortThread extends Thread{
		BasicMonitor myMonitor;
		public void run(){
			myMonitor.Aquire();
//			myMonitor.Write(new Object());
			myMonitor.Abort();
		}
	}
	public static void SmallAbort() throws InterruptedException
	{
		System.out.printf("Memory before Small allocation: %d kB\n", Runtime.getRuntime().freeMemory()/1000);
		ArrayList<String> arr = new ArrayList<String>();
		for(int i = 0; i < 10; i++)
		{
			arr.add("foo");
		}
		BasicMonitor myMonitor = new BasicMonitor(arr);
		SmallAbortThread t = new SmallAbortThread();
		t.myMonitor = myMonitor;
		long start = System.nanoTime();
		t.start();
		t.join();
		long end = System.nanoTime();
		System.out.printf("Time for small abort: %dms\n", (end-start)/(1000000));
		System.out.printf("Memory after Small allocation: %d kB\n", Runtime.getRuntime().freeMemory()/1000);
	}
	
	public static class MediumAbortThread extends Thread{
		BasicMonitor myMonitor;
		public void run(){
			myMonitor.Aquire();
//			myMonitor.Write(new Object());
			myMonitor.Abort();
		}
	}
	public static void MediumAbort() throws InterruptedException
	{
		System.out.printf("Memory before Medium allocation: %d kB\n", Runtime.getRuntime().freeMemory()/1000);
		ArrayList<String> arr = new ArrayList<String>();
		for(int i = 0; i < 10000; i++)
		{
			arr.add("foo");
		}
		BasicMonitor myMonitor = new BasicMonitor(arr);
		SmallAbortThread t = new SmallAbortThread();
		t.myMonitor = myMonitor;
		long start = System.nanoTime();
		t.start();
		t.join();
		long end = System.nanoTime();
		System.out.printf("Time for medium abort: %dms\n", (end-start)/(1000000));
		System.out.printf("Memory after Medium allocation: %d kB\n", Runtime.getRuntime().freeMemory()/1000);
	}
	
	public static class LargeAbortThread extends Thread{
		BasicMonitor myMonitor;
		public void run(){
			myMonitor.Aquire();
//			myMonitor.Write(new Object());
			myMonitor.Abort();
		}
	}
	public static void LargeAbort() throws InterruptedException
	{
		System.out.printf("Memory before Large allocation: %d kB\n", Runtime.getRuntime().freeMemory()/1000);
		ArrayList<String> arr = new ArrayList<String>();
		for(int i = 0; i < 10000000; i++)	//Any larger and we run out of heap space
		{
			arr.add("foo");
		}
		BasicMonitor myMonitor = new BasicMonitor(arr);
		SmallAbortThread t = new SmallAbortThread();
		t.myMonitor = myMonitor;
		long start = System.nanoTime();
		t.start();
		t.join();
		long end = System.nanoTime();
		System.out.printf("Time for large abort: %dms\n", (end-start)/(1000000));
		System.out.printf("Memory after Large allocation: %d kB\n", Runtime.getRuntime().freeMemory()/1000);
	}
	
	public static void main(String...args) throws InterruptedException
	{
		SmallAbort();
		MediumAbort();
		LargeAbort();
	}
}
