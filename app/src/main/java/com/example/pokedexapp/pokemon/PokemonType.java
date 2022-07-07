package com.example.pokedexapp.pokemon;

import android.graphics.Color;

import com.example.pokedexapp.R;

import java.util.HashMap;
import java.util.Map;

public enum PokemonType {
    NORMAL("normal", R.color.normal_type),
    FIRE("fire", R.color.fire_type),
    WATER("water", R.color.water_type),
    GRASS("grass", R.color.grass_type),
    ELECTRIC("electric", R.color.electric_type),
    ICE("ice", R.color.ice_type),
    FIGHTING("fighting", R.color.fighting_type),
    POISON("poison", R.color.poison_type),
    GROUND("ground", R.color.ground_type),
    FLYING("flying", R.color.flying_type),
    PSYCHIC("psychic", R.color.psychic_type),
    BUG("bug", R.color.bug_type),
    ROCK("rock", R.color.rock_type),
    GHOST("ghost", R.color.ghost_type),
    DRAGON("dragon", R.color.dragon_type),
    DARK("dark", R.color.dark_type),
    STEEL("steel", R.color.steel_type),
    FAIRY("fairy", R.color.fairy_type);

    private final String identifier;
    private final int colour;
    private static final Map<String, PokemonType> lookUp = new HashMap<>();

    static {
        for (PokemonType c : PokemonType.values()) {
            lookUp.put(c.identifier, c);
        }
    }

    PokemonType(final String identifier, final int colour) {
        this.identifier = identifier;
        this.colour = colour;
    }

    public String toString() {
        return identifier;
    }

    public static PokemonType get(final String identifier) {
        return lookUp.get(identifier);
    }


}
