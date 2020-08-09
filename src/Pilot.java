import java.util.*;

public class Pilot {
    String firstName;
    String lastName;
    Vector<Aircraft> aircraftLog;
    long pilotID;

    //constructor
    Pilot(String fName, String lName, long ID){
        this.firstName = fName;
        this.lastName = lName;
        this.aircraftLog = new Vector<Aircraft>();
        this.pilotID = ID;
    }




}
