package assignment2;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Arrays;
import java.util.Set;
import java.io.Serializable;
import java.util.*;
import java.util.Queue;
import java.io.File ;

public class StoreSystem extends UnicastRemoteObject implements StoreInterface, Serializable {
  
  String ma = "";
  Hashtable <String,String> metadata ;
  private static final long serialVersionUID = 24L;
  private String fileName = "store_inventory.txt" ;
  transient BufferedReader br ;
  int ran = 1 ;
  File file ;
  // Hashtable <String, LinkedList<String>> chunked ;
  char start ;
  char end ;
  // String chunkName ;
  private Queue<String> orderQueue = new LinkedList<>();
  private Queue<String> availabilityQueue = new LinkedList<>();


  public StoreSystem() throws RemoteException, Exception {
    super();
    file = new file(fileName);
    br = new BufferedReader(new FileReader(file));
  }



  public  String printFile() throws RemoteException ,  Exception
  {
      System.out.println("PrintFile request");

      String st ;
      String f = "" ;
      while ((st = br.readLine()) != null){
          f = f + st ;
      }
      return f ;
  }

  public synchronized ArrayList<String> placeOrder(String order) throws RemoteException, Exception {
    // order = "Game Boy 500 12/05/2021 12:00"
    String[] orderAsList = order.split(" "); // ["game", "boy", "500",...]
    String orderedProduct = String.join(" ", Arrays.copyOfRange(orderAsList, 0, orderAsList.length - 3)); //order[:-3]
    String orderedQuantity = orderAsList[orderAsList.length - 3]; //.get(orderAsList.length - 3);
    String orderDate = orderAsList[orderAsList.length - 2];
    String orderTime = orderAsList[orderAsList.length - 1]; 
    File space = new File("/home");
    String fname;
    FileReader fileReader = new FileReader("store_inventory.txt");
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    // Scanner scan = new Scanner("store_inventory.txt");

    // Game Boy 500 1 45
    // Apples 3500 11 450
    // Oranges 400 10 5645
    // Jolt Cola 4000 21 3445

    String line = bufferedReader.readLine();
    try {
      while(line != null) {
        inventoryEntry = line.split(" ");

        inventoryProduct = String.join(" ", Arrays.copyOfRange(inventoryEntry, 0, inventoryEntry.length - 3));
        if (orderedProduct == inventoryProduct) {
          if (orderCanBeCompleted(orderedProduct, orderedQuantity, orderDate, orderTime)) {
            confirmOrder(orderedProduct, orderedQuantity, orderDate, orderTime);
          }
          break;
        }
        line = bufferedReader.readLine();

      }
    } catch(FileNotFoundException e){
      e.printStackTrace();
    }

  }

  public synchronized void listOrders(String userId) { 
    FileReader fileReader = new FileReader("current_orders.txt");
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    
    String currentOrder = bufferedReader.readLine();
    try {
      while(currentOrder != null) {
        currentOrderList = line.split(" ");

        customerId = currentOrderList[0];
        if (customerId == userId) {
          System.out.println(currentOrder);
        }
      }
    } catch(FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public synchronized void checkProductAvailability(String productName, String checkDate) {
    
  }

  private void confirmOrder(String orderedProduct, String orderedQuantity, String orderDate, String orderTime) {
    try {
      FileWriter fileWriter = new FileWriter("current_orders.txt");
      // 1 Game Boy 500 12/5/2021 12:00
      fileWriter.write("1 " + orderedProduct + " " + orderedQuantity + " " + orderDate + " " + orderTime);
      fileWriter.close();
      System.out.println("Order Confirmed for " + orderedQuantity + " " + orderedProduct + "(s)" + " for " + orderDate + " " + orderTime);
      } catch (IOException e) {
        e.printStackTrace();
    }
  }

  private boolean orderCanBeCompleted(String orderedProduct, String orderedQuantity, String orderDate, String orderTime) {
    return true;
  }

}
