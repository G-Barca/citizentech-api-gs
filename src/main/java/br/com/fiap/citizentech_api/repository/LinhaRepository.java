package br.com.fiap.citizentech_api.repository;

import br.com.fiap.citizentech_api.model.Linha;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LinhaRepository extends JpaRepository<Linha, Long> {
    Page<Linha> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    @Query("select l from Linha l join l.pontos lp "
            + "where lp.ponto.id = :pontoId order by l.nome")
    List<Linha> buscarPorPonto(@Param("pontoId") Long pontoId);
}