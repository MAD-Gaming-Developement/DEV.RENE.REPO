package dev.enzo.spinscore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.VideoView;
import android.content.Context;
import android.content.SharedPreferences;

public class Splashscreen extends AppCompatActivity {
    Handler handler = new Handler();
    VideoView splashui;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        // Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize VideoView
        splashui = findViewById(R.id.splashui);
        pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Set video path (replace "your_video_path" with the actual path or URL)
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splashui);
        splashui.setVideoURI(videoUri);

        // Display splash screen for a second
        handler.postDelayed(() -> {
            // Start video playback
            splashui.start();

            // Add an OnCompletionListener to start the next activity after video completion
            splashui.setOnCompletionListener(mediaPlayer -> {
                if (pref.getBoolean("agreedToPolicy", false)) {
                    // User has agreed to the policy, go to Lobby activity
                    Intent intent = new Intent(Splashscreen.this, Lobby.class);
                    // Add the transition animation here
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                    startActivity(intent);
                } else {
                    // User has not agreed to the policy, go to Policy activity
                    Intent intent = new Intent(Splashscreen.this, Policy.class);
                    // Add the transition animation here
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                    startActivity(intent);
                }
                finish();
            });
        }, 3000);
    }
}
