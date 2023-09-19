package io.github.josiasmartins.swplanetapi.common;

import java.util.Optional;

import io.github.josiasmartins.swplanetapi.domain.Planet;

public class PlanetConstants {
    
    public static final Planet PLANET = new Planet("Emily", "frozen", "ice");

    public static final Planet INVALID_PLANET = new Planet("", "", "");
    
    // public static final Optional<Planet> NOT_FOUND = new Optional(new Planet());

    

}
