package exercises_db_apps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Application {
    public static void main(String[] args) throws SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String user = "root";
        String password = "";

        Properties properties = new Properties();

        properties.setProperty("user",user);
        properties.setProperty("password",password);

        Connection connection = DriverManager.getConnection
                ("jdbc:mysql://localhost:3306/minions_db",properties);

        Engine engine = new Engine(connection);
        engine.run ();
    }
}
