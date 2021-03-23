package assignment_1;

import java.util.Random;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.*;
import java.io.*;

public class MissionController implements Runnable {

    // initialise variables
    private final Logger log = Logger.getLogger("assignment_1");
    ArrayList<String> tasks = new ArrayList<String>();
    int[] missionIDs;
    Network network;
    String[] networkTypes = {"2MB-Controller", "2KB-Controller", "2B-Controller"};
    int missionsCompleted = 0;
    int missionsToComplete;
    Random random = new Random();

    public MissionController(int[] missionIDs, Network network, int totalMission) {
        // create date formatter and assign variables
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String startTime = formatter.format(date);
        this.missionIDs = this.missionIDs;
        this.network = network;
        this.missionsToComplete = totalMission;
        System.out.println("Created mission conrtoller at " + startTime);
    }

    public void run() {
        log.info("Starting mission controller with missions " + this.missionIDs);
        // loop through missions
        while (this.missionsCompleted < this.missionsToComplete) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // loop through network types
            for (String networkType : networkTypes) {
                // get network response
                String response = network.getFromNetworkControllerSide(networkType);
                log.info("response" + response);
                if (response != null) {
                    // set mission id - task - etc
                    String[] responseItems = response.split("-");
                    String missionId = responseItems[0];
                    String function = responseItems[1];
                    String data = responseItems[2];
                    // handle increment
                    if (function.equals("increment")) {
                        handleIncrement(missionId);
                        // handle failure
                        if (data.equals("failed")) {
                            handleFailure(missionId);
                        }
                    }
                    // handle report
                    if (function.equals("Report")) {
                        // log component report
                        writeOutput("\n*****COMPONENT REPORT*****\n\nMission Controller received Component report from Mission with mission ID " + missionId + "\nReport: " + data);
                    }
                   // handle ended
                    if (function.equals("Ended")) {
                        this.missionsCompleted += 1;
                        log.info(String.valueOf(this.missionsCompleted));
                        writeOutput("\n*****ENDED MISSION*****\n\nMission controller received message from mission with mission ID " + missionId + " that mission has completed final stage and ended\n");
                    }
                }
            }
        }
        writeOutput("ALL MISSION SUCCESSFULLY COMPLETED FINAL STAGE ***ENDING***");
    }

    public void handleIncrement(String missionId) {
        writeOutput("Mission Controller received request to increment stage from mission with mission ID " + missionId + "\n");
        network.transmitControllerSide(missionId + "-increment", "2KB-Mission");
    }

    public void handleFailure(String missionId) {
        // log mission when something failed
        writeOutput("\nMission Controller received message from mission with mission Id " + missionId + " that it attempted to increment its stage but failed\n");
        if (canStageFailureBeRecovered()) {
            network.transmitControllerSide(createSoftwareUpgrade(missionId), "2B-Mission");
            writeOutput(createSoftwareUpgrade(missionId));
        }
    }

    public void incrementMission(String id) {
        String task = id + "-increment";
        String networkType = "2KB";
        network.transmitControllerSide(task, networkType);
    }

    private boolean canStageFailureBeRecovered() {
        return (this.random.nextInt(4) == 0);
    }

    private String createSoftwareUpgrade(String missionID) {
        return (missionID + "-SoftwareUpgrade-of size " + (this.random.nextInt(25) + 25) + " MEGABYTES to recover stage failure for mission woth mission id" + missionID);

    }

    public synchronized void writeOutput(String outputString) {
        try {
            // write to output.dat
            File file = new File("output.dat");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file.getName(), true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(outputString);
            bw.close();

        } catch (IOException e) {
            System.out.println("Exception occurred:");
            e.printStackTrace();
        }
    }
}