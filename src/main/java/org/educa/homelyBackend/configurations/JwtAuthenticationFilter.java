package org.educa.homelyBackend.configurations;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
            @Value("${jwt.secret.key}")
            String secretKey,

            @Value("${jwt.issuer}")
            String issuer,

            @Value("${jwt.audience}")
            String audience
    ) {
        this.jwtParser = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .requireIssuer(issuer)
                .requireAudience(audience)
                .build();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/oauth2/");
    }

    @Override
    protected void doFilterInternal(
            @NonNull
            HttpServletRequest request,

            @NonNull
            HttpServletResponse response,

            @NonNull
            FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader(AUTH_HEADER);

        if (header == null || !header.toLowerCase().startsWith(BEARER_PREFIX.toLowerCase())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(BEARER_PREFIX.length()).trim();

        try {
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();
            String email = claims.getSubject();

            if (email != null && !email.isBlank() && SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticate(claims, request);
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            handleUnauthorized(response, e.getMessage(), "Expired token");
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            handleUnauthorized(response, e.getMessage(), "Invalid or corrupted token");
        } catch (Exception e) {
            handleUnauthorized(response, e.getMessage(), "Unknown error authenticating the jwt");
        }
    }

    private void authenticate(Claims claims, HttpServletRequest request) {
        String role = claims.get("role", String.class);

        List<SimpleGrantedAuthority> authorities = (role == null || role.isBlank())
                ? List.of()
                : List.of(new SimpleGrantedAuthority("ROLE_%s".formatted(role.trim().toUpperCase())));

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void handleUnauthorized(HttpServletResponse response, String error, String explanation) throws IOException {
        SecurityContextHolder.clearContext();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        String json = """
                {
                    "error": "%s",
                    "reason": "Not authenticated",
                    "explanation": "%s"
                }
                """.formatted(error, explanation);

        response.getWriter().write(json);
    }
}
