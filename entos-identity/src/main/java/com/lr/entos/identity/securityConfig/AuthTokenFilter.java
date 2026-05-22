package com.lr.entos.identity.securityConfig;

import com.lr.entos.infra.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // Skip JWT validation for auth and swagger paths
        return path.startsWith("/v1/auth/") ||
                path.startsWith("/api/swagger-ui/") ||
                path.startsWith("api/v3/api-docs");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException{
        String jwt = parseJwt(request);

        if (jwt != null && jwtUtils.validateToken(jwt)){
            String username = jwtUtils.extractEmail(jwt);
            String roleName = String.valueOf(jwtUtils.extractRole(jwt));
            UUID guid = jwtUtils.extractGuid(jwt);

            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleName));
            var userDetails = new CustomUserDetails(guid,username,"",authorities);
            var auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request,response);
    }

    private String parseJwt(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return (StringUtils.hasText(header) && header.startsWith("Bearer ")) ? header.substring(7) : null;
    }
}
