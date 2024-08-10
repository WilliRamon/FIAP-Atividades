package com.fiap.springblog.repository;

import com.fiap.springblog.model.Artigo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArtigoRepository extends MongoRepository<Artigo, String> {
    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);

    @Query("{ $and: [{'data': {$gte: ?0}}, {'data':  {$lte: ?1}} ]}")
    public List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate);
}
