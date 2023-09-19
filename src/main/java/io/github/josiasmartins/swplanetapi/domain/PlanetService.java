package io.github.josiasmartins.swplanetapi.domain;

import java.util.Optional;

import org.springframework.stereotype.Service;

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

}
