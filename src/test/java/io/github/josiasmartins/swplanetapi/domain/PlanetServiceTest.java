package io.github.josiasmartins.swplanetapi.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;
import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

import io.github.josiasmartins.swplanetapi.common.PlanetConstants;

//@SpringBootTest(classes = PlanetService.class)
@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

    // @Autowired
    @InjectMocks // instancia uma instancia real 
    private PlanetService planetService;

    // @MockBean
    @Mock
    private PlanetRepository planetRepository;
    
    // operacao_estado_retorno
    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        // AAA
        // Arange - arruma os dados
        when(planetRepository.save(PlanetConstants.PLANET)).thenReturn(PlanetConstants.PLANET);
        
        // action - acao
        // system under test
        Planet sut = planetService.create(PlanetConstants.PLANET);

        // assert
        Assertions.assertThat(sut).isEqualTo(PlanetConstants.PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {
        when(planetRepository.save(PlanetConstants.INVALID_PLANET)).thenThrow(RuntimeException.class);

        Assertions.assertThatThrownBy(
            () -> planetService.create(PlanetConstants.INVALID_PLANET))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() {
        when(planetRepository.findById(anyLong())).thenReturn(Optional.of(PlanetConstants.PLANET));

        Optional<Planet> sut = planetService.getById(1L);

        Assertions.assertThat(sut).isNotEmpty();
        Assertions.assertThat(sut.get()).isEqualTo(PlanetConstants.PLANET);

    }   

    @Test
    public void getPlanet_ByUnexistingId_ReturnsEmpty() {
        when(planetRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Planet> sut = planetService.getById(1L);

        Assertions.assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() {
        when(planetRepository.findByName(PlanetConstants.PLANET.getName())).thenReturn(Optional.of(PlanetConstants.PLANET));

        Optional<Planet> sut = planetService.getByName(PlanetConstants.PLANET.getName());

        Assertions.assertThat(sut).isNotEmpty();
        Assertions.assertThat(sut.get()).isEqualTo(PlanetConstants.PLANET);
    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnsEmpty() {
        final String name = "Unexisting name";
        when(planetRepository.findByName(name)).thenReturn(Optional.empty());

        Optional<Planet> sut = planetService.getByName(name);

        Assertions.assertThat(sut).isEmpty();   
    }

    @Test
    public void listPlanets_ReturnsAllPlanets() {
        final String climate = PlanetConstants.PLANET.getClimate();
        final String terrain = PlanetConstants.PLANET.getTerrain();
        // TODO: implements
        List<Planet> planets = new ArrayList<>() {
            {
                add(PlanetConstants.PLANET);
            }
        };
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(climate, terrain));

        when(planetRepository.findAll(query)).thenReturn(planets);

        List<Planet> sut = planetService.list(terrain, climate);

        Assertions.assertThat(sut).isNotEmpty();
        Assertions.assertThat(sut).hasSize(1);
        Assertions.assertThat(sut.get(0)).isEqualTo(PlanetConstants.PLANET);
    }

    @Test
    public void listPlanets_ReturnsANoPlanets() {
        // TODO: implements
        final String climate = PlanetConstants.PLANET.getClimate();
        final String terrain = PlanetConstants.PLANET.getTerrain();

        when(planetRepository.findAll(any())).thenReturn(Collections.emptyList());

        List<Planet> sut = planetService.list(terrain, climate);

        Assertions.assertThat(sut).isEmpty();
    }

}
