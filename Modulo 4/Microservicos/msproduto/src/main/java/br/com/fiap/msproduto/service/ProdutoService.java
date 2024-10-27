package br.com.fiap.msproduto.service;

import br.com.fiap.msproduto.model.Produto;
import br.com.fiap.msproduto.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;
    public List<Produto> listarProduto(){
        return produtoRepository.findAll();
    }
    public ResponseEntity<?> listarUmProduto(Integer produtoId){
        Produto produto = produtoRepository.findById(produtoId).orElse(null);
        if(produto != null)
            return ResponseEntity.ok(produto);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
    }
    public Produto cadastrarProduto(Produto produto){
        return produtoRepository.save(produto);
    }
    public Produto atualizarProduto(Integer produtoId, Produto novoPorduto){
        Produto produtoExistente = produtoRepository.findById(produtoId).orElse(null);
        if(produtoExistente != null){
            produtoExistente.setNome(novoPorduto.getNome());
            produtoExistente.setDescricao(novoPorduto.getDescricao());
            produtoExistente.setQuantidade_estoque(novoPorduto.getQuantidade_estoque());
            produtoExistente.setPreco(novoPorduto.getPreco());
            return produtoRepository.save(produtoExistente);
        }else{
            throw new NoSuchElementException("Produto não encontrado!");
        }
    }
    public void excluirProduto(Integer produtoId){
        Produto produtoExistente = produtoRepository.findById(produtoId).orElse(null);
        if(produtoExistente != null){
            produtoRepository.delete(produtoExistente);
        }else{
            throw new NoSuchElementException("Produto não encontrado!");
        }
    }
}
