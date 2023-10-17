package com.example.visites.configs;

public class AppConstants {
    public static final String[] PUBLIC_GET_URLS = { "/v3/api-docs", "/v2/api-docs", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
    "/configuration/**", "/swagger-resources", "/swagger-resources/**","/webjars/**", "v3/**", "/users/profile/**", "favicon.ico" };
    public static final String[] PUBLIC_POST_URLS = { "/register/**", "/login", "/register" };
    public static final Long ADMIN_ID = 101L;
    public static final Long USER_ID = 102L;

    public static final String CHARACTERS_ALLOWED_FOR_PASSWORD = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@-*+_?";

    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=\\-_!])(?=\\S+$).{8,}$";

    public static final String DEFAULT_PATH = "pictures";

    public static final String DEFAULT_IMAGE = "pictures/default.png";

    /* Default values of visit attibuts */
    public static final String[] type = {"ordinaire", "rendez-vous"};
    public static final String[] status = {"En Attente", "En cours", "Passe"};
}
