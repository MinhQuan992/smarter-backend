package com.example.smarterbackend.filter;

import com.example.smarterbackend.framework.common.constant.SecurityConstants;
import com.example.smarterbackend.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authorizationHeader = request.getHeader(SecurityConstants.HEADER_STRING);

    if (authorizationHeader != null
        && authorizationHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
      try {
        String token = authorizationHeader.substring(SecurityConstants.TOKEN_PREFIX.length());
        String username = JwtUtils.getUsernameFromJwt(token);
        Collection<SimpleGrantedAuthority> authorities = JwtUtils.getAuthoritiesFromJwt(token);

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(
                username, null, authorities);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      } catch (Exception exception) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> error = new HashMap<>();
        error.put("message", exception.getMessage());

        new ObjectMapper().writeValue(response.getOutputStream(), error);
      }
    }
    filterChain.doFilter(request, response);
  }
}
