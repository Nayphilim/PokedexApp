package com.example.pokedexapp.pokemonView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pokedexapp.R;
import com.example.pokedexapp.pokemon.Pokemon;
import com.example.pokedexapp.pokemonListView.PokemonListViewActivity;
import com.example.pokedexapp.pokemonapi.PokeAPIBean;

import java.util.ArrayList;

public class PokemonViewActivity extends AppCompatActivity implements View.OnClickListener {
    private int pokemonId;
    private Pokemon chosenPokemon;
    private TextView pokemonName;
    private ImageView pokemonSprite, backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_view);

        pokemonName = findViewById(R.id.pokemonViewName);
        pokemonSprite = findViewById(R.id.pokemonSpriteBox);
        backArrow = findViewById(R.id.pokemonViewBackArrow);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            pokemonId = extras.getInt("selectedPokemonId");
        }

        backArrow.setOnClickListener(this);

        getPokemonDetails();

    }

    private void getPokemonDetails() {
        if (pokemonId > 0) {
            GetPokemonTask getPokemonListTask = new GetPokemonTask(getApplicationContext(), pokemonId);
            getPokemonListTask.execute();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pokemonViewBackArrow:
                finish();
                break;
        }
    }

    public class GetPokemonTask extends AsyncTask<Void, Integer, Pokemon> {
        Context context;
        int id;

        public GetPokemonTask(final Context context, final int id) {
            this.context = context;
            this.id = id;
        }

        @Override
        protected Pokemon doInBackground(Void... voids) {
            Log.d("app", "finding pokemon...");
            return PokeAPIBean.getPokemonById(id);
        }

        @Override
        protected void onPostExecute(Pokemon pokemon) {
            super.onPostExecute(pokemon);
            Log.d("app", pokemon.toString());
            chosenPokemon = pokemon;
            pokemonName.setText(chosenPokemon.getName());
            Glide.with(getApplicationContext()).load(chosenPokemon.getSprites().get("Male Front")).into(pokemonSprite);
            //isLoading = true;
        }
    }

}