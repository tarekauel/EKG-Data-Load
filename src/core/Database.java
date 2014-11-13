package core;

import infoobject.WikiArticle;

import java.sql.*;

/**
 * This class will manage the connection to the used sqlite db
 */
public class Database {
    private static final Database singletonInstance = new Database();
    Connection c = null;

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
            System.out.println("DB-Connection failed!");
            System.exit(-1);
        }
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
        try {
            //Check if already in list
            PreparedStatement prep = c.prepareStatement("SELECT * FROM ARTICLES WHERE TITLE = ?;");
            prep.setString(1, article.getTitle());
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return;
            }

            //insert it
            prep = c.prepareStatement(
                    "INSERT INTO ARTICLES values (?, 0);");
            prep.setString(1, article.getTitle());
            prep.executeUpdate();
        } catch (SQLException e) {
            System.out.println("DB failed!(insert)");
            System.exit(-1);
        }
    }

    /**
     * Searches a not parsed title and puts it as parsed.
     *
     * @return
     */
    public synchronized WikiArticle getNextArticle() {
        WikiArticle a = null;
        try {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM ARTICLES WHERE PARSED = 0 LIMIT 1");

            if (rs.next()) {
                a = new WikiArticle(rs.getString("TITLE"));
                st.executeUpdate("UPDATE ARTICLES SET PARSED = 1 WHERE TITLE = '" + a.getTitle() + "';");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB failed!(get)");
            System.exit(-1);
        }
        return a;
    }
}
