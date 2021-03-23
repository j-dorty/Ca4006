package assignment_1;

import java.util.Random;
import java.util.logging.*;

public class Component extends Thread {

    private final Logger log = Logger.getLogger("assignment_1");
    Random random = new Random();
    String missionId;
    String type;
    String status;
    int reportRate;
    int size;
    String report = "";

    Component(String missionId, String type, String status, int size) {

        this.missionId = missionId;
        this.type = type;
        this.status = status;
        this.size = size;
        this.reportRate = random.nextInt(10) + 20; // reports between every 20-30 seconds
    }

    @Override
    public void run() {
        log.info("Starting component " + this.type + " of size " + this.size);
        int i = 0;
        while (true) {
            assert this.report != null;
            if (i == this.reportRate) {
                this.report = report();
                i = 0;
            } else {
                try {
                    Thread.sleep(1000);
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void decrementResource() {
        this.size = this.size / 2;
    }

    private String report() {
        return ("Report Component Report " + this.missionId + " " + this.type + " with size " + this.size + " is reporting with status " + this.status);
    }

    public boolean hasReport() {
        return !this.report.equals("");
    }

    public String getReport() {
        return this.report;
    }

}
