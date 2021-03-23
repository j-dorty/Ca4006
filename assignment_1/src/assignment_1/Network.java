package assignment_1;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.*;

public class Network {

  private final Logger log = Logger.getLogger("assignment_1");
  int bandwidth;
  boolean isAvailable;
  private static ConcurrentHashMap<String,NetworkQueue> queues = new ConcurrentHashMap<>();

  Network(int bandwidth) {
    this.bandwidth = bandwidth;
    this.isAvailable = true;
    // create controllers
    NetworkQueue bigController = new NetworkQueue();
    NetworkQueue medController = new NetworkQueue();
    NetworkQueue smallController = new NetworkQueue();

    // create missions
    NetworkQueue bigMission = new NetworkQueue();
    NetworkQueue medMission = new NetworkQueue();
    NetworkQueue smallMission = new NetworkQueue();

    // put controllers and missions into network queue
    queues.put("2MB-Controller", bigController);
    queues.put("2KB-Controller", medController);
    queues.put("2B-Controller", smallController);
    queues.put("2MB-Mission", bigMission);
    queues.put("2KB-Mission", medMission);
    queues.put("2B-Mission", smallMission);
    log.info(queues.toString());
  }

  public void transmitControllerSide(String task, String networkType){
    try {
      queues.get(networkType).add(task);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  public void transmitMissionSide(String task, String networkType) {
    try {
      queues.get(networkType).add(task);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  public String getFromNetworkMissionSide(String id, String networkType) {
    if (queues.get(networkType).get() == null) {
      return null;
    }
    if ((queues.get(networkType).get()).split("-")[0].equals(id)){
      String response = queues.get(networkType).get();
      queues.get(networkType).remove();
      return response;
    }
    return null;
  }

  public String getFromNetworkControllerSide(String networkType) {
    if (queues.get(networkType).get() == null) {
      return null;
    }
    String response = queues.get(networkType).get();
    queues.get(networkType).remove();
    return response;
  }

  public void setIsAvailable(boolean available) {
    this.isAvailable = available;
  }

  public boolean getIsAvailable() {
    return this.isAvailable;
  }

}
