package src.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import src.DataType.Product;

public class ProductDAOImpl implements IProductDAO {
    private Connection conn;
    private PreparedStatement pstmt;

    // public

    // 实例化时，给该类提供连接对象
    public ProductDAOImpl(Connection conn) {
        this.conn = conn;
    }

    public boolean insert(Product product) throws Exception {
        String sql = "INSERT INTO product(barcode,productName,price_x100,supplier) " + "VALUES(?,?,?,?)";
        this.pstmt = this.conn.prepareStatement(sql);
        this.pstmt.setString(1, product.getBarcode());
        this.pstmt.setString(2, product.getProductName());
        this.pstmt.setInt(3, product.getPrice_x100());
        this.pstmt.setString(4, product.getSupplier());
        if (this.pstmt.executeUpdate() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean update(Product product) throws Exception {
        String sql = "UPDATE product SET barcode=? ,productName=?,price_x100=?,supplier=? WHERE barcode=?";
        this.pstmt = this.conn.prepareStatement(sql);
        this.pstmt.setString(1, product.getBarcode());
        this.pstmt.setString(2, product.getProductName());
        this.pstmt.setInt(3, product.getPrice_x100());
        this.pstmt.setString(4, product.getSupplier());
        this.pstmt.setString(5, product.getBarcode());
        if (this.pstmt.executeUpdate() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean delete(String barcode) throws Exception {
        String sql = "DELETE FROM product WHERE barcode=?";
        this.pstmt = this.conn.prepareStatement(sql);
        this.pstmt.setString(1, barcode);
        if (this.pstmt.executeUpdate() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Product getByBarcode(String barcode) throws Exception {
        String sql = "SELECT barcode,productName,price_x100,supplier FROM product WHERE barcode=?";
        this.pstmt = this.conn.prepareStatement(sql);
        this.pstmt.setString(1, barcode);
        ResultSet rs = this.pstmt.executeQuery();
        if (rs.next()) {
            Product product = new Product();
            product.setBarcode(rs.getString("barcode"));
            product.setProductName(rs.getString("productName"));
            product.setPrice_x100(rs.getInt("price_x100"));
            product.setSupplier(rs.getString("supplier"));
            return product;
        } else {
            return null;
        }
    }

    public List<Product> query(String qstring) throws Exception {
        // 可以后续再实现，但是该方法不能删除，因为实现接口，必须实现接口的所有方法，即使该方法暂时没代码
        this.pstmt = this.conn.prepareStatement(qstring);
        ResultSet rs = this.pstmt.executeQuery();
        
        List<Product> products = new ArrayList<Product>();
        while (rs.next()) {
            Product product = new Product();
            product.setBarcode(rs.getString("barcode"));
            product.setProductName(rs.getString("productName"));
            product.setPrice_x100(rs.getInt("price_x100"));
            product.setSupplier(rs.getString("supplier"));
            products.add(product);
        }
        return products;
    }
}
