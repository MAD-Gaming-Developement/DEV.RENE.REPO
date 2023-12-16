package dev.pikagame.libs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import dev.pikagame.GlobalWebSetting;
import dev.pikagame.R;

public class WebApp extends AppCompatActivity {

    SharedPreferences MyPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_app);

        MyPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        GlobalWebSetting webApp = findViewById(R.id.webApp);
        webApp.addJavascriptInterface(new JSInterface(this), "jsBridge");
        webApp.loadUrl(MyPrefs.getString("gameURL", ""));
    }
}