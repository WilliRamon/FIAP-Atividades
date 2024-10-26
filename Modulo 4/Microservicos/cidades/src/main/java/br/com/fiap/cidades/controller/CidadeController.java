package br.com.fiap.cidades.controller;

import br.com.fiap.cidades.model.Cidade;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class CidadeController {
    private List<Cidade> cidades = Arrays.asList(
            new Cidade(1, "Campinas", "Pais", "Campinas é próximo de SP"),
            new Cidade(2, "Florianopolis", "Pais", "Uma cidade bonita"),
            new Cidade(3, "Porto Alegre", "Pais", "Porto Alegre é muito fria no inverno")
    );

    @GetMapping("/cidades")
    public List<Cidade> listarCidades(){
        return cidades;
    }

    @GetMapping("/cidade/{id}")
    public Cidade getCidade(@PathVariable int id){
        return cidades.stream().filter(cidade -> cidade.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
