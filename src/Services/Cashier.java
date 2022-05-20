package src.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import com.mysql.cj.xdevapi.Schema.Validation;

import src.DAO.ICashierDAO;
import src.DAO.IProductDAO;
import src.DataType.User;
import src.Factory.DAOFactory;
import src.Utilities.DataValidation;
import src.Utilities.DatabaseConnection;
import src.Utilities.UIUX;

public class Cashier {
    private DatabaseConnection dbc; // 数据库连接类
    private ICashierDAO cashierDAO; // 由工厂统一提供的 dao 实现类对象
    private IProductDAO productDAO;
    private src.DataType.Product product;
    private src.DataType.Record record;
    private src.DataType.User user;
    private UIUX util = new UIUX();
    private DataValidation validate = new DataValidation();

    // 从工厂类获取 dao 实现类对象
    public Cashier(User user) {
        this.dbc = new DatabaseConnection(); // 连接数据库
        this.cashierDAO = DAOFactory.getICashierDAOInstance(this.dbc.getConnection());
        this.productDAO = DAOFactory.getIProductDAOInstance(this.dbc.getConnection());
        this.user = user;
    }

    public boolean AddTransaction() {
        Scanner sc = new Scanner(System.in);
        String barcode = null;
        for (int i = 0; i == 0;) {
            System.out.println("Please input the barcode: ");
            barcode = sc.nextLine();
            while (!validate.isValidBarcode(barcode)) {
                System.out.println("Invalid barcode!");
                System.out.println("A valid barcode contains exactly 6 digits!");
                System.out.println("Retry in 2 seconds...");
                util.delay(2000);
                util.cls();
                System.out.println("Please input the barcode: ");
                barcode = sc.nextLine();
            }

            try {
                product = productDAO.getByBarcode(barcode);
                i = 1;
            } catch (Exception e) {
                System.out.println("Product not found! Please try again.");
                util.delay(2000);
            }
        }

        record.setTransaction_id(Integer.toString(Integer.parseInt(cashierDAO.getLastTransactionID()) + 1));
        record.setBarcode(barcode);
        record.setProductName(product.getProductName());
        record.setPrice_x100(product.getPrice_x100());
        record.setQuantity(sc.nextInt());
        record.setOperator(user.getUserName());
        record.setTime("now()");

        try {
            cashierDAO.insert(record);
            System.out.println("Transaction added successfully!");
            util.cls();
            sc.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error in adding transaction!");
            util.cls();
            sc.close();
            return false;
        }
    }

    public void searchByDate() {
        System.out.println("Please input the date: ");
        Scanner sc = new Scanner(System.in);
        String date = sc.nextLine();

        util.cls();
        String arrOfDate[] = validate.isValidDate(date);
        while (arrOfDate == null) {
            System.out.println("Invalid date!");
            System.out.println("A valid date is in the format of yyyy-mm-dd!");
            System.out.println("Retry in 2 seconds...");
            util.delay(2000);
            util.cls();
            System.out.println("Please input the date: ");
            date = sc.nextLine();
        }
        List<src.DataType.Record> records = null;
        try {
            records = cashierDAO.query("select * from record where time like '%" + date + "%'");
        } catch (Exception e) {
            System.out.println("Error in querying transactions!");
            util.cls();
        }

        ListIterator<src.DataType.Record> it = records.listIterator();
        listDateQuery(arrOfDate, it);
        sc.close();
    }

    public void export2sheet() {
        // TODO export to excel
    }

    public void export2text() {
        // TODO export to text
    }

    public void dataExportMenu() {
        exportUI();
        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();
        while (true) {
            switch (option) {
            case 1:
                export2sheet();
                break;
            case 2:
                export2text();
                break;
            case 3:
                util.cls();
                return;
            }
            exportUI();
            option = sc.nextInt();
        }
    }

    public void exportUI() {
        util.cls();
        // TODO show menu
        System.out.println("====Data Export Menu====\n");
        System.out.println("1. Export to Excel");
        System.out.println("2. Export to Text");
        System.out.println("3. Back");
        System.out.println("\nPlease input your choice: ");
    }

    public void listDateQuery(String[] date, ListIterator<src.DataType.Record> it) {
        util.cls();
        // show menu
        System.out.println("Transaction records of " + date[0] + "-" + date[1] + "-" + date[2]);
        System.out.println("Transaction ID\tBarcode\tProduct Name\tPrice\tQuantity\tOperator\tTime");
        System.out.println("--------------\t-------\t------------\t-----\t--------\t--------\t----");
        src.DataType.Record record;
        int amount_x100 = 0;
        List<String> products = new ArrayList<String>();
        int total_quantity = 0;
        while (it.hasNext()) {
            record = it.next();
            if (!products.contains(record.getBarcode())) {
                products.add(record.getBarcode());
                total_quantity += record.getQuantity();
                amount_x100 += record.getQuantity() * record.getPrice_x100();
            }
            System.out.println(record.getTransaction_id() + "\t" + record.getBarcode() + "\t" + record.getProductName()
                    + "\t" + record.getPrice_x100() + "\t" + record.getQuantity() + "\t" + record.getOperator() + "\t"
                    + record.getTime());
        }
        System.out.println("-------------------------------------------------------------");
        System.out.println("Total quantity: " + total_quantity + "\tTotal products: " + products.size()
                + "\tTotal amount: " + util.price2string(amount_x100));
        System.out.println("\nPress ENTER to continue...");
        util.wait4enter();
    }

    // Returns xx.xx in string format

}
