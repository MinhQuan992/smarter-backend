package com.example.smarterbackend.framework.common.constant;

public class SecurityConstants {
  public static final String SECRET = "SECRET_KEY";
  public static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 1 week
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING = "Authorization";
  public static final String LOG_IN_URL = "/smarter/api/v1/users/login";
  public static final String SIGN_UP_URL = "/smarter/api/v1/users/signup";
}
