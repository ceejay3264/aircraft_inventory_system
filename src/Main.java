import java.util.*;

public class Main {

    public static void main(String[] args) {
        Vector<Flight> flightLog;
        Vector<String> menu = new Vector<>();
        Scanner sc= new Scanner(System.in);
        int inputNum;
        Session sesh = new Session();

        menu.add("   Add an Aircraft  (1)");
        menu.add("   Log a Flight     (2)");
        menu.add("   Register a Pilot (3)");
        menu.add("   Display          (4)");
        menu.add("   Search Aircrafts (5)");
        menu.add("   Exit Program     (6)");

        while(true){
            System.out.println("Menu:");
            for (String s : menu) {
                System.out.println(s);
            }
            inputNum = sc.nextInt();
            if(inputNum == 1) sesh.addAircraft();
            else if(inputNum == 2) sesh.logFlight();
            else if(inputNum == 3) sesh.registerPilot();
            else if(inputNum == 4) sesh.display();
            else if(inputNum == 5) sesh.search();
            else break;

        }

    }

}
