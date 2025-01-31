package com.microservices.shared_utils.threadLocals;

import static com.microservices.shared_utils.constants.GlobalConstants.DEFAULT_DB_URL;
import static com.microservices.shared_utils.constants.GlobalConstants.DEFAULT_DB_URL_CLIENTS;

public class MongoConnectionStorage {

    private static final ThreadLocal<String> storage = new ThreadLocal<>();

    public static String getConnection(Boolean clients) {
        if (clients) {
            if (storage.get() == null) return DEFAULT_DB_URL_CLIENTS;
            return storage.get();
        } else return getConnection();
    }

    public static String getConnection() {
        if (storage.get() == null) return DEFAULT_DB_URL;
        return storage.get();
    }

    public static void setConnection(final String connectionString) {
        storage.set(connectionString);
    }

    public static void clear() {
        storage.remove();
        AuthTokenStorage.clear();
    }

}
