package assignment2;

import java.util.List;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Queue;
import java.util.ArrayList;
import java.time.LocalDate;



public interface StoreInterface extends Remote
{
    
    // define store system interface (stub) for frontend to be able to call backend functions

    public void placeOrder(String userId, String order, String quantity, LocalDate orderDate) throws RemoteException, Exception;
    public void listOrders(String order) throws RemoteException, Exception;
    public void checkProductAvailability(String productName, LocalDate checkDate) throws RemoteException, Exception;
    public void checkAllProductSixMonthAvailability() throws RemoteException, Exception;
    public void cancelOrder(String userId, String orderIdTobeCancelled) throws RemoteException, Exception;


}
