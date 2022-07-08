package com.example.pokedexapp.pokemonView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokedexapp.R;
import com.example.pokedexapp.pokemon.Pokemon;
import com.example.pokedexapp.pokemon.PokemonType;
import com.example.pokedexapp.pokemonListView.PokemonListAdaptor;
import com.example.pokedexapp.pokemonListView.PokemonListViewActivity;
import com.example.pokedexapp.pokemonapi.PokeAPIBean;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;

public class PokemonViewActivity extends AppCompatActivity implements View.OnClickListener {
    private int pokemonId;
    private Pokemon chosenPokemon;
    private TextView pokemonName, pokemonIdTitle;
    private ImageView pokemonSprite, backArrow;
    private HorizontalBarChart statChart;
    private RecyclerView typeList;
    ArrayList<PokemonType> types = new ArrayList<>();
    TypeAdapter typeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_view);

        pokemonName = findViewById(R.id.pokemonViewName);
        pokemonIdTitle = findViewById(R.id.pokemonViewId);
        pokemonSprite = findViewById(R.id.pokemonSpriteBox);
        backArrow = findViewById(R.id.pokemonViewBackArrow);
        statChart = findViewById(R.id.pokemonViewStatChart);
        typeList = findViewById(R.id.pokemonViewTypeList);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        typeList.setLayoutManager(layoutManager);
        typeAdapter = new TypeAdapter(this, types);
        typeList.setAdapter(typeAdapter);
        typeAdapter.notifyDataSetChanged();
        typeList.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return true;
            }
        });

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

    private void setStats(final int hp, final int attack, final int defense, final int specialAttack, final int specialDefense, final int speed) {
        //https://stackoverflow.com/questions/41564371/android-drawing-horizontal-bar-charts-with-mpandroidchart
        statChart.getXAxis().setDrawGridLines(false);
        statChart.getAxisRight().setDrawGridLines(false);
        statChart.getAxisLeft().setDrawGridLines(false);
        statChart.getXAxis().setDrawAxisLine(false);
        statChart.getAxisRight().setDrawAxisLine(false);
        statChart.getAxisLeft().setDrawAxisLine(false);
        //statChart.getXAxis().setDrawLabels(false);
        statChart.getAxisRight().setDrawLabels(false);
        statChart.getAxisLeft().setDrawLabels(false);
        statChart.setDrawMarkers(false);
        statChart.setTouchEnabled(false);
        statChart.getDescription().setEnabled(false);
        statChart.getLegend().setEnabled(false);
        statChart.setExtraOffsets(12f, 0, 75f, 0);
        statChart.setDrawValueAboveBar(true);


        int[] values = {hp, attack, defense, specialAttack, specialDefense, speed};
        Arrays.sort(values);
        statChart.getAxisLeft().setAxisMaximum(values[values.length - 1]);
        statChart.getAxisLeft().setAxisMinimum(0);


        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, speed));
        entries.add(new BarEntry(1, specialDefense));
        entries.add(new BarEntry(2, specialAttack));
        entries.add(new BarEntry(3, defense));
        entries.add(new BarEntry(4, attack));
        entries.add(new BarEntry(5, hp));


        BarDataSet dataSet = new BarDataSet(entries, "stats");
        BarData data = new BarData(dataSet);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        data.setValueTextSize(12f);
        //https://github.com/PhilJay/MPAndroidChart/wiki/The-ValueFormatter-interface
        data.setValueFormatter(new LargeValueFormatter());

        statChart.setData(data);
        statChart.animateY(1000);

        statChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        statChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                String label = "";
                switch ((int) value) {
                    case (0):
                        label = "SPD";
                        break;
                    case (1):
                        label = "SDEF";
                        break;
                    case (2):
                        label = "SATK";
                        break;
                    case (3):
                        label = "DEF";
                        break;
                    case (4):
                        label = "ATK";
                        break;
                    case (5):
                        label = "HP";
                        break;
                    default:
                        label = "";
                        break;
                }
                return label;
            }
        });

    }

    public void setTypes(ArrayList<PokemonType> types) {
        Log.d("app", types.toString());
        for (PokemonType type : types) {
            this.types.add(type);
            typeAdapter.notifyItemInserted(this.types.size());
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
            pokemonIdTitle.setText("#" + Integer.toString(chosenPokemon.getId()));
            Glide.with(getApplicationContext()).load(chosenPokemon.getSprites().get("Male Front")).into(pokemonSprite);
            setStats(pokemon.getHp(), pokemon.getAttack(), pokemon.getDefense(), pokemon.getSpecialAttack(), pokemon.getSpecialDefense(), pokemon.getSpeed());
            setTypes(pokemon.getPokemonTypes());
            //isLoading = true;
        }
    }

}