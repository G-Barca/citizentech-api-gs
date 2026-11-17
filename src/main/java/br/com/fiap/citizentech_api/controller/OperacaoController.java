package br.com.fiap.citizentech_api.controller;

import br.com.fiap.citizentech_api.dto.OperacaoDto;
import br.com.fiap.citizentech_api.service.OperacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/operacoes")
@Tag(name = "Operacoes")
public class OperacaoController {

    private final OperacaoService service;

    public OperacaoController(OperacaoService service) {
        this.service = service;
    }

    @GetMapping
    public PagedModel<EntityModel<OperacaoDto.Response>> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Long linhaId,
            @RequestParam(required = false) Long onibusId,
            @PageableDefault(size = 10, sort = "dataOperacao") Pageable pageable) {
        return Paginas.toModel(service.listar(data, linhaId, onibusId, pageable), this::toModel);
    }

    @GetMapping("/{id}")
    public EntityModel<OperacaoDto.Response> buscar(@PathVariable Long id) {
        return toModel(service.buscar(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<OperacaoDto.Response>> criar(@Valid @RequestBody OperacaoDto.Request req) {
        EntityModel<OperacaoDto.Response> model = toModel(service.criar(req));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<OperacaoDto.Response> toModel(OperacaoDto.Response dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(OperacaoController.class).buscar(dto.id())).withSelfRel(),
                linkTo(methodOn(LinhaController.class).buscar(dto.linhaId())).withRel("linha"),
                linkTo(methodOn(OnibusController.class).buscar(dto.onibusId())).withRel("onibus"),
                linkTo(OperacaoController.class).withRel("operacoes"));
    }
}