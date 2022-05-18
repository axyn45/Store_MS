package src.Services;

import java.util.Scanner;

import com.mysql.cj.xdevapi.AddResult;

import src.DatabaseConnection;
import src.User;
import src.DAO.ICashierDAO;
import src.Factory.DAOFactory;

public class Cashier {
    private DatabaseConnection dbc; // 数据库连接类
    private ICashierDAO cashierDAO; // 由工厂统一提供的 dao 实现类对象
    private Record record;
    private User user;

    // 从工厂类获取 dao 实现类对象
    public Cashier(User user) {
        this.dbc = new DatabaseConnection(); // 连接数据库
        this.cashierDAO = DAOFactory.getICashierDAOInstance(this.dbc.getConnection());
        this.user = user;
    }

    public boolean AddTransaction(){
        Scanner sc=new Scanner(System.in);
        int quantity;

        System.out.println("Please input the barcode: ");
        String barcode=sc.nextLine();
        while(!isValidBarcode(barcode)){
            System.out.println("Invalid barcode!");
            System.out.println("A valid barcode contains exactly 6 digits!");
            System.out.println("Retry in 2 seconds...");
            //TODO delay 2000ms
            //TODO clear screen
            System.out.println("Please input the barcode: ");
            barcode=sc.nextLine();
        }
        String productName=cashierDAO.getProductName(barcode);
    }
    public void CashierMenu() {
        // TODO clear screen
        // TODO show menu

    }

    public boolean isValidBarcode(String barcode) {
        // TODO check if barcode is valid
        return true;
    }

    public String getProductName(String barcode) {
        // TODO get product name from barcode
        return "";
    }
    public int get100xPrice(String barcode) {
        // TODO get 100x price from barcode
        return 0;
    }
}
