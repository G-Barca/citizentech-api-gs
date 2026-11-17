package br.com.fiap.citizentech_api.dto;

import br.com.fiap.citizentech_api.model.Registro;
import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

public final class RegistroDto {

    private RegistroDto() {
    }

    public record Request(
            @NotNull(message = "Data e hora da chegada são obrigatórias")
            LocalDateTime dataChegada,

            @NotNull(message = "pontoId obrigatorio")
            Long pontoId,

            @NotNull(message = "operacaoIdobrigatorio")
            Long operacaoId) {
    }

    @Relation(collectionRelation = "registros", itemRelation = "registro")
    public record Response(Long id, LocalDateTime dataChegada,
                           Long pontoId, String endereco, Long operacaoId) {
        public static Response de(Registro r) {
            return new Response(r.getId(), r.getDataChegada(),
                    r.getPonto().getId(), r.getPonto().getEndereco(), r.getOperacao().getId());
        }
    }
}