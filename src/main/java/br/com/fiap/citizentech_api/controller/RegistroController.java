package br.com.fiap.citizentech_api.controller;

import br.com.fiap.citizentech_api.dto.RegistroDto;
import br.com.fiap.citizentech_api.service.RegistroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/registros")
@Tag(name = "Registros de chegada")
public class RegistroController {

    private final RegistroService service;

    public RegistroController(RegistroService service) {
        this.service = service;
    }

    @GetMapping
    public PagedModel<RegistroDto.Response> listar(
            @RequestParam(required = false) Long pontoId,
            @PageableDefault(size = 10, sort = "dataChegada") Pageable pageable) {
        return Paginas.toModel(service.listar(pontoId, pageable), dto -> dto);
    }

    @GetMapping("/{id}")
    public RegistroDto.Response buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<RegistroDto.Response> criar(@Valid @RequestBody RegistroDto.Request req) {
        RegistroDto.Response criado = service.criar(req);
        return ResponseEntity.created(linkTo(methodOn(RegistroController.class).buscar(criado.id())).toUri())
                .body(criado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}