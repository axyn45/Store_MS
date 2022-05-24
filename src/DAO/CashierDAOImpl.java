package src.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import src.DataType.Record;

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
        String sql = "INSERT INTO salesrecords(transaction_id,barcode,productName,price_x100,quantity,operator,time) " + "VALUES(?,?,?,?,?,?,now())";
        this.pstmt = this.conn.prepareStatement(sql);
        this.pstmt.setString(1, record.getTransaction_id());
        this.pstmt.setString(2, record.getBarcode());
        this.pstmt.setString(3, record.getProductName());
        this.pstmt.setInt(4, record.getPrice_x100());
        this.pstmt.setInt(5, record.getQuantity());
        this.pstmt.setString(6, record.getOperator());
        // this.pstmt.setString(7, record.getTime());
        // this.pstmt.setDate(7, record.getTime());
        if (this.pstmt.executeUpdate() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public boolean delete(String transaction_id) throws Exception {
        String sql = "DELETE FROM salesrecords WHERE transaction_id=?";
        this.pstmt = this.conn.prepareStatement(sql);
        this.pstmt.setString(1, transaction_id);
        if (this.pstmt.executeUpdate() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public Record getById(String transaction_id) {
        String sql = 
        "SELECT * FROM salesrecords WHERE transaction_id=?";
        try {
            this.pstmt = this.conn.prepareStatement(sql);
            this.pstmt.setString(1, transaction_id);
            ResultSet rs = this.pstmt.executeQuery();
            if (rs.next()) {
                Record record = new Record();
                record.setTransaction_id(rs.getString("transaction_id"));
                record.setBarcode(rs.getString("barcode"));
                record.setProductName(rs.getString("productName"));
                record.setPrice_x100(rs.getInt("price_x100"));
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
        String sql = "SELECT * FROM salesrecords ORDER BY transaction_id DESC LIMIT 1";
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


    public List<src.DataType.Record> query(String qstring) throws Exception {
        // 可以后续再实现，但是该方法不能删除，因为实现接口，必须实现接口的所有方法，即使该方法暂时没代码
        this.pstmt = this.conn.prepareStatement(qstring);
        ResultSet rs = this.pstmt.executeQuery();
        
        List<Record> records = new ArrayList<Record>();
        while (rs.next()) {
            Record record = new Record();
            record.setTransaction_id(rs.getString("transaction_id"));
            record.setBarcode(rs.getString("barcode"));
            record.setProductName(rs.getString("productName"));
            record.setPrice_x100(rs.getInt("price_x100"));
            record.setQuantity(rs.getInt("quantity"));
            record.setOperator(rs.getString("operator"));
            record.setTime(rs.getString("time"));
            records.add(record);
        }
        return records;
    }
}
