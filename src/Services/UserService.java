package src.Services;

import java.security.MessageDigest;
// import java.util.HashMap;
// import java.util.Map;
import java.util.Scanner;

import src.DAO.IUserDAO;
import src.DataType.User;
import src.Factory.DAOFactory;
import src.Utilities.DatabaseConnection;

/**
 * 数据层完成后最终一定要交给业务层进行调用，在业务层里面就需要通过工厂取得数据层接口对象， 业务层的接口名称为 XxxxService，保存在
 * service 包之中。 同时业务层的方法没有特别明确的标 准，所以方法名称只要求有意义。
 * 一定要记住这个类要有两个功能：打开和关闭数据库（不管是否出异常， 数据库都要关闭）、调用数 据层方法。
 */
public class UserService {
    private DatabaseConnection dbc; // 数据库连接类
    private IUserDAO userDAO; // 由工厂统一提供的 dao 实现类对象
    private User user;

    public UserService() {
        this.dbc = new DatabaseConnection(); // 连接数据库
        this.userDAO = DAOFactory.getIUserDAOInstance(this.dbc.getConnection());
    } // 从工厂类获取 dao 实现类对象

    /**
     * 用户登录检查
     * 
     * @param user：输入的用户信息
     * @return;以 map 形式返回检查结果，code 值的具体含义可以由开发人员任意定义，比如 code 为
     *           0，表示成功登录，code=1，表示登录失败，code=2，表示存在异常；msg 中存放错误描述，
     * @throws Exception
     */

    /*
     * public Map<String, Object> checkLogin(User user) throws Exception {
     * Map<String, Object> mapResult = new HashMap<String, Object>(); try { User
     * foundUser = this.userDAO.getById(user.getUserName()); if (foundUser == null)
     * { mapResult.put("code", 1); mapResult.put("msg", "用户名不存在！"); } else { if
     * (!foundUser.getPassword().equals(user.getPassword())) { mapResult.put("code",
     * 1); mapResult.put("msg", "密码不正确！"); } else { mapResult.put("code", 0);
     * mapResult.put("msg", "登录成功！"); } } } catch (Exception e) {
     * mapResult.put("code", 2); mapResult.put("msg", e.getMessage()); } finally {
     * // 无论是否有异常，都需要关闭数据库连接 this.dbc.close(); } return mapResult; }
     */

    public User login() {
        int tries = 3;
        Scanner sc = new Scanner(System.in);

        System.out.println("Login");
        while (tries > 0) {

            // Input username
            System.out.println("Username: ");
            String username = sc.nextLine();
            try {
                user = userDAO.getById(username);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (user == null) {
                System.out.println("User does not exist!");
                // tries--;
                continue;
            }

            // Input password
            System.out.println("Password: ");
            String password = sc.nextLine();
            if (user.getPassword().equals(password)) {
                System.out.println("Login successful!");
                sc.close();
                return user;
            } else {
                System.out.println("Wrong password!");
                user = null;
                tries--;
                continue;
            }
        }
        System.out.println("You have no more chances left.");
        sc.close();
        return null;
    }

    public void changePassword() {
        System.out.println("Please input your old password for confirmation: ");
        Scanner sc = new Scanner(System.in);
        int tries = 3;
        while (!sc.nextLine().equals(user.getPassword())) {
            tries--;
            if (tries != 0) {
                System.out.println("Wrong password! " + tries + " chances left.");
                System.out.println("Please input your old password for confirmation: ");
            } else {
                System.out.println("You have no more chances left.");
                sc.close();
                return;
            }
        }

        System.out.println("Please input your new password: ");
        String newPassword = sc.nextLine();

        while (isValidPassword(newPassword)) {
            System.out.println("Please input your new password: ");
            newPassword = sc.nextLine();
        }

        System.out.println("Re-enter your new password to confirm your change: ");
        if (sc.nextLine().equals(newPassword)) {
            user.setPassword(newPassword);
            try {
                userDAO.update(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Password change cancelled!");
        }
        sc.close();
    }

    public boolean isValidPassword(String password) {
        boolean notNull=false;
        boolean lenChk=false;
        boolean containsLC=false;
        boolean containsUC=false;
        boolean containsDigit=false;

        if (password != null && password != "") {
            notNull=true;
        }
        else{
            System.out.println("Password cannot be empty!");
        }

        if(password.length()>=6) {
            lenChk=true;
        }
        else{
            System.out.println("Password must be at least 6 characters long!");
        }

        if(password.matches(".*[a-z].*")) {
            containsLC=true;
        }
        else{
            System.out.println("Password must contain at least one lowercase letter!");
        }

        if(password.matches(".*[A-Z].*")) {
            containsUC=true;
        }
        else{
            System.out.println("Password must contain at least one uppercase letter!");
        }

        if(password.matches(".*\\d.*")) {
            containsDigit=true;
        }
        else{
            System.out.println("Password must contain at least one digit!");
        }



        if(notNull && lenChk && containsLC && containsUC && containsDigit) {
            return true;
        }
        else {
            return false;
        }
    }
}