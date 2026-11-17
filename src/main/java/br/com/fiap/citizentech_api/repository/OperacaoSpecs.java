package br.com.fiap.citizentech_api.repository;

import br.com.fiap.citizentech_api.model.Operacao;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class OperacaoSpecs {

    private OperacaoSpecs() {
    }
    public static Specification<Operacao> comFiltros(LocalDate data, Long linhaId, Long onibusId) {
        return (root, query, cb) -> {
            List<Predicate> filtros = new ArrayList<>();
            if (data != null) {
                filtros.add(cb.equal(root.get("dataOperacao"), data));
            }
            if (linhaId != null) {
                filtros.add(cb.equal(root.get("linha").get("id"), linhaId));
            }
            if (onibusId != null) {
                filtros.add(cb.equal(root.get("onibus").get("id"), onibusId));
            }
            return cb.and(filtros.toArray(new Predicate[0]));
        };
    }
}