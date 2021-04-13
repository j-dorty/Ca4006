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
  private static final long serialVersionUID = 24L;
  private String fileName = "store_inventory.txt" ;
  transient BufferedReader br ;
  int ran = 1 ;
  File file ;
  char start ;
  char end ;



  public StoreSystem() throws RemoteException, Exception {
    super();
    file = new File(fileName);
  }

  public synchronized void placeOrder(String order) throws RemoteException, Exception {
    // order = "Game Boy 500 12/05/2021 12:00"
    String[] orderAsList = order.split(" "); // ["game", "boy", "500",...]
    String orderedProduct = String.join(" ", Arrays.copyOfRange(orderAsList, 0, orderAsList.length - 3)); //order[:-3]
    String orderedQuantity = orderAsList[orderAsList.length - 3]; //.get(orderAsList.length - 3);
    String orderDate = orderAsList[orderAsList.length - 2];
    String orderTime = orderAsList[orderAsList.length - 1]; 
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
        String[] inventoryEntry = line.split(" ");

        String inventoryProduct = String.join(" ", Arrays.copyOfRange(inventoryEntry, 0, inventoryEntry.length - 3));
        if (orderedProduct.equals(inventoryProduct)) {
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
    try {
    FileReader fileReader = new FileReader("current_orders.txt");
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    
    String currentOrder = bufferedReader.readLine();

      while(currentOrder != null) {
        String[] currentOrderList = currentOrder.split(" ");

        String customerId = currentOrderList[0];
        if (customerId.equals(userId)) {
          System.out.println(currentOrder);
        }
      }
      bufferedReader.close();
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  public synchronized void checkProductAvailability(String productName, LocalDate checkDate) {
    try {
      LocalDate currentDate = LocalDate.now(); //2017-01-03
      long daysBetweenDates = currentDate.until(checkDate, ChronoUnit.DAYS);
      long numberOfRestocks = daysBetweenDates % 30; // 1
      long daysRemaining = daysBetweenDates / 30; // 10

      FileReader fileReader = new FileReader("store_inventory.txt");
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      String line = bufferedReader.readLine();

      String[] productInfo = {"", "", "", ""};

      while(line != null) {
        String[] inventoryEntry = line.split(" ");

        String inventoryProduct = String.join(" ", Arrays.copyOfRange(inventoryEntry, 0, inventoryEntry.length - 3));
        if(inventoryProduct.equals(productName)) {
          productInfo = inventoryEntry;
          break;
        }
      }

      bufferedReader.close();

      String restockQuanity = productInfo[productInfo.length - 1];
      String restockDate = productInfo[productInfo.length - 2];
      String currentQuantity = productInfo[productInfo.length - 3];

      int currentDay = currentDate.getDayOfMonth();
      if(currentDay < Integer.parseInt(restockDate)) {
        long checkDay = currentDay + daysRemaining;
        if(checkDay > Integer.parseInt(restockDate)) {
          numberOfRestocks++;
        }
      }

      long futureQuantityWithNoOrders = Integer.parseInt(currentQuantity) + (Integer.parseInt(restockQuanity) * numberOfRestocks);

          
      FileReader orderFileReader = new FileReader("current_orders.txt");
      BufferedReader orderBufferedReader = new BufferedReader(orderFileReader);

      String currentOrder = orderBufferedReader.readLine();

      long totalOrderedAmount = 0;
      while(currentOrder != null) {
        String[] currentOrderList = currentOrder.split(" ");

        String orderedProduct = String.join(" ", Arrays.copyOfRange(currentOrderList, 1, currentOrderList.length - 3));
        String orderedQuanity = currentOrderList[-2];
        if(orderedProduct.equals(productName)) {
          totalOrderedAmount += Integer.parseInt(orderedQuanity);
        }


        currentOrder = orderBufferedReader.readLine();
      }
      orderBufferedReader.close();

      long predictedFutureQuantityWithOrders = futureQuantityWithNoOrders - totalOrderedAmount;

      System.out.println("The predicted amount of product: " + productName + " on " + checkDate + " is " + predictedFutureQuantityWithOrders);
      
    } catch(IOException e) {
      e.printStackTrace();
    }

  }


  public synchronized void checkAllProductSixMonthAvailability() {
    try {
      LocalDate currentDate = LocalDate.now(); //2017-01-03
      LocalDate sixMonthsFromNow = currentDate.plusMonths(6);
      FileReader fileReader = new FileReader("store_inventory.txt");
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      String line = bufferedReader.readLine();
      while(line != null) {
        String[] inventoryEntry = line.split(" ");

        String inventoryProduct = String.join(" ", Arrays.copyOfRange(inventoryEntry, 0, inventoryEntry.length - 3));

        checkProductAvailability(inventoryProduct,sixMonthsFromNow);

        line = bufferedReader.readLine();

      }

      bufferedReader.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
  }

  // userId OrderID orderredproduct qauntity date ....
  // 1 35 game boy....
  public synchronized void cancelOrder(String userId, String orderIdTobeCancelled) {
    try {
      ArrayList<String> ordersNotToBecCancelled = new ArrayList<String>();

      FileReader fileReader = new FileReader("current_orders.txt");
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      String currentOrder = bufferedReader.readLine();


      while(currentOrder != null) {
        String[] currentOrderList = currentOrder.split(" ");

        String customerId = currentOrderList[0];
        String orderId = currentOrderList[1];
        if(!(customerId.equals(userId) && orderId.equals(orderIdTobeCancelled))) {
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
