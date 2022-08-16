import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Class manage Customers - add customers, search customer, report customers
 *
 * @Author: Yen Vo
 */
public class CustomerManager {

    /**
     * Initialize data for CUSTOMER
     */
    public static void initializeData() {
        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {
            // Reading " Customer.csv"
            Scanner in = new Scanner(new File("Customer.csv"));
            in.nextLine(); // skip header (column names)
            // reading the csv file line by line
            while (in.hasNextLine()) {
                String line = in.nextLine();
                // CSV file separates values by commas. The line is partitioned into multiple values
                String[] fields = line.split(",");
                if(fields.length == 8) {
                    String customerID = fields[0];
                    String firstName = fields[1];
                    String lastName = fields[2];
                    String address = fields[3];
                    String city = fields[4];
                    String state = fields[5];
                    String zipcode = fields[6];
                    String phoneNumber = fields[7];
                    // Create insert query for CUSTOMER
                    String query = createQueryInsertCustomer(
                            customerID,
                            firstName,
                            lastName,
                            address,
                            city,
                            state,
                            zipcode,
                            phoneNumber);
                    stat.execute(query);
                }
            }
            in.close();
        } catch (SQLException | FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Print all CUSTOMERS
     */
    public static void printAllCustomers() {
        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {

            // Prepare the SQL statement to select all products
            String query = "SELECT *  FROM CUSTOMER";
            ResultSet result = stat.executeQuery(query);
            // Print Headers
            System.out.printf("%-10s %-15s %-15s %-40s %-15s %-10s %-10s %-12s\n"
                    , "CustomerId", "FirstName", "LastName", "Address", "City", "State", "ZipCode", "PhoneNumber");
            // Print Header line
            System.out.printf("%-10s %-15s %-15s %-40s %-15s %-10s %-10s %-12s\n",
                    "----------", "---------", "---------", "-------------", "---------", "------", "--------", "------------");
            while (result.next()) {
                String customerId = result.getString("CUSTOMER_ID");
                String firstName = result.getString("FIRST_NAME");
                String lastName = result.getString("LAST_NAME");
                String address = result.getString("ADDRESS");
                String city = result.getString("CITY");
                String state = result.getString("STATE");
                String zipcode = result.getString("ZIPCODE");
                String phone = result.getString("PHONE_NUMBER");

                System.out.printf("%-10s %-15s %-15s %-40s %-15s %-10s %-10s %-12s\n"
                        , customerId, firstName, lastName, address, city, state, zipcode, phone);
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Add CUSTOMER
     */
    public static void addCustomer() {
        Scanner console = new Scanner(System.in);
        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {
            System.out.println("Enter New Customer:\n");
            // Read ID
            System.out.print("Customer ID: ");
            String customerID = console.nextLine();
            // Read First name
            System.out.print("First Name: ");
            String firstName = console.nextLine();
            // Read First name
            System.out.print("Last Name:  ");
            String lastName = console.nextLine();
            // Read Address
            System.out.print("Address: ");
            String address = console.nextLine();
            // Read City
            System.out.print("City: ");
            String city = console.nextLine();
            // Read State
            System.out.print("State: ");
            String state = console.nextLine();
            // Read Zipcode
            System.out.print("Zipcode: ");
            String zipcode = console.nextLine();
            // Read Phone number
            System.out.print("Phone Number: ");
            String phoneNumber = console.nextLine();

            // Create insert query for CUSTOMER
            String query = createQueryInsertCustomer(
                    customerID,
                    firstName,
                    lastName,
                    address,
                    city,
                    state,
                    zipcode,
                    phoneNumber);
            stat.execute(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void searchCustomerByName() {

        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {
            System.out.println("***********Search Customer by name************");
            Scanner console = new Scanner(System.in);
            System.out.print("Enter Search Name: ");
            String searchName = console.nextLine();
            // Prepare the SQL statement in String format to search customer by name
            String query = "SELECT *  FROM CUSTOMER " +
                    "WHERE FIRST_NAME LIKE '%" + searchName + "%' " +
                    "OR  LAST_NAME LIKE '%" + searchName + "%'";
            ResultSet result = stat.executeQuery(query);
            // Print Headers
            System.out.printf("%-10s %-15s %-15s %-40s %-15s %-10s %-10s %-12s\n"
                    , "CustomerId", "FirstName", "LastName", "Address", "City", "State", "ZipCode", "PhoneNumber");
            // Print Header line
            System.out.printf("%-10s %-15s %-15s %-40s %-15s %-10s %-10s %-12s\n",
                    "----------", "---------", "---------", "-------------", "---------", "------", "--------", "------------");
            boolean found = false;
            while (result.next()) {
                found = true;
                String customerId = result.getString("CUSTOMER_ID");
                String firstName = result.getString("FIRST_NAME");
                String lastName = result.getString("LAST_NAME");
                String address = result.getString("ADDRESS");
                String city = result.getString("CITY");
                String state = result.getString("STATE");
                String zipcode = result.getString("ZIPCODE");
                String phone = result.getString("PHONE_NUMBER");

                System.out.printf("%-10s %-15s %-15s %-40s %-15s %-10s %-10s %-12s\n"
                        , customerId, firstName, lastName, address, city, state, zipcode, phone);
            }
            result.close();
            if(!found){
                System.out.println("Customer name ["+searchName+"] is not found ");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static String createQueryInsertCustomer(String customerID,
                                           String firstName,
                                           String lastName,
                                           String address,
                                           String city,
                                           String state,
                                           String zipcode,
                                           String phoneNumber) {

        // Prepare the SQL statement in String format with quotes to insert the CUSTOMER
        String query = "INSERT INTO CUSTOMER" +
                "(CUSTOMER_ID, FIRST_NAME, LAST_NAME, ADDRESS, CITY, STATE, ZIPCODE, PHONE_NUMBER)" +
                " VALUES (" +
                        "'" + customerID + "'," +
                        "'" + firstName + "'," +
                        "'" + lastName + "'," +
                        "'" + address + "'," +
                        "'" + city + "'," +
                        "'" + state + "'," +
                        "'" + zipcode + "'," +
                        "'" + phoneNumber + "')";
        return query;
    }
}
