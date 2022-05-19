package src.DAO;
import java.util.List;

import src.DataType.Record;

public interface ICashierDAO {
    // 增加一条记录
    public boolean insert(Record record) throws Exception;

    // 修改记录
    // public boolean update(salesrecord user) throws Exception;

    // 按主键（流水号）删除记录
    public boolean delete(String transaction_id) throws Exception;

    // 查询最后一条记录
    public Record getById(String transaction_id);
    // 查询满足条件的记录：查询条件封装在 user 对象中，若 user 对象的某个成员变量值为 null，则表示查询时忽略该字段查询条件

    public String getLastTransactionID();

    public List<src.DataType.Record> query(String query) throws Exception;
}
