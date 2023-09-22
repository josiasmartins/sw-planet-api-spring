package io.github.josiasmartins.swplanetapi.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.josiasmartins.swplanetapi.domain.Planet;
import io.github.josiasmartins.swplanetapi.domain.PlanetService;

import static io.github.josiasmartins.swplanetapi.common.PlanetConstants.PLANET;

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
    
}