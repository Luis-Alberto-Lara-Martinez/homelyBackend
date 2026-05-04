package org.educa.homelyBackend.services.shared;

public interface RandomTokenService {
    String generateRandomToken();

    String generateHashedRandomToken(String token);
}
