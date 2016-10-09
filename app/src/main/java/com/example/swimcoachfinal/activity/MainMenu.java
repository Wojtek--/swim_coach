package com.example.swimcoachfinal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.swimcoachfinal.R;

public class MainMenu extends Activity {
    private Intent nextScreen;
    private Button button1, button2, button3, button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                nextScreen = new Intent(getApplicationContext(),
                        UserProfile.class);
                startActivity(nextScreen);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                nextScreen = new Intent(getApplicationContext(),
                        TrainMenu.class);
                startActivity(nextScreen);

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                nextScreen = new Intent(getApplicationContext(),
                        Description.class);
                startActivity(nextScreen);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
                System.exit(0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
