package exercises_db_apps;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Engine implements Runnable {

    private Connection connection;

    public Engine(Connection connection) {
        this.connection = connection;
    }

    public void run() {
        try {
            this.increaseMinionsAge(); // here after this.   put the name of the method we call
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

/*
Problem 2 -- getVillainNames
 */

    private void getVillainsNames() throws SQLException {
        String query = "select v.name,count(v2.minion_id) from villains as v join minions_villains v2 on v.id = v2.villain_id group by v.name having count(v2.minion_id) >? order by count(v2.minion_id) desc";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);

        preparedStatement.setInt(1, 15);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println(String.format("%s %d",
                    resultSet.getString(1),
                    resultSet.getInt(2)));
        }
    }




    /*
  Problem 3 -- getMinionsName
   */

    private void getMinionsName() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        String query = "SELECT \n" +
                "    v.id,\n" +
                "    v.name,\n" +
                "    v.evilness_factor,\n" +
                "    m.id,\n" +
                "    m.name,\n" +
                "    m.age,\n" +
                "    m.town_id\n" +
                "FROM\n" +
                "    villains AS v\n" +
                "        LEFT JOIN\n" +
                "    minions_villains AS mv ON mv.villain_id = v.id\n" +
                "        LEFT JOIN\n" +
                "    minions AS m ON mv.minion_id = m.id\n" +
                "WHERE\n" +
                "    v.id = ?";

        PreparedStatement statement = connection.prepareStatement(query);

        int id = Integer.parseInt(scanner.nextLine());
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        int count = 0;
        while (resultSet.next()) {
            if (count == 0) {
                System.out.println("Villain: " + resultSet.getString("name"));
                count++;
            }
            String nameOfMinion = resultSet.getString("m.name");
            String ageOfMinion = resultSet.getString("m.age");
            if (nameOfMinion == null || ageOfMinion == null) {
                continue;
            }
            System.out.println(String.format("%d. %s %s", count++, nameOfMinion, ageOfMinion));
        }
        if (count == 0) {
            System.out.println(String.format("No villain with ID %d exists in the database.", id));
        }
    }



    /*
    Problem 4 -- addMinion
     */

    private void addMinion() throws SQLException {

        Scanner scanner = new Scanner(System.in);


        String[] minionParams = scanner.nextLine().split("\\s+");
        String[] villainParams = scanner.nextLine().split("\\s+");

        String minionName = minionParams[1];
        int minionAge = Integer.parseInt(minionParams[2]);
        String minionTown = minionParams[3];
        String villainName = villainParams[1];

        PreparedStatement preparedStatement =
                insertIntoVillains(minionTown,
                        "SELECT * FROM towns WHERE `name` = ?",
                        "INSERT INTO towns(name) VALUES (?)",
                        "Town %s was added to the database.%n");
        ResultSet townsResultSet;

        townsResultSet = preparedStatement.executeQuery();
        townsResultSet.next();
        int townId = townsResultSet.getInt("id");

        PreparedStatement villainSelectStatement =
                insertIntoVillains(villainName,
                        "SELECT * FROM villains WHERE `name` = ?",
                        "INSERT INTO villains(name, evilness_factor) VALUES (?, 'evil')",
                        "Villain %s was added to the database.%n");
        ResultSet villainsResultSet;

        villainsResultSet = villainSelectStatement.executeQuery();
        villainsResultSet.next();
        int villainId = villainsResultSet.getInt("id");

        insertMinions(minionName, minionAge, townId);

        PreparedStatement getMinionId =
                connection.prepareStatement("SELECT id FROM minions WHERE `name` = ?");
        getMinionId.setString(1, minionName);
        ResultSet minionsIdResultSet = getMinionId.executeQuery();
        minionsIdResultSet.next();
        int minionId = minionsIdResultSet.getInt("id");

        connectMinionsToVillains(minionName, villainName, villainId, minionId);

        minionsIdResultSet.close();
        townsResultSet.close();
        villainsResultSet.close();

        connection.close();
    }

    private PreparedStatement insertIntoVillains(String villainName, String s, String s2, String s3) throws SQLException {
        PreparedStatement villainInsert =
                connection.prepareStatement(s);
        villainInsert.setString(1, villainName);
        ResultSet villainRS = villainInsert.executeQuery();

        insertTowns(villainName, villainRS, s2, s3);
        return villainInsert;
    }

    private void connectMinionsToVillains(String minionName, String villainName, int villainId, int minionId) throws SQLException {
        PreparedStatement connectMinionToVillain =
                connection.prepareStatement("INSERT INTO minions_villains (minion_id, villain_id) VALUES(?, ?)");
        connectMinionToVillain.setInt(1, minionId);
        connectMinionToVillain.setInt(2, villainId);
        connectMinionToVillain.executeUpdate();

        System.out.printf("Successfully added %s to be minion of %s%n", minionName, villainName);
    }

    private void insertTowns(String minionTown, ResultSet townsResultSet, String s, String s2) throws SQLException {
        if (!townsResultSet.isBeforeFirst()) {
            PreparedStatement insertTownStatement =
                    connection.prepareStatement(s);
            insertTownStatement.setString(1, minionTown);
            insertTownStatement.executeUpdate();

            System.out.printf(s2, minionTown);
        }
    }

    private void insertMinions(String minionName, int minionAge, int townId) throws SQLException {
        PreparedStatement insertMinion =
                connection.prepareStatement
                        ("INSERT INTO minions(`name`, age, town_id) VALUES(?, ?, ?)");
        insertMinion.setString(1, minionName);
        insertMinion.setInt(2, minionAge);
        insertMinion.setInt(3, townId);
        insertMinion.executeUpdate();
    }



   /*
   Problem 5 -- Change Town Names Casing
    */

    private void changeTownNames() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        String countryName = scanner.nextLine();

        String query = "UPDATE towns\n" +
                "SET `name` = UPPER(`name`)\n" +
                "WHERE country = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, countryName);
        int affectedRows = preparedStatement.executeUpdate();

        List<String> updatedNames = new ArrayList<>();
        updateTownNamesMethod(countryName, affectedRows, updatedNames);

        preparedStatement.closeOnCompletion();
        connection.close();
    }

    private void updateTownNamesMethod(String countryName, int affectedRows, List<String> updatedNames) throws SQLException {
        if (affectedRows != 0) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT name FROM towns WHERE country = ?");
            preparedStatement.setString(1, countryName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String townName = resultSet.getString("name");
                updatedNames.add(townName);
            }

            preparedStatement.closeOnCompletion();
            resultSet.close();

            System.out.println(affectedRows + " town names were affected.");
            System.out.println(String.join(", ", updatedNames));
        } else {
            System.out.println("No town names were affected.");
        }
    }



    /*
    Problem 6 -- Remove Villain - task with stars, not mandatory
     */

    private void removeVillains() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String villainName;
        int villainsId = Integer.parseInt(scanner.nextLine());

        ResultSet resultSet = selectVillainStatements(villainsId);

        if (!resultSet.isBeforeFirst()) {
            System.out.println("No such villain was found");
        } else {
            resultSet.next();
            villainName = resultSet.getString("name");

            PreparedStatement releaseMinionsStatement =
                    connection.prepareStatement("DELETE mv FROM minions_villains AS mv WHERE mv.villain_id = ?");
            releaseMinionsStatement.setInt(1, villainsId);
            int affectedRows = releaseMinionsStatement.executeUpdate();

            PreparedStatement removeVillainStatement = removeVillainStatements(villainName, villainsId, affectedRows);

            releaseMinionsStatement.closeOnCompletion();
            removeVillainStatement.closeOnCompletion();
        }

        scanner.close();
        connection.close();
    }

    private ResultSet selectVillainStatements(int villainsId) throws SQLException {
        PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT name FROM villains WHERE id = ?");
        preparedStatement.setInt(1, villainsId);
        return preparedStatement.executeQuery();
    }

    private PreparedStatement removeVillainStatements(String villainName, int villainsId, int affectedRows) throws SQLException {
        PreparedStatement preparedStatement =
                connection.prepareStatement("DELETE FROM villains WHERE id = ?");
        preparedStatement.setInt(1, villainsId);
        preparedStatement.executeUpdate();

        System.out.println(villainName + " was deleted");
        System.out.println(affectedRows + " minions released");
        return preparedStatement;
    }



    /*
     Problem 7 -- Print All Minion Names
     */

    private void printAllMinionNames() throws SQLException {

        List<String> minionNames = new ArrayList<>();
        PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT name FROM minions");

        ResultSet resultSet = preparedStatement.executeQuery();

        printAllMinionNamesMethod(minionNames, resultSet);

        connection.close();
    }

    private void printAllMinionNamesMethod(List<String> minionNames, ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            do {
                String name = resultSet.getString("name");
                minionNames.add(name);
            } while (resultSet.next());
        }

        IntStream.range(0, minionNames.size() / 2).forEachOrdered(i -> {
            System.out.println(minionNames.get(i));
            System.out.println(minionNames.get(minionNames.size() - 1 - i));
        });
    }



    /*
    Problem 8 -- Increase Minions Age
     */

    private void increaseMinionsAge() throws SQLException {

        Scanner scanner = new Scanner(System.in);

        int[] minionsId = new int[10];
        int count = 0;
        for (String s : scanner.nextLine().split("\\s+")) {
            int i = Integer.parseInt(s);
            if (minionsId.length == count) minionsId = Arrays.copyOf(minionsId, count * 2);
            minionsId[count++] = i;
        }
        minionsId = Arrays.copyOfRange(minionsId, 0, count);

        PreparedStatement updateAgesStatement =
                connection.prepareStatement("UPDATE minions SET age = age + 1 WHERE id = ?");

        checkMinionsId(minionsId, updateAgesStatement);

        PreparedStatement printAllMinionsStatement =
                connection.prepareStatement("SELECT name, age FROM minions");
        ResultSet resultSet = printAllMinionsStatement.executeQuery();

        checkMinionsName(resultSet);

        scanner.close();
        updateAgesStatement.closeOnCompletion();
        printAllMinionsStatement.closeOnCompletion();
        resultSet.close();
    }

    private void checkMinionsName(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            do {
                String name = resultSet.getString("name");
                String age = resultSet.getString("age");
                System.out.println(name + " " + age);
            } while (resultSet.next());
        }
    }

    private void checkMinionsId(int[] minionsId, PreparedStatement updateAgesStatement) throws SQLException {
        for (int i = 0, minionsIdLength = minionsId.length; i < minionsIdLength; i++) {
            int id = minionsId[i];
            updateAgesStatement.setInt(1, id);
            updateAgesStatement.executeUpdate();
        }
    }



    /*
    Problem 9 -- Increase Age Stored Procedure
    // for this task you need to create stored procedure in yours database before run this method
     */

    private void increaseAgeStoredProcedures() throws SQLException {

        Scanner scanner = new Scanner(System.in);

        int inputId = Integer.parseInt(scanner.nextLine());

        increaseAgeМethod(inputId);

        getMinionIdMethod(inputId);

        scanner.close();
        connection.close();
    }

    private void getMinionIdMethod(int id) throws SQLException {
        PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT name, age FROM minions WHERE id = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        System.out.printf("%s %s%n", resultSet.getString("name"), resultSet.getString("age"));
    }

    private void increaseAgeМethod(int id) throws SQLException {
        CallableStatement increaseAgeStatement =
                connection.prepareCall("{CALL usp_get_older(?)}");
        increaseAgeStatement.setInt(1, id);
        increaseAgeStatement.execute();
    }
}








