package com.example.swimcoachfinal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.swimcoachfinal.R;
import com.example.swimcoachfinal.adapter.ListTraining;
import com.example.swimcoachfinal.db.SqlOpenHelper;
import com.example.swimcoachfinal.model.Training;

import java.util.ArrayList;
import java.util.List;

public class ListTrainingActivity extends Activity {
    private ListView list;
    private Button usun;
    private ArrayList<String> listDataHeader;
    private SqlOpenHelper sql;
    private List<Training> alltrain;
    private Intent nextScreen;
    private TextView nazwa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examples);
        usun = (Button) findViewById(R.id.button6);
        nazwa = (TextView) findViewById(R.id.textView1);
        list = (ListView) findViewById(R.id.listView1);
        sql = new SqlOpenHelper(this);
        listDataHeader = new ArrayList<String>();
        alltrain = sql.getAllTrainings();

        ListTraining listTraining = new ListTraining(getApplicationContext(), alltrain);
        list.setAdapter(listTraining);
        sql.closeDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.examples, menu);
        return true;
    }
}
