package com.fiap.springblog.controller;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.model.ArtigoStatusCount;
import com.fiap.springblog.model.AutorTotalArtigo;
import com.fiap.springblog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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


    // Exceção gerada pela diferente de versão de documento.
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLockingFailureException(OptimisticLockingFailureException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Erro de concorrência: O Artigo foi atualizado por outro usuário. Por favor, tente novamente!");
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
    @GetMapping("/busca-texto")
    public List<Artigo> findByTexto(@RequestParam("searchTerm") String termo){
        return this.artigoService.findByTexto(termo);
    }
    @GetMapping("/contar-artigo")
    public List<ArtigoStatusCount> contarArtigosPorStatus(){
        return this.artigoService.contarArtigosPorStatus();
    }

    @GetMapping("/total-artigo-autor-periodo")
    public List<AutorTotalArtigo> calcularTotalArtigosPorAutorNoPeriodo(@RequestParam("dataInicio")LocalDate dataInicio,
                                                                        @RequestParam("dataFim") LocalDate dataFim){
        return this.artigoService.calcularTotalArtigosPorAutorNoPeriodo(dataInicio, dataFim);
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