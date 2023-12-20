package dev.enzo.spinscore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class Policy extends AppCompatActivity {
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        // Initialize SharedPreferences
        pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        WebView webView = findViewById(R.id.webView);
        webView.loadUrl("https://sites.google.com/view/spin-score/home");

        Button agreeButton = findViewById(R.id.agreeButton);
        Button disagreeButton = findViewById(R.id.disagreeButton);

        agreeButton.setOnClickListener(v -> {
            // User agreed, set the preference
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("agreedToPolicy", true);
            editor.apply();

            // Start Lobby activity
            startActivity(new Intent(Policy.this, Lobby.class));
            finish();
        });

        disagreeButton.setOnClickListener(v -> {
            // User disagreed, close the app
            finishAffinity();
        });
    }
}
