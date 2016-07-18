package com.kbmsfx.db;

/**
 * Created by abalyshev on 18.07.16.
 */
public interface IDBConnection {
    <T> T getConnection() throws Exception;
    void initializeDB() throws Exception;
}
