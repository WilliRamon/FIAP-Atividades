package com.fiap.springblog.service.impl;

import com.fiap.springblog.model.Artigo;
import com.fiap.springblog.model.ArtigoStatusCount;
import com.fiap.springblog.model.Autor;
import com.fiap.springblog.model.AutorTotalArtigo;
import com.fiap.springblog.repository.ArtigoRepository;
import com.fiap.springblog.repository.AutorRepository;
import com.fiap.springblog.service.ArtigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Transactional(readOnly = true)
    @Override
    public Artigo obterPorCodigo(String codigo) {
        return this.artigoRepository
                .findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Artigo não existe"));
    }

    @Override
    public ResponseEntity<?> criar(Artigo artigo) {

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
        try{
            this.artigoRepository.save(artigo);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (DuplicateKeyException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Artigo já existe na coleção");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar artigo: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> atualizarArtigo(String id, Artigo artigo) {
        try{
            Artigo existeArtigo = this.artigoRepository.findById(id).orElse(null);
            if(existeArtigo == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Artigo nâo encontrado na coleção");
            }
            existeArtigo.setTitulo(artigo.getTitulo());
            existeArtigo.setData(artigo.getData());
            existeArtigo.setTexto(artigo.getTexto());
            this.artigoRepository.save(existeArtigo);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar artigo: " + e.getMessage());
        }
    }
/*    @Transactional //Estou transformando esse método TRANSACIONAL
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
        try{
            return this.artigoRepository.save(artigo);
        }catch (OptimisticLockingFailureException ex){ // Tratatica de exceção de OptimisticLockingFailureException
            // Desenvolver a estratégia

            //1. Recuperar o documento mais recente do banco de dados (na coleção artigo)
            Artigo atualizado = artigoRepository.findById(artigo.getCodigo()).orElse(null);

            if(atualizado != null){
                // 2.Atualizar os campos desejados
                atualizado.setTitulo(artigo.getTitulo());
                atualizado.setTexto(artigo.getTexto());
                atualizado.setStatus(artigo.getStatus());

                //3. Incrementar a versão manualmente do documento
                atualizado.setVersion(atualizado.getVersion() + 1);

                //4. Tentar salvar novamente
                return this.artigoRepository.save(atualizado);
            }else{
                //5. Documento não encontrado, tratar o erro adequadamente
                throw new RuntimeException("Artigo não encontrado: " + artigo.getCodigo());
            }
        }
    }*/

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

    @Transactional
    @Override
    public void atualizar(Artigo updateArtigo) {
        Artigo artigoAnterior = artigoRepository.findById(updateArtigo.getCodigo()).orElse(null);
        if (artigoAnterior != null){
            artigoAnterior.setTitulo(updateArtigo.getTitulo());
            artigoAnterior.setData(updateArtigo.getData());
            artigoAnterior.setTexto(updateArtigo.getTexto());
            artigoAnterior.setUrl(updateArtigo.getUrl());
            artigoAnterior.setStatus(updateArtigo.getStatus());
            artigoAnterior.setAutor(updateArtigo.getAutor());

            this.artigoRepository.save(artigoAnterior);
        }else{
            this.artigoRepository.save(updateArtigo);
        }
    }

    @Transactional
    @Override
    public void atualizarArtigo(String id, String novaURL) {
        Query query = new Query(Criteria.where("_id").is(id)); //Critério de busca pelo id
        Update update = new Update().set("url", novaURL);          //Os campos que serão atualizados
        this.mongoTemplate.updateFirst(query,update, Artigo.class);//Executo o comando
    }

    @Transactional
    @Override
    public void deleteById(String id){
        this.artigoRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteArtigobyId(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Artigo.class);
    }
}