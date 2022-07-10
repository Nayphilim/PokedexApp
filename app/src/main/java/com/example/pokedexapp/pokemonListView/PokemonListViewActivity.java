package com.example.pokedexapp.pokemonListView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.pokedexapp.R;
import com.example.pokedexapp.pokemon.Pokemon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
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
    private PokemonListAdaptor pokemonListAdaptor;
    private SearchView searchView;
    private ProgressBar progressBar;

    private Boolean isLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = this.findViewById(R.id.pokemon_list);
        searchView = this.findViewById(R.id.search_bar);
        progressBar = this.findViewById(R.id.pokemon_list_progress_bar);

        //https://stackoverflow.com/questions/31759171/recyclerview-and-java-lang-indexoutofboundsexception-inconsistency-detected-in?page=1&tab=votes#tab-top
        RecyclerView.LayoutManager layoutManager = new WrapContentGridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);
        pokemonListAdaptor = new PokemonListAdaptor(this, pokemonList, this);
        recyclerView.setAdapter(pokemonListAdaptor);
        pokemonListAdaptor.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
        GetPokemonListTask getPokemonListTask = new GetPokemonListTask(this, pokemonList.size());
        getPokemonListTask.execute();

        initScrollListener();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("app", "search: " + query);
                SearchPokemonListTask searchPokemonListTask = new SearchPokemonListTask(getApplicationContext(), query);
                searchPokemonListTask.execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                pokemonListAdaptor.notifyItemRangeRemoved(0, pokemonList.size());
                pokemonList.clear();
                progressBar.setVisibility(View.VISIBLE);
                //needs optimizing. Instead store list before search and on close load that list
                GetPokemonListTask getPokemonListTask = new GetPokemonListTask(getApplicationContext(), pokemonList.size());
                getPokemonListTask.execute();
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void populatePokemonList(ArrayList<Pokemon> pokemonList) {
        Log.d("app", pokemonList.toString());
        for (Pokemon pokemon : pokemonList) {
            this.pokemonList.add(pokemon);
            pokemonListAdaptor.notifyItemInserted(pokemonList.size());
        }
        progressBar.setVisibility(View.GONE);
    }

    public void populatePokemonListFromSearch(Pokemon pokemon) {
        pokemonList.add(pokemon);
        pokemonListAdaptor.notifyItemInserted(pokemonList.size());
        progressBar.setVisibility(View.GONE);
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
                if (dy > 0) {
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

    //https://stackoverflow.com/questions/14539623/unable-to-start-activity-componentinfo
    public class GetPokemonListTask extends AsyncTask<Void, Integer, ArrayList<Pokemon>> {
        Context context;
        int offset;

        public GetPokemonListTask(final Context context, final int offset) {
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

    public class SearchPokemonListTask extends AsyncTask<Void, Integer, ArrayList<String>> {
        Context context;
        String search;

        public SearchPokemonListTask(final Context context, final String search) {
            this.context = context;
            this.search = search;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            Log.d("app", "finding pokemon for search: " + search);
            return PokeAPIBean.getAllPokemonNames();
        }

        @Override
        protected void onPostExecute(ArrayList<String> pokemon) {
            super.onPostExecute(pokemon);
            if (pokemon != null) {
                //store current list so that we can quickly go back from search
                pokemonList.clear();
                int matches = 0;
                for (String name : pokemon) {
                    if (name.contains(search)) {
                        Log.d("app", "match:" + matches);
                        matches++;
                        GetPokemonTask getPokemonTask = new GetPokemonTask(getApplicationContext(), name);
                        getPokemonTask.execute();
                        //don't want to waste time getting too many matches for a short word search like 'po'
                        if (matches > 50) break;
                    }

                }
            }
        }
    }


    public class GetPokemonTask extends AsyncTask<String, Integer, Pokemon> {
        Context context;
        String name;

        public GetPokemonTask(final Context context, final String name) {
            this.context = context;
            this.name = name;
        }

        @Override
        protected Pokemon doInBackground(String... strings) {
            return PokeAPIBean.getPokemonByName(name);
        }

        @Override
        protected void onPostExecute(Pokemon pokemon) {
            super.onPostExecute(pokemon);
            if (pokemon != null) {
                populatePokemonListFromSearch(pokemon);
            }
        }
    }
}
