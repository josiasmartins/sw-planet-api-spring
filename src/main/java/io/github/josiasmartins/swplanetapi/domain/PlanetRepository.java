package io.github.josiasmartins.swplanetapi.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface PlanetRepository extends CrudRepository<Planet, Long>, QueryByExampleExecutor<Planet> { //QueryByExample: permite criar consultas com query examples
    
    Optional<Planet> findByName(String name); // query methods

    <S extends Planet> List<S> findAll(Example<S> example);

}
