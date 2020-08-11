import java.util.*;

public class Session {
    Scanner sc= new Scanner(System.in);//declaring scanner for user input
    Map<Long, Aircraft> serialMap = new HashMap<>();//map for serial numbers tied to an aircraft
    Map<String, Set<Aircraft>> modelMap = new HashMap<>();//map for all the aircrafts for each model
    Map<String, Set<Aircraft>> locationMap = new HashMap<>();//map for all the aircrafts for each location
    Map<Long, Pilot> serialPilotMap = new HashMap<>();//map to connect each serial number with respective pilot
    Map<Pilot, Set<Aircraft>> pilotMap = new HashMap<>();//map to connect all the aircrafts flown by a specific pilot
    Map<String, Set<Aircraft>> weaponsMap = new HashMap<>();//map to connect all the aircrafts flown with a specific weapon
    Vector<Flight> allFlights = new Vector<>();//vector of all flights taken

    void addAircraft() {
        while(true) {
            System.out.println("Enter Aircraft model:");
            String tempModel = sc.nextLine();
            long tempSerial;
            boolean weapons = false;
            while (true) {//loop that runs until a unique serial # is entered
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

            System.out.println("Are there weapons on this Aircraft? (Y or N)");
            String wAnswer = sc.nextLine();

            if (wAnswer.equals("Y")) weapons = true;
            while (weapons) {//loop that starts if there are weapons on board and exits after all weapons have been added
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

            serialMap.put(tempSerial, AC);//adding current aircraft to serialMap

            Set<Aircraft> tempSet = locationMap.get(tempLoc);

            //update locations map
            if (tempSet == null) {//creat new aircraft set and add to map
                Set<Aircraft> acSet = new HashSet<>();
                acSet.add(AC);
                locationMap.put(tempLoc, acSet);
            } else {//add aircraft to existing set
                tempSet.add(AC);
            }

            Set<Aircraft> tempModelSet = modelMap.get(tempModel);

            //update model map
            if (tempModelSet == null) {//creat new aircraft set and add to map
                Set<Aircraft> acSet = new HashSet<>();
                acSet.add(AC);
                modelMap.put(tempModel, acSet);
            } else {//add aircraft to existing set
                tempModelSet.add(AC);
            }

            if (weapons) {
                for (String current : AC.weaponsMap.keySet()) {//iterate through each weapon in the recently added aircraft
                    Set<Aircraft> tempWeaponsSet = weaponsMap.get(current); //attempt to find current weapon in weapons map

                    //update weapons map
                    if (tempWeaponsSet == null) {//creat new aircraft set and add to map
                        Set<Aircraft> acSet = new HashSet<>();
                        acSet.add(AC);
                        weaponsMap.put(current, acSet);
                    } else {//add aircraft to existing set
                        tempWeaponsSet.add(AC);
                    }
                }
            }

            System.out.println("Would you like to add another Aircraft? (Y or N)");
            String newACAnswer = sc.nextLine();
            if(!(newACAnswer.equals("Y"))) break;
        }
    }

    void logFlight() {
        while (true) {//loop runs until a unique serial # is entered
            long tempSerial;
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

            while (true) {//loop runs until a unique pilot ID is entered
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

            System.out.println("Do you know the date of arrival?(Y or N)");//block for user to add date of arrival, if known
            String answer = sc.nextLine();
            if(answer.equals("Y")){
                System.out.println("Enter date of arrival:");
                System.out.println("Enter as mm/dd/yyyy");
                newFlight.arrivalTime = sc.nextLine();
            }

            System.out.println("Do you know the landing location?(Y or N)");//block for user to add landing location, if known
            String landingAnswer = sc.nextLine();
            if(landingAnswer.equals("Y")){
                System.out.println("Enter landing location:");
                newFlight.finalLoc = sc.nextLine();
                String oldLocation = acInFlight.currentLocation;
                acInFlight.currentLocation = newFlight.finalLoc;

                Set<Aircraft> tempSet = locationMap.get(newFlight.finalLoc);
                //update locations map
                if (tempSet == null) {//creat new aircraft set and add to map
                    Set<Aircraft> acSet = new HashSet<>();
                    acSet.add(acInFlight);
                    locationMap.put(newFlight.finalLoc, acSet);
                } else {//add aircraft to existing set
                    tempSet.add(acInFlight);
                }

                Set<Aircraft> oldSet = locationMap.get(oldLocation);
                oldSet.remove(acInFlight);//removing old location
            }

            allFlights.add(newFlight);//add to flights vector
            acInFlight.flightHistory.add(newFlight);//adding flight to flight history
            acInFlight.pilotHistory.add(pilotInFlight);//adding pilot in flight to airccrafts pilot history

            Set<Aircraft> tempSet = pilotMap.get(pilotInFlight);

            //update pilotMap
            if (tempSet == null) {//create new aircraft set and add to map
                Set<Aircraft> acSet = new HashSet<>();
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
        while(true) {//loop runs until a unique pilot ID is entered
            System.out.println("Enter Pilot ID:");
            pID = sc.nextLong();
            sc.nextLine();//swallowing EOL token
            Pilot tempPilot = serialPilotMap.get(pID);
            if(tempPilot != null){//ID is in use
                System.out.println("ERROR: Please enter a unique Pilot ID");
            }
            else break;
        }

        Pilot newPilot = new Pilot(fName, lName, pID);
        serialPilotMap.put(pID, newPilot);//add to map
        return newPilot;
    }

    void display(){//function to display new menu prompt
        System.out.println("Enter corresponding number to display:");
        System.out.println("   Aircrafts (1)");
        System.out.println("   Flights   (2)");
        System.out.println("   Pilots    (3)");
        int answer = sc.nextInt();
        sc.nextLine();//swallowing EOL token
        if(answer == 1) displayAircrafts();
        else if(answer == 2) displayFlights();
        else if(answer == 3) displayPilots();
    }

    void displayAircrafts(){//function that displays ALL aircrafts

        for (Map.Entry<Long, Aircraft> entry : serialMap.entrySet()) {
            Aircraft currAC = entry.getValue();
            System.out.println("__________________________________________________________________");
            System.out.println("Serial Number: " + entry.getKey());
            System.out.println("Model: " + currAC.modelType);
            System.out.println("Current Location: " + currAC.currentLocation);
            System.out.println("__________________________________________________________________");
        }
    }

    void displayPilots(){//function to display ALL pilots

        for (Map.Entry<Long, Pilot> entry : serialPilotMap.entrySet()) {
            Pilot currPilot = entry.getValue();
            System.out.println("__________________________________________________________________");
            System.out.println("Pilot ID: " + entry.getKey());
            System.out.println("Name: " + currPilot.firstName + " " + currPilot.lastName);
            System.out.println("__________________________________________________________________");

        }
    }

    void displayFlights(){//function to display ALL flights
        for (Flight curr : allFlights) {
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
        System.out.println("Enter corresponding number to search:");//display menu
        System.out.println("   By Aircraft Serial Number (1)");
        System.out.println("   By Aircraft Model Type    (2)");
        System.out.println("   By Location               (3)");
        System.out.println("   By Pilot                  (4)");
        System.out.println("   By Weapon Model Type      (5)");
        System.out.println("   Back to main menu         (6)");


        int answer = sc.nextInt();
        sc.nextLine();//swallowing EOL token
        if(answer == 1){//for searching by serial #
            while(true) {//loop runs until a unique aircraft serial # is entered
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
                    for(Pilot  i : tempSerialAC.pilotHistory){//loop through all pilots to display
                        System.out.println("   _____________________________________________________________");
                        System.out.println("   Name: " + i.firstName + " " + i.lastName);
                        System.out.println("   Pilot ID: " + i.pilotID);
                        System.out.println("   _____________________________________________________________");

                    }
                    System.out.println();
                    System.out.println("Flight History: ");
                    for(Flight  i : tempSerialAC.flightHistory){//loop through all flights to display
                        System.out.println("   _____________________________________________________________");
                        System.out.println("   From: " + i.startLoc + " To: " + i.finalLoc + " Date: " + i.timeLeft);
                        System.out.println("   Final Location: " + i.finalLoc);
                        System.out.println();
                        System.out.println("   Aircraft: ");
                        System.out.println("      Serial Number: " + i.vehicle.serialNumber + " Model: "
                                + i.vehicle.modelType);
                        System.out.println();
                        System.out.println("   Pilot: " + i.pilot.firstName + " " + i.pilot.lastName);
                        System.out.println("   _____________________________________________________________");
                    }
                    System.out.println();
                    System.out.println("On-board Weapons: ");
                    for (Map.Entry<String, Integer> entry : tempSerialAC.weaponsMap.entrySet()) {//loop through all on-board weapons to display
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
                        System.out.println("Enter corresponding number:");//edit menu
                        System.out.println("   Update Current Location (1)");
                        System.out.println("   Add On-board Weapons    (2)");
                        System.out.println("   Back to main menu       (3)");

                        int editAnswer2 = sc.nextInt();
                        sc.nextLine();//swallowing EOL token
                        if(editAnswer2 == 1){//edit current location
                            System.out.println("What is the new current location?");
                            tempSerialAC.currentLocation = sc.nextLine();
                        }
                        else if(editAnswer2 == 2){//add more weapons
                            while (true) {//runs until there are no more weapons to add
                                System.out.println("Enter the weapon model type:");
                                String weaponsModel = sc.nextLine();
                                System.out.println("Enter " + weaponsModel + " count on Aircraft:");
                                int weaponCount = sc.nextInt();
                                sc.nextLine();//swallowing EOL token
                                Set<Aircraft> tempWeaponsSet = weaponsMap.get(weaponsModel); //attempt to find current weapon in weapons map

                                //update weapons map
                                if (tempWeaponsSet == null) {//creat new aircraft set and add to map
                                    Set<Aircraft> acSet = new HashSet<>();
                                    acSet.add(tempSerialAC);
                                    weaponsMap.put(weaponsModel, acSet);
                                } else {//add aircraft to existing set
                                    tempWeaponsSet.add(tempSerialAC);
                                }

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
        else if(answer == 2){//search by aircraft model type
            while(true){//runs until a model that is in the system is entered
                System.out.println("Enter Aircraft Model Type:");
                String modelAnswer = sc.nextLine();
                Set<Aircraft> modelAC = modelMap.get(modelAnswer);
                if (modelAC == null) System.out.println("ERROR: Model type does not exist in system or was entered " +
                        "incorrectly");
                else {
                    for (Aircraft ac: modelAC) {//displaying every aircraft of requested model type
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
        else if(answer == 3){//search by location
            while(true) {//runs until a Location that is in the system is entered
                System.out.println("Enter Location:");
                String locAnswer = sc.nextLine();
                Set<Aircraft> locAC = locationMap.get(locAnswer);
                if (locAC == null) System.out.println("ERROR: Location does not exist in system or was entered " +
                        "incorrectly");
                else {//displaying every aircraft at requested location
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
        else if(answer == 4){//search by pilot
            while(true) {//loop runs until a unique pilot ID is entered
                System.out.println("Enter Pilot ID:");
                Long serialAnswer = sc.nextLong();
                sc.nextLine();//swallowing EOL token
                Pilot tempPilot = serialPilotMap.get(serialAnswer);
                if(tempPilot == null) System.out.println("ERROR: Pilot does not exist in system or ID was entered " +
                        "incorrectly");
                else {//displaying every aircraft flown by requested pilot
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
        else if(answer == 5){//search by weapon
            while(true) {
                System.out.println("Enter weapon model name:");
                String wAnswer = sc.nextLine();
                Set<Aircraft> wAC = weaponsMap.get(wAnswer);
                if (wAC == null) System.out.println("ERROR:Weapon does not exist in system or was entered incorrectly");
                else {
                    for (Aircraft ac: wAC) {////displaying every aircraft of requested weapon type
                        System.out.println("__________________________________________________________________");
                        System.out.println("Serial Number: " + ac.serialNumber);
                        System.out.println("Model: " + ac.modelType);
                        System.out.println("Current Location: " + ac.currentLocation);
                        System.out.println("On-board Weapons: ");

                        for (Map.Entry<String, Integer> entry : ac.weaponsMap.entrySet()) {//display all weapons of each aircraft
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
    }
}