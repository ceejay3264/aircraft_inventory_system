import java.util.*;

public class Session {
    Scanner sc= new Scanner(System.in);
    Set<Long> serialSet = new HashSet<Long>();
    Map<String, Set<Aircraft>> modelMap = new HashMap<String, Set<Aircraft>>();
    Map<String, Set<Aircraft>> locationMap = new HashMap<String, Set<Aircraft>>();
    Map<Pilot, Set<Aircraft>> pilotMap = new HashMap<Pilot, Set<Aircraft>>();
    Map<String, Set<Aircraft>> weaponsMap = new HashMap<String, Set<Aircraft>>();
    boolean weapons = false;
    boolean pilot = false;

    void addAircraft() {

        System.out.println("Enter Aircraft model:");
        String tempModel = sc.nextLine();
        long tempSerial;
        while(true) {
            System.out.println("Enter Aircraft serial number:");
            tempSerial = sc.nextLong();
            sc.nextLine();//swallowing EOL token
            boolean exists = serialSet.contains(tempSerial);
            if (!exists) break;
            else{
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


        serialSet.add(tempSerial);//adding current aircraft to set


        Set<Aircraft> tempSet = locationMap.get(tempLoc);
        //update locations map
        if(tempSet == null){//add aircraft to existing location set
            Set<Aircraft> acSet = new HashSet<Aircraft>();
            acSet.add(tempAC);
            locationMap.put(tempLoc, acSet);
        }
        else{
            tempSet.add(tempAC);
        }

        Set<Aircraft> tempModelSet = modelMap.get(tempModel);
        //update model map
        if(tempModelSet == null){//add aircraft to existing location set
            Set<Aircraft> acSet = new HashSet<Aircraft>();
            acSet.add(tempAC);
            modelMap.put(tempModel, acSet);
        }
        else{
            tempModelSet.add(tempAC);
        }

        if(weapons){
            for (String current : AC.weaponsMap.keySet()){//iterate through each weapon in the recently added aircraft
                Set<Aircraft> tempWeaponsSet = locationMap.get(current); //attempt to fins current weapon in weapons map

                //update weapons map
                if (tempWeaponsSet == null){//add aircraft to existing location set
                    Set<Aircraft> acSet = new HashSet<Aircraft>();
                    acSet.add(tempAC);
                    weaponsMap.put(current, acSet);
                }
                else {
                    tempWeaponsSet.add(tempAC);
                }
            }
        }

    }

}
