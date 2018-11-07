import db.EntityDbContext;
import db.base.DbContext;
import entities.Department;
import entities.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * !RUN APP
 * Enter number from 1 to 4 for chose dbContext methods. For delete and find by id methods must enter additional input.
 * FOR PERSIST enter 1
 * FOR DELETE first must enter 2 and after change string in WHERE CONSTANT.
 * FOR FIND enter 3.
 * FOR FIND BY ID first enter 4 and after that enter valid id from database.
 * <p>
 * IN this case working only with table employees. If you want to change table must call  chooseDbContextMethods(departmentDbContext, getScanner(), department);
 */
public class App {

    private static final String CONNECTION_STRING =
            "jdbc:mysql://localhost:3306/soft_uni_simple";
    private static final String WHERE = "last_name LIKE ('I%')";

    public static void main(String[] args) throws SQLException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        Connection connection = getConnection();
        Scanner scanner = getScanner();

        DbContext<User> usersDbContext =
                getDbContext(connection, User.class);

        User user = new User("Petia", "Ilieva");

        DbContext<Department> departmentDbContext =
                getDbContext(connection, Department.class);

        Department department = new Department("IT", "Bulgaria");

        /**
         *  ! Enter number from 1 to 4 for chose dbContext methods. For delete and find by id methods must enter additional input.
         */
        chooseDbContextMethods(usersDbContext, getScanner(), user);
        // chooseDbContextMethods(departmentDbContext, getScanner(), department);

        connection.close();
    }


    private static <T> void chooseDbContextMethods(DbContext<T> dbContext, Scanner scanner, T entity) throws SQLException, IllegalAccessException, InstantiationException {
        switch (Integer.parseInt(scanner.nextLine())) {
            case 1:
                dbContext.persist(entity);
                break;
            case 2:
                dbContext.delete(WHERE);
                break;
            case 3:
                dbContext.find().forEach(System.out::println);
            case 4:
                int id = Integer.parseInt(getScanner().nextLine());
                T entity1 = dbContext.findById(id);
                System.out.println(entity1);
                break;
            default:
                System.out.println("Invalided operation!");
                break;


        }

    }

    private static <T> DbContext<T> getDbContext(Connection connection, Class<T> klass) throws SQLException {
        return new EntityDbContext<>(connection, klass);
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                CONNECTION_STRING,
                "root",
                ""
        );
    }

    private static Scanner getScanner() {

        return new Scanner(System.in);
    }

}
