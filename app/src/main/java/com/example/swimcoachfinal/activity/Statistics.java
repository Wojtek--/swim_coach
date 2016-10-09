package com.example.swimcoachfinal.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.swimcoachfinal.R;
import com.example.swimcoachfinal.db.SqlOpenHelper;

import java.text.DecimalFormat;

public class Statistics extends Activity {
    private SqlOpenHelper sql;
    private TextView rok_l, rok_n, msc_l, msc_n, lw_l, lw_n, we_l, we_n, ti_l, ti_n, pe_l, pe_n;
    private Button powrot, resetuj;
    private Intent nextScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        sql = new SqlOpenHelper(this);
        powrot = (Button) findViewById(R.id.button11);
        resetuj = (Button) findViewById(R.id.button10);
        rok_l = (TextView) findViewById(R.id.textView7);
        rok_n = (TextView) findViewById(R.id.textView8);
        msc_l = (TextView) findViewById(R.id.textView9);
        msc_n = (TextView) findViewById(R.id.textView10);
        we_l = (TextView) findViewById(R.id.textView13);
        we_n = (TextView) findViewById(R.id.textView14);
        ti_l = (TextView) findViewById(R.id.textView15);
        ti_n = (TextView) findViewById(R.id.textView16);
        pe_l = (TextView) findViewById(R.id.textView17);
        pe_n = (TextView) findViewById(R.id.textView18);
        int rok = 0, msc = 0, ostatni_trening= 0, week = 0, time = 0, done = 0, undone = 0;

        Cursor k = sql.getStatistics();
        while (k.moveToNext()) {
            rok = k.getInt(1);
            msc = k.getInt(2);
            ostatni_trening = k.getInt(3);
            week = k.getInt(4);
            time = k.getInt(5);
            done = k.getInt(6);
            undone = k.getInt(7);
        }
        rok_n.setText(rok + " m");
        msc_n.setText(msc + " m");
        we_n.setText(week + " m");
        ti_n.setText(time + " min");
        DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");
        double d;
        if (done + undone == 0) {
            d = 0;
        } else {
            d = (double) done / (done + undone) * 100;
        }
        pe_n.setText(REAL_FORMATTER.format(d) + " %");

        powrot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextScreen = new Intent(getApplicationContext(),
                        TrainMenu.class);
                startActivity(nextScreen);
            }
        });

        resetuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sql.insertStatistics(0, 0, 0, 0, 0, 0, 0);
                rok_n.setText(0 + " m");
                msc_n.setText(0 + " m");
                we_n.setText(0 + " m");
                ti_n.setText(0 + " min");
                pe_n.setText(0 + " %");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.statistics, menu);
        return true;
    }
}
