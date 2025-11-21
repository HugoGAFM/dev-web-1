package site_de_pesca.site_de_pesca.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(@NotEmpty(message = "Email é obrigatorio") String email, 
@NotEmpty(message = "Senha é obrigatória") String password) {

}
