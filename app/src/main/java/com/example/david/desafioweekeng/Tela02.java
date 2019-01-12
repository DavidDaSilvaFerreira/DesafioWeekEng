package com.example.david.desafioweekeng;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Tela02 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela02);
        if(SingletonClima.getInstance().getWeather().equals(getString(R.string.rain))){
            ((TextView)findViewById(R.id.textSuggestion)).setText(R.string.pizza);
            ((ImageView)findViewById(R.id.imageSuggestion)).setImageResource(R.drawable.pizza);
        }
    }
}
