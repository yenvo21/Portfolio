import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * This program reads sample HTML files and provides the location information.
 * @author Yen Vo- Look up and extract information from htm file
 * @author Samuel Prisco- reading html file
 */
public class LocationLookUp {
    public static void main(String[] args) throws IOException {
        Scanner console = new Scanner(System.in);
        boolean more = true;
        while (more) {
            System.out.println("Lookup by L)ocation, or Q)uit");
            String cmd = console.nextLine();

            if (cmd.equalsIgnoreCase("Q")) { // Q option is COMPLETE. Do not modify.
                more = false;
            } else if (cmd.equalsIgnoreCase("L")) {
                System.out.println("Enter full Location name:");
                String name = console.nextLine(); // stores the user input
                Scanner ins;

                String inputFileName = name + ".html";
                FileReader inputFile = new FileReader(inputFileName);
                ins = new Scanner(inputFile);

                String address = ""; // store address
                String streetAddress="";
                String addressLocality = "";
                String addressRegion = "";
                String postalCode = "";
                String hour = ""; // store the store's hour
                String phoneNumber = ""; // store phone number


                while (ins.hasNextLine()) {
                    String line = ins.nextLine();
                    if (line.contains("\"PostalAddress\"")) {
                        // Extract streetAddress
                        int idx1 = line.indexOf("\"streetAddress\":"); //length = 16
                        int idx2 = line.indexOf("\",\"", idx1);
                        streetAddress = line.substring(idx1 + 17, idx2).trim();
                        // Extract addressLocality
                        int idx3 = line.indexOf("addressLocality\":"); // length = 17
                        int idx4 = line.indexOf("\",\"", idx3);
                        addressLocality = line.substring(idx3 + 18, idx4).trim();
                        // Extract address Region
                        int idx5 = line.indexOf("addressRegion\":"); // length = 15
                        int idx6 = line.indexOf("\",\"", idx5);
                        addressRegion = line.substring(idx5 + 16, idx6).trim();
                       // Extract postalCode
                        int idx7 = line.indexOf("postalCode\":"); // length = 13
                        int idx8 = line.indexOf("\"},");
                        postalCode = line.substring(idx7 + 14, idx8).trim();
                    }

                    //	Extract store's hour
                    if (line.contains("<div class=\"bold\">Hours</div>")) {
                        line = ins.nextLine();// read next line
                       int idx1 = line.indexOf("<p>");
                       int idx2 = line.indexOf("</p>");
                       hour = line.substring(idx1 + 3 , idx2);

                    }
                    // Extract store's phone number
                    if (line.contains("<div class=\"bold\">Phone</div>")) {
                        line = ins.nextLine(); // skips 1 line
                        int idx1 = line.indexOf("<p>");
                        int idx2 = line.indexOf ("<",idx1 +1);
                        phoneNumber = line.substring(idx1 + 3, idx2);
                    }
                } // end while

                ins.close();
                System.out.println("Location Name: " + name);
                System.out.println("Address: " + streetAddress + ", " + addressLocality + ", " +
                        addressRegion+ ", " + postalCode);
                System.out.println("Hours: " + hour);
                System.out.println("Phone Number: " + phoneNumber);
                System.out.println();
            }
            else{
                System.out.println("Invalid choice!");
            }
        }
    }
}

