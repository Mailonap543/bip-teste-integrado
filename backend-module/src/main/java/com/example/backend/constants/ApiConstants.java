package com.example.backend.constants;

public final class ApiConstants {

    private ApiConstants() {}

    public static final String API_BASE_PATH = "/api/v1";
    public static final String AUTH_PATH = "/api/auth";

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    public static final String DEFAULT_SORT_FIELD = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
}
