package com.example.kimseungchul.wifiscantest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by kimseungchul on 15. 10. 7..
 */
public class TitleActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                Intent myIntent = new Intent(TitleActivity.this, APConnect.class);
                startActivity(myIntent);

                finish();
            }
        }, 2000);
    }




}