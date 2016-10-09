package com.example.swimcoachfinal.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.example.swimcoachfinal.R;
import com.example.swimcoachfinal.db.SqlOpenHelper;
import com.example.swimcoachfinal.activity.Done;
import com.example.swimcoachfinal.activity.Statistics;
import com.example.swimcoachfinal.model.DataHolder;
import com.example.swimcoachfinal.model.Exercise;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ListDone extends ArrayAdapter<DataHolder> {
    private ArrayList<Exercise> wszystkieZadania;
    private Context context;
    private ViewHolder viewHolder;
    private SqlOpenHelper sql;
    private Long id;
    private List<DataHolder> objects;
    private HashMap<Integer, Boolean> map;
    private Intent nextScreen;
    private Dialog dialog;
    private Button button;
    private NumberPicker numberPicker;

    public ListDone(Context context, int resourceId, long id, List<DataHolder> objects) {
        super(context, resourceId, objects);
        this.context = context;
        this.id = id;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.list_done_item, null);

            viewHolder = new ViewHolder();
            viewHolder.data = new DataHolder(context);
            viewHolder.style_spinner = (Spinner) view.findViewById(R.id.spinner5);
            viewHolder.style_spinner.setAdapter(viewHolder.data.getStyleAdapter());
            viewHolder.distance_spinner = (Spinner) view.findViewById(R.id.spinner6);
            viewHolder.distance_spinner.setAdapter(viewHolder.data.getDistanceAdapter());
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkBox);

            sql = new SqlOpenHelper(view.getContext());
            wszystkieZadania = new ArrayList<Exercise>(sql.getExerciseForTrain(id));
            map = new HashMap<Integer, Boolean>();
            for (Exercise e : wszystkieZadania) {
                map.put(e.getId(), false);
            }

            dialog = new Dialog(parent.getContext());
            dialog.setContentView(R.layout.timepicker);
            dialog.setTitle(R.string.czaspodaj);
            button = (Button) dialog.findViewById(R.id.button9);
            numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
            numberPicker.setMaxValue(240);
            numberPicker.setMinValue(0);
            numberPicker.setValue(60);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    int t = numberPicker.getValue();
                    sql.setMyActualTraining(-1);
                    int rok = 0, msc = 0, week = 0, time = 0, done = 0, undone = 0;
                    long last_train = 0;
                    Cursor k = sql.getStatistics();
                    while (k.moveToNext()) {
                        rok = k.getInt(1);
                        msc = k.getInt(2);
                        last_train = k.getInt(3);
                        week = k.getInt(4);
                        time = k.getInt(5);
                        done = k.getInt(6);
                        undone = k.getInt(7);
                    }
                    int d = 0, u = 0, przeplyniete = 0;
                    for (int i = 0; i < map.size(); i++) {
                        if (map.get(wszystkieZadania.get(i).getId()).equals(false)) {
                            u++;
                        } else {
                            d++;
                            przeplyniete += wszystkieZadania.get(i).getDistance();
                        }
                    }
//                    if do last_train
                    Date data = new Date();
                    long dataInMiliSeconds = data.getTime();
                    int SEC = 1000;
                    int MIN = SEC * 60;
                    int HOUR = MIN * 60;
                    int DAY = HOUR * 24;
                    long WEEK = DAY * 7;
                    long MONTH = WEEK * 4;
                    long YEAR = MONTH * 12;
                    if ((dataInMiliSeconds/DAY) - last_train < WEEK ) {
                        rok += przeplyniete;
                        msc += przeplyniete;
                        week += przeplyniete;
                    } else if ((dataInMiliSeconds/DAY) - last_train < MONTH) {
                        rok += przeplyniete;
                        msc += przeplyniete;
                        week = 0;
                    } else if ((dataInMiliSeconds/DAY) - last_train < YEAR) {
                        rok += przeplyniete;
                        msc = 0;
                        week = 0;
                    } else {
                        rok = 0;
                        msc = 0;
                        week = 0;
                    }
                    last_train = (int) dataInMiliSeconds;
                    Log.d("STATY", dataInMiliSeconds+" "+last_train+" "+WEEK+" "+MONTH+" "+YEAR);
                    Log.d("STATY", przeplyniete+" "+rok+" "+msc+" "+week);

                    done += d;
                    undone += u;
                    time += t;
                    sql.insertStatistics(rok, msc, last_train, week, time, done, undone);
                    nextScreen = new Intent(context, Statistics.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(nextScreen);

                }
            });

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (((CheckBox) view).isChecked()) {
                        map.put(wszystkieZadania.get(position).getId(), true);
                    } else {
                        map.put(wszystkieZadania.get(position).getId(), false);
                    }
                }
            });

            Done.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();
                }
            });

            viewHolder.style_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    viewHolder.data.setSelectedStyle(arg2);
                    try {
                        aktualizujStylZadania(wszystkieZadania.get(position).getId());
                    } catch (IndexOutOfBoundsException e) {
                        Log.d("IndexOutOfBoundsException", e + "");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }

            });
            viewHolder.distance_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    viewHolder.data.setSelectedDistance(arg2);
                    try {
                        aktualizujDystansZadania(wszystkieZadania.get(position).getId());
                    } catch (IndexOutOfBoundsException e) {
                        Log.d("IndexOutOfBoundsException", e + "");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }

            });
            view.setTag(viewHolder);

        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.style_spinner.setSelection(getItem(position).getSelectedStyle());
        holder.distance_spinner.setSelection(getItem(position).getSelectedDistance());
        return view;
    }

    private void aktualizujDystansZadania(int id) {
        Exercise e;
        e = sql.getExercise(id);
        e.setStyle(e.getStyle());
        e.setDistance(viewHolder.data.getDistance());
        sql.updateExercise(e);
        wszystkieZadania = new ArrayList<Exercise>(sql.getExerciseForTrain(this.id));

    }

    private void aktualizujStylZadania(int id) {
        Exercise e;
        e = sql.getExercise(id);
        e.setStyle(viewHolder.data.getStyle());
        e.setDistance(e.getDistance());
        sql.updateExercise(e);
        wszystkieZadania = new ArrayList<Exercise>(sql.getExerciseForTrain(this.id));
    }

    static class ViewHolder {
        protected DataHolder data;
        private Spinner style_spinner;
        private Spinner distance_spinner;
        private CheckBox checkBox;
    }
}


