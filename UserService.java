import java.util.HashMap;
import java.util.Map;

/**
 * 数据层完成后最终一定要交给业务层进行调用，在业务层里面就需要通过工厂取得数据层接口对象， 业务层的接口名称为 XxxxService，保存在
 * service 包之中。 同时业务层的方法没有特别明确的标 准，所以方法名称只要求有意义。
 * 一定要记住这个类要有两个功能：打开和关闭数据库（不管是否出异常， 数据库都要关闭）、调用数 据层方法。
 */
public class UserService {
    private DatabaseConnection dbc; // 数据库连接类
    private IUserDAO userDAO; // 由工厂统一提供的 dao 实现类对象

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
    public Map<String, Object> checkLogin(User user) throws Exception {
        Map<String, Object> mapResult = new HashMap<String, Object>();
        try {
            User foundUser = this.userDAO.getById(user.getUserName());
            if (foundUser == null) {
                mapResult.put("code", 1);
                mapResult.put("msg", "用户名不存在！");
            } else {
                if (!foundUser.getPassword().equals(user.getPassword())) {
                    mapResult.put("code", 1);
                    mapResult.put("msg", "密码不正确！");
                } else {
                    mapResult.put("code", 0);
                    mapResult.put("msg", "登录成功！");
                }
            }
        } catch (Exception e) {
            mapResult.put("code", 2);
            mapResult.put("msg", e.getMessage());
        } finally { // 无论是否有异常，都需要关闭数据库连接
            this.dbc.close();
        }
        return mapResult;
    }
}