package org.educa.homelyBackend.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtParser jwtParser;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain
    ) {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            try {
                filterChain.doFilter(request, response);
            } catch (IOException | ServletException e) {
                throw ExceptionUtil.manageException(e, HttpStatus.INTERNAL_SERVER_ERROR, "Error procesando la petición");
            }
            return;
        }

        String token = header.substring(BEARER_PREFIX.length()).trim();

        try {
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();

            String email = claims.getSubject();

            if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw ExceptionUtil.manageException(HttpStatus.UNAUTHORIZED, "Token con formato de email inválido").get();
            }

            String role = claims.get("role", String.class);

            if (role == null || role.isBlank()) {
                throw ExceptionUtil.manageException(HttpStatus.UNAUTHORIZED, "Token sin claim 'role'").get();
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticate(email, role, request);
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            throw ExceptionUtil.manageException(e, HttpStatus.UNAUTHORIZED, "Token expirado");
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw ExceptionUtil.manageException(e, HttpStatus.UNAUTHORIZED, "Token inválido");
        } catch (IOException | ServletException e) {
            throw ExceptionUtil.manageException(e, HttpStatus.INTERNAL_SERVER_ERROR, "Error procesando el token");
        }
    }

    private void authenticate(String email, String role, HttpServletRequest request) {
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
        );

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, null, authorities);

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
