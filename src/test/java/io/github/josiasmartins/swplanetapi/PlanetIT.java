package io.github.josiasmartins.swplanetapi;

import org.h2.table.Plan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import io.github.josiasmartins.swplanetapi.domain.Planet;

import static io.github.josiasmartins.swplanetapi.common.PlanetConstants.PLANET;
import static io.github.josiasmartins.swplanetapi.common.PlanetConstants.TATOOINE;
import static io.github.josiasmartins.swplanetapi.common.PlanetConstants.ALDERAAN;

@ActiveProfiles("it") // adiciona o arquivo application-it.properties
@Sql(scripts = { "/import_planets.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD) // ExecutionPhase.AFTER_TEST_METHOD: depois que terminar de executar o metodo, ele aciona o sql
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

   @Test
   @DisplayName("test IT")
   public void getPlanet_ReturnsPlanet() {
        ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/1", Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(TATOOINE);
   }    

   @Test
   public void getPlanetByName_ReturnsPlanet() {
    ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/name/" + ALDERAAN.getName(), Planet.class);

    assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(sut.getBody()).isEqualTo(ALDERAAN);
   }

   @Test
   public void listPlanets_ReturnsAllPlanets() {
        ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets", Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(3);
        assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
   }

   @Test
   public void listPlanets_ByClimate_ReturnsPlanets() {
        ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets?climate=" + ALDERAAN.getClimate(), Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(1);
        assertThat(sut.getBody()[0]).isEqualTo(ALDERAAN);
   }

   @Test
   public void listPlanets_ByTerrain_ReturnsPlanets() {
        ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets?terrain=" + ALDERAAN.getTerrain(), Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(1);
        assertThat(sut.getBody()[0]).isEqualTo(ALDERAAN);
   }

   @Test
   public void removePlanet_ReturnsNoContent() {
    ResponseEntity<Void> sut = restTemplate.exchange("/planets/" + TATOOINE.getId(), HttpMethod.DELETE, null, Void.class); // excahange: metodo generico

    assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
   }

}