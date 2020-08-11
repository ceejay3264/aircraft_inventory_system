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

            System.out.println("Date of takeoff:");
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

            System.out.println("Do you know the date of arrival?(Y or N)");
            String answer = sc.nextLine();
            if(answer.equals("Y")){
                System.out.println("Enter date of arrival:");
                System.out.println("Enter as mm/dd/yyyy");
                String arrivalTime = sc.nextLine();
                newFlight.arrivalTime = arrivalTime;
            }

            System.out.println("Do you know the landing location?(Y or N)");
            String landingAnswer = sc.nextLine();
            if(landingAnswer.equals("Y")){
                System.out.println("Enter landing location:");
                String landingLoc = sc.nextLine();
                newFlight.finalLoc = landingLoc;
            }

            allFlights.add(newFlight);
            acInFlight.flightHistory.add(newFlight);
            acInFlight.pilotHistory.add(pilotInFlight);

            Set<Aircraft> tempSet = pilotMap.get(pilotInFlight);

            //update locations map
            if (tempSet == null) {//add aircraft to existing location set
                Set<Aircraft> acSet = new HashSet<Aircraft>();
                acSet.add(acInFlight);
                pilotMap.put(pilotInFlight, acSet);
            } else {
                tempSet.add(acInFlight);
            }

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
            System.out.println("   Serial Number: " + curr.vehicle.serialNumber + " Model: " + curr.vehicle.modelType);
            System.out.println();
        System.out.println("Pilot: " + curr.pilot.firstName + " " + curr.pilot.lastName);
        System.out.println("__________________________________________________________________");


    }
}

    void search(){
        System.out.println("Enter corresponding number to search:");
        System.out.println("   By Aircraft Serial Number (1)");
        System.out.println("   By Aircraft Model Type    (2)");
        System.out.println("   By Location               (3)");
        System.out.println("   By Pilot                  (4)");
        System.out.println("   By Weapon Model Type      (5)");
        System.out.println("   Back to main menu         (6)");


        int answer = sc.nextInt();
        sc.nextLine();//swallowing EOL token
        if(answer == 1){
            while(true) {
                System.out.println("Enter Aircraft Serial Number:");
                Long serialanswer = sc.nextLong();
                sc.nextLine();//swallowing EOL token
                Aircraft tempSerialAC = serialMap.get(serialanswer);
                if (tempSerialAC == null) System.out.println("ERROR: Serial number does not exist in system");
                else {
                    System.out.println("__________________________________________________________________");
                    System.out.println("Serial Number: " + serialanswer);
                    System.out.println("Model: " + tempSerialAC.modelType);
                    System.out.println("Current Location: " + tempSerialAC.currentLocation);
                    System.out.println("Pilot History: ");
                    for(Pilot  i : tempSerialAC.pilotHistory){
                        System.out.println("   _____________________________________________________________");
                        System.out.println("   Name: " + i.firstName + " " + i.lastName);
                        System.out.println("   Pilot ID: " + i.pilotID);
                        System.out.println("   _____________________________________________________________");

                    }
                    System.out.println();
                    System.out.println("Flight History: ");
                    for(Flight  i : tempSerialAC.flightHistory){
                        System.out.println("   _____________________________________________________________");
                        System.out.println("   From: " + i.startLoc + " To: " + i.finalLoc + " Date: " + i.timeLeft);
                        System.out.println("   Final Location: " + i.finalLoc);
                        System.out.println();
                        System.out.println("   Aircraft: ");
                        System.out.println("      Serial Number: " + i.vehicle.serialNumber + " Model: " + i.vehicle.modelType);
                        System.out.println();
                        System.out.println("   Pilot: " + i.pilot.firstName + " " + i.pilot.lastName);
                        System.out.println("   _____________________________________________________________");
                    }
                    System.out.println();
                    System.out.println("On-board Weapons: ");
                    Iterator<Map.Entry<String, Integer>> it = tempSerialAC.weaponsMap.entrySet().iterator();
                    while(it.hasNext())
                    {
                        Map.Entry<String, Integer> entry = it.next();
                        Integer ammo = entry.getValue();
                        System.out.println("   _____________________________________________________________");
                        System.out.println("   Weapon Model: " + entry.getKey());
                        System.out.println("   Count: " + ammo);
                        System.out.println("   _____________________________________________________________");
                    }

                    System.out.println("__________________________________________________________________");
                    System.out.println();
                    System.out.println("Edit this Aircraft? (Y or N)");
                    String editAnswer = sc.nextLine();
                    if(editAnswer.equals("Y")){
                        System.out.println("Enter corresponding number:");
                        System.out.println("   Update Current Location (1)");
                        System.out.println("   Add On-board Weapons    (2)");
                        System.out.println("   Back to main menu       (3)");

                        int editAnswer2 = sc.nextInt();
                        sc.nextLine();//swallowing EOL token
                        if(editAnswer2 == 1){
                            System.out.println("What is the new current location?");
                            String newLoc = sc.nextLine();
                            tempSerialAC.currentLocation = newLoc;
                        }
                        else if(editAnswer2 == 2){
                            while (true) {
                                System.out.println("Enter the weapon model type:");
                                String weaponsModel = sc.nextLine();
                                System.out.println("Enter " + weaponsModel + " count on Aircraft:");
                                int weaponCount = sc.nextInt();
                                sc.nextLine();//swallowing EOL token
                                tempSerialAC.weaponsMap.put(weaponsModel, weaponCount);
                                System.out.println("Would you like to add more weapons to this Aircraft? (Y or N)");
                                String moreAnswer = sc.nextLine();
                                if (moreAnswer.equals("N")) break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        else if(answer == 2){
            while(true) {
                System.out.println("Enter Aircraft Model Type:");
                String modelAnswer = sc.nextLine();
                Set<Aircraft> modelAC = modelMap.get(modelAnswer);
                if (modelAC == null) System.out.println("ERROR: Model type does not exist in system or was entered incorrectly");
                else {
                    for (Aircraft ac: modelAC) {
                        System.out.println("__________________________________________________________________");
                        System.out.println("Serial Number: " + ac.serialNumber);
                        System.out.println("Model: " + ac.modelType);
                        System.out.println("Current Location: " + ac.currentLocation);
                        System.out.println("__________________________________________________________________");
                    }
                    break;
                }
            }
        }
        else if(answer == 3){
            while(true) {
                System.out.println("Enter Location:");
                String locAnswer = sc.nextLine();
                Set<Aircraft> locAC = locationMap.get(locAnswer);
                if (locAC == null) System.out.println("ERROR: Location does not exist in system or was entered incorrectly");
                else {
                    for (Aircraft ac: locAC) {
                        System.out.println("__________________________________________________________________");
                        System.out.println("Serial Number: " + ac.serialNumber);
                        System.out.println("Model: " + ac.modelType);
                        System.out.println("Current Location: " + ac.currentLocation);
                        System.out.println("__________________________________________________________________");
                    }
                    break;
                }
            }
        }
        else if(answer == 4){
            while(true) {
                System.out.println("Enter Pilot ID:");
                Long serialAnswer = sc.nextLong();
                sc.nextLine();//swallowing EOL token
                Pilot tempPilot = serialPilotMap.get(serialAnswer);
                if(tempPilot == null) System.out.println("ERROR: Pilot does not exist in system or ID was entered incorrectly");
                else {
                    Set<Aircraft> tempSet = pilotMap.get(tempPilot);
                    for (Aircraft ac: tempSet) {
                        System.out.println("__________________________________________________________________");
                        System.out.println("Serial Number: " + ac.serialNumber);
                        System.out.println("Model: " + ac.modelType);
                        System.out.println("Current Location: " + ac.currentLocation);
                        System.out.println("__________________________________________________________________");
                    }
                    break;
                }
            }
        }
        else if(answer == 5){
            while(true) {
                System.out.println("Enter weapon model name:");
                String wAnswer = sc.nextLine();
                Set<Aircraft> wAC = weaponsMap.get(wAnswer);
                if (wAC == null) System.out.println("ERROR: Weapon does not exist in system or was entered incorrectly");
                else {
                    for (Aircraft ac: wAC) {
                        System.out.println("__________________________________________________________________");
                        System.out.println("Serial Number: " + ac.serialNumber);
                        System.out.println("Model: " + ac.modelType);
                        System.out.println("Current Location: " + ac.currentLocation);
                        System.out.println("On-board Weapons: ");
                        Iterator<Map.Entry<String, Integer>> it = ac.weaponsMap.entrySet().iterator();

                        while(it.hasNext())
                        {
                            Map.Entry<String, Integer> entry = it.next();
                            Integer ammo = entry.getValue();
                            System.out.println("   _____________________________________________________________");
                            System.out.println("   Weapon Model: " + entry.getKey());
                            System.out.println("   Count: " + ammo);
                            System.out.println("   _____________________________________________________________");
                        }
                        System.out.println("__________________________________________________________________");
                    }
                    break;
                }
            }
        }
        else return;
    }

}