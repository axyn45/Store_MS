package src.Services;

import src.DatabaseConnection;
import src.User;
import src.DAO.IProductDAO;

public class ProductMaintainance {
    private DatabaseConnection dbc; // 数据库连接类
    private src.User user;
    private src.Product product;
    private IProductDAO productDAO;

    public ProductMaintainance(User user) {
        this.dbc = new DatabaseConnection(); // 连接数据库
        // this.cashierDAO = DAOFactory.getICashierDAOInstance(this.dbc.getConnection());
        this.user = user;
    }

    public void importFromExcel(){
        // TODO read from excel
    }
    public void importFromText(){
        // TODO read from text
    }
    public void manualInput(){
        // TODO manual input
    }
    public void searchProduct(){
        // TODO search product
    }
    public void menu(){
        // TODO menu
    }
}
