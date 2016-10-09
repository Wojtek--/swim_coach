package com.example.swimcoachfinal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swimcoachfinal.R;
import com.example.swimcoachfinal.db.SqlOpenHelper;
import com.example.swimcoachfinal.model.Exercise;

import java.util.ArrayList;
import java.util.HashMap;

public class Training extends Activity {
    private Button use;
    private String name;
    private Long id;
    private TextView textView;
    private Intent intent;
    private Bundle bundle;
    private SqlOpenHelper sql;
    private ListView listview;
    private ArrayList<Exercise> allexe;
    private ArrayList<Integer> dis;
    private ArrayList<String> style;
    private HashMap<ArrayList<Integer>, ArrayList<String>> hash;
    private com.example.swimcoachfinal.model.Training training;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_view);
        textView = (TextView) findViewById(R.id.textView1);
        listview = (ListView) findViewById(R.id.listView1);
        use = (Button) findViewById(R.id.button1);
        sql = new SqlOpenHelper(this);
        intent = getIntent();
        bundle = intent.getExtras();
        id = bundle.getLong("id");
        name = bundle.getString("name");
        textView.setText(name);
        allexe = new ArrayList<Exercise>(sql.getExerciseForTrain(id));
        dis = new ArrayList<Integer>();
        style = new ArrayList<String>();
        for (Exercise e : allexe) {
            style.add(e.getStyle() + " " + e.getDistance());
        }
        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(Training.this, R.layout.list_item, R.id.textView1, style);
        listview.setAdapter(itemsAdapter);
        use.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                training = new com.example.swimcoachfinal.model.Training();
                training = sql.getTraining(id);
                sql.setMyActualTraining(training.getId());
                Toast.makeText(getApplicationContext(),
                        "Bieżący trening " + training.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.training_view, menu);
        return true;
    }
}
