package core;

import infoobject.WikiArticle;

import java.sql.*;
import java.util.logging.Logger;

/**
 * This class will manage the connection to the used sqlite db
 */
public class Database {
    private static final Database singletonInstance = new Database();
    Connection c = null;
    private Logger log = Logger.getGlobal();

    /**
     * initialises the Database to get access to it.
     */
    private Database() {

        try {
            //Load the classes
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:InfoObjects.db");

            //prepare the tables, delete all old stuff and refill it.
            Statement stat = c.createStatement();
            stat.executeUpdate("DROP TABLE IF  EXISTS ARTICLES;");
            stat.executeUpdate("CREATE TABLE ARTICLES (TITLE, PARSED);");

        } catch (Exception e) {
            log.severe("Database failed to create without errors.");
            System.exit(-1);
        }
        log.fine("Database successfully createds");
    }

    /**
     * Gives you access to the Database manager
     *
     * @return Database
     */
    public static Database getDatabase() {
        return singletonInstance;
    }

    /**
     * adds an article to the database.
     * This will check, if the article is already added.
     *
     * @param article
     */
    public synchronized void insertArticleIntoDatabase(WikiArticle article) {
        log.finer("Adding article [" + article.toString() + "] to database.");
        try {
            //Check if already in list
            PreparedStatement prep = c.prepareStatement("SELECT * FROM ARTICLES WHERE TITLE = ?;");
            prep.setString(1, article.getTitle());
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                log.info("Article [" + article.toString() + "] was already in database.");
                return;
            }

            //insert it
            prep = c.prepareStatement(
                    "INSERT INTO ARTICLES values (?, 0);");
            prep.setString(1, article.getTitle());
            prep.executeUpdate();
            log.finer("Article [" + article.toString() + "] successful added to database.");
        } catch (SQLException e) {
            log.severe("Could not insert article [\" + article.toString() + \"] into database.");
        }
    }

    /**
     * Searches a not parsed title and puts it as parsed.
     *
     * @return
     */
    public synchronized WikiArticle getNextArticle() {
        log.finer("Requesting next article from Database.");
        WikiArticle a = null;
        try {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM ARTICLES WHERE PARSED = 0 LIMIT 1");

            if (rs.next()) {
                a = new WikiArticle(rs.getString("TITLE"));
                log.finest("Next article is [" + a.getTitle() + "].");
                st.executeUpdate("UPDATE ARTICLES SET PARSED = 1 WHERE TITLE = '" + a.getTitle() + "';");
                log.finer("Updated article [\" + a.getTitle() + \"].");
            }
        } catch (SQLException e) {
            log.severe("Could not get next article from database.");
        }
        return a;
    }
}
