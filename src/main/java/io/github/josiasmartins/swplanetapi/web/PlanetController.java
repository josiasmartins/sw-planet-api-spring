package io.github.josiasmartins.swplanetapi.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.josiasmartins.swplanetapi.domain.Planet;
import io.github.josiasmartins.swplanetapi.domain.PlanetService;

@RestController
@RequestMapping("/planets")
public class PlanetController {

    @Autowired
    private PlanetService planetService;
    
    @PostMapping
    public ResponseEntity<Planet> create(@RequestBody Planet planet) {
        Planet planetCreated = planetService.create(planet);
        return ResponseEntity.status(HttpStatus.CREATED).body(planetCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planet> getById(@PathVariable("id") Long id) {
       return planetService.getById(id)
            .map(planet -> ResponseEntity.ok(planet))
            .orElseGet(() -> ResponseEntity.notFound().build());
        // return ResponseEntity.status(HttpStatus.OK).body(
        //     planetService.getById(id)
        // );
    }

}
