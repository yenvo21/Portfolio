import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class manage database - create database tables, Initialize data for database
 *
 * @Author Yen Vo
 */
public class DatabaseManager {

    /**
     * Initialize Grocery Database
     *
     * @Author Yen Vo
     */
    public static void createDBTables() {

        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {

            // --------------------------------
            // Create table PRODUCT
            // --------------------------------
            dropTable("PRODUCT", stat); // drop if table exist
            stat.execute("CREATE TABLE PRODUCT " +
                    "(PRODUCT_ID VARCHAR(10)," +
                    " PRODUCT_NAME VARCHAR(30)," +
                    " DESCRIPTION VARCHAR (150)," +
                    " ON_HAND INT," +
                    " LOCATION VARCHAR(2)," +
                    " PRICE DECIMAL(10,2))");

            // --------------------------------
            // Create table CUSTOMER
            // --------------------------------
            dropTable("CUSTOMER", stat); // drop if table exist
            stat.execute("CREATE TABLE CUSTOMER (" +
                                                "CUSTOMER_ID VARCHAR(10)," +
                                                "FIRST_NAME VARCHAR(30)," +
                                                "LAST_NAME VARCHAR (20)," +
                                                "ADDRESS VARCHAR (60)," +
                                                "CITY VARCHAR(40)," +
                                                "STATE CHAR (2)," +
                                                "ZIPCODE CHAR (5)," +
                                                "PHONE_NUMBER CHAR (12))");

            // --------------------------------
            // Create table RECEIPT
            // --------------------------------
            dropTable("RECEIPT", stat); // drop if table exist
            stat.execute("CREATE TABLE RECEIPT (" +
                                                "RECEIPT_ID VARCHAR(10)," +
                                                "PURCHASE_DATE DATE," +
                                                "CUSTOMER_ID VARCHAR(10)" +
                                                ")");

            // --------------------------------
            // Create table RECEIPT_DETAIL
            // --------------------------------
            dropTable("RECEIPT_DETAIL", stat); // DROP IF TABLE EXIST
            stat.execute("CREATE TABLE RECEIPT_DETAIL (" +
                                                "RECEIPT_ID VARCHAR(10)," +
                                                "PRODUCT_ID VARCHAR(10)," +
                                                "QUANTITY INT," +
                                                "UNIT_PRICE DECIMAL(10,2)" +
                                                ")");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Drop a table
     *
     * @param tableName
     * @param statement
     * @Author Yen Vo
     */
    private static void dropTable(String tableName, Statement statement) {
        try {
            statement.execute("DROP TABLE " + tableName); // if the table exists, drop it.
            // This makes sure that you do not have the existing table
        } catch (SQLException ignoredEx) {
            // Catch exception if table doesn't exist yet.
            // But, do nothing (it was thrown because the table does not exist, and you will create one in the next line).
        }
    }
}