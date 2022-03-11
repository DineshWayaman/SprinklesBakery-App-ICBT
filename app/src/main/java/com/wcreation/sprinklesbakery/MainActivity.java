package com.wcreation.sprinklesbakery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txtMain;
    Button btnGetStart;
    SharedPreferences sharedPreferences, sharedPreferencesGet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);



        sharedPreferencesGet = getSharedPreferences("GetStarted", MODE_PRIVATE);
        String firsttimeOpen = sharedPreferencesGet.getString("firsttime", "");

        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String userName = sharedPreferences.getString("username", "");

        if (userName.length()!=0){
            startActivity(new Intent(MainActivity.this, UserHome.class));
        }
        if (firsttimeOpen.equals("opened")){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }


        txtMain = findViewById(R.id.fastdel);
        btnGetStart = findViewById(R.id.btnGetStart);

        String mainText = "The Fastest In \nDelivery Food";

        SpannableString sP = new SpannableString(mainText);

        ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);

        sP.setSpan(fcsRed, 24, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtMain.setText(sP);

        btnGetStart.setOnClickListener(view -> {
            SharedPreferences.Editor spEditor = sharedPreferencesGet.edit();
            spEditor.putString("firsttime", "opened");
            spEditor.commit();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        });
    }
}