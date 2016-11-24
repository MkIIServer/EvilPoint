package tw.mics.spigot.plugin.evilpoint.data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import tw.mics.spigot.plugin.evilpoint.EvilPoint;

public class Database {
    private EvilPoint plugin;
    
    private File dbfile;
    Connection db_conn;
    
    public Database(EvilPoint p, File dataFolder){
        this.plugin = p;
        dbfile = new File(dataFolder, "database.db");
        this.initDatabase();
    }
    
    public Connection getConnection(){
        return db_conn;
    }
    
    private void initDatabase() {
        try {
          Class.forName("org.sqlite.JDBC");
          db_conn = DriverManager.getConnection("jdbc:sqlite:"+dbfile.getPath());
          db_conn.setAutoCommit(false);
       
          //新增表格
          Statement stmt = db_conn.createStatement();
          String sql = "CREATE TABLE IF NOT EXISTS PLAYER_EVIL_POINT " +
                  "(UUID TEXT NOT NULL," +
                  " EVIL_POINT INTEGER NOT NULL)";
          stmt.executeUpdate(sql);
          sql = "CREATE UNIQUE INDEX IF NOT EXISTS EVIL_POINT_INDEX "
                  + "ON PLAYER_EVIL_POINT (UUID);";
          stmt.executeUpdate(sql);
          stmt.close();
          db_conn.commit();
          plugin.logDebug("Opened database successfully");
        } catch ( SQLException | ClassNotFoundException e ) {
            e.printStackTrace();
        }
    }
    
    public void close(){
        try {
            db_conn.close();
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }
}
