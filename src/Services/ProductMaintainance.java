package src.Services;

import java.util.List;
import java.util.Scanner;

import src.DAO.IProductDAO;
import src.DataType.Product;
import src.DataType.User;
import src.Factory.DAOFactory;
import src.Utilities.DatabaseConnection;
import src.Utilities.UIUX;

public class ProductMaintainance {
    private DatabaseConnection dbc; // 数据库连接类
    private src.DataType.User user;
    // private src.DataType.Product product;
    private IProductDAO productDAO;
    private UIUX util = new UIUX();

    public ProductMaintainance(User user) {
        this.dbc = new DatabaseConnection(); // 连接数据库
        // this.cashierDAO =
        // DAOFactory.getICashierDAOInstance(this.dbc.getConnection());
        productDAO=DAOFactory.getIProductDAOInstance(this.dbc.getConnection());
        this.user = user;
    }

    public void importFromExcel() {
        // TODO read from excel
    }

    public void menu() {
        util.cls();
        if(!user.getRole().equals("admin")) {
            System.out.println("You are not authorized to access this page! Contact your administrator for more information.");
            System.out.println("Quitting in 2 seconds...");
            util.delay(2000);
            return;
        }
        MaintainanceUI();
        Scanner sc = new Scanner(System.in);
        int choice;

        while (true) {
            choice = sc.nextInt();
            switch (choice) {
            case 1:
                // TODO excel
                break;
            case 2:
                // TODO text
                break;
            case 3:
                // TODO type
                break;
            case 4:
                // TODO search
                break;
            case 5:
                // back
                return;
            default:
                System.out.println("Invalid choice!");
                break;
            }
        }

    }

    public void importFromText() {
        // TODO read from text
    }

    public void manualInput() {
        // TODO manual input
    }

    public void searchProduct() {
        // TODO search product
        System.out.println("Tips: Try type relevant information to search products.");
        System.out.println("      Type \"exit\" to quit searching.");
            System.out.println("\tSearch: ");
        Scanner sc = new Scanner(System.in);
        String key = sc.nextLine();
        
        String search_query;

        List<Product> products;
        while (!key.equals("exit")) {
            util.cls();
            search_query = "SELECT * FROM product WHERE barcode LIKE '%" + key + "%' OR productName LIKE '%" + key
                + "%' OR price LIKE '%" + key + "%' OR supplyer LIKE '%" + key + "%'";

            products = null;
            try {
                products = productDAO.query(search_query);
            } catch (Exception e) {
                System.out.println("No such product.");
                util.delay(2000);
                break;
            }
            listProducts(products, key);
            System.out.println("\nPress ENTER to search again or type 'exit' to go back.");
            if(sc.nextLine().equals("exit")){
                break;
            }
            util.cls();
            System.out.println("Tip: Try type relevant information to search product.");
            System.out.println("Search: ");
            key = sc.nextLine();
        }
        return;
    }

    public void MaintainanceUI() {
        // menu
        util.cls();
        System.out.println("====Product Maintainance====");
        System.out.println("1. Import from Excel");
        System.out.println("2. Import from Text");
        System.out.println("3. Manual Input");
        System.out.println("4. Search Product");
        System.out.println("5. Back");
        System.out.println("\nPlease input your choice: ");

    }

    public void listProducts(List<Product> products, String key) {
        util.cls();
        System.out.println("====Product List====");
        System.out.println("Found " + products.size() + " products about \"" + key + "\".");
        System.out.println("No.\tBarcode\tProduct Name\tPrice\tSupplyer");
        int count = 1;

        for (int i = 0; i < products.size(); i++) {
            System.out.println(count + '\t' + products.get(i).getBarcode() + '\t' + products.get(i).getProductName()
                    + '\t' + util.price2string(products.get(i).getPrice_x100()) + '\t' + products.get(i).getSupplyer());
            count++;
        }
    }
}
