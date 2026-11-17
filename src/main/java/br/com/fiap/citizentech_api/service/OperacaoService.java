package br.com.fiap.citizentech_api.service;

import br.com.fiap.citizentech_api.dto.OperacaoDto;
import br.com.fiap.citizentech_api.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech_api.exception.RegraNegocioException;
import br.com.fiap.citizentech_api.model.Linha;
import br.com.fiap.citizentech_api.model.Onibus;
import br.com.fiap.citizentech_api.model.Operacao;
import br.com.fiap.citizentech_api.model.StatusOnibus;
import br.com.fiap.citizentech_api.repository.OperacaoRepository;
import br.com.fiap.citizentech_api.repository.OperacaoSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class OperacaoService {

    private final OperacaoRepository repository;
    private final LinhaService linhaService;
    private final OnibusService onibusService;

    public OperacaoService(OperacaoRepository repository,
                           LinhaService linhaService,
                           OnibusService onibusService) {
        this.repository = repository;
        this.linhaService = linhaService;
        this.onibusService = onibusService;
    }

    @Transactional(readOnly = true)
    public Page<OperacaoDto.Response> listar(LocalDate data, Long linhaId, Long onibusId, Pageable pageable) {
        return repository.findAll(OperacaoSpecs.comFiltros(data, linhaId, onibusId), pageable)
                .map(OperacaoDto.Response::de);
    }

    @Transactional(readOnly = true)
    public OperacaoDto.Response buscar(Long id) {
        return OperacaoDto.Response.de(obter(id));
    }

    @Transactional
    public OperacaoDto.Response criar(OperacaoDto.Request req) {
        if (req.horaFim() <= req.horaInicio()) {
            throw new RegraNegocioException("A hora final deve ser maior que a hora inicial.");
        }
        Linha linha = linhaService.obter(req.linhaId());
        Onibus onibus = onibusService.obter(req.onibusId());

        if (onibus.getStatus() != StatusOnibus.DISPONIVEL) {
            throw new RegraNegocioException("O onibus " + onibus.getPlaca() + " nao esta DISPONIVEL.");
        }
        if (repository.contarConflitos(onibus.getId(), req.dataOperacao(),
                req.horaInicio(), req.horaFim()) > 0) {
            throw new RegraNegocioException("O onibus ja possui outra operacao nesse horario.");
        }

        Operacao operacao = new Operacao();
        operacao.setDataOperacao(req.dataOperacao());
        operacao.setHoraInicio(req.horaInicio());
        operacao.setHoraFim(req.horaFim());
        operacao.setLinha(linha);
        operacao.setOnibus(onibus);
        return OperacaoDto.Response.de(repository.save(operacao));
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(obter(id));
        repository.flush();
    }

    public Operacao obter(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Operacao " + id + " nao encontrada."));
    }
}