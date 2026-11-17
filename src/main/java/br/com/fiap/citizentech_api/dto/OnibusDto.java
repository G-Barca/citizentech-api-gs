package br.com.fiap.citizentech_api.dto;

import br.com.fiap.citizentech_api.model.Onibus;
import br.com.fiap.citizentech_api.model.StatusOnibus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.hateoas.server.core.Relation;

public final class OnibusDto {

    private OnibusDto() {
    }

    public record Request(
            @NotBlank(message = "Placa obrigatoria")
            @Size(max = 10, message = "A Placa deve ter no maximo 10 caracteres")
            String placa,

            @NotNull(message = "Status e obrigatorio")
            StatusOnibus status) {
    }

    @Relation(collectionRelation = "onibus", itemRelation = "onibus")
    public record Response(Long id, String placa, StatusOnibus status) {
        public static Response de(Onibus o) {
            return new Response(o.getId(), o.getPlaca(), o.getStatus());
        }
    }
}