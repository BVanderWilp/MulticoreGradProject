import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class BasicMonitorTest {

	/**
	 * Acquires lock, writes foo, waits, then aborts
	 * @author Brad
	 *
	 */
	public static class Thread1A extends Thread{
		public BasicMonitor myMonitor;
		public void run(){
			myMonitor.Aquire();
			myMonitor.Write("foo");
			assertEquals(myMonitor.read(), "foo");
			try {
				myMonitor.Wait();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			assertEquals(myMonitor.read(), "foo");
			myMonitor.Abort();
			assertTrue(false);
		}
	}
	
	/**
	 * Acquires lock, writes bar, notifies, then aborts
	 * @author Brad
	 *
	 */
	public static class Thread1B extends Thread{
		public BasicMonitor myMonitor;
		public void run(){
			myMonitor.Aquire();
			myMonitor.Write("bar");
			assertEquals(myMonitor.read(), "bar");
			myMonitor.Notify();
			myMonitor.Abort();
			assertTrue(false);
			
		}
	}
	
	//String foo is null
	//tA writes "foo", sleeps
	//tB writes "bar", aborts, String back to "foo"
	//tA aborts, string back to null
	@Test public void Test1() throws InterruptedException
	{
		String foo = "";
		BasicMonitor myMonitor = new BasicMonitor(foo);
		myMonitor.Aquire();
		assertEquals(myMonitor.read(), "");
		myMonitor.Release();
		Thread1A tA = new Thread1A();
		Thread1B tB = new Thread1B();
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
		myMonitor.Aquire();
		assertEquals(myMonitor.read(), "");
		myMonitor.Release();
	}
	
	public static class Thread2A extends Thread{
		public BasicMonitor myMonitor;
		public void run(){
			myMonitor.Aquire();
			myMonitor.Write("foo");
			assertEquals(myMonitor.read(), "foo");
			myMonitor.Release();
			
		}
	}
	
	//TA writes foo
	//main thread reads foo afterwards
	@Test public void Test2() throws InterruptedException
	{
		String foo = "";
		BasicMonitor myMonitor = new BasicMonitor(foo);
		assertEquals(myMonitor.read(), null);
		Thread2A tA = new Thread2A();
		tA.myMonitor = myMonitor;
		tA.start();
		tA.join();
		myMonitor.Aquire();
		assertEquals(myMonitor.read(), "foo");
		myMonitor.Release();
	}
	
	//Cannot write without acquiring first
	@Test public void Test3() throws InterruptedException
	{
		String foo = "";
		BasicMonitor myMonitor = new BasicMonitor(foo);
		
		myMonitor.Write("foo");
		
		myMonitor.Aquire();
		assertEquals(myMonitor.read(), "");
		myMonitor.Release();
		
	}
	
	public static class Thread4A extends Thread{
		public BasicMonitor myMonitor;
		public ArrayList<String> fooList;
		public void run(){
			myMonitor.Aquire();
			fooList.add("bar2");
			myMonitor.Write(fooList);
			assertEquals(((ArrayList<String>) myMonitor.read()).size(), 2);
			myMonitor.Release();
			
		}
	}
	
	//main adds "bar1" to monitored list
	//thread adds "bar2"
	//main checks that list is of size 2
	@Test public void Test4() throws InterruptedException
	{
		ArrayList<String> fooList = new ArrayList<String>();
		BasicMonitor myMonitor = new BasicMonitor(fooList);
		myMonitor.Aquire();
		fooList.add("bar1");
		myMonitor.Write(fooList);
		assertEquals(((ArrayList<String>) myMonitor.read()).size(), 1);
		myMonitor.Release();
		
		Thread4A tA = new Thread4A();
		tA.myMonitor = myMonitor;
		tA.fooList = fooList;
		tA.start();
		tA.join();
		
		myMonitor.Aquire();
		assertEquals(((ArrayList<String>) myMonitor.read()).size(), 2);
		myMonitor.Release();
	}
	
	public static class Thread5A extends Thread{
		public BasicMonitor myMonitor;
		public void run(){
			myMonitor.Aquire();
			ArrayList<String> fooList = (ArrayList<String>) myMonitor.read();
			fooList.add("bar2");
			myMonitor.Write(fooList);
			assertEquals(((ArrayList<String>) myMonitor.read()).size(), 2);
			myMonitor.Abort();
			assertTrue(false);
			
		}
	}
	
	//main adds "bar1" to monitored list
	//thread adds "bar2", aborts
	//main checks that list is of size 1
	@Test public void Test5() throws InterruptedException
	{
		ArrayList<String> fooList = new ArrayList<String>();
		BasicMonitor myMonitor = new BasicMonitor(fooList);
		myMonitor.Aquire();
		fooList.add("bar1");
		myMonitor.Write(fooList);
		assertEquals(((ArrayList<String>) myMonitor.read()).size(), 1);
		myMonitor.Release();
		
		Thread5A tA = new Thread5A();
		tA.myMonitor = myMonitor;
		tA.start();
		tA.join();
		
		myMonitor.Aquire();
		assertEquals(((ArrayList<String>) myMonitor.read()).size(), 1);
		myMonitor.Release();
	}
	
	public static class Thread6A extends Thread{
		public BasicMonitor myMonitor;
		public void run(){
			myMonitor.Aquire();
			ArrayList<ArrayList<String>> fooList = (ArrayList<ArrayList<String>>) myMonitor.read();
			ArrayList<String> innerList = fooList.get(0);
			innerList.add("BAR2");
			myMonitor.Write(fooList);
			assertEquals(((ArrayList<ArrayList<String>>) myMonitor.read()).size(), 1);
			assertEquals(((ArrayList<ArrayList<String>>) myMonitor.read()).get(0).size(), 1);
			myMonitor.Abort();
			assertTrue(false);
			
		}
	}
	
	//fooList is a list of lists of strings
	//main adds an empty list
	//thread adds an element to list0, aborts
	//main checks that list0 is empty
	@Test public void Test6() throws InterruptedException
	{
		ArrayList<ArrayList<String>> fooList = new ArrayList<ArrayList<String>>();
		BasicMonitor myMonitor = new BasicMonitor(fooList);
		myMonitor.Aquire();
		fooList.add(new ArrayList<String>());
		myMonitor.Write(fooList);
		assertEquals(((ArrayList<ArrayList<String>>) myMonitor.read()).size(), 1);
		assertEquals(((ArrayList<ArrayList<String>>) myMonitor.read()).get(0).size(), 0);
		myMonitor.Release();
		
		Thread6A tA = new Thread6A();
		tA.myMonitor = myMonitor;
		tA.start();
		tA.join();
		
		myMonitor.Aquire();
		assertEquals(((ArrayList<ArrayList<String>>) myMonitor.read()).size(), 1);
		assertEquals(((ArrayList<ArrayList<String>>) myMonitor.read()).get(0).size(), 0);
		myMonitor.Release();
	}
}
