package com.solvedunsolved.plantsymbiosis.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.solvedunsolved.plantsymbiosis.Activities.YourSymbiosis.Details;
import com.solvedunsolved.plantsymbiosis.Data.SymbiosisDB;
import com.solvedunsolved.plantsymbiosis.Model.Plant;
import com.solvedunsolved.plantsymbiosis.Model.Plantpair;
import com.solvedunsolved.plantsymbiosis.R;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class RVAdapterSymbiosis extends RecyclerView.Adapter<RVAdapterSymbiosis.ViewHolder> {
    private Context context;
    private List<Plantpair> plantpairItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RVAdapterSymbiosis(Context context, List<Plantpair> plantpairItems) {
        this.context = context;
        this.plantpairItems = plantpairItems;
    }

    @Override
    public RVAdapterSymbiosis.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.symbiosis_list_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RVAdapterSymbiosis.ViewHolder holder, int position) {

        Plantpair plantpair = plantpairItems.get(position);

        holder.plantpairName1.setText(plantpair.getName1());
        holder.plantpairName2.setText(plantpair.getName2());
        holder.plantpairCategory.setText("Category: " +plantpair.getCategory());
        holder.imageview.setImageBitmap(getImage(plantpair.getImg1()));
        holder.imageview3.setImageBitmap(getImage(plantpair.getImg2()));
    }

    @Override
    public int getItemCount() {
        return plantpairItems.size();
    }

    // add functionalites here

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView plantpairName1;
        public TextView plantpairName2;
        public TextView plantpairCategory;
        public TextView plantpairRelationship;
        public ImageView imageview;
        public ImageView imageview3;
        public Button SymDel;
        public int id;


        public ViewHolder(View view, Context ctx) {
            super(view);
            declare(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Plantpair plantpair = plantpairItems.get(position);

                    Intent intent = new Intent(context, Details.class);
                    intent.putExtra("id", plantpair.getId());

                    context.startActivity(intent);
                    CustomIntent.customType(context, "left-to-right");
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Plantpair plantpair = plantpairItems.get(position);
            switch (v.getId()) {
                case R.id.SymDel:
                    deleteItem(plantpair.getId());
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
                    SymbiosisDB db = new SymbiosisDB(context);
                    //delete item
                    db.deletePlantpair(id);
                    plantpairItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();
                }
            });
        }

        public void declare(View view)
        {
            plantpairName1 = view.findViewById(R.id.Plantpairname1);
            plantpairName2 = view.findViewById(R.id.Plantpairname2);
            plantpairCategory = view.findViewById(R.id.SymCategory);
            plantpairRelationship = view.findViewById(R.id.symRelationship);
            imageview=view.findViewById(R.id.imageView);
            imageview3=view.findViewById(R.id.imageView3);
            SymDel = (Button) view.findViewById(R.id.SymDel);
            SymDel.setOnClickListener(this);
        }
    }
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public void filterlist(ArrayList<Plantpair> filteredlist) {
        plantpairItems=filteredlist;
        notifyDataSetChanged();
    }
}
