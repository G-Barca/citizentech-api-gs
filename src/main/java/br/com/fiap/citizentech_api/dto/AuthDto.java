package br.com.fiap.citizentech_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AuthDto {

    private AuthDto() {
    }

    public record LoginRequest(
            @NotBlank(message = "Login e obrigatorio") String login,
            @NotBlank(message = "Senha e obrigatoria") String senha) {
    }

    public record CadastroRequest(
            @NotBlank(message = "Login e obrigatorio")
            @Size(min = 3, max = 50, message = "Login deve ter entre 3 e 50 caracteres")
            String login,

            @NotBlank(message = "Senha e obrigatoria")
            @Size(min = 6, max = 72, message = "Senha deve ter entre 6 e 72 caracteres")
            String senha) {
    }

    public record UsuarioResponse(Long id, String login, String papel) {
    }

    public record TokenResponse(String token, String tipo, String papel) {
    }
}