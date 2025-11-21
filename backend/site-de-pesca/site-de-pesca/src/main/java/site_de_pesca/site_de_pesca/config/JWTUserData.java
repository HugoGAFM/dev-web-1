package site_de_pesca.site_de_pesca.config;

import lombok.Builder;

@Builder
public record JWTUserData(Long userId, String email) {

}
