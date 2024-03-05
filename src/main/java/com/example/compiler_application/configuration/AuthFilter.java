package com.example.compiler_application.configuration;

import com.example.compiler_application.entity.UserSession;
import com.example.compiler_application.repository.service.UserSessionRepositoryImplementation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final UserSessionRepositoryImplementation userSessionRepositoryImplementation;

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getCookies() != null) {
            String jwt = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("token"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
            String jit = null;
            if(jwt != null) {
                jit = jwtService.extractUsername(jwt);
            }

            UserSession session = userSessionRepositoryImplementation.findByUniqueId(jit).orElse(null);
            if(session == null) {
                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jit,jwt,null);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
