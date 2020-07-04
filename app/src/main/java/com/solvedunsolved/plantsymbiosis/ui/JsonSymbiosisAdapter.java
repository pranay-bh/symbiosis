package com.solvedunsolved.plantsymbiosis.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.solvedunsolved.plantsymbiosis.Activities.Jsonsymbiosis.Json_symbyosis_deatails;
import com.solvedunsolved.plantsymbiosis.Model.JsonSymbiosis;
import com.solvedunsolved.plantsymbiosis.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class JsonSymbiosisAdapter extends  RecyclerView.Adapter<JsonSymbiosisAdapter.ViewHolder> {
    private Context context;
    private List<JsonSymbiosis> plantList;

    public JsonSymbiosisAdapter(Context context, List<JsonSymbiosis> plants) {
        this.context = context;
        plantList = plants;
    }

    @NonNull
    @Override
    public JsonSymbiosisAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.json_symbiosis_listrow, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(JsonSymbiosisAdapter.ViewHolder holder, int position) {
        JsonSymbiosis plant = plantList.get(position);

        String photoUrl=plant.getPhotoURL();
        holder.plantname.setText(plant.getName());
        holder.helpedby.setText(plant.getHelpedby());
        holder.helps.setText(plant.getHelps());

        Picasso.get()
                .load(photoUrl)
                .placeholder(android.R.drawable.ic_popup_sync)
                .into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView plantname;
        ImageView photo;
        TextView helpedby;
        TextView helps;
        public ViewHolder(View itemView,final Context ctx) {
            super(itemView);

            plantname = itemView.findViewById(R.id.name);
            photo = itemView.findViewById(R.id.photoUrl);
            helpedby = itemView.findViewById(R.id.helpedby);
            helps = itemView.findViewById(R.id.helps);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JsonSymbiosis plant = plantList.get(getAdapterPosition());
                    Intent intent = new Intent(context, Json_symbyosis_deatails.class);
                    intent.putExtra("id",plant.getPlantid());
                    ctx.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
        }


}
    public void filterlist(ArrayList<JsonSymbiosis> filteredlist) {
        plantList=filteredlist;
        notifyDataSetChanged();
    }
}