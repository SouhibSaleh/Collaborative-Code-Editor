package com.example.lastproject.filter;

import com.example.lastproject.model.User;
import com.example.lastproject.service.JwtUtil;
import com.example.lastproject.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private UserService userService;

    public JwtFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.endsWith("/login") || path.endsWith("/signup") || path.startsWith("/ws") || path.endsWith("/google");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) {

        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String email = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                email = JwtUtil.extractEmail(token);
            }
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.getUserByEmail(email);

                if (JwtUtil.validateToken(token, user)) {
                    System.out.println(user);
                    var passedToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                    );
                    passedToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(passedToken);
                }
                filterChain.doFilter(request, response);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}