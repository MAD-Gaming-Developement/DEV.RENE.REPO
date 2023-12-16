package dev.pikagame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000; // 2 seconds
    SharedPreferences MyPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        MyPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);


        RequestQueue connectAPI = Volley.newRequestQueue(this);
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("appid", "PG");
            requestBody.put("package", getPackageName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String endPoint = "https://backend.madgamingdev.com/api/gameid" + "?appid=PG&package=" + getPackageName();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, endPoint, requestBody,
                response -> {
                    Log.d("API:RESPONSE", response.toString());

                    try {


                        GlobalCFG.gameURL = response.getString("gameURL");
                        GlobalCFG.policyURL = response.getString("policyURL");

                        if(!Boolean.parseBoolean(mSharedPreferences.getString("permitSendData","false")))
                        {
                            UserDataPolicy dialogFragment = new UserDataPolicy();
                            dialogFragment.setUserDataPolicyListener(this);
                            dialogFragment.show(getSupportFragmentManager(), "UserDataConsent");

                        }
                        else
                            startApp();

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }, error -> {
            Log.d("API:RESPONSE", error.getMessage());
        });

        connectAPI.add(jsonRequest);
    }

        // Using a Handler to delay the transition to the next activity
        new Handler().postDelayed(() -> {
            // Connect API
            // SharedPrefs save gameURL



            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close this activity
        }, SPLASH_TIME_OUT);
    }
}
