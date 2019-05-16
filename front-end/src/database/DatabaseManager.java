package database;

import java.sql.*;

public class DatabaseManager {

    public Connection connectToDatabase(Database database) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(database.getUrl());
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }

        return connection;

    }

    public void crateNewTable(Database database, String data) {
        try (
            Connection connection = connectToDatabase(database);
            Statement statement = connection.createStatement()
        ) {
            statement.execute(data);
        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public void insertTable(Database database, String tableName, String data, String values) {
        try(
                Connection connection = connectToDatabase(database);
                Statement statement = connection.createStatement()
        ) {
            statement.execute("INSERT INTO " + tableName + "(" + data + ") VALUES (" + values + ");");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteTable(Database db, int id) {
        String sql = "DELETE FROM " + db.getDatabaseName() + " WHERE id = ?";

        try (
                Connection connection = this.connectToDatabase(db);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateTable(Database db, int id, String name, double capacity) {
        String sql = "UPDATE " + db.getDatabaseName() + " SET name = ? , " + "capacity = ? " + "WHERE id = ?";

        try(
                Connection connection = this.connectToDatabase(db);
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, capacity);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void selectAll(Database db) {
        String sql = "SELECT id, name, capacity FROM " + db.getDatabaseName();

        try (
                Connection connection = this.connectToDatabase(db);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {
                System.out.println(
                        resultSet.getInt("id") +  "\t" +
                                resultSet.getString("name") + "\t" +
                                resultSet.getDouble("capacity")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
