package assignment2;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.rmi.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Arrays;
import java.util.Set;
import java.io.Serializable;
import java.util.*;
import java.time.LocalDate;  
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
  }

  public synchronized void placeOrder(String order) throws RemoteException, Exception {
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
    bufferedReader.close();

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
    bufferedReader.close();
  }

  public synchronized void checkProductAvailability(String productName, LocalDate checkDate) {
    LocalDate currentDate = LocalDate.now(); //2017-01-03
    int daysBetweenDates = currentDate.until(checkDate, ChronoUnit.DAYS);
    int numberOfRestocks = daysBetweenDates % 30; // 1
    int daysRemaining = daysBetweenDates / 30; // 10

    FileReader fileReader = new FileReader("store_inventory.txt");
    BufferedReader bufferedReader = new BufferedReader(fileReader);

    String line = bufferedReader.readLine();

    String productInfo;
    try {
      while(line != null) {
        inventoryEntry = line.split(" ");

        inventoryProduct = String.join(" ", Arrays.copyOfRange(inventoryEntry, 0, inventoryEntry.length - 3));
        if(inventoryProduct == productName) {
          productInfo = inventoryEntry;
          break;
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
    }

    String restockQuanity = inventoryEntry[inventoryEntry.length - 1];
    String restockDate = inventoryEntry[inventoryEntry.length - 2];
    String currentQuantity = inventoryEntry[inventoryEntry.length - 3];

    currentDay = currentDate.getDayOfMonth();
    if(currentDay < parseInt(restockDate)) {
      checkDay = currentDay + daysRemaining;
      if(checkDay > parseInt(restockDate)) {
        numberOfRestocks ++;
      }
    }

    int futureQuantityWithNoOrders = parseInt(currentQuantity) + (parseInt(restockQuanity) * numberofRestocks);
    
    FileReader ordferFileReader = new FileReader("current_orders.txt");
    BufferedReader orderBufferedReader = new BufferedReader(fileReader);

    String currentOrder = orderBufferedReader.readLine();

  }


  // userId OrderID orderredproduct qauntity date ....
  // 1 35 game boy....
  public synchronized void cancelOrder(String userId, String orderIdTobeCancelled) {
    ArrayList<String> ordersNotToBecCancelled;

    FileReader fileReader = new FileReader("current_orders.txt");
    BufferedReader bufferedReader = new BufferedReader(fileReader);

    String currentOrder = bufferedReader.readLine();

    try {
      while(currentOrder != null) {
        currentOrderList = line.split(" ");

        customerId = currentOrderList[0];
        orderId = currentOrderList[1];
        if(!(customerId == userId && orderId == orderIdTobeCancelled)) {
          ordersNotToBecCancelled.add(currentOrder);
        }

      }

      FileWriter fileWriter = new FileWriter("filepath.txt", false);

      for(String order : ordersNotToBecCancelled) {

        fileWriter.write(order);
      }
      fileWriter.close();
      bufferedReader.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

  } 


  private void confirmOrder(String orderedProduct, String orderedQuantity, String orderDate, String orderTime) {
    try {
      FileWriter fileWriter = new FileWriter("current_orders.txt", true);
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
