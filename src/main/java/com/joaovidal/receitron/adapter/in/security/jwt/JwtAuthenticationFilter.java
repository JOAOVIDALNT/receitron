package com.joaovidal.receitron.adapter.in.security.jwt;

import com.joaovidal.receitron.adapter.out.persistence.repository.UserRepository;
import com.joaovidal.receitron.domain.model.User;
import com.joaovidal.receitron.domain.port.out.TokenProviderPort;
import com.joaovidal.receitron.domain.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
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

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String userEmail = tokenProvider.extractUserName(jwt);


            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {

                UserDetails user = this.userRepository.findByEmail(userEmail).get();

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
