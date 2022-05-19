package src.Services;

import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import com.mysql.cj.util.Util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import src.DAO.ICashierDAO;
import src.DAO.IProductDAO;
import src.DataType.User;
import src.Factory.DAOFactory;
import src.Utilities.DatabaseConnection;
import src.Utilities.EnterKeyListener;
import src.Utilities.UIUX;

public class Cashier {
    private DatabaseConnection dbc; // 数据库连接类
    private ICashierDAO cashierDAO; // 由工厂统一提供的 dao 实现类对象
    private IProductDAO productDAO;
    private src.DataType.Product product;
    private src.DataType.Record record;
    private src.DataType.User user;
    private EnterKeyListener enterListener=new EnterKeyListener();
    private UIUX util = new UIUX();

    // 从工厂类获取 dao 实现类对象
    public Cashier(User user) {
        this.dbc = new DatabaseConnection(); // 连接数据库
        this.cashierDAO = DAOFactory.getICashierDAOInstance(this.dbc.getConnection());
        this.user = user;
    }

    public boolean AddTransaction() {
        Scanner sc = new Scanner(System.in);
        String barcode = null;
        for (int i = 0; i == 0;) {
            System.out.println("Please input the barcode: ");
            barcode = sc.nextLine();
            while (!isValidBarcode(barcode)) {
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
        record.setPrice(product.getPrice_x100() / 100.0);
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
        String arrOfDate[] = isValidDate(date);
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
        dateQueryList(arrOfDate, it);
        sc.close();
    }

    public void export2sheet() {
        // TODO export to excel
    }

    public void export2text() {
        // TODO export to text
    }

    public void CashierMenu() {
        util.cls();
        // TODO show menu

    }

    public void dataExportMenu() {
        util.cls();
        // TODO show menu
        
    }

    public void dateQueryList(String[] date, ListIterator<src.DataType.Record> it) {
        util.cls();
        // TODO show menu
        enterListener.wait4enter();
    }

    public boolean isValidBarcode(String barcode) {
        // TODO check if barcode is valid
        return true;
    }

    public String[] isValidDate(String date) {
        // TODO check if date is valid
        return null;
    }
}
