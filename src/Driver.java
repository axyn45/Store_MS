package src;
import java.util.Scanner;

public class Driver {
    static User user = null;
    static DatabaseConnection dbconnection = null;
    static IUserDAO dbapi = null;

    static void dbInitialize() {
        dbconnection = new DatabaseConnection();
        dbapi = DAOFactory.getIUserDAOInstance(dbconnection.getConnection());
    }

    public static void main(String[] args) {
        // Create a new instance of the class
        dbInitialize();
        if (!login()) {
            System.out.println("Login failed! Please contact your administrator or try again later.");
            System.out.println("\nQuitting...");
            // TODO: delay 1500ms
            return;
        }

        menu();
        Scanner sc = new Scanner(System.in);
        System.out.println("\nPlease select an option: ");
        int option = sc.nextInt();
        switch (option) {
        case 1:
            // TODO: cashing();
            break;
        case 2:
            // TODO: query();
            break;
        case 3:
            // TODO: merchandise();
            break;
        case 4:
            // TODO: changePassword();
            break;
        case 5:
            // TODO: exportData();
            break;
        case 6:
            System.out.println("\nQuitting...");
            // TODO: delay 1000ms
            return;
        }

    }

    public static boolean login() {
        int tries = 3;
        Scanner sc = new Scanner(System.in);

        System.out.println("Login");
        while (tries > 0) {

            //Input username
            System.out.println("Username: ");
            String username = sc.nextLine();
            try {
                user = dbapi.getById(username);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (user == null) {
                System.out.println("User does not exist!");
                // tries--;
                continue;
            }

            //Input password
            System.out.println("Password: ");
            String password = sc.nextLine();
            if (user.getPassword().equals(password)) {
                System.out.println("Login successful!");
                sc.close();
                return true;
            } else {
                System.out.println("Wrong password!");
                user=null;
                tries--;
                continue;
            }
        }

        System.out.println("You have no more chances left.");
        sc.close();
        return false;
    }

    public static void menu() {
        System.out.println("===Store Cashing System===");
        System.out.println("1. Cashing");
        System.out.println("2. Query and Statistics");
        System.out.println("3. Merchandise Maintainance");
        System.out.println("4. Change Password");
        System.out.println("5. Export Data");
        System.out.println("6. Exit");
        System.out.println("\nCashier: " + user.getChrName());

    }

    public static void cashing() {
        //TODO: clear screen
        System.out.println("===Cashing===");
        System.out.println("Type in the merchandise barcode: ");
        Scanner sc = new Scanner(System.in);
        String barcode=sc.nextLine();

        while(barcode.length()!=6){
            System.out.println("Invalid barcode!A valid barcode contains exactly 6 digits. Please try again.");
            //TODO: delay 1500ms
            //TODO: clear screen
            System.out.println("Type in the merchandise barcode: ");
            barcode=sc.nextLine();
        }

        System.out.println("Type in the quantity: ");
        int quantity=sc.nextInt();
        
    }
}
