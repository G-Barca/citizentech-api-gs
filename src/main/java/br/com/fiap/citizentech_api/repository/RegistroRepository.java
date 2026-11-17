package br.com.fiap.citizentech_api.repository;

import br.com.fiap.citizentech_api.model.Registro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroRepository extends JpaRepository<Registro, Long> {

    Page<Registro> findByPontoId(Long pontoId, Pageable pageable);
    Page<Registro> findByPontoIdOrderByDataChegadaDesc(Long pontoId, Pageable pageable);
}