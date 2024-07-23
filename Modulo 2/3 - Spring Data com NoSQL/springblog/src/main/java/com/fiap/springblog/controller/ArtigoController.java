package com.fiap.springblog.controller;

import com.fiap.springblog.model.Artigo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artigos")
public class ArtigoController {

    @GetMapping()
    public List<Artigo> obterTodos(){
        return null;
    }

    @GetMapping("/{codigo}")
    public Artigo obterPorCodigo(@PathVariable String codigo){
        return null;
    }

    @PostMapping
    public Artigo criar(@RequestBody Artigo artigo){
        return null;
    }
}