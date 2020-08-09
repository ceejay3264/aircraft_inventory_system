import java.util.*;

public class Main {

    public static void main(String[] args) {
        Vector<Trip> tripLog;
        Vector<String> menu = new Vector<>();
        Scanner sc= new Scanner(System.in);
        int inputNum;
        Session sesh = new Session();

        System.out.println("Menu:");
        menu.add("Add an Aircraft (1)");
        menu.add("Search          (3)");
        menu.add("Exit Program    (4)");



        while(true){
            System.out.println("Menu:");
            for (String s : menu) {
                System.out.println(s);
            }
            inputNum = sc.nextInt();
            if(inputNum == 1) sesh.addAircraft();

            if(inputNum == 4) break;

        }

    }

}
