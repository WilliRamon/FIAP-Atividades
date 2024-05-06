package br.com.fiap.pettech.pettech;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repo;

    public Collection<Produto> findAll(){
        //Collection<Produto> produtos = produtoRepository.findAll();
        var produtos = repo.findAll();
        return produtos;
    }

    public Optional<Produto> findById(UUID id){
        //Optional<Produto> produto = repo.findById(id);
        var produto = repo.findById(id);
        return produto;
    }

    public Produto save(Produto produto){
        produto = repo.save(produto);
        return produto;
    }

    public Produto update(UUID id, Produto produto){
        Produto buscaProduto = repo.getOne(id);
        buscaProduto.setNome(produto.getNome());
        buscaProduto.setDescricao(produto.getDescricao());
        buscaProduto.setUrlDaImagem(produto.getUrlDaImagem());
        buscaProduto.setPreco(produto.getPreco());
        buscaProduto = repo.save(buscaProduto);

        return buscaProduto;
    }

    public void delete(UUID id){
        repo.deleteById(id);
    }
}