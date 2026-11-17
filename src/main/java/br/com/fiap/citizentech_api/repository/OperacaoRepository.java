package br.com.fiap.citizentech_api.repository;

import br.com.fiap.citizentech_api.model.Operacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface OperacaoRepository
        extends JpaRepository<Operacao, Long>, JpaSpecificationExecutor<Operacao> {
    @Query("select count(o) from Operacao o "
            + "where o.onibus.id = :onibusId and o.dataOperacao = :data "
            + "and o.horaInicio < :fim and o.horaFim > :inicio")
    long contarConflitos(@Param("onibusId") Long onibusId,
                         @Param("data") LocalDate data,
                         @Param("inicio") Integer inicio,
                         @Param("fim") Integer fim);
}