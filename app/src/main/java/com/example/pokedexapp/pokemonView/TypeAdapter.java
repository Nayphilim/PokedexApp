package com.example.pokedexapp.pokemonView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.pokedexapp.pokemon.PokemonType;

import java.util.ArrayList;

public class TypeAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<PokemonType> types;

    public TypeAdapter(Context context, ArrayList<PokemonType> types){
        this.context = context;
        this.types = types;
    }

    @Override
    public int getCount() {
        return types.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
