package com.example.swimcoachfinal.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.swimcoachfinal.R;
import com.example.swimcoachfinal.db.SqlOpenHelper;
import com.example.swimcoachfinal.model.DataHolder;
import com.example.swimcoachfinal.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<DataHolder> {
    private ArrayList<Exercise> wszystkieZadania;
    private Context context;
    private ViewHolder viewHolder;
    private SqlOpenHelper sql;
    private Long id;
    private List<DataHolder> objects;

    public ListAdapter(Context context, int resourceId, long id, List<DataHolder> objects) {
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
            view = inflater.inflate(R.layout.list_group, null);

            viewHolder = new ViewHolder();
            viewHolder.data = new DataHolder(context);
            viewHolder.style_spinner = (Spinner) view.findViewById(R.id.spinner4);
            viewHolder.style_spinner.setAdapter(viewHolder.data.getStyleAdapter());
            viewHolder.distance_spinner = (Spinner) view.findViewById(R.id.spinner3);
            viewHolder.distance_spinner.setAdapter(viewHolder.data.getDistanceAdapter());
            viewHolder.button = (Button) view.findViewById(R.id.button);

            sql = new SqlOpenHelper(view.getContext());
            wszystkieZadania = new ArrayList<Exercise>(sql.getExerciseForTrain(id));

            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Usunieto zadanie", "" + wszystkieZadania.get(position).getId() + "z treningu " + id);
                    int i = sql.deleteExerciseFromTraining(wszystkieZadania.get(position).getId());
                    objects.remove(position);
                    wszystkieZadania.remove(position);
                    notifyDataSetChanged();

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

    }

    private void aktualizujStylZadania(int id) {
        Exercise e;
        e = sql.getExercise(id);
        e.setStyle(viewHolder.data.getStyle());
        e.setDistance(e.getDistance());
        sql.updateExercise(e);
    }

    static class ViewHolder {
        protected DataHolder data;
        private Spinner style_spinner;
        private Spinner distance_spinner;
        private Button button;
    }
}
