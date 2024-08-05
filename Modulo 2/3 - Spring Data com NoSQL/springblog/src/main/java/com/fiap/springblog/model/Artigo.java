package com.fiap.springblog.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;

@Document
@Data
public class Artigo {

    @Id
    private String codigo;
    private String titulo;
    private LocalTime data;
    private String texto;
    private String url;
    private Integer status;

    @DBRef // Referencia de uma collection dentro da outra
    private Autor autor;
}
