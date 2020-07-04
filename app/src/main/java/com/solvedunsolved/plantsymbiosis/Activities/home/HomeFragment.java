package com.solvedunsolved.plantsymbiosis.Activities.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.solvedunsolved.plantsymbiosis.Activities.Jsonsymbiosis.Json_symbiosis;
import com.solvedunsolved.plantsymbiosis.Activities.Menu.Feedback;
import com.solvedunsolved.plantsymbiosis.Activities.YourSymbiosis.symbiosis;
import com.solvedunsolved.plantsymbiosis.Activities.yourplants.Plants;
import com.solvedunsolved.plantsymbiosis.Model.Images;
import com.solvedunsolved.plantsymbiosis.R;
import com.solvedunsolved.plantsymbiosis.ui.imageAdapter;



public class HomeFragment extends Fragment {
    SliderView sliderView;
    private imageAdapter adapter;
    CardView cardView1;
    CardView cardView2;
    CardView cardView4;
    CardView cardView;
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cardView1 = (CardView) view.findViewById(R.id.cardView1);
        cardView2 = (CardView) view.findViewById(R.id.cardView2);
        cardView4 = (CardView) view.findViewById(R.id.cardView4);
        cardView = (CardView) view.findViewById(R.id.cardView);
        sliderView = view.findViewById(R.id.imageSlider);

        slider();
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Plants.class);
                startActivity(intent);
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Json_symbiosis.class ));
            }
        });


        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Feedback.class));
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), symbiosis.class));
            }
        });

        return view;
    }

    private void slider() {
        adapter = new imageAdapter(getActivity());
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.DROP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        Images images = new Images();
        images.setDescription("A Pink Rose");
        images.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        adapter.addItem(images);

        Images images2 = new Images();
        images2.setDescription("Harmony in Garden");
        images2.setImageUrl("https://get.pxhere.com/photo/plants-green-white-background-white-decoration-design-plant-succulents-background-wallpaper-flowerpot-product-design-houseplant-1418858.jpg");
        adapter.addItem(images2);

        Images images3 = new Images();
        images3.setDescription("Plant Taxonomy");
        images3.setImageUrl("https://cdn.britannica.com/42/91642-050-332E5C66/Keukenhof-Gardens-Lisse-Netherlands.jpg");
        adapter.addItem(images3);

        Images images4 = new Images();
        images4.setDescription("Environmental Relationships");
        images4.setImageUrl("https://static3.bigstockphoto.com/4/5/3/large1500/354230642.jpg");
        adapter.addItem(images4);

        Images images5 = new Images();
        images5.setDescription("Painting : modern garden");
        images5.setImageUrl("https://i.guim.co.uk/img/media/10cf557aba700ba58163b925db73d6e62ee614bb/17_17_2763_2473/master/2763.jpg?width=1920&quality=85&auto=format&fit=max&s=4fef0b6814533f1df605bd00641934df");
        adapter.addItem(images5);
    }

}
