package com.fiap.springblog.controller;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/artigos")
public class ArtigoController {

    @Autowired
    private ArtigoService artigoService;

    @GetMapping()
    public List<Artigo> obterTodos(){
        return this.artigoService.obterTodos();
    }
    @GetMapping("/{codigo}")
    public Artigo obterPorCodigo(@PathVariable String codigo){
        return this.artigoService.obterPorCodigo(codigo);
    }
    @GetMapping("/maiordata")
    public List<Artigo> findByDataGreaterThan(@RequestParam("data")LocalDateTime data){
        return this.artigoService.findByDataGreaterThan(data);
    }
    @GetMapping("/data-status")
    public List<Artigo> findByDataAndStatus(@RequestParam("data") LocalDateTime data, @RequestParam("status")Integer status){
        return this.artigoService.findByDataAndStatus(data, status);
    }
    @GetMapping("/status-maiordata")
    public List<Artigo> findByStatusAndDataGreaterThan(@RequestParam("status") Integer status, @RequestParam("data") LocalDateTime data){
        return this.artigoService.findByStatusAndDataGreaterThan(status, data);
    }
    @GetMapping("/periodo")
    public List<Artigo> obterArtigoPorDataHora(@RequestParam("de") LocalDateTime de, @RequestParam("ate") LocalDateTime ate){
        return this.artigoService.obterArtigoPorDataHora(de, ate);
    }
    @GetMapping("/artigo-complexo")
    public List<Artigo> encontrarArtigosComplexos(@RequestParam("status") Integer status,
                                                  @RequestParam("data") LocalDateTime data,
                                                  @RequestParam("titulo") String titulo){
        return this.artigoService.encontrarArtigosComplexos(status, data, titulo);
    }
    @GetMapping("/pagina-artigos")
    public ResponseEntity<Page<Artigo>> obterArtigosPaginados(Pageable pageable){
        Page<Artigo> artigos = this.artigoService.listaArtigos(pageable);
        return ResponseEntity.ok(artigos);
    }
    @GetMapping("/status-ordenado")
    public List<Artigo> findByStatusOrderByTituloAsc(@RequestParam Integer status){
        return this.artigoService.findByStatusOrderByTituloAsc(status);
    }
    @GetMapping("/status-query-ordenacao")
    public List<Artigo> obterArtigoPorStatusComOrdenacao(@RequestParam Integer status){
        return this.artigoService.obterArtigoPorStatusComOrdenacao(status);
    }

    @PostMapping
    public Artigo criar(@RequestBody Artigo artigo){
        return this.artigoService.criar(artigo);
    }
    @PutMapping
    public void atualizar(@RequestBody Artigo artigo){
        this.artigoService.atualizar(artigo);
    }
    @PutMapping("/{id}")
    public void atualizarArtigo(@PathVariable String id, @RequestBody String novaURL){
        this.artigoService.atualizarArtigo(id, novaURL);
    }
    @DeleteMapping("/{id}")
    public void deleteArtigo(@PathVariable String id){
        this.artigoService.deleteById(id);
    }
    @DeleteMapping("/delete")
    public void deleteArtigoById(@RequestParam("Id") String id){
        this.artigoService.deleteArtigobyId(id);
    }
}