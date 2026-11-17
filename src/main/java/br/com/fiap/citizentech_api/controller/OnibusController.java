package br.com.fiap.citizentech_api.controller;


import br.com.fiap.citizentech_api.dto.OnibusDto;
import br.com.fiap.citizentech_api.model.StatusOnibus;
import br.com.fiap.citizentech_api.service.OnibusService;
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
@RequestMapping("/api/onibus")
@Tag(name = "Onibus")
public class OnibusController {

    private final OnibusService service;

    public OnibusController(OnibusService service) {
        this.service = service;
    }

    @GetMapping
    public PagedModel<OnibusDto.Response> listar(
            @RequestParam(required = false) StatusOnibus status,
            @PageableDefault(size = 10, sort = "placa") Pageable pageable) {
        return Paginas.toModel(service.listar(status, pageable), dto -> dto);
    }

    @GetMapping("/{id}")
    public OnibusDto.Response buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<OnibusDto.Response> criar(@Valid @RequestBody OnibusDto.Request req) {
        OnibusDto.Response criado = service.criar(req);
        return ResponseEntity.created(linkTo(methodOn(OnibusController.class).buscar(criado.id())).toUri())
                .body(criado);
    }

    @PutMapping("/{id}")
    public OnibusDto.Response atualizar(@PathVariable Long id, @Valid @RequestBody OnibusDto.Request req) {
        return service.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}