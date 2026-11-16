package br.com.fiap.citizentech_api.dto;


import br.com.fiap.citizentech_api.model.PontoOnibus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.hateoas.server.core.Relation;

public final class PontoDto {

    private PontoDto() {
    }

    /** O que o app ENVIA (com validacao). */
    public record Request(
            @NotBlank(message = "Endereco obrigatorio")
            @Size(max = 50, message = "Endereco deve ter no maximo 50 caracteres")
            String endereco,

            @NotNull(message = "Numero obrigatorio")
            @Positive(message = "O numero deve ser maior que zero")
            Integer numero) {
    }

    @Relation(collectionRelation = "pontos", itemRelation = "ponto")
    public record Response(Long id, String endereco, Integer numero) {
        public static Response de(PontoOnibus p) {
            return new Response(p.getId(), p.getEndereco(), p.getNumero());
        }
    }
}