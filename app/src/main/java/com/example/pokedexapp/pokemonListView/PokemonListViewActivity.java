package com.example.pokedexapp.pokemonListView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.pokedexapp.R;
import com.example.pokedexapp.pokemon.Pokemon;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedexapp.pokemonView.PokemonViewActivity;
import com.example.pokedexapp.pokemonapi.PokeAPIBean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class PokemonListViewActivity extends AppCompatActivity implements PokemonListAdaptor.OnPokemonClickListener {

    private RecyclerView recyclerView;
    private ArrayList<Pokemon> pokemonList = new ArrayList<>();
    Handler pokemonListResultsHandler = new Handler();
    private PokemonListAdaptor pokemonListAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = this.findViewById(R.id.pokemon_list);

        Log.d("app", "\n\n\napp started\n\n\n");
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        pokemonListAdaptor = new PokemonListAdaptor(this, pokemonList, this);
        recyclerView.setAdapter(pokemonListAdaptor);
        pokemonListAdaptor.notifyDataSetChanged();
        GetPokemonListTask getPokemonListTask = new GetPokemonListTask(this);
        getPokemonListTask.execute();

    }

    public void populatePokemonList(ArrayList<Pokemon> pokemonList){
        Log.d("app", pokemonList.toString());
        for(Pokemon pokemon: pokemonList){
            this.pokemonList.add(pokemon);
            pokemonListAdaptor.notifyItemInserted(pokemonList.size());
        }
    }

    @Override
    public void onPokemonClick(int position) {
        Pokemon selectedPokemon = pokemonList.get(position);
        int pokemonId = selectedPokemon.getId();

        Intent intent = new Intent(this, PokemonViewActivity.class);
        intent.putExtra("selectedPokemonId", pokemonId);
        startActivity(intent);
    }

    public class GetPokemonListTask extends AsyncTask<Void, Integer, ArrayList<Pokemon>> {
        Context context;
        public GetPokemonListTask(Context context){
            this.context = context;
        }
        @Override
        protected ArrayList<Pokemon> doInBackground(Void... voids) {
            Log.d("app", "finding pokemon...");
            return PokeAPIBean.getPokemonList();
        }

        @Override
        protected void onPostExecute(ArrayList<Pokemon> pokemon) {
            super.onPostExecute(pokemon);
            Log.d("app", pokemon.toString());
            populatePokemonList(pokemon);
        }
    }

}