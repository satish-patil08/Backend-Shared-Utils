package com.microservices.shared_utils.constants;

import java.util.HashMap;

public class GlobalConstants {
    public static final String DEFAULT_DB_URL;
    public static final String DEFAULT_DB_URL_CLIENTS;
    public static final String ENVIRONMENT = System.getenv("ENV");

    static {
        DEFAULT_DB_URL = "mongodb+srv://sypatil0803:Pos%402262@satishpatil.tx0oz.mongodb.net/springBoot?retryWrites=true&w=majority&appName=SatishPatil";
        DEFAULT_DB_URL_CLIENTS = "mongodb+srv://sypatil0803:Pos%402262@satishpatil.tx0oz.mongodb.net/springBootDefault?retryWrites=true&w=majority&appName=SatishPatil";
    }

    public static final String CLIENT_SERVICE_BASE_URL = "http://localhost:7002/clients/get-mongo-url?clientId=";
    public static final String CLIENT_HEADER = "X-Client";
    public static HashMap<Long, String> CLIENT_DB_CONNECTIONS = new HashMap<>();
    public static final String CLIENT_REPLACEMENT = "springBootDefault";

    public static String generateClientsMongoURL(String clientId) {
        return "http://localhost:7002/clients/get-mongo-url?clientId=" + clientId;
    }

}