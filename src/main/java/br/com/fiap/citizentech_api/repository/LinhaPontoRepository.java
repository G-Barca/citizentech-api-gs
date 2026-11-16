package br.com.fiap.citizentech_api.repository;

import br.com.fiap.citizentech_api.model.LinhaPonto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinhaPontoRepository extends JpaRepository<LinhaPonto, Long> {

    boolean existsByLinhaIdAndPontoId(Long linhaId, Long pontoId);

    boolean existsByLinhaIdAndSequencia(Long linhaId, Integer sequencia);

    List<LinhaPonto> findByLinhaIdOrderBySequenciaAsc(Long linhaId);
}
