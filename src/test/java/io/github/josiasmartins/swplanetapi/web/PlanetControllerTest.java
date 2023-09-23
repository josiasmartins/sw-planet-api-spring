package io.github.josiasmartins.swplanetapi.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.josiasmartins.swplanetapi.common.PlanetConstants;
import io.github.josiasmartins.swplanetapi.domain.Planet;
import io.github.josiasmartins.swplanetapi.domain.PlanetService;

import static io.github.josiasmartins.swplanetapi.common.PlanetConstants.PLANET;
import static io.github.josiasmartins.swplanetapi.common.PlanetConstants.TATOOINE;
import static io.github.josiasmartins.swplanetapi.common.PlanetConstants.PLANETS;

@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlanetService planetService;

    @Test
    public void createPlanet_WithValidData_ReturnsCreated() throws Exception {

        when(planetService.create(PLANET)).thenReturn(PLANET);

        mockMvc.perform(
            MockMvcRequestBuilders
            .post("/planets")
                .content(objectMapper.writeValueAsString(PLANET)).contentType(MediaType.APPLICATION_JSON)) // transforma em string | json
            .andExpect(MockMvcResultMatchers.status().isCreated()) // status esperado
            .andExpect(MockMvcResultMatchers.jsonPath("$").value(PLANET));  // Faz a comparacao dos valores"$" raiz do json

    }

    @Test
    public void createPlanet_WithInvalidData_ReturnsBadRequest() throws  Exception {
        Planet emptyPlanet = new Planet();
        Planet invalidPlanet = new Planet("", "", "");

        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/planets")
                    .content(objectMapper.writeValueAsString(emptyPlanet))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

         mockMvc
            .perform(
                MockMvcRequestBuilders.post("/planets")
                    .content(objectMapper.writeValueAsString(invalidPlanet))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

    }

    @Test
    public void createPlanet_WithExistingName_ReturnsConflict() throws Exception {
      when(planetService.create(any())).thenThrow(DataIntegrityViolationException.class);

      mockMvc
        .perform(
          MockMvcRequestBuilders.post("/planets").content(objectMapper.writeValueAsString(PLANET))
            .contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() throws Exception {
        // TODO implement
        when(planetService.getById(1L)).thenReturn(Optional.of(PLANET));

        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/planets/1").content(objectMapper.writeValueAsString(PLANET))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsNotFound() throws Exception {
        // TODO implement
        // when(planetService.getById(1L)).thenReturn(Optional.of(PLANET));

        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/planets/1").content(objectMapper.writeValueAsString(PLANET))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() throws Exception {
       when(planetService.getByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

       mockMvc
        .perform(
            MockMvcRequestBuilders.get("/planets/name/" + PLANET.getName()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnsNotFound() throws Exception {

       mockMvc
        .perform(
            MockMvcRequestBuilders.get("/planets/name/" + PLANET.getName()))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void listPlanets_ReturnsFilteredPlanets() throws Exception {
        when(planetService.list(null, null)).thenReturn(PLANETS);
        when(planetService.list(TATOOINE.getTerrain(), TATOOINE.getClimate())).thenReturn(List.of(TATOOINE));

        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/planets?" + String.format("terrain=%s&climate=%s", TATOOINE.getTerrain(), TATOOINE.getClimate())))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(TATOOINE));

    }

    @Test
    public void listPlanets_ReturnsNoPlanets() throws Exception {
        when(planetService.list(null, null)).thenReturn(Collections.emptyList());

        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/planets"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    public void removePlanet_WithExistingId_ReturnsNoContent() throws Exception {
        // when(planetService.deleteById(0L)).thenReturn(any());
        // when(planetService.deleteById(null))
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/planets/1"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void removePlanet_WithUnexistingId_ReturnsNotFound() throws Exception {
        final Long planetId = 1L;
        doThrow(new EmptyResultDataAccessException(1)).when(planetService).deleteById(planetId); // doThrow: para metodos sem retorno (void)

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/planets/" + planetId))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    
}
