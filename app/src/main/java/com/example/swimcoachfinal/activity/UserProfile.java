package com.example.swimcoachfinal.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swimcoachfinal.R;
import com.example.swimcoachfinal.db.SqlOpenHelper;

public class UserProfile extends Activity {
    private RadioButton radioSexButton;
    private String plec, imie;
    private int nr, wiek, wzrost, waga, train_id;
    private EditText editText1, editText2, editText3, editText4;
    private TextView textView;
    private RadioGroup radioSexGroup;
    private SqlOpenHelper sql;
    private Button button1;
    private Intent nextScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        sql = new SqlOpenHelper(this);
        button1 = (Button) findViewById(R.id.button1);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        textView = (TextView) findViewById(R.id.textView5);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                radioSexButton = (RadioButton) findViewById(selectedId);
                Integer wiek = Integer.parseInt(editText2.getText().toString());
                Integer wzrost = Integer.parseInt(editText3.getText().toString());
                Integer waga = Integer.parseInt(editText4.getText().toString());
                if (wiek < 7 || wiek > 110) {
                    editText2.setError("Błędne dane");
                } else if (wzrost < 100 || wzrost > 250) {
                    editText3.setError("Błędne dane");
                } else if (waga < 10 || waga > 450) {
                    editText4.setError("Błędne dane");
                } else  {
                    sql.insertData(editText1.getText().toString(),
                            wiek, wzrost, waga,
                            radioSexButton.getText().toString().toLowerCase(),
                            sql.getMyActualTraining());
                    Toast.makeText(getApplicationContext(), "Pomyślnie zmieniono dane", Toast.LENGTH_SHORT).show();
                    nextScreen = new Intent(getApplicationContext(),
                            MainMenu.class);
                    startActivity(nextScreen);
                }
            }
        });

        Cursor k = sql.getUserData();
        while (k.moveToNext()) {
            nr = k.getInt(0);
            imie = k.getString(1);
            wiek = k.getInt(2);
            wzrost = k.getInt(3);
            waga = k.getInt(4);
            plec = k.getString(5);
            train_id = k.getInt(6);
        }
        editText1.setText(imie, TextView.BufferType.EDITABLE);
        editText2.setText(String.valueOf(wiek), TextView.BufferType.EDITABLE);
        editText3.setText(String.valueOf(wzrost), TextView.BufferType.EDITABLE);
        editText4.setText(String.valueOf(waga), TextView.BufferType.EDITABLE);
        Log.d("Id biezacego treningu", train_id + "");
        if (train_id == -1) {
            textView.setText("Nie masz bieżącego treningu.");
        } else {
            String name = sql.getTraining(train_id).getName();
            if (!name.equals("")) {
                textView.setText("Nazwa twojego bieżącego treningu: " + name + ".");
            }
        }

        if (plec.equals(null) || plec.equals("mezczyzna")) {
            radioSexGroup.check(R.id.radio0);
        }
        if (plec.equals("kobieta")) {
            radioSexGroup.check(R.id.radio1);
        }
        sql.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_profile, menu);
        return true;
    }

}
