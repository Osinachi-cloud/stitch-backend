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
            "/api/v1/verify-email",
            "/api/v1/validateEmailCode",
            "/api/v1/request-password-reset",
            "/api/v1/validate-reset-code",
            "/api/v1/get-all-products",
            "/api/v1/get-product-by-id",
            "/api/v1/addresses",
            "/api/v1/vendors",
            "/api/v1/vendor-details"
//            "/api/v1/fetch-customer-orders"
    };
}
