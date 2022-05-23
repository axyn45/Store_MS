package src.Services;

import java.util.Scanner;

import src.DAO.IUserDAO;
import src.DataType.User;
import src.Factory.DAOFactory;
import src.Utilities.ConsoleColor;
import src.Utilities.DataValidation;
import src.Utilities.DatabaseConnection;
import src.Utilities.UIUX;

/**
 * 数据层完成后最终一定要交给业务层进行调用，在业务层里面就需要通过工厂取得数据层接口对象， 业务层的接口名称为 XxxxService，保存在
 * service 包之中。 同时业务层的方法没有特别明确的标 准，所以方法名称只要求有意义。
 * 一定要记住这个类要有两个功能：打开和关闭数据库（不管是否出异常， 数据库都要关闭）、调用数 据层方法。
 */
public class UserService {
    private DatabaseConnection dbc; // 数据库连接类
    private IUserDAO userDAO; // 由工厂统一提供的 dao 实现类对象
    private User user;
    private DataValidation validate = new DataValidation();
    private UIUX util = new UIUX();
    private ConsoleColor color = new ConsoleColor();
    private Scanner sc = null;

    public UserService(Scanner sc) {
        this.dbc = new DatabaseConnection(); // 连接数据库
        this.sc = sc;
        this.userDAO = DAOFactory.getIUserDAOInstance(this.dbc.getConnection());
    } // 从工厂类获取 dao 实现类对象

    protected void finalize() {
        this.dbc.close();
    }

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

    public void signup() {
        util.cls();
        if (!user.getRole().equals("admin")) {
            System.out.println(
                    "You are not authorized to access this page! Contact your administrator for more information.");
            System.out.println("Quitting in 2 seconds...");
            util.delay(2000);
            return;
        }

        User newUser = new User();

        System.out.println("Sign up");
        System.out.println("User name (for login): ");
        String userName = sc.nextLine();
        while (userName.equals("")) {
            color.printRedText("User name cannot be empty! Try again");
            util.delay(2000);
            util.cls();
            System.out.println("Sign up");
            System.out.println("User name (for login): ");
            userName = sc.nextLine();
        }
        newUser.setUserName(userName);

        util.cls();
        System.out.printf("Sign up as ");
        color.printYellowText(userName);
        System.out.println("Display name: ");
        String displayName = sc.nextLine();
        while (displayName.equals("")) {
            color.printRedText("Display name cannot be empty! Try again");
            util.delay(2000);
            util.cls();
            System.out.printf("Sign up as ");
            color.printYellowText(userName);
            System.out.println("Display name: ");
            displayName = sc.nextLine();
        }
        newUser.setChrName(displayName);

        util.cls();
        System.out.printf("Sign up as ");
        color.printGreenText(displayName);
        System.out.println("Password: ");
        String password = sc.nextLine();
        while (!validate.isValidPassword(password)) {
            util.delay(2000);
            util.cls();
            System.out.printf("Sign up as ");
            color.printYellowText(userName);
            System.out.println("Display name: ");
            password = sc.nextLine();
        }
        newUser.setPassword(password);

        util.cls();
        System.out.printf("Sign up as ");
        color.printGreenText(displayName);
        System.out.println("Confirm your password: ");
        while (!password.equals(sc.nextLine())) {
            color.printRedText("Password does not match! Try again");
            util.delay(2000);
            util.cls();
            System.out.printf("Sign up as ");
            color.printGreenText(displayName);
            System.out.println("Confirm your password: ");
        }

        util.cls();
        System.out.printf("Sign up as ");
        color.printGreenText(displayName);
        System.out.println("Role: ");
        String role = sc.nextLine();
        while (role.equals("")) {
            color.printRedText("Role name cannot be empty! Try again");
            util.delay(2000);
            util.cls();
            System.out.printf("Sign up as ");
            color.printGreenText(displayName);
            System.out.println("Role: ");
            role = sc.nextLine();
        }
        newUser.setChrName(displayName);

        util.cls();
        System.out.printf("Sign up as ");
        color.printGreenText(displayName + "(" + role + ")");
        System.out.println("Press ENTER to confirm adding user or type anything to go back...");
        if (!sc.nextLine().equals("")) {
            return;
        }

        util.cls();
        try {
            if(userDAO.insert(newUser)){
                color.printGreenText("User " + userName + " added successfully!");
            }else{
                color.printRedText("User " + userName + " already exists!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            color.printRedText("User already exists!");
            util.delay(2000);
            return;
        }
        util.delay(2000);
        return;
    }

    public User login() {
        int tries = 3;

        while (tries > 0) {
            util.cls();
            System.out.println("Login\n");
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
                util.delay(2000);
                continue;
            }

            // Input password
            System.out.println("Password: ");
            String password = sc.nextLine();
            util.cls();
            if (user.getPassword().equals(password)) {
                color.printGreenText("Login successful!");
                //
                util.delay(2000);
                return user;
            } else {
                color.printRedText("Wrong password!");
                user = null;
                tries--;
                util.delay(2000);
                continue;
            }
        }
        util.cls();
        color.printRedText("You have no more chances left.");
        return null;
    }

    public boolean changePassword() {
        util.cls();
        if (user == null) {
            color.printYellowText("Please login first!\nReturn in 2 seconds...");
            util.delay(2000);
            return false;
        }
        System.out.println("Please input your old password for confirmation: ");
        int tries = 3;
        while (!sc.nextLine().equals(user.getPassword())) {
            tries--;
            util.cls();
            if (tries != 0) {
                util.cls();
                color.printRedText("Wrong password! " + tries + " chances left.");
                System.out.println("Please input your old password for confirmation: ");
            } else {
                color.printRedText("You have no more chances left.");

                return false;
            }

        }

        util.cls();
        System.out.println("Please input your new password: ");
        String newPassword = sc.nextLine();
        util.cls();

        while (validate.isValidPassword(newPassword)) {
            System.out.println("Please input your new password: ");
            newPassword = sc.nextLine();
        }
        util.cls();
        System.out.println("Re-enter your new password to confirm your change: ");
        if (sc.nextLine().equals(newPassword)) {
            user.setPassword(newPassword);
            try {
                userDAO.update(user);
                color.printGreenText("Password changed successfully!");

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error occured when changing password!\nReturn in 2 seconds...");
                util.delay(2000);

                return false;
            }

        } else {
            color.printRedText("Dosen't match with your priviously input!");
            System.out.println("Password change cancelled! Return in 2 seconds...");
            util.delay(2000);

            return false;
        }
    }

}