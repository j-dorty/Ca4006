package assignment2;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.io.*;
import java.util.Queue;


enum State
{
    STOP, RUN
}


public class Client
{
    private FileInterface file;
    private Scanner scanner;
    private State STATE;
    private int dn = 0 ;

    public static void main(String[] args) throws
        RemoteException, NotBoundException, MalformedURLException, ParseException ,Exception
    {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        String serviceUrl = "rmi://" + host + ":" + port + "/booking";
        System.out.println(serviceUrl);

        Client client = new Client(serviceUrl);
        client.start();

        System.out.println("FINISHED");
    }

    public Client(String url) throws NotBoundException, MalformedURLException, RemoteException
    {
        this.STATE = State.RUN;
        this.file = (FileInterface) Naming.lookup(url);
        this.scanner = new Scanner(System.in);
    }
///////////////////

private void mainOption() throws RemoteException ,Exception
{
    List<String> options = new LinkedList<>();
    options.add("Chunk File");
    options.add("Get Queue");
    options.add("Exit.");

    System.out.println("\n\nChoose one of the procedures: ");
    int choice = chooseOne(options);
    switch (choice)
    {
        case 1:
            chunkOptions();
            break ;
        case 2:
            System.out.println(file.getQueue());
            break;
        case 3:
        STATE = State.STOP;

        default:
            break;
    }
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



    private void chunkOptions()  throws RemoteException, Exception{
        List<String> options = new LinkedList<>();
        options.add("Specify chunk");
        options.add("Exit.");
        int choice = chooseOne(options);
        switch (choice)
        {
            case 1:
                specifyChunk();
                break;

            case 2:
            STATE = State.STOP;
            break;

            default:
                break;
        }

    }



public void specifyChunk() throws RemoteException, Exception{
    List<String> options = new LinkedList<>();
    String a = "" ;
    for(int i = 0 ; i < 2 ; i ++ ){
        System.out.println("Choses start chunk.");
        char c =  charIn();
        a = a + c ;
    }
    {
        file.addQueue(a.charAt(0),a.charAt(1));
        Queue<String> q = file.getQueue() ;
        System.out.println("Estimated time in ms : " + q.size() * 14788);
        ArrayList<String> f = file.chunkFile(a.charAt(0),a.charAt(1));
        List<String> options2 = new LinkedList<>();
        options2.add("Yes");
        options2.add("No.");
        System.out.println(f.get(1));
        System.out.println("\n Cancel Download :");
        int c = chooseOne(options2);
        switch (c)
        {
            case 1:
                break;
            case 2:
              try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file.getChunkName()+".txt"), "utf-8"))) {
                       writer.write(f.get(0));
                   }
            default :
                break;
            }
}


    }
    private char charIn(){
        char choice = scanner.next().charAt(0);
        System.out.println("\n");
        return choice;
    }


    private void chunkInput(){
    }


    public void start() throws Exception
    {
        //run is true call main options
        while(STATE == State.RUN)
        {
            try
            {
                mainOption();
            }
            catch (RemoteException e)
            {
                System.out.println("\n\tI'M SORRY BUT AN ERROR OCCURED: " + e.getMessage());
            }
        }
    }
}