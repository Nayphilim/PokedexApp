package com.example.pokedexapp.pokemonapi;

import java.io.InputStream;
import java.net.HttpURLConnection;

public interface PokeAPI {
    InputStream getPokemonJson(String pokemonURL);

}
