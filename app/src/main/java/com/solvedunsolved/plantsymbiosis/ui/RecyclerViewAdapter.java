package com.solvedunsolved.plantsymbiosis.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.solvedunsolved.plantsymbiosis.Activities.yourplants.DetailsActivity;
import com.solvedunsolved.plantsymbiosis.Activities.yourplants.ListActivity;
import com.solvedunsolved.plantsymbiosis.Data.PlantDB;
import com.solvedunsolved.plantsymbiosis.Model.JsonSymbiosis;
import com.solvedunsolved.plantsymbiosis.Model.Plant;
import com.solvedunsolved.plantsymbiosis.R;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.startActivity;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Plant> plantItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Plant> plantItems) {
        this.context = context;
        this.plantItems = plantItems;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plant_list_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        Plant plant = plantItems.get(position);
        holder.plantName.setText(plant.getName());
        holder.Category.setText("Category: " + plant.getCategory());
        holder.plantimg.setImageBitmap(getImage(plant.getImg()));
    }

    @Override
    public int getItemCount() {
        return plantItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView plantName;
        public TextView Category;
        public ImageView plantimg;
        public TextView plantDescription;
        public Button deleteButton;
        public int id;


        public ViewHolder(View view, Context ctx) {
            super(view);
            declare(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Plant plant = plantItems.get(position);

                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("id", plant.getId());

                    context.startActivity(intent);
                    CustomIntent.customType(context, "left-to-right");
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Plant plant = plantItems.get(position);
            switch (v.getId()) {
                case R.id.deleteButton:
                    deleteItem(plant.getId());
                    break;
            }
        }
        public void deleteItem(final int id) {

            //create an AlertDialog
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton = (Button) view.findViewById(R.id.noButton);
            Button yesButton = (Button) view.findViewById(R.id.yesButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();


            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlantDB db = new PlantDB(context);
                    db.deletePlant(id);
                    //TODO: delete photo
                    plantItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();
                }
            });

        }


        public void declare(View view)
        {
            plantName = view.findViewById(R.id.plantname);
            Category = view.findViewById(R.id.category);
            plantDescription = view.findViewById(R.id.DescriptionDet);
            plantimg=view.findViewById(R.id.plantimage);
            deleteButton = (Button) view.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(this);
        }

    }
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public void filterlist(ArrayList<Plant> filteredlist) {
        plantItems=filteredlist;
        notifyDataSetChanged();
    }
}
