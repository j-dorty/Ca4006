import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.text.ParseException;
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

    private Scanner scanner;
    private StoreInterface store;
    private State STATE;
  
  public static void main(String[] args) throws RemoteException, 
  NotBoundException, MalformedURLException, ParseException , Exception{

    String host = args[0];
    int port = Integer.parseInt(args[1]);
    Frontend.start();

    System.out.print("Finished");
  }

  public Frontend(String url) throws NotBoundException, MalformedURLException, RemoteException {
    this.STATE = State.RUN;
    this.store = (StoreInterface) Naming.lookup(url);
    // inventory interface
    this.scanner = new Scanner(System.in);
  }

  private int chooseOne(List<String> options)
    {
        for (int i = 0; i < options.size(); i++)
            System.out.println(String.format("%10d. %s", i+1, options.get(i).toString()));
        System.out.print("\n\n\tCHOICE: ");
        int choice = scanner.nextInt();
        System.out.println("\n");
        return choice;
    }

  private void offerSelectOption() throws RemoteException, Exception {
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
        STATE = STATE.STOP;
        break;

      default:
        break;
    }
  }

  private void availabilityOptions() throws RemoteException, Exception {
    List<String> options = new LinkedList<>();
    options.add("Display 6 month product availabilty");
    options.add("Check availability of product for specified date");
    options.add("Exit.");
    int choice = chooseOne(options);
    switch (choice)
    {
        case 1:
          displayAllProductsSixMonthAvailability();
          break;

        case 2:
          checkProductAvailability();
          break;

        case 3:
          STATE = State.STOP;
          break;

        default:
            break;
    }
  }

  private void orderOptions() throws RemoteException, Exception {
    List<String> options = new LinkedList<>();
    options.add("Display all current orders");
    options.add("Cancel an order");
    options.add("place a new order");
    options.add("Exit.");
    int choice = chooseOne(options);
    switch (choice)
    {
        case 1:
          // display current orders
          break;

        case 2:
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

  private void displayAllProductsSixMonthAvailability() throws RemoteException, Exception {
    System.out.println("You should be seeing all products availability");
  }

  private void checkProductAvailability() throws RemoteException, Exception {
    System.out.println("You should be promted for input to check a products availability on a specified date")
  }

  private void specifyOrder() throws RemoteException, Exception {
    List<String> options = new LinkedList<String>();
    String order = "" ;
    for(int i = 0 ; i < 3 ; i ++ ){
      System.out.println("specify order as prodcut name, quantity, date");
      String c =  charIn();
      a = a + c + " " ;
    }

    ArrayList<String> orderResponse = store.placeOrder(order);
  }

  private char stringIn(){
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
