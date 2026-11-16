package br.com.fiap.citizentech_api.dto;

import br.com.fiap.citizentech_api.model.Linha;
import br.com.fiap.citizentech_api.model.LinhaPonto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.hateoas.server.core.Relation;

public final class LinhaDto {

    private LinhaDto() {
    }

    public record Request(
            @NotBlank(message = "Nome e obrigatorio")
            @Size(max = 50, message = "Nome deve ter no maximo 50 caracteres")
            String nome,

            @NotBlank(message = "Descricao e obrigatoria")
            @Size(max = 100, message = "Descricao deve ter no maximo 100 caracteres")
            String descricao,

            @NotNull(message = "Intervalo e obrigatorio")
            @Positive(message = "Intervalo deve ser maior que zero")
            Integer intervaloMinutos) {
    }

    @Relation(collectionRelation = "linhas", itemRelation = "linha")
    public record Response(Long id, String nome, String descricao, Integer intervaloMinutos) {
        public static Response de(Linha l) {
            return new Response(l.getId(), l.getNome(), l.getDescricao(), l.getIntervaloMinutos());
        }
    }

    public record PontoRequest(
            @NotNull(message = "pontoId e obrigatorio")
            Long pontoId,

            @NotNull(message = "Sequencia e obrigatoria")
            @Positive(message = "Sequencia deve ser maior que zero")
            Integer sequencia) {
    }

    public record PontoResponse(Long pontoId, String endereco, Integer numero, Integer sequencia) {
        public static PontoResponse de(LinhaPonto lp) {
            return new PontoResponse(lp.getPonto().getId(), lp.getPonto().getEndereco(),
                    lp.getPonto().getNumero(), lp.getSequencia());
        }
    }
}