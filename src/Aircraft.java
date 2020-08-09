import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.*;

public class Aircraft {
    String currentLocation;
    String currentPilot;
    String dateAdded;
    long serialNumber;
    String modelType;
    Vector<Pilot> pilotHistory;
    Vector<Flight> flightHistory;
    Map<String, Integer> weaponsMap;


    //constructor
    Aircraft(long serial, String model, String loc){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDateTime now = LocalDateTime.now();
        dateAdded = dtf.format(now);
        this.serialNumber = serial;
        this.currentLocation = loc;
        this.modelType = model;
        this.pilotHistory = new Vector<Pilot>();
        this.flightHistory = new Vector<Flight>();
        this.weaponsMap = new HashMap<String, Integer>();
    }











}
