package com.fiap.springblog.service;

import com.fiap.springblog.model.Artigo;

import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoService {
    public List<Artigo> obterTodos();
    public Artigo obterPorCodigo(String codigo);
    public Artigo criar(Artigo artigo);
    public List<Artigo> findByDataGreaterThan(LocalDateTime data);
    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status);
    public void atualizar(Artigo updateArtigo);
    public void atualizarArtigo(String id, String novaURL);
    public void deleteById(String id);
    public void deleteArtigobyId(String id);
    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);
}
