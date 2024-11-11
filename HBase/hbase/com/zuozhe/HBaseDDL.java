package HBase.hbase.com.zuozhe;

import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;

public class HBaseDDL {
    public static Connection connection = HBaseConnection.connection;

    public static void createNamespace(String nameSpace) {
        // Get Admin class
        Admin admin = connection.getAdmin();

        // Create Builder - helper function to populate configuration for namespace
        NamespaceDescriptor.Builder builder = NamespaceDescriptor.create(nameSpace);
        // Add more properties
        builder.addConfiguration("user", "zuozhe");

        // Create namespace
        builder.createNamespace(builder.build());
    }

    public static void main(String[] args) {

    }
}
