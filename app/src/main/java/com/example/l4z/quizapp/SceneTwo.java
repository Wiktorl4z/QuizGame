package com.example.l4z.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SceneTwo extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_two);
    }

    public void goToNext (View view){
        Intent toMainMenu = new Intent(this, SceneThree.class);
        startActivity(toMainMenu);

    }
}
