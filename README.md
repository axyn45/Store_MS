# DAO设计模式

DAO 的全称是（Data Access Object），数据访问对象，使用 DAO 设计模式，来封装数据库 持久层的所有操作，使得底层的数据逻辑和高层的业务逻辑相分离，达到解耦合的目的。 

##### DAO 包括六个重要的部分：

- 数据库连接类：连接数据库并获取连接对象，关闭连接对象。

- VO 实体类：与数据库结构进行映射的类。主要由属性，setter, getter 方法组成，VO 类中 的属性与表中的字段相对应，每一个 VO 类的对象都表示表中的每一条记录 

- DAO 接口：主要定义操作的接口，定义一系列数据库的原子性操作，例如增删改查（通常称为 CRUD）等。

- DAO 实现类：DAO 接口的真实实现类，主要完成具体数据库操作，但不负责数据库的打开和 关闭。 

- DAO 工厂类：通过工厂类取得一个 DAO 的实例化对象

- 业务逻辑实现类：对于数据层的原子操作进行整合。还要负责数据库的打开与关闭（不管 是否出异常，数据库都要关闭） 

##### 对于包的命名：

- 数据库连接： xxx.dbc.DatabaseConnection

- DAO 接口： xxx.dao.IXxxDAO

- DAO 接口真实实现类：xxx.dao.impl.XxxDAOImpl

- 业务逻辑实现类：xxx.dao.service.XxxService

- VO 类： xxx.vo.Xxx, VO 命名要与表的命名一致

- 工厂类：xxx.factory.DAOFactory.


