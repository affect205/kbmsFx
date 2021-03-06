package com.kbmsfx.db;

import org.sqlite.SQLiteConfig;

import javax.inject.Singleton;
import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 11.07.16
 */
@Singleton
public class SQLiteDBConnection implements IDBConnection {

    private Connection conn;
    private SQLiteConfig config;

    private static final String JDBC_SQLLITE_URI =  "jdbc:sqlite::resource:";
    private static final String KBMS_DB = "kbmsdb.s3db";

    public SQLiteDBConnection() throws Exception {
        System.out.println("init SQLiteDBConnection...");
        Class.forName("org.sqlite.JDBC");
    }

    @Override
    public void initializeDB() throws Exception {
        Statement statmt = conn.createStatement();
        statmt.execute("CREATE TABLE [category] (\n" +
                "[id] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "[name] TEXT NOT NULL,\n" +
                "[parent] INTEGER  NULL,\n" +
                "[order] INTEGER DEFAULT '1' NOT NULL,\n" +
                "UNIQUE(name, parent) ON CONFLICT REPLACE\n" +
                ");");
        statmt.execute("CREATE TABLE IF NOT EXISTS [notice] (\n" +
                "[id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[name] TEXT  NOT NULL,\n" +
                "[content] TEXT  NOT NULL,\n" +
                "[categoryid] INTEGER  NOT NULL\n" +
                ");");

        System.out.println("All tables has been initialized...");
    }

    @Override
    public Connection getConnection() throws Exception {
        if (conn == null || conn.isClosed()) return createConnection();
        return conn;
    }

    private Connection createConnection() throws Exception {
        return DriverManager.getConnection(JDBC_SQLLITE_URI + KBMS_DB);
    }
}
