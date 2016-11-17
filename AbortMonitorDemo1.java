import java.util.ArrayList;
import java.util.Random;

public class AbortMonitorDemo1 {

	public static class RandChampThread extends Thread {
		public AbortMonitor myMonitor;
		public void run() {
			Random rand = new Random();
			while(true)
			{
				int add = rand.nextInt(3) - 1;
				myMonitor.Aquire();
				ArrayList<Integer> nums = (ArrayList<Integer>) myMonitor.read();
				Integer lastNum = nums.get(nums.size()-1);
				nums.add(lastNum + add);
				int newNum = nums.get(nums.size()-1).intValue();
				if(newNum < 0 || newNum > 5)
				{
					System.out.println("ERROR: val reached " + newNum);
					myMonitor.Abort();
					System.out.println("WE HAVE ABORTED");
				}
				myMonitor.Release();
				//System.out.println(val);
			}
		}
	}

	public static void main(String... args) throws InterruptedException {
		ArrayList<RandChampThread> threads = new ArrayList<RandChampThread>();
		ArrayList<Integer> nums = new ArrayList<Integer>();
		nums.add(3);
		AbortMonitor myMonitor = new AbortMonitor(nums);
		for (int i = 0; i < 5; i++) {
			RandChampThread t = new RandChampThread();
			t.myMonitor = myMonitor;
			threads.add(t);
		}
		for(Thread t : threads)
		{
			t.start();
		}
		for(Thread t : threads)
		{
			t.join();
		}
		myMonitor.Aquire();
		System.out.print("Result: " + myMonitor.read() + "\n");
		myMonitor.Release();
	}
}
