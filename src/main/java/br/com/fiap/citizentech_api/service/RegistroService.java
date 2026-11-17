package br.com.fiap.citizentech_api.service;

import br.com.fiap.citizentech_api.dto.RegistroDto;
import br.com.fiap.citizentech_api.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech_api.exception.RegraNegocioException;
import br.com.fiap.citizentech_api.model.Operacao;
import br.com.fiap.citizentech_api.model.PontoOnibus;
import br.com.fiap.citizentech_api.model.Registro;
import br.com.fiap.citizentech_api.repository.LinhaPontoRepository;
import br.com.fiap.citizentech_api.repository.RegistroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistroService {

    private final RegistroRepository repository;
    private final LinhaPontoRepository linhaPontoRepository;
    private final PontoService pontoService;
    private final OperacaoService operacaoService;

    public RegistroService(RegistroRepository repository,
                           LinhaPontoRepository linhaPontoRepository,
                           PontoService pontoService,
                           OperacaoService operacaoService) {
        this.repository = repository;
        this.linhaPontoRepository = linhaPontoRepository;
        this.pontoService = pontoService;
        this.operacaoService = operacaoService;
    }

    @Transactional(readOnly = true)
    public Page<RegistroDto.Response> listar(Long pontoId, Pageable pageable) {
        Page<Registro> pagina = (pontoId == null)
                ? repository.findAll(pageable)
                : repository.findByPontoId(pontoId, pageable);
        return pagina.map(RegistroDto.Response::de);
    }

    @Transactional(readOnly = true)
    public RegistroDto.Response buscar(Long id) {
        return RegistroDto.Response.de(obter(id));
    }

    @Transactional
    public RegistroDto.Response criar(RegistroDto.Request req) {
        PontoOnibus ponto = pontoService.obter(req.pontoId());
        Operacao operacao = operacaoService.obter(req.operacaoId());

        if (!req.dataChegada().toLocalDate().equals(operacao.getDataOperacao())) {
            throw new RegraNegocioException("A chegada deve ocorrer na data da operacao.");
        }
        if (!linhaPontoRepository.existsByLinhaIdAndPontoId(operacao.getLinha().getId(), ponto.getId())) {
            throw new RegraNegocioException("O ponto informado nao pertence a linha da operacao.");
        }

        Registro registro = new Registro();
        registro.setDataChegada(req.dataChegada());
        registro.setPonto(ponto);
        registro.setOperacao(operacao);
        return RegistroDto.Response.de(repository.save(registro));
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(obter(id));
        repository.flush();
    }

    private Registro obter(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Registro " + id + " nao encontrado."));
    }
}