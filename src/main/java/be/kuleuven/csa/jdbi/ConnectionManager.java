package be.kuleuven.csa.jdbi;

import be.kuleuven.csa.MainDatabase;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private Connection connection;
    public static final String ConnectionString = "jdbc:sqlite:" + MainDatabase.DatabasePath;

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
        URL res = getClass().getClassLoader().getResource("dbcreate.sql");
        File file = Paths.get(res.toURI()).toFile();
        String absolutePath = file.getAbsolutePath();

        var sql = new String(Files.readAllBytes(Paths.get(absolutePath)));
        System.out.println(absolutePath);
        System.out.println(sql);

        var s = connection.createStatement();
        s.executeUpdate(sql);
        s.close();
    }


}
