package br.com.fiap.citizentech_api.controller;


import br.com.fiap.citizentech_api.dto.LinhaDto;
import br.com.fiap.citizentech_api.service.LinhaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/linhas")
@Tag(name = "Linhas")
public class LinhaController {

    private final LinhaService service;

    public LinhaController(LinhaService service) {
        this.service = service;
    }

    @GetMapping
    public PagedModel<EntityModel<LinhaDto.Response>> listar(
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return Paginas.toModel(service.listar(nome, pageable), this::toModel);
    }

    @GetMapping("/{id}")
    public EntityModel<LinhaDto.Response> buscar(@PathVariable Long id) {
        return toModel(service.buscar(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<LinhaDto.Response>> criar(@Valid @RequestBody LinhaDto.Request req) {
        EntityModel<LinhaDto.Response> model = toModel(service.criar(req));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public EntityModel<LinhaDto.Response> atualizar(@PathVariable Long id,
                                                    @Valid @RequestBody LinhaDto.Request req) {
        return toModel(service.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pontos")
    public List<LinhaDto.PontoResponse> listarPontos(@PathVariable Long id) {
        return service.listarPontos(id);
    }

    @PostMapping("/{id}/pontos")
    public ResponseEntity<LinhaDto.PontoResponse> adicionarPonto(@PathVariable Long id,
                                                                 @Valid @RequestBody LinhaDto.PontoRequest req) {
        LinhaDto.PontoResponse criado = service.adicionarPonto(id, req);
        return ResponseEntity.created(linkTo(methodOn(LinhaController.class).listarPontos(id)).toUri())
                .body(criado);
    }

    private EntityModel<LinhaDto.Response> toModel(LinhaDto.Response dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(LinhaController.class).buscar(dto.id())).withSelfRel(),
                linkTo(methodOn(LinhaController.class).listarPontos(dto.id())).withRel("pontos-da-linha"),
                linkTo(LinhaController.class).withRel("linhas"));
    }
}