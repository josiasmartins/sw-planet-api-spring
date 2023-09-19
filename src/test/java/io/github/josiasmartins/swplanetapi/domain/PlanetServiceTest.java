package io.github.josiasmartins.swplanetapi.domain;

import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

}
