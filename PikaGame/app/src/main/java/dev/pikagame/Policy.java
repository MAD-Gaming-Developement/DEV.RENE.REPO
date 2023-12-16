package dev.pikagame;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

public class Policy extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences;
        WebView policy;
        Button denied;
        Button accept;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        boolean accepted = preferences.getBoolean("accepted", false);
        if (accepted) {
            moveToMainActivity();
        }

        policy = findViewById(R.id.policy);
        accept = findViewById(R.id.accept);
        denied = findViewById(R.id.reject);

        policy.loadUrl("file:///android_res/raw/policy.html");

        accept.setOnClickListener(view -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("accepted", true);
            editor.apply();
            moveToMainActivity();
        });

        denied.setOnClickListener(view -> finishAffinity());
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(Policy.this, SplashScreen.class);
        startActivity(intent);
        finish(); // Finish this activity so that pressing back won't bring it back
    }
}
