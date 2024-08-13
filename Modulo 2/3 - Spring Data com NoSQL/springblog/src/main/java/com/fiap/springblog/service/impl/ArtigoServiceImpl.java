package com.fiap.springblog.service.impl;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.model.ArtigoStatusCount;
import com.fiap.springblog.model.Autor;
import com.fiap.springblog.model.AutorTotalArtigo;
import com.fiap.springblog.repository.ArtigoRepository;
import com.fiap.springblog.repository.AutorRepository;
import com.fiap.springblog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArtigoServiceImpl implements ArtigoService {

    private final MongoTemplate mongoTemplate;
    public ArtigoServiceImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private ArtigoRepository artigoRepository;
    @Autowired
    private AutorRepository autorRepository;
    @Override
    public List<Artigo> obterTodos() {
        return this.artigoRepository.findAll();
    }

    @Override
    public Artigo obterPorCodigo(String codigo) {
        return this.artigoRepository
                .findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Artigo não existe"));
    }
    @Override
    public Artigo criar(Artigo artigo) {

        // Se o autor existe
        if(artigo.getAutor().getCodigo() != null){

            // Recuperar o autor
            Autor autor = this.autorRepository
                    .findById(artigo.getAutor().getCodigo())
                    .orElseThrow(() -> new IllegalArgumentException("Autor inexistente."));

            //Define o autor no artigo
            artigo.setAutor(autor);

        }else{
            //Senão, não atribuir um autor ao código
            artigo.setAutor(null);
        }
        return this.artigoRepository.save(artigo);
    }

    @Override
    public List<Artigo> findByDataGreaterThan(LocalDateTime data) {
        Query query = new Query(Criteria.where("data").gt(data)); // buscar uma data onde seja maior que o parãmetro informado.
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status) {
        Query query = new Query(Criteria.where("data").is(data).and("status").is(status)); // buscar dois parâmentros iguais
        return mongoTemplate.find(query, Artigo.class);
    }
    @Override
    public List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data) {
        return this.artigoRepository.findByStatusAndDataGreaterThan(status, data);
    }

    @Override
    public List<Artigo> obterArtigoPorDataHora(LocalDateTime de, LocalDateTime ate) {
        return this.artigoRepository.obterArtigoPorDataHora(de,ate);
    }

    @Override
    public List<Artigo> encontrarArtigosComplexos(Integer status, LocalDateTime data, String titulo) {
        Criteria criteria = new Criteria();
        criteria.and("data").lte(data);
        if(status != null){
            criteria.and("status").is(status);
        }
        if(titulo != null && !titulo.isEmpty()){
            criteria.and("titulo").regex(titulo, "i");
        }
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Artigo.class);
    }

    //Essa nova paginacao permite ordenar as consultas com os
    // parametros que passei. Nesse caso,
    // ainda recebo um objeto pageable, porém, crio um novo objeto sort para passar os criterios de ordenacao
    @Override
    public Page<Artigo> listaArtigos(Pageable pageable) {
        Sort sort = Sort.by("titulo").ascending();
        Pageable paginacao = PageRequest.of(pageable.getPageNumber(),
                                            pageable.getPageSize(),
                                            sort);
        return this.artigoRepository.findAll(paginacao);
    }

    @Override
    public List<Artigo> findByStatusOrderByTituloAsc(Integer status) {
        return this.artigoRepository.findByStatusOrderByTituloAsc(status);
    }

    @Override
    public List<Artigo> obterArtigoPorStatusComOrdenacao(Integer status) {
        return this.artigoRepository.obterArtigoPorStatusComOrdenacao(status);
    }

    @Override
    public List<Artigo> findByTexto(String searchTerm) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingPhrase(searchTerm);
        Query query = TextQuery.queryText(criteria).sortByScore();
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<ArtigoStatusCount> contarArtigosPorStatus() {
        TypedAggregation<Artigo> aggregation = Aggregation.newAggregation(
                Artigo.class,
                Aggregation.group("status").count().as("quantidade"),
                Aggregation.project("quantidade").and("status") //A exibição será da direita para esquerta
                        .previousOperation()
        );
        AggregationResults<ArtigoStatusCount> results = mongoTemplate.aggregate(aggregation,ArtigoStatusCount.class);
        return results.getMappedResults();
    }

    @Override
    public List<AutorTotalArtigo> calcularTotalArtigosPorAutorNoPeriodo(LocalDate dataInico, LocalDate dataFim) {
        TypedAggregation<Artigo> aggregation = Aggregation.newAggregation(
                Artigo.class,
                Aggregation.match( //Esse critério me permite adicionar condições a minha agragação.
                        Criteria.where("data")
                        .gte(dataInico.atStartOfDay()) //atStartOfDay -> Inicia a consulta a partir da hora 00:00
                        .lt(dataFim.plusDays(1).atStartOfDay())
                ),
                Aggregation.group("autor").count().as("totalArtigo"),
                Aggregation.project("totalArtigo").and("autor")
                        .previousOperation()
        );
        AggregationResults<AutorTotalArtigo> results = mongoTemplate.aggregate(aggregation,AutorTotalArtigo.class);
        return results.getMappedResults();
    }

    @Override
    public void atualizar(Artigo updateArtigo) {
        this.artigoRepository.save(updateArtigo);
    }

    @Override
    public void atualizarArtigo(String id, String novaURL) {
        Query query = new Query(Criteria.where("_id").is(id)); //Critério de busca pelo id
        Update update = new Update().set("url", novaURL);          //Os campos que serão atualizados
        this.mongoTemplate.updateFirst(query,update, Artigo.class);//Executo o comando
    }

    @Override
    public void deleteById(String id){
        this.artigoRepository.deleteById(id);
    }

    @Override
    public void deleteArtigobyId(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Artigo.class);
    }
}