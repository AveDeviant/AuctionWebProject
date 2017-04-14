package by.buslauski.auction.validator;

/**
 * Created by Acer on 12.04.2017.
 */
public class daoPattern {
//    package by.epam.dot.dao;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
//
//    public class AbstractDAO {
//        protected Connection connection;
//
//        void setConnection(Connection connection) {
//            this.connection = connection;
//        }
//    }
//    package by.epam.dot.dao;
//
//
//    public class AbonentDAO extends AbstractDAO {
//
//    }
//    package by.epam.dot.dao;
//
//
//
//    public class PaymentDAO extends AbstractDAO {
//    }
//    package by.epam.dot.dao;
//
//import by.epam.dot.pool.ConnectionPool;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
//
//    public class TransactionHelper {
//        private Connection connection = ConnectionPool.getInstance().getConnection();
//        public  void beginTransaction(AbstractDAO ... daos){
//            try {
//                connection.setAutoCommit(false);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            for (AbstractDAO dao : daos) {
//                dao.setConnection(connection);
//            }
//        }
//        public  void endTransaction(){
//            try {
//                connection.setAutoCommit(true);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            ConnectionPool.getInstance().releaseConnection(connection);
//        }
//    }
//    package by.epam.dot.run;
//
//import by.epam.dot.dao.AbonentDAO;
//import by.epam.dot.dao.PaymentDAO;
//import by.epam.dot.dao.TransactionHelper;
//
//
//    public class Logic {
//        public void doLogic(){
//            AbonentDAO abonentDAO = new AbonentDAO();
//            PaymentDAO paymentDAO = new PaymentDAO();
//            TransactionHelper helper = new TransactionHelper();
//            helper.beginTransaction(abonentDAO, paymentDAO);
//            /// вызов методов DAO
//            helper.endTransaction();
//        }
//    }
}
