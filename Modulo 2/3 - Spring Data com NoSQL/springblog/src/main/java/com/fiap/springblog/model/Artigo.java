package com.fiap.springblog.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
public class Artigo {

    @Id
    private String codigo;
    private String titulo;
    private LocalDateTime data;

    @TextIndexed //Estou informando para o mongoDb que esse campo é indexado. Busca equivalente ao método findByTexto().
    private String texto; //Para finalizar a indexacao, preciso fazer esse comando no mongosh db.artigo.createIndex({texto: "text"})

    private String url;
    private Integer status;

    @DBRef // Referencia de uma collection dentro da outra
    private Autor autor;

    @Version //Essa dependecia é para garantir que meu documento estará na mesma versão - Utilizado para garantir a atomicidade
    private Long version;
}
