package com.onerty.yeogi.customer.security;

import com.onerty.yeogi.customer.auth.JwtTokenProvider;
import com.onerty.yeogi.customer.auth.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomerUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   CustomerUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;

    }

    // 회원 전용 경로 관리
    private boolean isProtectedPath(String path) {
        return path.startsWith("/api/users") ||
                path.startsWith("/api/reservation") ||
                path.startsWith("/api/payment");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (!isProtectedPath(path)) {
            // 비회원 경로면 바로 통과
            filterChain.doFilter(request, response);
            return;
        }

        String token = JwtUtil.removeBearerPrefix(request.getHeader("Authorization"));
        if (token != null && jwtTokenProvider.isValidToken(token)) {
            String userId = jwtTokenProvider.getUserIdFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("🛡️ 인증 유저: {}"+ userDetails.getUsername());
            System.out.println("🪪 권한 목록: {}"+ userDetails.getAuthorities());

        }
        filterChain.doFilter(request, response);
    }
}
