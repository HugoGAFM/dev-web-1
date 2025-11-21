package site_de_pesca.site_de_pesca.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record RegisterUserRequest(@NotEmpty(message = "Usuario é obrigatório") String username, 
@NotEmpty(message = "E-mail é obrigatório") String email, 
@NotEmpty(message = "Senha é obrigatorio") String password,
@NotEmpty(message = "Papel é obrigatorio") String role,
@NotEmpty(message = "Nome é obrigatorio") String nome,
@NotEmpty(message = "Sobrenome é obrigatorio") String sobrenome) {

}
