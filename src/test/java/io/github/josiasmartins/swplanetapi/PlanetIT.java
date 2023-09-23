package io.github.josiasmartins.swplanetapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.josiasmartins.swplanetapi.domain.Planet;

import static io.github.josiasmartins.swplanetapi.common.PlanetConstants.PLANET;

@ActiveProfiles("it") // adiciona o arquivo application-it.properties
@Sql(scripts = { "/remove_planets.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD) // ExecutionPhase.AFTER_TEST_METHOD: depois que terminar de executar o metodo, ele aciona o sql
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // webEnvironment = WebEnvironment.RANDOM_PORT: adiciona uma porta aleatoria
public class PlanetIT {

    @Autowired
    TestRestTemplate restTemplate;

   @Test
   public void createPlanet_ReturnsCreated() {
        ResponseEntity<Planet> sut = restTemplate.postForEntity("/planets", PLANET, Planet.class); // postForEntity: adiciona o mestodo e serializa a responsa no formato json com base na classe
        
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(sut.getBody().getId()).isNotNull();
        assertThat(sut.getBody().getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());

   }

}