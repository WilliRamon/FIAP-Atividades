package br.com.fiap.mspedido.service;

import br.com.fiap.mspedido.model.Pedido;
import br.com.fiap.mspedido.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public Pedido criarPedido(Pedido pedido){
        return pedidoRepository.save(pedido);
    }
}
