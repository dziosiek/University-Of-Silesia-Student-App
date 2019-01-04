package com.example.adam.universityofsilesiaapp.variables;

public abstract class GlobalVariables {
    private static String API_URL = "http://192.168.1.104:8080";
    private static Integer userId = null;
    private static String token= null;

    public static String getApiUrl() {
        return API_URL;
    }

    public static void setApiUrl(String apiUrl) {
        API_URL = apiUrl;
    }

    public static Integer getUserId() {
        return userId;
    }

    public static void setUserId(Integer userId) {
        GlobalVariables.userId = userId;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        GlobalVariables.token = token;
    }
}

