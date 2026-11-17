package br.com.fiap.citizentech_api.dto;

import br.com.fiap.citizentech_api.model.Operacao;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

public final class OperacaoDto {

    private OperacaoDto() {
    }

    public record Request(
            @NotNull(message = "Data da operacao e obrigatoria")
            LocalDate dataOperacao,

            @NotNull(message = "Hora de inicio e obrigatoria")
            @Min(value = 0, message = "Hora de inicio deve ser entre 0 e 1439 (minutos)")
            @Max(value = 1439, message = "Hora de inicio deve ser entre 0 e 1439 (minutos)")
            Integer horaInicio,

            @NotNull(message = "Hora de fim e obrigatoria")
            @Min(value = 1, message = "Hora de fim deve ser entre 1 e 1440 (minutos)")
            @Max(value = 1440, message = "Hora de fim deve ser entre 1 e 1440 (minutos)")
            Integer horaFim,

            @NotNull(message = "linhaId e obrigatorio")
            Long linhaId,

            @NotNull(message = "onibusId e obrigatorio")
            Long onibusId) {
    }

    @Relation(collectionRelation = "operacoes", itemRelation = "operacao")
    public record Response(Long id, LocalDate dataOperacao, Integer horaInicio, Integer horaFim,
                           String inicio, String fim,
                           Long linhaId, String linhaNome, Long onibusId, String placa) {

        public static Response de(Operacao o) {
            return new Response(o.getId(), o.getDataOperacao(), o.getHoraInicio(), o.getHoraFim(),
                    hhmm(o.getHoraInicio()), hhmm(o.getHoraFim()),
                    o.getLinha().getId(), o.getLinha().getNome(),
                    o.getOnibus().getId(), o.getOnibus().getPlaca());
        }
        private static String hhmm(int minutos) {
            return String.format("%02d:%02d", minutos / 60, minutos % 60);
        }
    }
}