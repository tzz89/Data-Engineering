package HBase.hbase.com.zuozhe;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

public class HBaseConnection {
    public static Connection connection = null;
    // Static block will be run when the class is first used
    {
        try {
            connection = ConnectionFactory.createConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection() {
        HBaseConnection.connection.close();
    }

    public static void main(String[] args) {

        // Run program
        System.out.println(HBaseConnection.connection);

        // Close connection
        HBaseConnection.closeConnection();
    }
}
