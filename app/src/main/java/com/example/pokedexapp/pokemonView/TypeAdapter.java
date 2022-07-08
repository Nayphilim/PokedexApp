package com.example.pokedexapp.pokemonView;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokedexapp.R;
import com.example.pokedexapp.pokemon.Pokemon;
import com.example.pokedexapp.pokemon.PokemonType;
import com.example.pokedexapp.pokemonListView.PokemonListAdaptor;

import java.util.ArrayList;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<PokemonType> types;

    public TypeAdapter(Context context, ArrayList<PokemonType> types){
        this.context = context;
        this.types = types;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linearlayout_type, null);
        TypeAdapter.MyViewHolder viewHolder = new TypeAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final PokemonType type = types.get(position);

        if (type != null) {
            Log.d("app", "Type colour: " + Integer.toHexString(type.getColour()));
            holder.typeText.setText(type.toString());
            //https://stackoverflow.com/questions/23517879/set-background-color-programmatically
            holder.typeCard.setCardBackgroundColor(ContextCompat.getColor(context,type.getColour()));
        }
    }

    @Override
    public int getItemCount() {
        if (types != null) {
            return types.size();
        } else {
            return 0;
        }
    }


//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        final PokemonType type = types.get(i);
//        Log.d("app", "creating new type..");
//        if (view == null) {
//            final LayoutInflater layoutInflater = LayoutInflater.from(context);
//            view = layoutInflater.inflate(R.layout.linearlayout_type, null);
//        }
//
//        if(type != null) {
//            Log.d("app", type.toString());
//            final CardView typeCard = view.findViewById(R.id.type_card_view);
//            final TextView typeText = view.findViewById(R.id.type_text);
//
//            typeCard.setCardBackgroundColor(type.getColour());
//            typeText.setText(type.toString());
//
//        }
//
//        return view;
//    }

public class MyViewHolder extends RecyclerView.ViewHolder{

     CardView typeCard;
    TextView typeText;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        typeCard = itemView.findViewById(R.id.type_card_view);
        typeText = itemView.findViewById(R.id.type_text);

    }
}
}
