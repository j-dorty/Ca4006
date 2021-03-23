package assignment_1;

import java.util.logging.*;
import java.util.Queue;
import java.util.*;


public class NetworkQueue {
  
  private final Logger log = Logger.getLogger("assignment_1");
  private Queue<String> networkQueue; //queue of String

  public NetworkQueue() {
    networkQueue = new LinkedList<>();
  }

  public synchronized void add(String task) {
    networkQueue.add(task);
  }

    // remove TASK
	public synchronized void remove() {
    networkQueue.remove();
  }
	public synchronized String get() {
    return networkQueue.peek();
  }

	public boolean isEmpty() {
    return size() == 0;
  }

	public int size() {
    return networkQueue.size();
  }
}
  
