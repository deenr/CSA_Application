package be.kuleuven.csa.jdbi;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private Connection connection;
    public static final String ConnectionString = "jdbc:sqlite:csa_database.db";

    public Connection getConnection() {
        return connection;
    }

    public void flushConnection() {
        try {
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ConnectionManager() {
        try {
            // auto-creates if not exists
            connection = DriverManager.getConnection(ConnectionString);
            connection.setAutoCommit(false);

            initTables();
        } catch (Exception e) {
            System.out.println("Db connection handle failure");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void initTables() throws Exception {
        var sql = new String(Files.readAllBytes(Paths.get("D:\\School\\Intellij\\CSA_Application\\src\\main\\resources\\dbcreate.sql")));
        System.out.println(sql);

        var s = connection.createStatement();
        s.executeUpdate(sql);
        s.close();
    }


}
