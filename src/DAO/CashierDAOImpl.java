package src.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import src.Record;

public class CashierDAOImpl implements ICashierDAO{
    private Connection conn;
    private PreparedStatement pstmt;

    // public

    // 实例化时，给该类提供连接对象
    public CashierDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // @Override
    public boolean insert(Record record) throws Exception {
        String sql = "INSERT INTO salesdetail(userName,chrName,password,role) " + "VALUES(?,?,?,?)";
        this.pstmt = this.conn.prepareStatement(sql);
        this.pstmt.setString(1, record.getTransaction_id());
        this.pstmt.setString(2, record.getBarcode());
        this.pstmt.setString(3, record.getProductName());
        this.pstmt.setDouble(4, record.getPrice());
        this.pstmt.setInt(5, record.getQuantity());
        this.pstmt.setString(6, record.getOperator());
        // this.pstmt.setString(7, record.getTime());
        this.pstmt.setString(7, record.getTime());
        if (this.pstmt.executeUpdate() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // @Override
    // public boolean update(Record record) throws Exception {
    //     String sql = "UPDATE user SET chrName=? ,password=?,role=? WHERE userName=?";
    //     this.pstmt = this.conn.prepareStatement(sql);
    //     this.pstmt.setString(1, record.getTransaction_id());
    //     this.pstmt.setString(2, record.getBarcode());
    //     this.pstmt.setString(3, record.getProductName());
    //     this.pstmt.setDouble(4, record.getPrice());
    //     this.pstmt.setInt(5, record.getQuantity());
    //     this.pstmt.setString(6, record.getOperator());
    //     // this.pstmt.setString(7, record.getTime());
    //     this.pstmt.setString(7, "now()");
    //     if (this.pstmt.executeUpdate() > 0) {
    //         return true;
    //     } else {
    //         return false;
    //     }
    // }

    // @Override
    public boolean delete(String transaction_id) throws Exception {
        String sql = "DELETE FROM user WHERE userName=?";
        this.pstmt = this.conn.prepareStatement(sql);
        this.pstmt.setString(1, transaction_id);
        if (this.pstmt.executeUpdate() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // @Override
    public Record getById(String transaction_id) {
        String sql = "SELECT userName,chrName,password,role FROM user WHERE userName=?";
        try {
            this.pstmt = this.conn.prepareStatement(sql);
            this.pstmt.setString(1, transaction_id);
            ResultSet rs = this.pstmt.executeQuery();
            if (rs.next()) {
                Record record = new Record();
                record.setTransaction_id(rs.getString("transaction_id"));
                record.setBarcode(rs.getString("barcode"));
                record.setProductName(rs.getString("productName"));
                record.setPrice(rs.getDouble("price"));
                record.setQuantity(rs.getInt("quantity"));
                record.setOperator(rs.getString("operator"));
                record.setTime(rs.getString("time"));
                return record;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getLastTransactionID(){
        String sql = "SELECT * FROM salesdetail ORDER BY transaction_id DESC LIMIT 1";
        try {
            this.pstmt = this.conn.prepareStatement(sql);
            ResultSet rs = this.pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("transaction_id");
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // @Override
    public List<src.Record> query(String query) throws Exception {
        // 可以后续再实现，但是该方法不能删除，因为实现接口，必须实现接口的所有方法，即使该方法暂时没代码
        
        return null;
    }
}
