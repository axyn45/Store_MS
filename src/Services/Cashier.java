package src.Services;

import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import com.mysql.cj.xdevapi.AddResult;

import src.DatabaseConnection;
import src.Product;
import src.User;
import src.DAO.ICashierDAO;
import src.DAO.IProductDAO;
import src.Factory.DAOFactory;

public class Cashier {
    private DatabaseConnection dbc; // 数据库连接类
    private ICashierDAO cashierDAO; // 由工厂统一提供的 dao 实现类对象
    private IProductDAO productDAO;
    private src.Product product;
    private src.Record record;
    private src.User user;

    // 从工厂类获取 dao 实现类对象
    public Cashier(User user) {
        this.dbc = new DatabaseConnection(); // 连接数据库
        this.cashierDAO = DAOFactory.getICashierDAOInstance(this.dbc.getConnection());
        this.user = user;
    }

    public boolean AddTransaction() {
        Scanner sc = new Scanner(System.in);
        String barcode=null;
        for (int i = 0; i == 0;) {
            System.out.println("Please input the barcode: ");
            barcode = sc.nextLine();
            while (!isValidBarcode(barcode)) {
                System.out.println("Invalid barcode!");
                System.out.println("A valid barcode contains exactly 6 digits!");
                System.out.println("Retry in 2 seconds...");
                // TODO delay 2000ms
                // TODO clear screen
                System.out.println("Please input the barcode: ");
                barcode = sc.nextLine();
            }

            try {
                product = productDAO.getByBarcode(barcode);
                i = 1;
            } catch (Exception e) {
                System.out.println("Product not found! Please try again.");
                // TODO delay 2000ms
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
            // TODO clear screen
            sc.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error in adding transaction!");
            // TODO clear screen
            sc.close();
            return false;
        }
    }

    public void queryByDate(){
        System.out.println("Please input the date: ");
        Scanner sc = new Scanner(System.in);
        String date = sc.nextLine();
        
        //TODO clear screen
        String arrOfDate[]=isValidDate(date);
        while(arrOfDate==null){
            System.out.println("Invalid date!");
            System.out.println("A valid date is in the format of yyyy-mm-dd!");
            System.out.println("Retry in 2 seconds...");
            // TODO delay 2000ms
            // TODO clear screen
            System.out.println("Please input the date: ");
            date = sc.nextLine();
        }
        List<src.Record> records=null;
        try {
            records = 
            cashierDAO.query("select * from record where time like '%" + date + "%'");
        } catch (Exception e) {
            System.out.println("Error in querying transactions!");
            // TODO clear screen
        }

        
        ListIterator<src.Record> it = records.listIterator();
        dateQueryMenu(arrOfDate,it);
    }

    public void CashierMenu() {
        // TODO clear screen
        // TODO show menu

    }

    public void dateQueryMenu(String[] date,ListIterator<src.Record> it) {
        // TODO clear screen
        // TODO show menu
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
