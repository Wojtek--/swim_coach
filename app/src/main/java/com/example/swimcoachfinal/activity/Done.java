package com.example.swimcoachfinal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.swimcoachfinal.adapter.ListDone;
import com.example.swimcoachfinal.R;
import com.example.swimcoachfinal.db.SqlOpenHelper;
import com.example.swimcoachfinal.model.DataHolder;
import com.example.swimcoachfinal.model.Exercise;
import com.example.swimcoachfinal.model.Training;

import java.util.ArrayList;
import java.util.List;

public class Done extends Activity {
    public static Button button;
    private TextView textView, textView1;
    private ListView listView;
    private SqlOpenHelper sql;
    private ArrayList<Exercise> allexe;
    private List<DataHolder> dataHolders;
    private DataHolder dataHolder;
    private ArrayList<String> style;
    private List<Integer> dystanse;
    private ListDone listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        button = (Button) findViewById(R.id.button8);
        textView = (TextView) findViewById(R.id.textView);
        textView1 = (TextView) findViewById(R.id.textView6);
        listView = (ListView) findViewById(R.id.listView);
        sql = new SqlOpenHelper(this);
        Bundle extras = getIntent().getExtras();
        prepareListData();
        if (extras != null) {
            Integer id = null;
            id = extras.getInt("id");
            if (id != -1) {
                Training przechwycony = sql.getTraining(id);
                textView.setText("    " + przechwycony.getName());
                allexe = new ArrayList<Exercise>(sql.getExercisesForTraining(przechwycony.getName()));
                dataHolders = new ArrayList<DataHolder>();

                for (Exercise e : allexe) {
                    dataHolder = new DataHolder(Done.this);
                    dataHolder.setSelectedDistance(dystanse.indexOf(e.getDistance()));
                    dataHolder.setSelectedStyle(style.indexOf(e.getStyle()));
                    dataHolders.add(dataHolder);
                }

                listAdapter = new ListDone(getApplicationContext(), R.layout.list_done_item, id, dataHolders);
                listView.setAdapter(listAdapter);

            } else {
                listView.setVisibility(View.INVISIBLE);
                button.setVisibility(View.INVISIBLE);
                textView.setText("Nie posiadasz bieżącego treningu");
                textView1.setVisibility(View.INVISIBLE);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    private void prepareListData() {
        style = new ArrayList<String>();
        style.add("Dowolny");
        style.add("Grzbietowy");
        style.add("Klasyczny");
        style.add("Motylkowy");
        List<Integer> tmp = new ArrayList<Integer>();
        for (int i = 1; i <= 60; i++) {
            tmp.add(25 * i);
        }
        dystanse = new ArrayList<Integer>(tmp);
    }
}
