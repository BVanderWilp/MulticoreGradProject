import java.util.ArrayList;

public class AbortMonitorPerformanceTests {

	public static class SmallAbortThread extends Thread {
		AbortMonitor myMonitor;

		public void run() {
			myMonitor.Aquire();
			myMonitor.Write(new Object());
			myMonitor.Abort();
		}
	}

	/**
	 * Acquire monitor on a small object, then abort
	 * @throws InterruptedException
	 */
	public static void SmallAbort() throws InterruptedException {
//		long startmem = Runtime.getRuntime().freeMemory();
		ArrayList<String> arr = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			arr.add("foo");
		}
		AbortMonitor myMonitor = new AbortMonitor(arr);
		SmallAbortThread t = new SmallAbortThread();
		t.myMonitor = myMonitor;
		long start = System.nanoTime();
		t.start();
		t.join();
		long end = System.nanoTime();
//		long endmem = Runtime.getRuntime().freeMemory();
		System.out.printf("Time for small abort: %dms\n", (end - start) / (1000000));
//		System.out.printf("Memory used in Small Abort: %d kB\n", (startmem - endmem) / 1000);
	}

	public static class MediumAbortThread extends Thread {
		AbortMonitor myMonitor;

		public void run() {
			myMonitor.Aquire();
			myMonitor.Write(new Object());
			myMonitor.Abort();
		}
	}

	/**
	 * Acquire monitor on a medium object, then abort
	 * @throws InterruptedException
	 */
	public static void MediumAbort() throws InterruptedException {
//		long startmem = Runtime.getRuntime().freeMemory();
		ArrayList<String> arr = new ArrayList<String>();
		for (int i = 0; i < 10000; i++) {
			arr.add("foo");
		}
		AbortMonitor myMonitor = new AbortMonitor(arr);
		SmallAbortThread t = new SmallAbortThread();
		t.myMonitor = myMonitor;
		long start = System.nanoTime();
		t.start();
		t.join();
		long end = System.nanoTime();
//		long endmem = Runtime.getRuntime().freeMemory();
		System.out.printf("Time for medium abort: %dms\n", (end - start) / (1000000));
//		System.out.printf("Memory used in Medium Abort: %d kB\n", (startmem - endmem) / 1000);
	}

	public static class LargeAbortThread extends Thread {
		AbortMonitor myMonitor;

		public void run() {
			myMonitor.Aquire();
			myMonitor.Write(new Object());
			myMonitor.Abort();
		}
	}

	/**
	 * Acquire monitor on a large object, then abort
	 * @throws InterruptedException
	 */
	public static void LargeAbort() throws InterruptedException {
//		long startmem = Runtime.getRuntime().freeMemory();
		ArrayList<String> arr = new ArrayList<String>();
		for (int i = 0; i < 10000000; i++) // Any larger and we run out of heap
											// space
		{
			arr.add("foo");
		}
		AbortMonitor myMonitor = new AbortMonitor(arr);
		SmallAbortThread t = new SmallAbortThread();
		t.myMonitor = myMonitor;
		long start = System.nanoTime();
		t.start();
		t.join();
		long end = System.nanoTime();
//		long endmem = Runtime.getRuntime().freeMemory();
		System.out.printf("Time for large abort: %dms\n", (end - start) / (1000000));
//		System.out.printf("Memory used in Large Abort: %d kB\n", (startmem - endmem) / 1000);
	}

	public static class NormalIncrementThread extends Thread {
		Integer P;
		int max;

		public void run() {
			while (P < max) {
				synchronized (P) {
					P += 1;
				}
			}
		}
	}

	/**
	 * 10 threads using standard monitor to increment to a small number
	 * @throws InterruptedException
	 */
	public static void SmallNormalIncrement() throws InterruptedException {
//		long startmem = Runtime.getRuntime().freeMemory();
		ArrayList<NormalIncrementThread> arr = new ArrayList<NormalIncrementThread>();
		Integer P = new Integer(0);
		for (int i = 0; i < 10; i++) {
			NormalIncrementThread t = new NormalIncrementThread();
			t.P = P;
			t.max = 1000;
			arr.add(t);
		}
		long start = System.nanoTime();
		for (NormalIncrementThread t : arr) {
			t.start();
		}
		for (NormalIncrementThread t : arr) {
			t.join();
		}
		long end = System.nanoTime();
//		long endmem = Runtime.getRuntime().freeMemory();
		System.out.printf("Time for Small Normal Increment: %dms\n", (end - start) / (1000000));
//		System.out.printf("Memory used in Small Normal Increment: %d kB\n", (startmem - endmem) / 1000);
	}

	/**
	 * 10 threads using standard monitor to increment to a small number
	 * @throws InterruptedException
	 */
	public static void MediumNormalIncrement() throws InterruptedException {
//		long startmem = Runtime.getRuntime().freeMemory();
		ArrayList<NormalIncrementThread> arr = new ArrayList<NormalIncrementThread>();
		Integer P = new Integer(0);
		for (int i = 0; i < 10; i++) {
			NormalIncrementThread t = new NormalIncrementThread();
			t.P = P;
			t.max = 100000;
			arr.add(t);
		}
		long start = System.nanoTime();
		for (NormalIncrementThread t : arr) {
			t.start();
		}
		for (NormalIncrementThread t : arr) {
			t.join();
		}
		long end = System.nanoTime();
//		long endmem = Runtime.getRuntime().freeMemory();
		System.out.printf("Time for Medium Normal Increment: %dms\n", (end - start) / (1000000));
//		System.out.printf("Memory used in Medium Normal Increment: %d kB\n", (startmem - endmem) / 1000);
	}
	
	/**
	 * 10 threads using standard monitor to increment to a large number
	 * @throws InterruptedException
	 */
	public static void LargeNormalIncrement() throws InterruptedException {
//		long startmem = Runtime.getRuntime().freeMemory();
		ArrayList<NormalIncrementThread> arr = new ArrayList<NormalIncrementThread>();
		Integer P = new Integer(0);
		for (int i = 0; i < 10; i++) {
			NormalIncrementThread t = new NormalIncrementThread();
			t.P = P;
			t.max = 10000000;
			arr.add(t);
		}
		long start = System.nanoTime();
		for (NormalIncrementThread t : arr) {
			t.start();
		}
		for (NormalIncrementThread t : arr) {
			t.join();
		}
		long end = System.nanoTime();
//		long endmem = Runtime.getRuntime().freeMemory();
		System.out.printf("Time for Large Normal Increment: %dms\n", (end - start) / (1000000));
//		System.out.printf("Memory used in Large Normal Increment: %d kB\n", (startmem - endmem) / 1000);
	}

	public static class CustomIncrementThread extends Thread {
		AbortMonitor P;
		int max;

		public void run() {
			while (true) {
				P.Aquire();
				Integer Pval = (Integer)P.read();
				if (Pval < max) {
					Pval += 1;
					P.Write(Pval);
					P.Release();
				} else {
					P.Release();
					break;
				}
			}
		}
	}
	
	/**
	 * 10 threads using our monitor to increment to a small number
	 * @throws InterruptedException
	 */
	public static void SmallCustomIncrement() throws InterruptedException {
//		long startmem = Runtime.getRuntime().freeMemory();
		ArrayList<CustomIncrementThread> arr = new ArrayList<CustomIncrementThread>();
		AbortMonitor P = new AbortMonitor(new Integer(0));
		for (int i = 0; i < 10; i++) {
			CustomIncrementThread t = new CustomIncrementThread();
			t.P = P;
			t.max = 1000;
			arr.add(t);
		}
		long start = System.nanoTime();
		for (CustomIncrementThread t : arr) {
			t.start();
		}
		for (CustomIncrementThread t : arr) {
			t.join();
		}
		long end = System.nanoTime();
//		long endmem = Runtime.getRuntime().freeMemory();
		System.out.printf("Time for Small Custom Increment: %dms\n", (end - start) / (1000000));
//		System.out.printf("Memory used in Small Custom Increment: %d kB\n", (startmem - endmem) / 1000);
	}
	
	/**
	 * 10 threads using our monitor to increment to a small number
	 * @throws InterruptedException
	 */
	public static void MediumCustomIncrement() throws InterruptedException {
//		long startmem = Runtime.getRuntime().freeMemory();
		ArrayList<CustomIncrementThread> arr = new ArrayList<CustomIncrementThread>();
		AbortMonitor P = new AbortMonitor(new Integer(0));
		for (int i = 0; i < 10; i++) {
			CustomIncrementThread t = new CustomIncrementThread();
			t.P = P;
			t.max = 100000;
			arr.add(t);
		}
		long start = System.nanoTime();
		for (CustomIncrementThread t : arr) {
			t.start();
		}
		for (CustomIncrementThread t : arr) {
			t.join();
		}
		long end = System.nanoTime();
//		long endmem = Runtime.getRuntime().freeMemory();
		System.out.printf("Time for Medium Custom Increment: %dms\n", (end - start) / (1000000));
//		System.out.printf("Memory used in Medium Custom Increment: %d kB\n", (startmem - endmem) / 1000);
	}
	
	/**
	 * 10 threads using our monitor to increment to a large number
	 * @throws InterruptedException
	 */
	public static void LargeCustomIncrement() throws InterruptedException {
//		long startmem = Runtime.getRuntime().freeMemory();
		ArrayList<CustomIncrementThread> arr = new ArrayList<CustomIncrementThread>();
		AbortMonitor P = new AbortMonitor(new Integer(0));
		for (int i = 0; i < 10; i++) {
			CustomIncrementThread t = new CustomIncrementThread();
			t.P = P;
			t.max = 10000000;
			arr.add(t);
		}
		long start = System.nanoTime();
		for (CustomIncrementThread t : arr) {
			t.start();
		}
		for (CustomIncrementThread t : arr) {
			t.join();
		}
		long end = System.nanoTime();
//		long endmem = Runtime.getRuntime().freeMemory();
		System.out.printf("Time for Large Custom Increment: %dms\n", (end - start) / (1000000));
//		System.out.printf("Memory used in Large Custom Increment: %d kB\n", (startmem - endmem) / 1000);
	}

	public static void main(String... args) throws InterruptedException {
		//comment out all but the desired test to avoid incorrect memory measurements
//		SmallAbort();
//		MediumAbort();
//		LargeAbort();
//		SmallNormalIncrement();
//		MediumNormalIncrement();
//		LargeNormalIncrement();
//		SmallCustomIncrement();
//		MediumCustomIncrement();
		LargeCustomIncrement();
	}
}
