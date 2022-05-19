package src.Driver;

import java.util.EventObject;
import java.util.Scanner;

// import org.w3c.dom.events.Event;

import src.DAO.IUserDAO;
import src.DataType.User;
import src.Services.Cashier;
import src.Services.UserService;
import src.Utilities.DatabaseConnection;
import src.Utilities.UIUX;

public class Driver {
    static User user = null;
    static DatabaseConnection dbconnection = null;
    static IUserDAO dbapi = null;

    static UIUX util = new UIUX();
    static UserService userService = new UserService();
    static Cashier cashier = null;

    // static void dbInitialize() {
    // dbconnection = new DatabaseConnection();
    // dbapi = DAOFactory.getIUserDAOInstance(dbconnection.getConnection());
    // }

    public static void main(String[] args) {
        // Create a new instance of the class
        // dbInitialize();

        // login
        user = userService.login();
        if (user == null) {
            System.out.println("Login failed, quitting...");
            System.exit(0);
        }

        // construct a new cashier with current user
        cashier = new Cashier(user);

        menu();
    }

    // Scanner sc = new Scanner(System.in);
    // System.out.println("\nPlease select an option: ");
    // int option = sc.nextInt();
    // switch (option) {
    // case 1:
    // TODO: cashing();
    // break;
    // case 2:
    // TODO: query();
    // break;
    // case 3:
    // TODO: merchandise();
    // break;
    // case 4:
    // TODO: changePassword();
    // break;
    // case 5:
    // TODO: exportData();
    // break;
    // case 6:
    // System.out.println("\nQuitting...");
    // TODO: delay 1000ms
    // sc.close();
    // return;
    // }
    // sc.close();

    // }

    // public static boolean login() {
    // int tries = 3;
    // Scanner sc = new Scanner(System.in);

    // System.out.println("Login");
    // while (tries > 0) {

    // //Input username
    // System.out.println("Username: ");
    // String username = sc.nextLine();
    // try {
    // user = dbapi.getById(username);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // if (user == null) {
    // System.out.println("User does not exist!");
    // // tries--;
    // continue;
    // }

    // //Input password
    // System.out.println("Password: ");
    // String password = sc.nextLine();
    // if (user.getPassword().equals(password)) {
    // System.out.println("Login successful!");
    // sc.close();
    // return true;
    // } else {
    // System.out.println("Wrong password!");
    // user=null;
    // tries--;
    // continue;
    // }
    // }

    // System.out.println("You have no more chances left.");
    // sc.close();
    // return false;
    // }

    public static void menu() {
        System.out.println("===Store Cashing System===");
        System.out.println("1. Cashing");
        System.out.println("2. Query and Statistics");
        System.out.println("3. Merchandise Maintainance");
        System.out.println("4. Change Password");
        System.out.println("5. Export Data");
        System.out.println("6. Exit");
        System.out.println("\nCashier: " + user.getChrName());
        System.out.println("\nPlease select an option: ");

        Scanner sc = new Scanner(System.in);

        int option;
        while (true) {
            option = sc.nextInt();
            switch (option) {
            case 1:
                // add new transaction
                if (!cashier.AddTransaction()) {
                    System.out.println("Abnormal transaction, quitting back...");
                }
                util.delay(1000);
                break;
            case 2:
                // TODO: query();
                cashier.searchByDate();
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

                util.delay(1000);
                sc.close();
                return;
            default:

                util.cls();
                System.out.println("\nInvalid option!");

            }
        }
    }

}
