package src.Services;

import src.DatabaseConnection;

public class ProductMaintainance {
    private DatabaseConnection dbc; // 数据库连接类

    public class ProductMaintainance(User user) {
        this.dbc = new DatabaseConnection(); // 连接数据库
        this.cashierDAO = DAOFactory.getICashierDAOInstance(this.dbc.getConnection());
        this.user = user;
    }
}
