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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

    private Boolean isLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = this.findViewById(R.id.pokemon_list);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);
        pokemonListAdaptor = new PokemonListAdaptor(this, pokemonList, this);
        recyclerView.setAdapter(pokemonListAdaptor);
        pokemonListAdaptor.notifyDataSetChanged();
        GetPokemonListTask getPokemonListTask = new GetPokemonListTask(this, pokemonList.size());
        getPokemonListTask.execute();

        initScrollListener();
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

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //https://stackoverflow.com/questions/26543131/how-to-implement-endless-list-with-recyclerview
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if(dy > 0) {
                    int visibleItemCount = gridLayoutManager.getChildCount();
                    int totalItemCount = gridLayoutManager.getItemCount();
                    int pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (isLoading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            isLoading = false;
                            //bottom of list!
                            loadMore();
                        }
                    }
                }
            }
        });
    }

    //WIP needs changing as this solution is slow and buggy
    private void loadMore() {
        pokemonList.add(null);
        pokemonListAdaptor.notifyItemInserted(pokemonList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pokemonList.remove(pokemonList.size() - 1);
                int scrollPosition = pokemonList.size();
                pokemonListAdaptor.notifyItemRemoved(scrollPosition);

                GetPokemonListTask getPokemonListTask = new GetPokemonListTask(getApplicationContext(), pokemonList.size());
                getPokemonListTask.execute();
            }
        }, 2000);
    }

    public class GetPokemonListTask extends AsyncTask<Void, Integer, ArrayList<Pokemon>> {
        Context context;
        int offset;

        public GetPokemonListTask(final Context context,final int offset){
            this.context = context;
            this.offset = offset;
        }
        @Override
        protected ArrayList<Pokemon> doInBackground(Void... voids) {
            Log.d("app", "finding pokemon...");
            return PokeAPIBean.getPokemonList(offset);
        }

        @Override
        protected void onPostExecute(ArrayList<Pokemon> pokemon) {
            super.onPostExecute(pokemon);
            Log.d("app", pokemon.toString());
            populatePokemonList(pokemon);
            isLoading = true;
        }
    }

}