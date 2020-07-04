package com.solvedunsolved.plantsymbiosis.Activities.Menu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.solvedunsolved.plantsymbiosis.Model.Images;
import com.solvedunsolved.plantsymbiosis.R;
import com.solvedunsolved.plantsymbiosis.ui.imageAdapter;

import maes.tech.intentanim.CustomIntent;

public class Feedback extends AppCompatActivity {
    private SeekBar seekbar;
    Button submit;
    TextView result;
    SliderView sliderView;
    private imageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        CustomIntent.customType(this, "fadein-to-fadeout");
        sliderView = findViewById(R.id.imageSlider);
        seekbar= findViewById(R.id.seekBar);
        submit=findViewById(R.id.submit);
        result=findViewById(R.id.result);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        slider();

        result.setText("Rating : "+seekbar.getProgress()+ "/"+ seekbar.getMax());

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // on progress
                result.setTextColor(Color.DKGRAY);
                if(seekbar.getProgress()>=7)
                {result.setTextColor(Color.BLUE);}
                else
                {result.setTextColor(Color.DKGRAY);}
                result.setText("Rating : "+seekbar.getProgress()+ "/"+ seekbar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // on start tracking touch
                //result.setText("Rating : "+seekbar.getProgress()+ seekbar.getMax());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // on stop
                if(seekbar.getProgress()>=7)
                {result.setTextColor(Color.BLUE);}

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbar.setProgress(0);
                Toast.makeText(Feedback.this, "Thanks for feedback", Toast.LENGTH_LONG).show();
                // save feedback value
            }
        });
    }

    private void slider() {
        adapter = new imageAdapter(this);
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

    public void finish() {
        super.finish();
        CustomIntent.customType(this, "fadein-to-fadeout");//fadein-to-fadeout
    }

}