package com.example.pokedexapp.pokemonapi;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import com.example.pokedexapp.pokemon.Pokemon;
import com.example.pokedexapp.pokemonListView.PokemonListViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class PokeAPIBean {

    static final String POKE_ENDPOINT_URL = "https://pokeapi.co/api/v2/";


    public static ArrayList<Pokemon> getPokemonList(final int offset) {
        ArrayList<Pokemon> pokemonList = new ArrayList<>();
        try {
            final URL pokeApiEndpoint = new URL(POKE_ENDPOINT_URL + "pokemon/?limit=40&offset=" + offset);
            HttpsURLConnection connection =
                    (HttpsURLConnection) pokeApiEndpoint.openConnection();
            connection.setRequestProperty("User-Agent", "Pokedex App");
            if (connection.getResponseCode() == 200) {
                InputStream responseBody = connection.getInputStream();
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
                pokemonList = PokemonJsonParserBean.parsePokemonList(jsonObject);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("app", "results:" + pokemonList.toString());
        return pokemonList;
    }

    public static JSONObject getPokemonJson(final String pokemonURL) {
        JSONObject pokemonJson = null;
        try {
            final URL pokeApiEndpoint = new URL(pokemonURL);
            HttpsURLConnection connection =
                    (HttpsURLConnection) pokeApiEndpoint.openConnection();
            connection.setRequestProperty("User-Agent", "Pokedex App");
            if (connection.getResponseCode() == 200) {
                InputStream responseBody = connection.getInputStream();
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                pokemonJson = new JSONObject(responseStrBuilder.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pokemonJson;
    }

    public static Pokemon getPokemonById(final int id){
        final URL pokeApiEndpoint;
        try {
            pokeApiEndpoint = new URL("https://pokeapi.co/api/v2/pokemon/" + String.valueOf(id));
            HttpsURLConnection connection =
                    (HttpsURLConnection) pokeApiEndpoint.openConnection();
            connection.setRequestProperty("User-Agent", "Pokedex App");
            if (connection.getResponseCode() == 200) {
                InputStream responseBody = connection.getInputStream();
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                JSONObject pokemonJson = new JSONObject(responseStrBuilder.toString());
                return PokemonJsonParserBean.parsePokemon(pokemonJson);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
