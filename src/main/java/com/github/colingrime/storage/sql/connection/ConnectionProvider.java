package com.github.colingrime.storage.sql.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public interface ConnectionProvider {

    /**
     * @return name of the provider
     */
    String getName();

    /**
     * Initializes the connection provider.
     */
    void init();

    /**
     * Shuts down the connection provider.
     */
    void shutdown();

    /**
     * @return the connection of the provider
     * @throws SQLException connection failed
     */
    Connection getConnection() throws SQLException;

    /**
     * @return the statement processor of the provider
     */
    Function<String, String> getStatementProcessor();
}
