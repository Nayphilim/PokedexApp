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
import java.util.Iterator;
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
            ArrayList<String> sprites = new ArrayList<>();
            if(spriteJson.getString("front_default") != "null") sprites.add(spriteJson.getString("front_default"));
            if(spriteJson.getString("back_default") != "null") sprites.add(spriteJson.getString("back_default"));
            if(spriteJson.getString("front_female") != "null") sprites.add(spriteJson.getString("front_female"));
            if(spriteJson.getString("back_female") != "null") sprites.add(spriteJson.getString("back_female"));
            if(spriteJson.getString("front_shiny") != "null") sprites.add(spriteJson.getString("front_shiny"));
            if(spriteJson.getString("back_shiny") != "null") sprites.add(spriteJson.getString("back_shiny"));
            if(spriteJson.getString("front_shiny_female") != "null") sprites.add(spriteJson.getString("front_shiny_female"));
            if(spriteJson.getString("back_shiny_female") != "null") sprites.add(spriteJson.getString("back_shiny_female"));
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

    public static ArrayList<String> parsePokemonForNames(JSONArray json){
        ArrayList<String> pokemonList = new ArrayList<>();
        try {
//            Iterator<String> keys = json.keys();
//            json = json.getJSONObject("results");
//            Log.d("app", "test:" + json.toString());
//            while(keys.hasNext()){
//                String key = keys.next();
//                Log.d("app", key);
//                if(json.get(key) instanceof JSONObject){
//                   pokemonList.add(json.getJSONObject(key).getString("name"));
//                }
//            }

            for(int i=0;i< json.length();i++){
                JSONObject pokemon = json.getJSONObject(i);
                pokemonList.add(pokemon.getString("name"));
            }
            Log.d("app", "found " +pokemonList.size() +" pokemon:" + pokemonList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pokemonList;
    }
}
