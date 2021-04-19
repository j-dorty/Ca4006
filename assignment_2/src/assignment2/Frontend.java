package assignment2;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.List;


public class Frontend {

    private static Scanner scanner;
    private static StoreInterface store;
  
  public static void main(String[] args) throws RemoteException, 
  NotBoundException, MalformedURLException, ParseException , Exception{

    String host = args[0];
    int port = Integer.parseInt(args[1]);

    String serviceUrl = "rmi://" + host + ":" + port + "/booking";
    System.out.println(serviceUrl);

    // initialise and start the frontend 
    Frontend frontend = new Frontend(serviceUrl);
    frontend.start();

    System.out.print("Finished");
  }

  public Frontend(String url) throws NotBoundException, MalformedURLException, RemoteException {
    this.store = (StoreInterface) Naming.lookup(url);
    this.scanner = new Scanner(System.in);
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
    options.add("Orders");
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
        System.exit(0);
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
          System.exit(0);
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
          System.exit(0);
          break;

        default:
            break;
    }
  }

  private static void specifyProductAvailability() {
    System.out.println("Enter the name of the product you want to check the availability of:");
    scanner.nextLine(); // consume \n
    String product = scanner.nextLine();
    System.out.println(product);
    System.out.println("Enter the date as dd/mm/yyyy");
    System.out.println("Enter the day:");
    int dayOfMonth = scanner.nextInt();
    System.out.println("Enter the month:");
    int month = scanner.nextInt();
    System.out.println("Enter the year");
    int year = scanner.nextInt();

    LocalDate checkDate = LocalDate.of(year, month, dayOfMonth);

    try {
      store.checkProductAvailability(product, checkDate);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static void specifyOrder() throws RemoteException, Exception {
    scanner.nextLine(); // consume new line
    System.out.println("Enter the name of the prodcut you would like to order:");
    String order = scanner.nextLine();
    System.out.println("Enter the quantity of the prodcut for the order:");
    String quanity = scanner.nextLine();
    System.out.println("Enter the date for the order as dd/mm/yyyy");
    System.out.println("Enter the day:");
    int dayOfMonth = scanner.nextInt();
    System.out.println("Enter the month:");
    int month = scanner.nextInt();
    System.out.println("Enter the year");
    int year = scanner.nextInt();

    LocalDate orderDate = LocalDate.of(year, month, dayOfMonth);
    

    store.placeOrder("1", order, quanity, orderDate);
  }

  private static void specifyCancelOrder() {
    System.out.println("Please enter the ID number of the order you would like to cancel:");
    scanner.nextLine();
    String orderIdToBeCancelled = scanner.nextLine();
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

  public void start() throws Exception
  {
      //run is true call main options
      while(true)
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
