package dev.enzo.spinscore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class Lobby extends AppCompatActivity {

    ImageButton btnPlay;
    ImageButton btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        //Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnPlay = findViewById(R.id.btnPlay);
        btnExit = findViewById(R.id.btnExit);

        btnPlay();
        btnExit();
    }
    private void btnPlay(){
        btnPlay.setOnClickListener(v -> {
            Intent intent = new Intent(Lobby.this, MainActivity.class);
            startActivity(intent);
        });
    }
    private void btnExit(){
        btnExit.setOnClickListener(v -> finishAffinity());
    }
}