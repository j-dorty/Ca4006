package assignment_1;

import java.util.logging.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Random;
import java.util.Date;
import java.util.concurrent.*; 
import java.util.concurrent.TimeUnit;

public class Main{

  private static final Logger log = Logger.getLogger("assignment_1");

  // possible mission destinations and their respective distances
  final static String[] destinations = { "Moon", "Mercury","Venus","Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"};
  final static int[] distances = {38, 149, 256, 234, 882, 1599, 3053, 4624, 5207};
  // the constant to be multiplied by the distance to get the fuel in kilograms required
  final static int fuelConst = 2;
  final static int thrusterConst = 10;
  final static int instrumentsConst = 5;
  final static int controlSystemsConst = 1;
  
  public static void main(String[] args) throws InterruptedException{
    // read in number of missions to create
    Scanner scan = new Scanner(System.in);
    System.out.println("");
    System.out.println("");
    System.out.print("Enter amount of missions: ");
    int totalMission = scan.nextInt();
    scan.close();

    // create thread pool
    ExecutorService executor = Executors.newFixedThreadPool(100);

    int[] missionIDs = new int[totalMission];
    for (int i = 0; i < totalMission; i++){
      missionIDs[i] = i;
    }

    // create a Network object of bandwidth 3
    Network network = new Network(3);
    MissionController missionController = new MissionController(missionIDs, network, totalMission);
    
    // create x number of random missions
      Mission[] missions = new Mission[totalMission];
    for (int i = 0; i < totalMission; i++) {
      String id = "" + i;
      missions[i] = generateRandomMission(id, network, missionController);
      log.info(missions[i].toString());
    }

    // execute the thread pool
    executor.execute(missionController);

    // add mission threads to thread pool
    for (Mission m: missions){
		  executor.execute(m); 
	  }

    // end thread pool
    executor.shutdown();
    executor.awaitTermination(1, TimeUnit.MINUTES); 
  
  }

  public static Mission generateRandomMission(String i, Network network, MissionController missionController) {
      // Create date formatter
      SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
      Date date = new Date();
      // initialise random generator
      Random r = new Random();
      int destinationIndex = r.nextInt(9);
      // set destination
      String destination = destinations[destinationIndex];
      // set requirements of mission
      int fuelRequired = distances[destinationIndex] * fuelConst; // fuel required is const of distance
      int thrustersRequired = distances[destinationIndex] / thrusterConst; // thrusters required is const of distance
      int instrumentsRequired = thrustersRequired * instrumentsConst; // each thruster requires 5 instruments
      int controlSystemsRequired = (instrumentsRequired / 5) * controlSystemsConst; // every 5 instruments require 1 control system
      int powerPlantsRequired = destinationIndex; // further the distance more 1 power plant required
      // create Component objects
      Component fuel = new Component(i,"fuel", "ready", fuelRequired);
      Component thrusters = new Component(i,"thrusters", "ready", thrustersRequired);
      Component instruments = new Component(i,"instruments", "ready", instrumentsRequired);
      Component controlSystems = new Component(i,"controlSystems", "ready", controlSystemsRequired);
      Component powerPlants = new Component(i,"powerPlants", "ready", powerPlantsRequired);
      String ID = i;
      String startTime = formatter.format(date);
      return new Mission(ID,
                          destination,
                          startTime,
                          network,
                          missionController,
                          fuel,
                          thrusters,
                          instruments,
                          controlSystems,
                          powerPlants
                          );
    }
}