package com.example.swimcoachfinal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swimcoachfinal.R;
import com.example.swimcoachfinal.db.SqlOpenHelper;
import com.example.swimcoachfinal.activity.YourTraining;
import com.example.swimcoachfinal.model.Training;

import java.util.List;

public class ListTraining extends BaseAdapter {
    private SqlOpenHelper sqlOpenHelper;
    private Context context;
    private List<Training> allTraining;
    private Button usun;
    private TextView nazwa;
    private Intent nextScreen;

    public ListTraining(Context context, List allTraining) {
        this.context = context;
        this.allTraining = allTraining;
    }

    @Override
    public int getCount() {
        return allTraining.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View row = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        row = inflater.inflate(R.layout.list_item, parent, false);
        sqlOpenHelper = new SqlOpenHelper(parent.getContext());

        usun = (Button) row.findViewById(R.id.button6);
        usun.setTag(allTraining.get(position).getId());
        nazwa = (TextView) row.findViewById(R.id.textView1);
        nazwa.setText(allTraining.get(position).getName());
        nazwa.setTag(allTraining.get(position).getId());

        long id = sqlOpenHelper.getMyActualTraining();
        if (allTraining.get(position).getId() == id) {
            row.setBackgroundColor(Color.parseColor("#33B5E5"));
            nazwa.setTextColor(Color.BLACK);

        }

        nazwa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.textView1) {
                    nextScreen = new Intent(parent.getContext(), YourTraining.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    nextScreen.putExtra("id", (Integer) view.getTag());
                    context.startActivity(nextScreen);
                }
            }
        });
        usun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.button6) {

                    long my_id = sqlOpenHelper.getMyActualTraining();
                    Integer id = (int) (long) my_id;
                    if ((view.getTag()).equals(id)) {
                        Log.d("Usunieto biezacy", my_id + " " + view.getTag());
                        sqlOpenHelper.setMyActualTraining(Integer.valueOf(-1));
                    }
//                    DODAC ALERT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    sqlOpenHelper.deleteTraining((Integer) view.getTag());

                    allTraining.remove(position);
                    Toast.makeText(parent.getContext(), "Usunięto trening", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            }
        });
        return row;
    }

}
