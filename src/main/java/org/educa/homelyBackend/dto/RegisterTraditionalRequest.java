package org.educa.homelyBackend.dto;

public record RegisterTraditionalRequest(String name, String email, String password, String confirmedPassword) {
}