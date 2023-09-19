package io.github.josiasmartins.swplanetapi.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.josiasmartins.swplanetapi.common.PlanetConstants;

@SpringBootTest(classes = PlanetService.class)
public class PlanetServiceTest {

    @Autowired
    private PlanetService planetService;
    
    // operacao_estado_retorno
    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        // system under test
        Planet sut = planetService.create(PlanetConstants.PLANET);

        Assertions.assertThat(sut).isEqualTo(PlanetConstants.PLANET);
    }   

}
