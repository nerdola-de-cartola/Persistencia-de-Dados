package br.ufg.inf.repository;

import br.ufg.inf.model.Estudante;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudanteRepository extends MongoRepository<Estudante, String> {
}