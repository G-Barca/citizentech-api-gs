package br.com.fiap.citizentech_api.controller;


import br.com.fiap.citizentech_api.dto.PontoDto;
import br.com.fiap.citizentech_api.service.PontoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/pontos")
@Tag(name = "Pontos de onibus")
public class PontoController {

    private final PontoService service;

    public PontoController(PontoService service) {
        this.service = service;
    }

    @GetMapping
    public PagedModel<EntityModel<PontoDto.Response>> listar(
            @RequestParam(required = false) String endereco,
            @PageableDefault(size = 10, sort = "endereco") Pageable pageable) {
        return Paginas.toModel(service.listar(endereco, pageable), this::toModel);
    }

    @GetMapping("/{id}")
    public EntityModel<PontoDto.Response> buscar(@PathVariable Long id) {
        return toModel(service.buscar(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<PontoDto.Response>> criar(@Valid @RequestBody PontoDto.Request req) {
        EntityModel<PontoDto.Response> model = toModel(service.criar(req));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public EntityModel<PontoDto.Response> atualizar(@PathVariable Long id,
                                                    @Valid @RequestBody PontoDto.Request req) {
        return toModel(service.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<PontoDto.Response> toModel(PontoDto.Response dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(PontoController.class).buscar(dto.id())).withSelfRel(),
                linkTo(methodOn(PontoConsultaController.class).linhasDoPonto(dto.id())).withRel("linhas"),
                linkTo(PontoController.class).slash(dto.id()).slash("chegadas").withRel("chegadas"),
                linkTo(PontoController.class).withRel("pontos"));
    }
}