package com.example.swimcoachfinal.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.swimcoachfinal.R;
import com.example.swimcoachfinal.db.SqlOpenHelper;

public class TrainMenu extends Activity {
    private SqlOpenHelper sqlOpenHelper;
    private Intent nextScreen;
    private Button button1, button2, button3, button4, button5, button7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_menu);
        sqlOpenHelper = new SqlOpenHelper(this);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button7 = (Button) findViewById(R.id.button7);


        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                nextScreen = new Intent(getApplicationContext(),
                        YourTraining.class);
                startActivity(nextScreen);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                nextScreen = new Intent(getApplicationContext(), ListTrainingActivity.class);
                startActivity(nextScreen);

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Cursor k = sqlOpenHelper.getUserData();
                int train_id = 0;
                k.moveToNext();
                train_id = k.getInt(6);
                Bundle b = new Bundle();
                b.putInt("id", train_id);
                Log.d("ID MOJEGO TRENINGU", train_id + "");
                nextScreen = new Intent(getApplicationContext(), Done.class);
                nextScreen.putExtras(b);
                startActivity(nextScreen);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                nextScreen = new Intent(getApplicationContext(),
                        Statistics.class);
                startActivity(nextScreen);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Cursor k = sqlOpenHelper.getUserData();
                int train_id = 0;
                k.moveToNext();
                train_id = k.getInt(6);
                Bundle b = new Bundle();
                b.putInt("id", train_id);
                Log.d("ID MOJEGO TRENINGU", train_id + "");

                nextScreen = new Intent(getApplicationContext(),
                        YourTraining.class);
                nextScreen.putExtras(b);
                startActivity(nextScreen);
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                nextScreen = new Intent(getApplicationContext(),
                        MainMenu.class);
                startActivity(nextScreen);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.train_menu, menu);
        return true;
    }
}
