public class Flight {
    String startLoc;
    String finalLoc = "N/A";
    String timeLeft;
    String arrivalTime = "N/A";
    Aircraft vehicle;
    Pilot pilot;

    //constructor
    Flight(String sLoc, String takeoff, Aircraft plane, Pilot pilot_){
        this.startLoc = sLoc;
        this.timeLeft = takeoff;
        this.vehicle = plane;
        this.pilot = pilot_;
    }
}
