package br.com.fiap.citizentech_api.service;

import br.com.fiap.citizentech_api.dto.LinhaDto;
import br.com.fiap.citizentech_api.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech_api.exception.RegraNegocioException;
import br.com.fiap.citizentech_api.model.Linha;
import br.com.fiap.citizentech_api.model.LinhaPonto;
import br.com.fiap.citizentech_api.model.PontoOnibus;
import br.com.fiap.citizentech_api.repository.LinhaPontoRepository;
import br.com.fiap.citizentech_api.repository.LinhaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LinhaService {

    private final LinhaRepository repository;
    private final LinhaPontoRepository linhaPontoRepository;
    private final PontoService pontoService;

    public LinhaService(LinhaRepository repository,
                        LinhaPontoRepository linhaPontoRepository,
                        PontoService pontoService) {
        this.repository = repository;
        this.linhaPontoRepository = linhaPontoRepository;
        this.pontoService = pontoService;
    }

    @Transactional(readOnly = true)
    public Page<LinhaDto.Response> listar(String nome, Pageable pageable) {
        Page<Linha> pagina = (nome == null || nome.isBlank())
                ? repository.findAll(pageable)
                : repository.findByNomeContainingIgnoreCase(nome, pageable);
        return pagina.map(LinhaDto.Response::de);
    }

    @Transactional(readOnly = true)
    public LinhaDto.Response buscar(Long id) {
        return LinhaDto.Response.de(obter(id));
    }

    @Transactional
    public LinhaDto.Response criar(LinhaDto.Request req) {
        Linha linha = new Linha();
        preencher(linha, req);
        return LinhaDto.Response.de(repository.save(linha));
    }

    @Transactional
    public LinhaDto.Response atualizar(Long id, LinhaDto.Request req) {
        Linha linha = obter(id);
        preencher(linha, req);
        return LinhaDto.Response.de(repository.save(linha));
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(obter(id));
        repository.flush();
    }

    @Transactional(readOnly = true)
    public List<LinhaDto.PontoResponse> listarPontos(Long linhaId) {
        obter(linhaId);
        return linhaPontoRepository.findByLinhaIdOrderBySequenciaAsc(linhaId).stream()
                .map(LinhaDto.PontoResponse::de)
                .toList();
    }

    @Transactional
    public LinhaDto.PontoResponse adicionarPonto(Long linhaId, LinhaDto.PontoRequest req) {
        Linha linha = obter(linhaId);
        PontoOnibus ponto = pontoService.obter(req.pontoId());

        if (linhaPontoRepository.existsByLinhaIdAndPontoId(linhaId, ponto.getId())) {
            throw new RegraNegocioException("Esse ponto ja faz parte da linha.");
        }
        if (linhaPontoRepository.existsByLinhaIdAndSequencia(linhaId, req.sequencia())) {
            throw new RegraNegocioException("Ja existe um ponto nessa posicao da linha.");
        }

        LinhaPonto linhaPonto = new LinhaPonto();
        linhaPonto.setLinha(linha);
        linhaPonto.setPonto(ponto);
        linhaPonto.setSequencia(req.sequencia());
        return LinhaDto.PontoResponse.de(linhaPontoRepository.save(linhaPonto));
    }

    @Transactional(readOnly = true)
    public List<LinhaDto.Response> linhasDoPonto(Long pontoId) {
        pontoService.obter(pontoId);
        return repository.buscarPorPonto(pontoId).stream()
                .map(LinhaDto.Response::de)
                .toList();
    }

    public Linha obter(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Linha " + id + " nao encontrada."));
    }

    private void preencher(Linha linha, LinhaDto.Request req) {
        linha.setNome(req.nome());
        linha.setDescricao(req.descricao());
        linha.setIntervaloMinutos(req.intervaloMinutos());
    }
}