public class Flight {
    String startLoc;
    String finalLoc;
    String timeLeft;
    String arrivalTime;
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
