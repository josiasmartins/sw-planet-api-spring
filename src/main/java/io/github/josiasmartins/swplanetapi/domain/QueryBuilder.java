package io.github.josiasmartins.swplanetapi.domain;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class QueryBuilder {
    
    public static Example<Planet> makeQuery(Planet planet) {
        ExampleMatcher exampleMatcher = ExampleMatcher
            .matchingAll()
            .withIgnoreCase()
            .withIncludeNullValues();
        return Example.of(planet, exampleMatcher);
    }

}