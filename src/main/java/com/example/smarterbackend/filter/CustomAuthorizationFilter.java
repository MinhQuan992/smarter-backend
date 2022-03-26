package com.example.smarterbackend.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.smarterbackend.framework.common.constant.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (request.getServletPath().equals(SecurityConstants.LOG_IN_URL)
        || request.getServletPath().equals(SecurityConstants.SIGN_UP_URL)) {
      filterChain.doFilter(request, response);
    } else {
      String authorizationHeader = request.getHeader(SecurityConstants.HEADER_STRING);
      if (authorizationHeader != null
          && authorizationHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
        try {
          String token = authorizationHeader.substring(SecurityConstants.TOKEN_PREFIX.length());
          Algorithm algorithm = Algorithm.HMAC256(SecurityConstants.SECRET.getBytes());
          JWTVerifier jwtVerifier = JWT.require(algorithm).build();
          DecodedJWT decodedJWT = jwtVerifier.verify(token);

          String username = decodedJWT.getSubject();
          String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

          Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
          Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

          UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(username, null, authorities);
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);

          filterChain.doFilter(request, response);
        } catch (Exception exception) {
          response.setStatus(HttpStatus.FORBIDDEN.value());
          Map<String, String> error = new HashMap<>();
          error.put("message", exception.getMessage());
          response.setContentType(MediaType.APPLICATION_JSON_VALUE);
          new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
      } else {
        filterChain.doFilter(request, response);
      }
    }
  }
}
