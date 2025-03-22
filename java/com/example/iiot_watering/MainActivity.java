package com.example.iiot_watering;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    TextView tx_temp, tx_wet, textView, textView1;
    Button btn_water;
    String result1 = "", result2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tx_temp = findViewById(R.id.tx_temp);
        tx_wet = findViewById(R.id.tx_wet);
        textView = findViewById(R.id.tx_temp);
        textView1 = findViewById(R.id.tx_wet);
        btn_water = findViewById(R.id.btn_water);

        btn_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(mutiThread);
                thread.start();
            }
        });

    }

    private Runnable mutiThread = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL("http://192.168.50.69:3000/string_test");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.connect();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        JSONArray dataJson = new JSONArray(line);
                        int i = dataJson.length() - 1;
                        JSONObject info = dataJson.getJSONObject(i);
                        String time = info.getString("time");
                        String dB = info.getString("dB");
                        result1 = time;
                        result2 = dB;
                    }
                    inputStream.close();
                }
            } catch (Exception e) {
                result1 = e.toString();
                result2 = e.toString();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tx_temp.setText(result1);
                    tx_wet.setText(result2);
                }
            });

        }
    };
}
