package com.example.pokedexapp.pokemonapi;

import android.util.JsonReader;
import android.util.Log;

import com.example.pokedexapp.pokemon.Pokemon;
import com.example.pokedexapp.pokemon.PokemonType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class PokemonJsonParserBean{

    public static ArrayList<Pokemon> parsePokemonList(JSONObject json) {
        ArrayList<Pokemon> pokemonList = new ArrayList<>();
        try {
            JSONArray pokemonListJson = json.getJSONArray("results");
            for(int i=0;i<pokemonListJson.length();i++){
                pokemonList.add(parsePokemon(PokeAPIBean.getPokemonJson(pokemonListJson.getJSONObject(i).getString("url"))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pokemonList;
    }

    public static Pokemon parsePokemon(JSONObject json) {
        try {
            final int baseExperience = json.getInt("base_experience");
            final String name = json.getString("name");
            final int height = json.getInt("height");
            final int id = json.getInt("id");
            final JSONObject spriteJson = json.getJSONObject("sprites");
            Map<String, String> sprites = new HashMap<>();
            sprites.put("Male Front", spriteJson.getString("front_default"));
            sprites.put("Male Back", spriteJson.getString("back_default"));
            sprites.put("Female Front", spriteJson.getString("front_female"));
            sprites.put("Female Back", spriteJson.getString("back_female"));
            sprites.put("Shiny Male Front", spriteJson.getString("front_shiny"));
            sprites.put("Shiny Male Back", spriteJson.getString("back_shiny"));
            sprites.put("Shiny Female Front", spriteJson.getString("front_shiny_female"));
            sprites.put("Shiny Female Back", spriteJson.getString("back_shiny_female"));
            final JSONArray stats = json.getJSONArray("stats");
            final int hp = stats.getJSONObject(0).getInt("base_stat");
            final int attack = stats.getJSONObject(1).getInt("base_stat");
            final int defense = stats.getJSONObject(2).getInt("base_stat");
            final int specialAttack = stats.getJSONObject(3).getInt("base_stat");
            final int specialDefense = stats.getJSONObject(4).getInt("base_stat");
            final int speed = stats.getJSONObject(5).getInt("base_stat");
            final ArrayList<PokemonType> pokemonTypes = new ArrayList<>();
            final JSONArray types = json.getJSONArray("types");

            Log.d("app", "pokemon:" + name);

            for (int i = 0; i < types.length(); i++) {
                pokemonTypes.add(PokemonType.get(types.getJSONObject(i).getJSONObject("type").getString("name")));
            }
            final int weight = json.getInt("weight");
            Pokemon pokemon = new Pokemon(baseExperience, name, height, id, sprites, hp, attack, defense, specialAttack, specialDefense, speed, pokemonTypes, weight);
            return pokemon;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
