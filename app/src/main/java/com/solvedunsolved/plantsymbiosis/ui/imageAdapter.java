package com.solvedunsolved.plantsymbiosis.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.solvedunsolved.plantsymbiosis.Model.Images;
import com.solvedunsolved.plantsymbiosis.R;

import java.util.ArrayList;
import java.util.List;

public class imageAdapter extends SliderViewAdapter<imageAdapter.SliderAdapterVH> {

    private Context context;
    private List<Images> mImagess = new ArrayList<>();

    public imageAdapter(Context context) {
        this.context = context;
    }

    public void renewItems(List<Images> Imagess) {
        this.mImagess = Imagess;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mImagess.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(Images Images) {
        this.mImagess.add(Images);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        Images Images = mImagess.get(position);

        viewHolder.textViewDescription.setText(Images.getDescription());
        viewHolder.textViewDescription.setTextSize(16);
        viewHolder.textViewDescription.setTextColor(Color.WHITE);
        Glide.with(viewHolder.itemView)
                .load(Images.getImageUrl())
                .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getCount() {
        return mImagess.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}