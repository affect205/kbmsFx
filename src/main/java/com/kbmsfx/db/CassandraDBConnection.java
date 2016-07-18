package com.kbmsfx.db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import javax.inject.Singleton;

/**
 * Created by abalyshev on 18.07.16.
 */
@Singleton
public class CassandraDBConnection implements IDBConnection {

    Cluster cluster;
    Session session;

    private static final String KEYSPACE = "kbmsfx";

    public CassandraDBConnection() throws Exception {
        System.out.println("init CassandraDBConnection...");
        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = cluster.connect(KEYSPACE);
    }

    @Override
    public void initializeDB() {

    }

    @Override
    public Session getConnection() throws Exception {
        return session == null || session.isClosed() ? createSession() : session;
    }

    public void close(Cluster cluster) {
        if (cluster != null) cluster.close();
    }

    private Session createSession() throws Exception {
        session = cluster.connect(KEYSPACE);
        return session;
    }
}
