package br.com.fiap.citizentech_api.repository;

import br.com.fiap.citizentech_api.model.PontoOnibus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PontoOnibusRepository extends JpaRepository<PontoOnibus, Long> {
    Page<PontoOnibus> findByEnderecoContainingIgnoreCase(String endereco, Pageable pageable);
}