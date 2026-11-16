package br.com.fiap.citizentech_api.repository;

import br.com.fiap.citizentech_api.model.Linha;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinhaRepository extends JpaRepository<Linha, Long> {
    Page<Linha> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}