package io.github.josiasmartins.swplanetapi.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;

import io.github.josiasmartins.swplanetapi.common.PlanetConstants;

import static io.github.josiasmartins.swplanetapi.common.PlanetConstants.PLANET;
import static io.github.josiasmartins.swplanetapi.common.PlanetConstants.TATOOINE;;

// @SpringBootTest(classes = PlanetRepository.class)
@DataJpaTest // usa o banco em memoria h2
public class PlanetRepositoryTest {
    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager;
    
    @AfterEach // chama apos cada teste
    public void afterEach() {
        PlanetConstants.PLANET.setId(null);
    }
    

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        Planet planet = planetRepository.save(PLANET);

        Planet sut = testEntityManager.find(Planet.class, planet.getId());

        System.out.println(planet);

        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {
        Planet emptyPlanet = new Planet();
        Planet invalidPlanet = new Planet("", "", "");

        assertThatThrownBy(() -> planetRepository.save(emptyPlanet)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> planetRepository.save(invalidPlanet)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createPlanet_WithExistingName_ThrowsException() {
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        testEntityManager.detach(planet); // detach: permite pegar o novo planet e não so atulizar
        planet.setId(null);

        assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() {
        // TOOD implements
        Planet planet = testEntityManager.persistFlushFind(PlanetConstants.PLANET);

        Optional<Planet> planetOpt = planetRepository.findById(planet.getId());

        assertThat(planetOpt).isNotEmpty();
        assertThat(planetOpt.get()).isEqualTo(planet);
    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsEmpty() {
        // TODO implements
        Optional<Planet> planetOpt = planetRepository.findById(1L);

        assertThat(planetOpt).isEmpty();
    }

    // @Sql(scripts = "/import_planets.sql")
    // @Test
    // public void listPlanets_ReturnsFilteredPlanets() {
    //   Example<Planet> queryWithoutFilters = QueryBuilder.makeQuery(new Planet());
    //   Example<Planet> queryWithFilters = QueryBuilder.makeQuery(new Planet(TATOOINE.getClimate(), TATOOINE.getTerrain()));
  
    //   List<Planet> responseWithoutFilters = planetRepository.findAll(queryWithoutFilters);
    //   List<Planet> responseWithFilters = planetRepository.findAll(queryWithFilters);
  
    //   assertThat(responseWithoutFilters).isNotEmpty();
    //   assertThat(responseWithoutFilters).hasSize(3);
    //   assertThat(responseWithFilters).isNotEmpty();
    //   assertThat(responseWithFilters).hasSize(1);
    //   assertThat(responseWithFilters.get(0)).isEqualTo(TATOOINE);
    // }

    @Sql(scripts = "/import_planets.sql")
    @Test
    public void listPlanets_ReturnsFilteredPlanets() {
      Example<Planet> queryWithoutFilters = QueryBuilder.makeQuery(new Planet());
      Example<Planet> queryWithFilters = QueryBuilder.makeQuery(new Planet(TATOOINE.getClimate(), TATOOINE.getTerrain()));
  
      List<Planet> responseWithoutFilters = planetRepository.findAll(queryWithoutFilters);
      List<Planet> responseWithFilters = planetRepository.findAll(queryWithFilters);
  
      assertThat(responseWithoutFilters).isNotEmpty();
      assertThat(responseWithoutFilters).hasSize(3);
      assertThat(responseWithFilters).isNotEmpty();
      assertThat(responseWithFilters).hasSize(1);
      assertThat(responseWithFilters.get(0)).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ReturnsNoPlanets() {
        Example<Planet> query = QueryBuilder.makeQuery(new Planet());

        List<Planet> responseWithoutFilters = planetRepository.findAll(query);

        assertThat(responseWithoutFilters).isEmpty();
        assertThat(responseWithoutFilters).hasSize(0);
    }

}
