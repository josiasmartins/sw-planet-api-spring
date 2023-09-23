package io.github.josiasmartins.swplanetapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("it") // adiciona o arquivo application-it.properties
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // webEnvironment = WebEnvironment.RANDOM_PORT: adiciona uma porta aleatoria
public class PlanetIT {

    @Test
    public void contextLoads() {

    }

}