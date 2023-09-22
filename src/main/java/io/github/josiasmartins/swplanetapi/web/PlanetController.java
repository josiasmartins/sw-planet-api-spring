package io.github.josiasmartins.swplanetapi.web;

import java.util.List;

import javax.validation.Valid;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Planet> create(@RequestBody @Valid Planet planet) {
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

    @GetMapping("/name/{name}")
    public ResponseEntity<Planet> getByName(@PathVariable("name") String name) {
        // System.out.println(planetService.getByName(name).get() + " ibg con");
        return planetService.getByName(name)
            .map(planet -> ResponseEntity.ok(planet))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Planet>> list(
        @RequestParam(required = false) String terrain, 
        @RequestParam(required = false) String climate) {
        List<Planet> planets = planetService.list(terrain, climate);
        return ResponseEntity.ok(planets);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Planet> delete(@PathVariable("id") long id) {
        planetService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Planet> patch(@PathVariable("id") Long id, @RequestBody Planet planet) {
        return planetService.updatePlanet(id, planet)
            .map(pl -> {
                planetService.create(pl);
                return ResponseEntity.ok(pl);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
        // return ResponseEntity.noContent().build();
    }



}
