package br.com.fiap.citizentech_api.service;



import br.com.fiap.citizentech_api.dto.PontoDto;
import br.com.fiap.citizentech_api.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech_api.model.PontoOnibus;
import br.com.fiap.citizentech_api.repository.PontoOnibusRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@Service
public class PontoService {

    private final PontoOnibusRepository repository;

    public PontoService(PontoOnibusRepository repository) {
        this.repository = repository;
    }

    @Cacheable("pontos")
    @Transactional(readOnly = true)
    public Page<PontoDto.Response> listar(String endereco, Pageable pageable) {
        Page<PontoOnibus> pagina = (endereco == null || endereco.isBlank())
                ? repository.findAll(pageable)
                : repository.findByEnderecoContainingIgnoreCase(endereco, pageable);
        return pagina.map(PontoDto.Response::de);
    }

    @Transactional(readOnly = true)
    public PontoDto.Response buscar(Long id) {
        return PontoDto.Response.de(obter(id));
    }

    @CacheEvict(cacheNames = "pontos", allEntries = true)
    @Transactional
    public PontoDto.Response criar(PontoDto.Request req) {
        PontoOnibus ponto = new PontoOnibus();
        preencher(ponto, req);
        return PontoDto.Response.de(repository.save(ponto));
    }

    @CacheEvict(cacheNames = "pontos", allEntries = true)
    @Transactional
    public PontoDto.Response atualizar(Long id, PontoDto.Request req) {
        PontoOnibus ponto = obter(id);
        preencher(ponto, req);
        return PontoDto.Response.de(repository.save(ponto));
    }

    @CacheEvict(cacheNames = "pontos", allEntries = true)
    @Transactional
    public void excluir(Long id) {
        repository.delete(obter(id));
        repository.flush(); // forca o DELETE para um erro de FK aparecer aqui
    }

    public PontoOnibus obter(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new RecursoNaoEncontradoException("Ponto " + id + " nao encontrado."));
    }

    private void preencher(PontoOnibus ponto, PontoDto.Request req) {
        ponto.setEndereco(req.endereco());
        ponto.setNumero(req.numero());
    }
}