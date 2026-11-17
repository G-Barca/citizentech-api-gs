package br.com.fiap.citizentech_api.service;

import br.com.fiap.citizentech_api.dto.OnibusDto;
import br.com.fiap.citizentech_api.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech_api.exception.RegraNegocioException;
import br.com.fiap.citizentech_api.model.Onibus;
import br.com.fiap.citizentech_api.model.StatusOnibus;
import br.com.fiap.citizentech_api.repository.OnibusRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OnibusService {

    private final OnibusRepository repository;

    public OnibusService(OnibusRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<OnibusDto.Response> listar(StatusOnibus status, Pageable pageable) {
        Page<Onibus> pagina = (status == null)
                ? repository.findAll(pageable)
                : repository.findByStatus(status, pageable);
        return pagina.map(OnibusDto.Response::de);
    }

    @Transactional(readOnly = true)
    public OnibusDto.Response buscar(Long id) {
        return OnibusDto.Response.de(obter(id));
    }

    @Transactional
    public OnibusDto.Response criar(OnibusDto.Request req) {
        String placa = normalizar(req.placa());
        if (repository.existsByPlaca(placa)) {
            throw new RegraNegocioException("Ja existe um onibus com esta placa " + placa + ".");
        }
        Onibus onibus = new Onibus();
        onibus.setPlaca(placa);
        onibus.setStatus(req.status());
        return OnibusDto.Response.de(repository.save(onibus));
    }

    @Transactional
    public OnibusDto.Response atualizar(Long id, OnibusDto.Request req) {
        Onibus onibus = obter(id);
        String placa = normalizar(req.placa());
        if (repository.existsByPlacaAndIdNot(placa, id)) {
            throw new RegraNegocioException("Ja existe outro onibus com esta placa " + placa + ".");
        }
        onibus.setPlaca(placa);
        onibus.setStatus(req.status());
        return OnibusDto.Response.de(repository.save(onibus));
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(obter(id));
        repository.flush();
    }

    public Onibus obter(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Onibus " + id + " nao encontrado."));
    }

    private String normalizar(String placa) {
        return placa.trim().toUpperCase();
    }
}