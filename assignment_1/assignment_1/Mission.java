package assignment_1;

import java.util.Random;
import java.util.logging.*;
import java.io.*;


public class Mission extends Thread {

    private final Logger log = Logger.getLogger("assignment_1");
    private Random random = new Random();
    private String ID;
    private String stageName;
    private int stage;
    private String startTime;
    private String target;
    private Network network;
    private MissionController missionController;
    private Component fuel;
    private Component thrusters;
    private Component instruments;
    private Component controlSystems;
    private Component powerPlants;
    String status = "ready";
    String[] networkTypes = {"2MB-Mission", "2KB-Mission", "2B-Mission"};
    boolean transmitted = false;
    boolean didStageFail = false;
    String[] componentReports = {"", "", "", "", ""};
    boolean hasComponentReports = false;

    // stages array
    String[] stages = {"boost stage", "Interplanetary transit stage", "entry/landing stage ", "exploration (rover) stage"};

    Mission(String uid, String target, String startTime, Network network, MissionController missionController, Component fuel, Component thrusters, Component instruments, Component controlSystems, Component powerPlants) {
        // set mission details
        this.ID = uid;
        this.target = target;
        this.startTime = startTime;
        this.stage = 0;
        this.stageName = stages[stage];
        this.network = network;
        this.missionController = missionController;
        this.fuel = fuel;
        this.thrusters = thrusters;
        this.instruments = instruments;
        this.controlSystems = controlSystems;
        this.powerPlants = powerPlants;

        // start components
        this.fuel.start();
        this.thrusters.start();
        this.instruments.start();
        this.controlSystems.start();
        this.powerPlants.start();

        log.info("Created Mission" + uid);
        // report();
    }

    @Override
    public void run() {
        log.info("Starting mission to" + ID);
        while (this.stage < 3) {
            try {
                Thread.sleep(1000);
                try {
                    if (!transmitted && !didStageFail) {
                        network.transmitMissionSide(this.ID + "-increment-normal", "2KB-Controller");
                        transmitted = true;
                    } else if (!transmitted && didStageFail) {
                        network.transmitMissionSide(this.ID + "-increment-failed-stage", "2KB-Controller");
                        transmitted = true;
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                System.out.println(this.ID + " mission trying to increment");
                for (String networkType : networkTypes) {
                    String response = network.getFromNetworkMissionSide(this.ID, networkType);
                    if (response != null) {
                        transmitted = false;
                        String[] responseItems = response.split("-");
                        String missionID = responseItems[0];
                        String task = responseItems[1];
                        if (task.equals("increment")) {
                            didStageFail = this.tryIncrementStage();
                        }
                        if (task.equals("SoftwareUpgrade")) {
                            this.incrementStage();
                        }
                    }
                }

                if (!checkForComponentReports(this.fuel).isEmpty()) {
                    this.componentReports[0] = checkForComponentReports(this.fuel);
                    this.hasComponentReports = true;
                }

                if (!checkForComponentReports(this.thrusters).isEmpty()) {
                    this.componentReports[1] = checkForComponentReports(this.thrusters);
                    this.hasComponentReports = true;
                }

                if (!checkForComponentReports(this.instruments).equals("")) {
                    this.componentReports[2] = checkForComponentReports(this.instruments);
                    this.hasComponentReports = true;
                }

                if (!checkForComponentReports(this.controlSystems).isEmpty()) {
                    this.componentReports[3] = checkForComponentReports(this.controlSystems);
                    this.hasComponentReports = true;
                }

                if (!checkForComponentReports(this.powerPlants).isEmpty()) {
                    this.componentReports[4] = checkForComponentReports(this.powerPlants);
                    this.hasComponentReports = true;
                }

                if (hasComponentReports) {
                    this.hasComponentReports = false;
                    for (String report : componentReports) {
                        if (!report.equals("")) {
                            network.transmitMissionSide(this.ID + "-Report-" + report, "2KB-Controller");
                        }
                    }
                    this.fuel.report = "";
                    this.thrusters.report = "";
                    this.instruments.report = "";
                    this.controlSystems.report = "";
                    this.powerPlants.report = "";
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        this.endMission();
    }

    public boolean tryIncrementStage() {
        boolean result = true;
        if (this.random.nextInt(10) > 0) {
            this.stage += 1;
            this.stageName = stages[stage];
            writeOutput("\n***MISSION INCREMENTED STAGE***\nMISSION_" + this.ID + "***IS_CHANGING_TO_STAGE " + this.stageName + "\n");
            result = false;
        }
        decrementResources();
        return result;

    }

    private void incrementStage() {
        this.stage += 1;
        this.stageName = stages[stage];
        decrementResources();
        writeOutput("\n***SOFWARE UPGRADE RECIEVED***\nMISSION_" + this.ID + "*****IS_CHANGING_TO_STAGE after recovery due to software upgrade " + this.stageName + "\n");

    }

    private void decrementResources() {
        this.fuel.decrementResource();
        this.thrusters.decrementResource();
        this.instruments.decrementResource();
        this.controlSystems.decrementResource();
        this.powerPlants.decrementResource();
    }


    public String checkForComponentReports(Component component) {
        if (component.hasReport()) {
            return component.getReport();
        }
        return "";
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

    public void endMission() {
        network.transmitMissionSide(this.ID + "-Ended-endingmission", "2KB-Controller");
    }
}
