package com.github.colingrime.storage;

public record StorageCredentials(String host, int port, String database, String username,
                                 String password) {

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
