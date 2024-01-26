import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestClient {
    private static String connectionString;
    private static boolean verbose = false;

    public static void main(String[] args) {
        if (args.length == 0)  {
            System.out.println("Usage: TestClient \"<connectionString>\" [-v]");
            System.out.println("Connection string format: \"jdbc:postgresql://host:port/database?user=USER&password=PASSWORD\"");
            System.exit(1);
        }
    	connectionString = args[0];

	if ( args.length > 1 ) 
		verbose = args[1].equals("-v");

	showInfo("Args: connectString:" + connectionString + " verbose is " + verbose);
        showInfo("Running workload ...");
        runworkload();
    }

    public static void showInfo(String msg){
	    showInfo(msg,null);
    }
    public static void showInfo(String msg, Throwable e){
	    System.out.println(msg);
	    if (verbose && (e!=null)) e.printStackTrace(System.out);
    }

    public static void runworkload(){

        Connection connection = null;
        connection = getConnection(0L);
        
        while (true) {
            
            try {
                
                while (true) {
                    insertData(connection);
                }

            } catch (SQLException e) {
                showInfo("Connection lost reconnecting ...",e);
                connection = getConnection(System.currentTimeMillis());

            }
        }

    }
    public static Connection getConnection(long starttime) {
        while (true){
            try {
                Connection c = DriverManager.getConnection(connectionString);
                if (starttime>0){
                    long endTime = System.currentTimeMillis();
                    showInfo("Reconnected in " + (endTime - starttime) + " ms");
                }
                return c;
            } catch (SQLException e) {
		    if (verbose) showInfo("failed to get connection",e);
            }
        }
    }

    public static void insertData(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO ping (timestamp) SELECT now()");
        statement.close();
    }
}
