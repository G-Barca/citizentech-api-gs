package br.com.fiap.citizentech_api.controller;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

import java.util.List;
import java.util.function.Function;

public final class Paginas {

    private Paginas() {
    }

    public static <T, R> PagedModel<R> toModel(Page<T> pagina, Function<T, R> conversor) {
        List<R> conteudo = pagina.getContent().stream().map(conversor).toList();
        PagedModel.PageMetadata meta = new PagedModel.PageMetadata(
                pagina.getSize(), pagina.getNumber(), pagina.getTotalElements(), pagina.getTotalPages());
        return PagedModel.of(conteudo, meta);
    }
}