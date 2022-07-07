package com.example.pokedexapp.pokemon;

import java.util.ArrayList;
import java.util.Map;

public class Pokemon {
    private int baseExperience;
    private String name;
    private int height;
    private int id;
    private Map<String,String> sprites;
    private int hp;
    private int attack;
    private int defense;
    private int specialAttack;
    private int specialDefense;
    private int speed;
    private ArrayList<PokemonType> pokemonTypes;
    private int weight;

    public Pokemon(final int baseExperience,final String name,final int height,final int id,final Map<String, String> sprites,final int hp,final int attack,final int defense,final int specialAttack,final int specialDefense,final int speed,final ArrayList<PokemonType> pokemonTypes,final int weight) {
        this.baseExperience = baseExperience;
        this.name = name;
        this.height = height;
        this.id = id;
        this.sprites = sprites;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
        this.pokemonTypes = pokemonTypes;
        this.weight = weight;
    }

    public int getBaseExperience() {
        return baseExperience;
    }

    public void setBaseExperience(final int baseExperience) {
        this.baseExperience = baseExperience;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public Map<String, String> getSprites() {
        return sprites;
    }

    public void setSprites(final Map<String, String> sprites) {
        this.sprites = sprites;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(final int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(final int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(final int defense) {
        this.defense = defense;
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public void setSpecialAttack(final int specialAttack) {
        this.specialAttack = specialAttack;
    }

    public int getSpecialDefense() {
        return specialDefense;
    }

    public void setSpecialDefense(final int specialDefense) {
        this.specialDefense = specialDefense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(final int speed) {
        this.speed = speed;
    }

    public ArrayList<PokemonType> getPokemonTypes() {
        return pokemonTypes;
    }

    public void setPokemonTypes(final ArrayList<PokemonType> pokemonTypes) {
        this.pokemonTypes = pokemonTypes;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(final int weight) {
        this.weight = weight;
    }
}
