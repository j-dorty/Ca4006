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
public class FileSystem extends UnicastRemoteObject implements FileInterface, Serializable
{
    String ma = "";
    Hashtable <String,String> metadata ;
    private static final long serialVersionUID = 24L;
    private String fileName = "inventory.txt" ;
    BufferedReader br ;
    int ran = 1 ;
    File file ;
    Hashtable <String, LinkedList<String>> chunked ;
    char start ;
    char fin ;
    String chunkName ;
    Queue<String> q = new LinkedList<>();

    public FileSystem() throws RemoteException, Exception
        {
            super();
            file = new File(fileName);
            br = new BufferedReader(new FileReader(file));
        }

    //adds request to Queue
    public void addQueue(char start, char fin) throws RemoteException ,  Exception {
        this.start = start;
        this.fin = fin;
        chunkName = "";
        chunkName = chunkName + Character.toString(start);
        chunkName = chunkName + Character.toString(fin);
        q.add(chunkName);
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

    public synchronized  ArrayList<String> chunkFile(char start, char fin) throws RemoteException, Exception {
        File space = new File("/home");
        if(space.getFreeSpace( )> 500){
        String fname;
        Scanner scan = new Scanner("inventory.txt");
        fname = scan.nextLine();
        String line = null;
        chunked = new Hashtable<>() ;
        try
        {

        while(start <=fin){
            try{
                String a = Character.toString(start);
            chunked.put(a,new LinkedList<>()) ;
        }catch (NullPointerException e) {
        }
            start++;
        }
        /* FileReader reads text files in the default encoding */
        FileReader fileReader = new FileReader(fname);

        /* always wrap the FileReader in BufferedReader */
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String[] nline;
        while((line = bufferedReader.readLine()) != null)
        {
            nline = line.split("\\s+");
            List <String> listS = new ArrayList <String> ();
            listS = Arrays.asList(nline);
            if(listS.size() > 1) {
            try{
               String c = listS.get(1).substring(0,1);
               LinkedList<String> l = (chunked.get(c));

                   l.add(line);
           }
           catch (NullPointerException e) {
           }
           }
        }
        bufferedReader.close();
    }
    catch(IOException ex)
    {
        System.out.println("Error reading file named '" + fname + "'");
    }
    q.remove();
     ArrayList<String> mylist = new ArrayList<String>();
     mylist.add(getFile());
     mylist.add(getMeta());
     return mylist;

    }
    System.out.println("Disk space is low");
     ArrayList<String> mylist = new ArrayList<String>();
     mylist.add("Sever disk space is low");
     mylist.add("Sever disk space is low");

    return mylist ;
}

    public synchronized String getMeta() throws RemoteException , Exception
     {
         int sum = 0 ;
         Set<String> keys = chunked.keySet();
         String words;
         int sumLetters = 0 ;
         String s = "";
         // for-each loop
         for(String key : keys) {
             // for each item in list
             LinkedList<String> a = chunked.get(key);

             // for last member of item -> number of words
             for(int i = 0 ; i <  a.size(); i ++){
             words = a.get(i);
             String[] nline = words.split("\\s+");
             List <String> l = new ArrayList <String> ();
             l = Arrays.asList(nline);
             String num = l.get(l.size() - 1);
             sum = sum + Integer.parseInt(num);
             sumLetters ++ ;

         }
         }
         s = s + "\n Meta data for chunk " + getChunkName().charAt(0) + " --> " + getChunkName().charAt(1) ;
         s = s + "\n Total number of words for chunk : " + sumLetters +"\n";
         s = s + "Count of words : " + sum + "\n ";
         return s;

     }

    public synchronized String getFile() throws RemoteException , Exception
     {
         Set<String> keys = chunked.keySet();
         String words = "";
         ArrayList<String> mylist = new ArrayList<String>();
         for(String key : keys) {
             LinkedList<String> a = chunked.get(key);
             for(int i = 0 ; i <  a.size(); i ++){
                 words = a.get(i);
                 String[] nline = words.split("\\s+");
                 List <String> l = new ArrayList <String> ();
                l = Arrays.asList(nline);
                String num = l.get(l.size() - 1);
                int repeat = Integer.parseInt(num);
                for(int j = 0 ; j <= repeat ; j++){
                    mylist.add(l.get(2)+" ");
             }
        }
        }
    Collections.shuffle(mylist);
    String str = mylist.toString() ;
    words = str.substring(1,str.length() - 2 ) ;
    return words;

    }

    public synchronized String getChunkName() throws RemoteException , Exception
    {
        return chunkName ;
    }

    public  Queue<String> getQueue() throws RemoteException , Exception
    {
        return q ;
    }

}