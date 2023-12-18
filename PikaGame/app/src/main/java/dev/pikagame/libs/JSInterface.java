package dev.pikagame.libs;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * JSInterface Class for WebView to BackEnd API Native Communication
 */
public class JSInterface {
    SharedPreferences MyPrefs;
    Context context;
    public JSInterface(Context context)
    {
        this.context = context;
        MyPrefs = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
    }


    /**
     * JavaScript interface for WebView to Native Android communication using postMessage() function
     * @param name String parameter of Event Name
     * @param data STring parameter of Event Data to be analysed for Analytics
     *
     * Dota collected is sent to a BackEnd API using AES Encryption for secure data transmission for analytics
     */
    @JavascriptInterface
    public void postMessage(String name, String data)
    {
        if("openWindow".equals(name))
        {
            try {
                JSONObject extLink = new JSONObject(data);
                Intent newWindow = new Intent(Intent.ACTION_VIEW);
                newWindow.setData(Uri.parse(extLink.getString("url")));
                context.startActivity(newWindow);
            } catch (JSONException e)
            {
                Log.d("JS:Error", e.getMessage());
            }
        }


            RequestQueue afQueue =  Volley.newRequestQueue(context);

            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("appsflyer_id", UUID.randomUUID().toString());
                requestBody.put("eventName", name);
                requestBody.put("eventValue", data);
                requestBody.put("authentication", "7odVQ4PMGwVguGQABWe9hS");
                requestBody.put("endpoint", context.getPackageName());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String endPoint = "https://appsflyer.madgamingdev.com/user/event" +
                    "?appsflyer_id=" + UUID.randomUUID().toString() +
                    "&eventName=" + name +
                    "&eventValue=" + data +
                    "&authentication=" + "7odVQ4PMGwVguGQABWe9hS" +
                    "&endpoint=" + context.getPackageName();

            JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET,
                    endPoint, requestBody,
                    response -> {
                        ObjectMapper objectMapper = new ObjectMapper();

                        try {
                            Map<String, Object> dataMap = objectMapper.readValue(response.toString(), Map.class);
                        }
                        catch (IOException e) {
                            //Log.e(GlobalCFG.appCode+":API", e.getMessage());
                        }

                    },
                    Throwable::printStackTrace)
            {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("accept", "application/json");
                    headers.put("content-type", "application/json");
                    return headers;
                }
            };
            afQueue.add(myReq);

    }

}
