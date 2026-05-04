package org.educa.homelyBackend.services.shared;

public interface JwtService {
    String generatePersonalizedJwt(String email, String role);
}
