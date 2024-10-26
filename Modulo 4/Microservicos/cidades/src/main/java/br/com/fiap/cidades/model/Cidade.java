package br.com.fiap.cidades.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Cidade {
    private int id;
    private String nome;
    private String pais;
    private String descricao;
}
