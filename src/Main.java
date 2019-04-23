import database.Database;
import database.DatabaseManager;

public class Main {

    public static Database database = new Database("movies");

    public static void main(String[] args) {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.connectToDatabase(database);
    }
}
