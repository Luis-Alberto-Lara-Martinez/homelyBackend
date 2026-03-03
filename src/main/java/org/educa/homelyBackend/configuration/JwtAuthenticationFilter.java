package org.educa.homelyBackend.configuration;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtParser jwtParser;

    public JwtAuthenticationFilter(
            @Value("${jwt.secret.key}") String secretKey,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.audience}") String audience) {
        this.jwtParser = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .requireIssuer(issuer)
                .requireAudience(audience)
                .build();
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();
            authenticate(claims, request);
        } catch (ExpiredJwtException e) {
            handleUnauthorized(response, "Token expirado");
            return;
        } catch (JwtException | IllegalArgumentException e) {
            handleUnauthorized(response, "Token inválido");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(AUTH_HEADER);
        if (header == null || !header.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())) {
            return null;
        }

        String token = header.substring(BEARER_PREFIX.length()).trim();
        return token.isEmpty() ? null : token;
    }

    private void authenticate(Claims claims, HttpServletRequest request) {
        String email = claims.getSubject();
        if (email == null || email.isBlank() || SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }

        String role = claims.get("role", String.class);
        List<GrantedAuthority> authorities = buildAuthorities(role);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, null, authorities);

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private List<GrantedAuthority> buildAuthorities(String role) {
        return role == null || role.isBlank()
                ? List.of()
                : List.of(new SimpleGrantedAuthority("ROLE_" + role.trim().toUpperCase()));
    }

    private void handleUnauthorized(HttpServletResponse response, String reason) throws IOException {
        if (response.isCommitted()) return;

        SecurityContextHolder.clearContext();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write("""
                {
                    "error": "No autenticado",
                    "reason": "%s"
                }
                """.formatted(reason));

        response.getWriter().flush();
    }
}