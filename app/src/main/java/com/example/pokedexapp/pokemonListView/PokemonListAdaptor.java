package com.example.pokedexapp.pokemonListView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.pokedexapp.R;
import com.example.pokedexapp.pokemon.Pokemon;
import com.example.pokedexapp.pokemonapi.PokeAPIBean;

import java.util.ArrayList;

public class PokemonListAdaptor extends RecyclerView.Adapter<PokemonListAdaptor.MyViewHolder>{
    private final Context mContext;
    private ArrayList<Pokemon> pokemonList = new ArrayList<>();
    private OnPokemonClickListener onPokemonClickListener;

    // Constructor
    public PokemonListAdaptor(Context c, ArrayList<Pokemon> pokemonList, OnPokemonClickListener onPokemonClickListener) {
        mContext = c;
        this.pokemonList = pokemonList;
        this.onPokemonClickListener = onPokemonClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linearlayout_pokemon, null);
        MyViewHolder viewHolder = new MyViewHolder(view, onPokemonClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Pokemon pokemon = pokemonList.get(position);

        if (pokemon != null) {
            holder.nameTextView.setText(pokemon.getName());
            holder.numberTextView.setText("#" + pokemon.getId());

            if (pokemon.getSprites() == null) {
                holder.pokemonSpriteImageView.setVisibility(View.GONE);
            } else {
                holder.pokemonSpriteImageView.setVisibility(View.VISIBLE);
                Glide.with(holder.pokemonSpriteImageView.getContext()).load(pokemon.getSprites().get("Male Front")).into(holder.pokemonSpriteImageView);

            }
        }


    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if (pokemonList != null) {
            return pokemonList.size();
        } else {
            return 0;
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTextView, numberTextView;
        ImageView pokemonSpriteImageView;
        OnPokemonClickListener onPokemonClickListener;

        public MyViewHolder(@NonNull View itemView, OnPokemonClickListener onPokemonClickListener) {
            super(itemView);

            pokemonSpriteImageView = (ImageView) itemView.findViewById(R.id.imageview_pokemon_sprite);

            nameTextView = (TextView) itemView.findViewById(R.id.textview_pokemon_name);
            numberTextView = (TextView) itemView.findViewById(R.id.textview_pokemon_number);

            this.onPokemonClickListener = onPokemonClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onPokemonClickListener.onPokemonClick(getAdapterPosition());
        }
    }

    public interface OnPokemonClickListener {
        void onPokemonClick(int position);
    }
}
