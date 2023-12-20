package dev.jon.kawaiislot;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import pl.droidsonroids.gif.GifImageView;
public class splash extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024,1024);
        setContentView(R.layout.activity_splash);

        mediaPlayer = MediaPlayer.create(this, R.raw.win);
        mediaPlayer.start();

        GifImageView gifImageView = findViewById(R.id.gifImageView);
        gifImageView.setImageResource(R.drawable.kawaii);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(splash.this,MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the media player when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}