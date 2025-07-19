package com.stitch.gateway.util;

public final class Constants {
    private Constants() {
    }

    public static final String BASE_URL = "/api/v1";
    public static final String EMPTY_STRING = "";

    public static final String[] ALLOWED_URLS = {
            "/altair",
            "/actuator/health",
            "/graphql",
            "/vendor/**",
            "/api/v1/create-customer",
            "/api/v1/get-users",
            "/api/v1/customer-login",
            "/api/v1/verify-email"
    };
}
