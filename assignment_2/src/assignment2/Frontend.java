package assignment2;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.*;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.List;


enum State
{
    STOP, RUN
}

public class Frontend {

    private static Scanner scanner;
    private static StoreInterface store;
    private static State STATE;
  
  public static void main(String[] args) throws RemoteException, 
  NotBoundException, MalformedURLException, ParseException , Exception{

    String host = args[0];
    int port = Integer.parseInt(args[1]);
    Frontend.start();

    System.out.print("Finished");
  }

  public Frontend(String url) throws NotBoundException, MalformedURLException, RemoteException {
    Frontend.STATE = State.RUN;
    Frontend.store = (StoreInterface) Naming.lookup(url);
    // inventory interface
    Frontend.scanner = new Scanner(System.in);
  }

  private static int chooseOne(List<String> options)
    {
        for (int i = 0; i < options.size(); i++)
            System.out.println(String.format("%10d. %s", i+1, options.get(i).toString()));
        System.out.print("\n\n\tCHOICE: ");
        int choice = scanner.nextInt();
        System.out.println("\n");
        return choice;
    }

  private static void offerSelectOption() throws RemoteException, Exception {
    List<String> options = new LinkedList<String>();
    options.add("Place order");
    options.add("Check availability");
    options.add("Exit.");
    int choice = chooseOne(options);
    switch (choice) {
      case 1:
        orderOptions();
        break;
        ////place order
      case 2:
        availabilityOptions();
        break;
        //check availability
      case 3:
        STATE = State.STOP;
        break;

      default:
        break;
    }
  }

  private static void availabilityOptions() throws RemoteException, Exception {
    List<String> options = new LinkedList<>();
    options.add("Display 6 month product availabilty");
    options.add("Check availability of product for specified date");
    options.add("Exit.");
    int choice = chooseOne(options);
    switch (choice)
    {
        case 1:
          store.checkAllProductSixMonthAvailability();  
        // displayAllProductsSixMonthAvailability();
          break;

        case 2:
        specifyProductAvailability();  
        // checkProductAvailability();
          break;

        case 3:
          STATE = State.STOP;
          break;

        default:
            break;
    }
  }

  private static void orderOptions() throws RemoteException, Exception {
    List<String> options = new LinkedList<>();
    options.add("Display all current orders");
    options.add("Cancel an order");
    options.add("Place a new order");
    options.add("Exit");
    int choice = chooseOne(options);
    switch (choice)
    {
        case 1:
          store.listOrders("1");
          // display current orders
          break;

        case 2:
          specifyCancelOrder();
          // cancel order
          break;
        
        case 3:
          specifyOrder();
          break;

        case 4:
          STATE = State.STOP;
          break;

        default:
            break;
    }
  }

  private static void specifyProductAvailability() {
    String product = stringIn();
    int dayOfMonth = Frontend.scanner.nextInt();
    int month = Frontend.scanner.nextInt();
    int year = Frontend.scanner.nextInt();

    LocalDate checkDate = LocalDate.of(year, month, dayOfMonth);

    try {
      store.checkProductAvailability(product, checkDate);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static void specifyOrder() throws RemoteException, Exception {
    String order = "" ;
    for(int i = 0 ; i < 3 ; i ++ ){
      System.out.println("Specify order as product name, quantity, date");
      String c =  stringIn();
      order = order + c + " " ;
    }

    store.placeOrder(order);
  }

  private static void specifyCancelOrder() {
    String orderIdToBeCancelled = stringIn();
    try {
      store.cancelOrder("1", orderIdToBeCancelled);
    } catch (RemoteException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static String stringIn(){
    String choice = scanner.next();
    System.out.println("\n");
    return choice;
}

  public static void start() throws Exception
  {
      //run is true call main options
      while(STATE == State.RUN)
      {
          try
          {
              offerSelectOption();
          }
          catch (RemoteException e)
          {
              System.out.println("\n\tAn error has occured: " + e.getMessage());
          }
      }
  }

}
