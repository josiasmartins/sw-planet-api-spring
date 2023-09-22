package io.github.josiasmartins.swplanetapi.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class PlanetService {

    private PlanetRepository planetRepository;

    public PlanetService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    public Planet create(Planet planet) {
        return planetRepository.save(planet);
    }

    public Optional<Planet> getById(Long id) {
        return planetRepository.findById(id);
    }

    public Optional<Planet> getByName(String name) {
        // System.out.println(planetRepository.findByName(name).get() + " ibg");
        return planetRepository.findByName(name);
    }

    // public Planet getById(Long id) {
    //     return planetRepository.findById(id).get();
    // }

    public List<Planet> list(String terrain, String climate) {
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(climate, terrain));
        return planetRepository.findAll(query);
    }

    public void deleteById(Long id) {
        planetRepository.deleteById(id);
    }

    public Optional<Planet> updatePlanet(Long id, Planet updatePlanet) {
        Planet ps = planetRepository.findById(id).get();
        return Optional.of(PlanetService.updateFields(updatePlanet, ps));
    }

    //     public ResponseEntity<Planet> getByName(@PathVariable("name") String name) {
    //     // System.out.println(planetService.getByName(name).get() + " ibg con");
    //     return planetService.getByName(name)
    //         .map(planet -> ResponseEntity.ok(planet))
    //         .orElseGet(() -> ResponseEntity.notFound().build());
    // }

    // public static Planet update(Planet newPlanet, Planet planet) {
    //     if (newPlanet.getName() != null) {
    //         planet.setName(planet.getName());
    //     }
    //     if (planet.getClimate() != null) {
    //         planet.setClimate(planet.getClimate());
    //     }
    //     if (planet.getTerrain() != null) {
    //         planet.setTerrain(planet.getTerrain());
    //     }
        
    //     return planet;
    // }

    public static Planet updateFields(Planet updatePlanet, Planet oldPlanet) {
        String name = updatePlanet.getName() != null ? updatePlanet.getName() : oldPlanet.getName();
        String climate = updatePlanet.getClimate() != null ? updatePlanet.getClimate() : oldPlanet.getClimate();
        String terrain = updatePlanet.getTerrain() != null ? updatePlanet.getTerrain() : oldPlanet.getTerrain();
        Planet planet = new Planet(name, climate, terrain);
        planet.setId(oldPlanet.getId());
        return planet;
    } 

}
