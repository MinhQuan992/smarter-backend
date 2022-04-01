package com.example.smarterbackend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.smarterbackend.framework.common.constant.SecurityConstants;
import com.example.smarterbackend.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtUtils {
  public static String generateJwt(User user) {
    Algorithm algorithm =
        Algorithm.HMAC256(SecurityConstants.SECRET.getBytes(StandardCharsets.UTF_8));
    return JWT.create()
        .withSubject(user.getUsername())
        .withIssuer(SecurityConstants.ISSUER)
        .withIssuedAt(new Date(System.currentTimeMillis()))
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .withClaim(
            "roles",
            user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()))
        .sign(algorithm);
  }

  private static DecodedJWT decodeJwt(String token) {
    Algorithm algorithm = Algorithm.HMAC256(SecurityConstants.SECRET.getBytes());
    JWTVerifier jwtVerifier = JWT.require(algorithm).build();
    return jwtVerifier.verify(token);
  }

  public static String getUsernameFromJwt(String token) {
    DecodedJWT decodedJWT = decodeJwt(token);
    return decodedJWT.getSubject();
  }

  public static Collection<SimpleGrantedAuthority> getAuthoritiesFromJwt(String token) {
    DecodedJWT decodedJWT = decodeJwt(token);
    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

    return authorities;
  }
}
