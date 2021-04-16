package assignment2;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.MalformedURLException;


public class Backend {
  
  // register the backend on the rmi registry with given port and host
  public static void main(String[] args) throws AlreadyBoundException, RemoteException, MalformedURLException, NotBoundException , Exception {
    String host = args[0];
    int port = Integer.parseInt(args[1]);
    String serviceName = "rmi://" + host + ":" + port + "/booking"; 
    System.out.println("Creating registry on " + serviceName);
    Registry registry = LocateRegistry.createRegistry(port);
    System.out.println(port);
    StoreSystem system = new StoreSystem();
    Naming.rebind(serviceName, system);
    System.out.println(serviceName + " is running.");
  }
}
