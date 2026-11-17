package br.com.fiap.citizentech_api.controller;

import br.com.fiap.citizentech_api.dto.LinhaDto;
import br.com.fiap.citizentech_api.dto.RegistroDto;
import br.com.fiap.citizentech_api.service.LinhaService;
import br.com.fiap.citizentech_api.service.RegistroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pontos/{pontoId}")
@Tag(name = "Consultas do cidadao")
public class PontoConsultaController {

    private final LinhaService linhaService;
    private final RegistroService registroService;

    public PontoConsultaController(LinhaService linhaService, RegistroService registroService) {
        this.linhaService = linhaService;
        this.registroService = registroService;
    }

    @GetMapping("/linhas")
    public List<LinhaDto.Response> linhasDoPonto(@PathVariable Long pontoId) {
        return linhaService.linhasDoPonto(pontoId);
    }

    @GetMapping("/chegadas")
    public PagedModel<RegistroDto.Response> chegadas(
            @PathVariable Long pontoId,
            @PageableDefault(size = 5) Pageable pageable) {
        return Paginas.toModel(registroService.ultimasChegadas(pontoId, pageable), dto -> dto);
    }
}