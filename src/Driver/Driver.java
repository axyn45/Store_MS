package src.Driver;

import java.util.Scanner;

import src.DataType.User;
import src.Services.Cashier;
import src.Services.ProductMaintainance;
import src.Services.UserService;
import src.Utilities.ConsoleColor;
import src.Utilities.UIUX;

public class Driver {
    static User user = null;
    static UIUX util = new UIUX();
    static ConsoleColor color = new ConsoleColor();
    static UserService us = null;
    static Cashier cashier = null;
    static ProductMaintainance pm = null;// ProductMaintainance instance
    static Scanner sc=new Scanner(System.in);

    public static void main(String[] args) {
        us=new UserService(sc);
        user = us.login();
        if (user == null) {
            System.out.println("Login failed, quitting...");
            util.delay(2000);
            System.exit(0);
        }
        else{
            cashier = new Cashier(user,sc);
            pm = new ProductMaintainance(user,sc);
        }
        menu();
        sc.close();
    }

    public static void menu() {
        int option;

        while (true) {
            //Login
            if (user == null) {
                user = us.login();
                if (user == null) {
                    System.out.println("Login failed, quitting...");
                    util.delay(2000);
                    System.exit(0);
                }
                else{
                    cashier = new Cashier(user,sc);
                    pm = new ProductMaintainance(user,sc);
                }
            }
            menuUI();
            option = Integer.parseInt(sc.nextLine());
            switch (option) {
            case 1:
                // add new transaction
                cashier.AddTransaction();
                util.delay(1000);
                break;

            case 2:
                while (cashier.searchByDate());
                break;

            case 3:
                pm.menu();
                break;

            case 4:
                if (us.changePassword()) {
                    user = null;
                    color.printYellowText("Password changed! Re-login required!");
                    util.delay(2000);
                }
                break;

            case 5:
                cashier.dataExportMenu();
                break;

            case 6:
                us.signup();
                break;
            case 7:
                cashier.deleteTransactionRecord();
                break;

            case 8:
                util.cls();
                System.out.println("\nQuitting...");
                util.delay(1000);
                return;

            default:
                util.cls();
                System.out.println("\nInvalid option!");
            }
        }
    }
    public static void menuUI(){
        util.cls();
        System.out.println("===Store Cashing System===");
        System.out.println("1. Cashing");
        System.out.println("2. Query and Statistics");
        System.out.println("3. Merchandise Maintainance");
        System.out.println("4. Change Password");
        System.out.println("5. Export Data");
        System.out.println("6. Create Account");
        System.out.println("7. Delete Transaction Record");
        System.out.println("8. Exit");
        System.out.println("\nCashier: " + user.getChrName());
        System.out.println("\nPlease select an option: ");
    }

}
