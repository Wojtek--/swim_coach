package com.example.swimcoachfinal.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swimcoachfinal.adapter.ListAdapter;
import com.example.swimcoachfinal.R;
import com.example.swimcoachfinal.db.SqlOpenHelper;
import com.example.swimcoachfinal.model.DataHolder;
import com.example.swimcoachfinal.model.Exercise;
import com.example.swimcoachfinal.model.Training;

import java.util.ArrayList;
import java.util.List;

public class YourTraining extends Activity {
    private Button addExercise = null, dodajDialog, use;
    private Dialog dialog;
    private EditText editText;
    private SqlOpenHelper sql;
    private long training_id, exercise_id;
    private TextView nazwa;
    private Intent nextScreen;
    private Training training;
    private ListView listview;
    private ArrayList<Exercise> allexe;
    private ArrayList<String> style;
    private List<Integer> dystanse;
    private Spinner spinner, spinner2;
    private List<DataHolder> dataHolders;
    private DataHolder dataHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_training);
        addExercise = (Button) findViewById(R.id.button1);
        nazwa = (TextView) findViewById(R.id.textView1);
        use = (Button) findViewById(R.id.button2);
        listview = (ListView) findViewById(R.id.listView1);
        editText = (EditText) findViewById(R.id.editText1);
        prepareListData();
        sql = new SqlOpenHelper(this);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            Integer id = null;
            id = extras.getInt("id");
            if (id != -1) {
                Training przechwycony = sql.getTraining(id);
                editText.setText(przechwycony.getName());
                editText.setFocusable(false);
                long my_id = sql.getMyActualTraining();
                Integer my_id2 = (int) (long) my_id;
                getActionBar().setTitle("Edytuj trening");

                if ((my_id2).equals(id)) {
                    use.setText("To jest Twój trening");
                    use.setEnabled(false);
                    getActionBar().setTitle("Edytuj swój trening");
                }

                allexe = new ArrayList<Exercise>(sql.getExercisesForTraining(przechwycony.getName()));
                dataHolders = new ArrayList<DataHolder>();

                for (Exercise e : allexe) {
                    dataHolder = new DataHolder(YourTraining.this);
                    dataHolder.setSelectedDistance(dystanse.indexOf(e.getDistance()));
                    dataHolder.setSelectedStyle(style.indexOf(e.getStyle()));
                    dataHolders.add(dataHolder);
                }

                ListAdapter listAdapter = new ListAdapter(getApplicationContext(), R.layout.list_group, id, dataHolders);
                listview.setAdapter(listAdapter);
                Log.d("Id biezacego treningu ", id + " ");
                training_id = id;
                extras = null;
            } else {
                use.setVisibility(View.INVISIBLE);
                addExercise.setVisibility(View.INVISIBLE);
                listview.setVisibility(View.INVISIBLE);
                editText.setVisibility(View.INVISIBLE);
                nazwa.setText("Nie posiadasz bieżącego treningu");

            }
        }

        use.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (!editText.getText().toString().equals("")) {
                    sql.setMyActualTraining(training_id);
                    training = new Training();
                    training = sql.getTraining(training_id);
                    Toast.makeText(getApplicationContext(),
                            "Bieżący trening " + training.getName(),
                            Toast.LENGTH_SHORT).show();
                    nextScreen = new Intent(getApplicationContext(),
                            TrainMenu.class);
                    startActivity(nextScreen);
                } else {
                    Toast.makeText(getApplicationContext(), "Wpisz nazwę treningu.", Toast.LENGTH_LONG).show();
                }
            }
        });
        addExercise.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().equals("")) {
                    if (checkExists(editText.getText().toString())) {
                        training_id = sql.createTraining(editText.getText().toString());
                    } else {
                        training_id = sql.getTrainingByName(editText.getText().toString()).getId();
                    }
                    dialog = new Dialog(YourTraining.this);
                    dialog.setContentView(R.layout.mydialog);
                    dialog.setTitle(R.string.new_task);

                    dodajDialog = (Button) dialog.findViewById(R.id.button2);
                    spinner = (Spinner) dialog.findViewById(R.id.spinner);
                    spinner2 = (Spinner) dialog.findViewById(R.id.spinner2);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(YourTraining.this, android.R.layout.simple_spinner_item, style);
                    ArrayAdapter<Integer> arrayAdapter2 = new ArrayAdapter<Integer>(YourTraining.this, android.R.layout.simple_spinner_item, dystanse);
                    spinner.setAdapter(arrayAdapter);
                    spinner2.setAdapter(arrayAdapter2);


                    dodajDialog.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            editText.setFocusable(false);
                            exercise_id = sql.createExercise((Integer) spinner2.getSelectedItem(), spinner.getSelectedItem().toString());
                            sql.addExerciseToTraining(exercise_id, training_id);
                            allexe = new ArrayList<Exercise>(sql.getExerciseForTrain(training_id));
                            dataHolders = new ArrayList<DataHolder>();

                            for (Exercise e : allexe) {
                                dataHolder = new DataHolder(YourTraining.this);
                                dataHolder.setSelectedDistance(dystanse.indexOf(e.getDistance()));
                                dataHolder.setSelectedStyle(style.indexOf(e.getStyle()));
                                dataHolders.add(dataHolder);
                            }

                            ListAdapter listAdapter = new ListAdapter(getApplicationContext(), R.layout.list_group, training_id, dataHolders);
                            listview.setAdapter(listAdapter);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Wpisz nazwę treningu.", Toast.LENGTH_LONG).show();
                }

            }
        });
        sql.closeDB();
    }

    private Boolean checkExists(String name) {
        Boolean ans = true;
        ArrayList<Training> arrayAllTraining = new ArrayList<Training>(sql.getAllTrainings());
        for (Training training : arrayAllTraining) {
            if (training.getName().equals(name)) {
                ans = false;
                break;
            }
        }
        return ans;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.your_training, menu);
        return true;
    }
}
