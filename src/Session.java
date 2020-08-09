import java.util.*;

public class Session {
    Scanner sc= new Scanner(System.in);
    //Map<Long> serialSet = new HashSet<Long>();
    Map<Long, Aircraft> serialMap = new HashMap<Long, Aircraft>();
    Map<String, Set<Aircraft>> modelMap = new HashMap<String, Set<Aircraft>>();
    Map<String, Set<Aircraft>> locationMap = new HashMap<String, Set<Aircraft>>();
    Map<Long, Pilot> serialPilotMap = new HashMap<Long, Pilot>();
    Map<Pilot, Set<Aircraft>> pilotMap = new HashMap<Pilot, Set<Aircraft>>();
    Map<String, Set<Aircraft>> weaponsMap = new HashMap<String, Set<Aircraft>>();
    Vector<Flight> allFlights = new Vector<Flight>();

    boolean pilot = false;

    void addAircraft() {
        while(true) {
            System.out.println("Enter Aircraft model:");
            String tempModel = sc.nextLine();
            long tempSerial;
            boolean weapons = false;
            while (true) {
                System.out.println("Enter Aircraft serial number:");
                tempSerial = sc.nextLong();
                sc.nextLine();//swallowing EOL token
                Aircraft tempSerialAC = serialMap.get(tempSerial);
                if (tempSerialAC == null) break;
                else {
                    System.out.println("ERROR: Please enter a unique serial number");
                }
            }

            System.out.println("Enter Aircraft's current location:");
            String tempLoc = sc.nextLine();

            Aircraft AC = new Aircraft(tempSerial, tempModel, tempLoc);
            Aircraft tempAC = AC;

            System.out.println("Are there weapons on this Aircraft? (Y or N)");
            String wAnswer = sc.nextLine();

            if (wAnswer.equals("Y")) weapons = true;
            while (weapons) {
                System.out.println("Enter the weapon model type:");
                String weaponsModel = sc.nextLine();
                System.out.println("Enter " + weaponsModel + " count on Aircraft:");
                int weaponCount = sc.nextInt();
                sc.nextLine();//swallowing EOL token
                AC.weaponsMap.put(weaponsModel, weaponCount);
                System.out.println("Are there more weapons on this Aircraft? (Y or N)");
                String moreAnswer = sc.nextLine();
                if (moreAnswer.equals("N")) break;
            }

            serialMap.put(tempSerial, tempAC);//adding current aircraft to serialMap

            Set<Aircraft> tempSet = locationMap.get(tempLoc);
            //update locations map
            if (tempSet == null) {//add aircraft to existing location set
                Set<Aircraft> acSet = new HashSet<Aircraft>();
                acSet.add(tempAC);
                locationMap.put(tempLoc, acSet);
            } else {
                tempSet.add(tempAC);
            }

            Set<Aircraft> tempModelSet = modelMap.get(tempModel);
            //update model map
            if (tempModelSet == null) {//add aircraft to existing location set
                Set<Aircraft> acSet = new HashSet<Aircraft>();
                acSet.add(tempAC);
                modelMap.put(tempModel, acSet);
            } else {
                tempModelSet.add(tempAC);
            }

            if (weapons) {
                for (String current : AC.weaponsMap.keySet()) {//iterate through each weapon in the recently added aircraft
                    Set<Aircraft> tempWeaponsSet = locationMap.get(current); //attempt to fins current weapon in weapons map

                    //update weapons map
                    if (tempWeaponsSet == null) {//add aircraft to existing location set
                        Set<Aircraft> acSet = new HashSet<Aircraft>();
                        acSet.add(tempAC);
                        weaponsMap.put(current, acSet);
                    } else {
                        tempWeaponsSet.add(tempAC);
                    }
                }
            }

            System.out.println("Would you like to add another Aircraft? (Y or N)");
            String newACAnswer = sc.nextLine();
            if(!(newACAnswer.equals("Y"))) break;
        }
    }

    void logFlight() {

        while (true) {
            Long tempSerial;
            Aircraft acInFlight;
            while (true) {
                System.out.println("Enter serial number of Aircraft in flight:");
                tempSerial = sc.nextLong();
                sc.nextLine();//swallowing EOL token
                acInFlight = serialMap.get(tempSerial);

                if (acInFlight != null) break;
                else {
                    System.out.println("ERROR: Serial number does not exist in system.");
                    System.out.println("Would you like to register this aircraft? (Y or N)");
                    String answer = sc.nextLine();
                    if (answer.equals("Y")) addAircraft();
                    else return;
                }
            }

            String currentAcLoc = acInFlight.currentLocation;

            System.out.println("Time of takeoff:");
            System.out.println("Enter as mm/dd/yyyy");
            String tempTakeoff = sc.nextLine();

            Pilot pilotInFlight;

            while (true) {
                System.out.println("Enter the ID of pilot in flight:");
                long tempPilotID = sc.nextLong();
                sc.nextLine();//swallowing EOL token
                pilotInFlight = serialPilotMap.get(tempPilotID);
                if (pilotInFlight == null) {
                    System.out.println("Pilot is not in system. Would you like to register pilot? (Y or N)");
                    String input = sc.nextLine();
                    if (input.equals("Y")) {
                        pilotInFlight = registerPilot();
                        break;
                    }
                }
                else break;
            }

            Flight newFlight = new Flight(currentAcLoc, tempTakeoff, acInFlight, pilotInFlight);
            allFlights.add(newFlight);
            acInFlight.flightHistory.add(newFlight);

            System.out.println("Would you like to log another flight? (Y or N)");
            String newACAnswer = sc.nextLine();
            if(!(newACAnswer.equals("Y"))) break;
        }
    }

    Pilot registerPilot () {
        System.out.println("First name of Pilot:");
        String fName = sc.nextLine();
        System.out.println("Last name of Pilot:");
        String lName = sc.nextLine();
        long pID;
        while(true) {
            System.out.println("Enter Pilot ID:");
            pID = sc.nextLong();
            sc.nextLine();//swallowing EOL token
            Pilot tempPilot = serialPilotMap.get(pID);
            if(tempPilot != null){
                System.out.println("ERROR: Please enter a unique Pilot ID");
            }
            else break;
        }

        Pilot newPilot = new Pilot(fName, lName, pID);
        serialPilotMap.put(pID, newPilot);
        return newPilot;
    }

    void display(){
        System.out.println("Enter corresponding number to display:");
        System.out.println("Aircrafts (1)");
        System.out.println("Flights   (2)");
        System.out.println("Pilots    (3)");
        int answer = sc.nextInt();
        sc.nextLine();//swallowing EOL token
        if(answer == 1) displayAircrafts();
        else if(answer == 2) displayFlights();
        else if(answer == 3) displayPilots();
    }

    void displayAircrafts(){
        Iterator<Map.Entry<Long, Aircraft>> it = serialMap.entrySet().iterator();

        while(it.hasNext())
        {
            Map.Entry<Long, Aircraft> entry = it.next();
            Aircraft currAC = entry.getValue();
            System.out.println("__________________________________________________________________");
            System.out.println("Serial Number: " + entry.getKey());
            System.out.println("Model: " + currAC.modelType);
            System.out.println("Current Location: " + currAC.currentLocation);
            System.out.println("__________________________________________________________________");
            System.out.println();
        }
    }

    void displayPilots(){
        Iterator<Map.Entry<Long, Pilot>> it = serialPilotMap.entrySet().iterator();

        while(it.hasNext())
        {
            Map.Entry<Long, Pilot> entry = it.next();
            Pilot currPilot = entry.getValue();
            System.out.println("__________________________________________________________________");
            System.out.println("Pilot ID: " + entry.getKey());
            System.out.println("Name: " + currPilot.firstName + " " + currPilot.lastName);
            System.out.println("__________________________________________________________________");
            System.out.println();

        }
    }

    void displayFlights(){
        for(int i=0; i<allFlights.size(); i++){
            Flight curr = allFlights.get(i);
            System.out.println("__________________________________________________________________");
            System.out.println("From: " + curr.startLoc + " To: " + curr.finalLoc + " Date: " + curr.timeLeft);
            System.out.println("Final Location: " + curr.finalLoc);
            System.out.println();
            System.out.println("Aircraft: ");
            System.out.println("Serial Number: " + curr.vehicle.serialNumber + " Model: " + curr.vehicle.modelType);
            System.out.println("Pilot: " + curr.pilot);
            System.out.println("__________________________________________________________________");
            System.out.println();


        }
    }

}