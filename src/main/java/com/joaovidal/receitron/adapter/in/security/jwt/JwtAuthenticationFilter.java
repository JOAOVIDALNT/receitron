package com.joaovidal.receitron.adapter.in.security.jwt;

import com.joaovidal.receitron.adapter.in.exception.ApiException;
import com.joaovidal.receitron.adapter.out.persistence.repository.UserRepository;
import com.joaovidal.receitron.domain.port.out.TokenProviderPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${app.skip-auth:false}")
    private boolean skipAuth;

    private final TokenProviderPort tokenProvider;
    private final UserRepository userRepository;
    private final HandlerExceptionResolver exceptionResolver;

    public JwtAuthenticationFilter(TokenProviderPort tokenProvider, UserRepository userRepository, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.exceptionResolver = exceptionResolver;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        String path = request.getRequestURI();
        if (path.startsWith("/auth") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                skipAuth) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalid or not present");
            return;
        }


        try {
            final String jwt = authHeader.substring(7);
            final String userEmail = tokenProvider.extractUserName(jwt);


            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {

                UserDetails user = this.userRepository.findByEmail(userEmail).orElseThrow(
                        () -> new ApiException("Non authenticated user", HttpStatus.UNAUTHORIZED)
                );

                if (tokenProvider.isTokenValid(jwt)) {
                    var roles = tokenProvider.extractRoles(jwt);
                    var authorities = roles
                            .stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .toList();

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, authorities);

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            exceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
