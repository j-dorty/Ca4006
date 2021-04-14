package assignment2;

import java.util.List;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Queue;
import java.util.ArrayList;



public interface StoreInterface extends Remote
{
    
    public void placeOrder(String order) throws RemoteException, Exception;
    public void listOrders(String order) throws RemoteException, Exception;
    public void checkProductAvailability(String productName, LocalDate checkDate) throws RemoteException, Exception;
    public void checkAllProductSixMonthAvailability() throws RemoteException, Exception;
    public void cancelOrder(String userId, String orderIdTobeCancelled) throws RemoteException, Exception;


}
