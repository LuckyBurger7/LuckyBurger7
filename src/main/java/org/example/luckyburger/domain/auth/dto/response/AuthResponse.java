package org.example.luckyburger.domain.auth.dto.response;

public record AuthResponse(String accessToken) {
    public static AuthResponse of(String accessToken) {
        return new AuthResponse(accessToken);
    }
}
