package br.com.fiap.citizentech_api.repository;

import br.com.fiap.citizentech_api.model.Onibus;
import br.com.fiap.citizentech_api.model.StatusOnibus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnibusRepository extends JpaRepository<Onibus, Long> {
    Page<Onibus> findByStatus(StatusOnibus status, Pageable pageable);
    boolean existsByPlaca(String placa);
    boolean existsByPlacaAndIdNot(String placa, Long id);
}