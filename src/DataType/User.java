package src.DataType;
public class User {
    private String userName;
    private String chrName;
    private String password;
    private String role;

    public User() {
        super();
    }

    public User(String userName, String chrName, String password, String role) {
        super();
        this.userName = userName;
        this.chrName = chrName;
        this.password = password;
        this.role = role;
    }
    // @Override
    public String getUserName() {
        return userName;
    }
    public String getChrName() {
        return chrName;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setChrName(String chrName) {
        this.chrName = chrName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(String role) {
        this.role = role;
    }

    // 剩下的 getter,setter，toString 方法略,自行补充
}