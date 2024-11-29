package br.com.fiap.mspedido.service;

import br.com.fiap.mspedido.model.ItemPedido;
import br.com.fiap.mspedido.model.Pedido;
import br.com.fiap.mspedido.repository.PedidoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public Pedido criarPedido(Pedido pedido){

        boolean produtoDisponivel = verificarDisponibilidadeProdutos(pedido.getItensPedido());
        if(!produtoDisponivel){
            throw new NoSuchElementException("Um ou mais produtos não estão disponíveis.");
        }
        double valorTotal = calcularValorTotal(pedido.getItensPedido());
        pedido.setValorTotal(valorTotal);

        atualizarEstoqueProdutos(pedido.getItensPedido());

        return pedidoRepository.save(pedido);
    }
    private boolean verificarDisponibilidadeProdutos(List<ItemPedido> itensPedido) {
        for(ItemPedido itemPedido : itensPedido){
            Integer idProduto = itemPedido.getIdProduto();
            int quantidade = itemPedido.getQuantidade();

            ResponseEntity<String> response = restTemplate.getForEntity(
                    "http://localhost:8080/api/produtos/{produtoId}",
                    String.class,
                    idProduto
            );

            if(response.getStatusCode() == HttpStatus.NOT_FOUND){
                throw new NoSuchElementException("Produto não encontrado!");
            }else{
                try{
                    JsonNode produtoJson = objectMapper.readTree((response.getBody()));
                    int quantidadeEstoque = produtoJson.get("quantidade_estoque").asInt();

                    if(quantidadeEstoque < quantidade){
                        return false;
                    }
                }catch (IOException e){

                }
            }
        }
        return true;
    }
    private double calcularValorTotal(List<ItemPedido> itensPedido) {
        double valorTotal = 0.0;

        for (ItemPedido itemPedido: itensPedido) {
            Integer idProduto = itemPedido.getIdProduto();
            int quantidade = itemPedido.getQuantidade();

            ResponseEntity<String> response = restTemplate.getForEntity(
                    "http://localhost:8080/api/produtos/{produtoId}",
                    String.class,
                    idProduto
            );

            if (response.getStatusCode() == HttpStatus.OK){
                try{
                    JsonNode produtoJson = objectMapper.readTree(response.getBody());
                    double preco = produtoJson.get("preco").asDouble();
                    valorTotal += preco * quantidade;
                }catch (IOException e){
                    //Tratar depois
                }
            }
        }
        return valorTotal;
    }
    private void atualizarEstoqueProdutos(List<ItemPedido> itensPedido) {
        for(ItemPedido itemPedido : itensPedido){
            Integer idProduto = itemPedido.getIdProduto();
            int quantidade = itemPedido.getQuantidade();

            restTemplate.put(
                    "http://localhost:8080/api/produtos/atualizar/estoque/{produtoId}/{quantidade}",
                    null,
                    idProduto,
                    quantidade
            );
        }
    }
}
