package com.example.swimcoachfinal.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.swimcoachfinal.model.Exercise;
import com.example.swimcoachfinal.model.Training;

import java.util.ArrayList;

public class SqlOpenHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "swim.db";
    public static final int VERSION = 1;
    public static final String USER_TABLE = "user_data";
    public static final String TRAIN_TABLE = "training";
    public static final String EXE_TABEL = "exercise";
    public static final String TREX_TABEL = "train_exer";
    public static final String STAT_TABEL = "stat";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";
    public static final String SEX = "sex";
    public static final String TRAIN_ID = "train_id";
    public static final String STYLE = "style";
    public static final String TRAINING_ID = "id_train";
    public static final String EXERCISE_ID = "id_exer";
    public static final String DISTANCE = "distance";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String LAST_TRAIN = "last_train";
    public static final String WEEK = "week";
    public static final String TIME = "time";
    public static final String DONE = "done";
    public static final String UNDONE = "undone";
    private static final String LOG = "SqlOpenHelper";


    public SqlOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +
                        USER_TABLE + " (" +
                        ID + " INTEGER PRIMARY KEY, " +
                        NAME + " TEXT, " +
                        AGE + " INTEGER, " +
                        HEIGHT + " INTEGER, " +
                        WEIGHT + " INTEGER, " +
                        SEX + " TEXT, " +
                        TRAIN_ID + " INTEGER);"
        );
        db.execSQL("CREATE TABLE " +
                        TRAIN_TABLE + " (" +
                        ID + " INTEGER PRIMARY KEY, " +
                        NAME + " TEXT );"
        );
        db.execSQL("CREATE TABLE " +
                        EXE_TABEL + " (" +
                        ID + " INTEGER PRIMARY KEY, " +
                        DISTANCE + " INTEGER, " +
                        STYLE + " TEXT );"
        );
        db.execSQL("CREATE TABLE " +
                        TREX_TABEL + " (" +
                        ID + " INTEGER PRIMARY KEY, " +
                        TRAINING_ID + " INTEGER, " +
                        EXERCISE_ID + " INTEGER );"
        );
        db.execSQL("CREATE TABLE " +
                        STAT_TABEL + " (" +
                        ID + " INTEGER PRIMARY KEY, " +
                        YEAR + " INTEGER, " +
                        MONTH + " INTEGER, " +
                        LAST_TRAIN + " INTEGER, " +
                        WEEK + " INTEGER, " +
                        TIME + " INTEGER, " +
                        DONE + " INTEGER, " +
                        UNDONE + " INTEGER);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TRAIN_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EXE_TABEL);
        db.execSQL("DROP TABLE IF EXISTS " + TREX_TABEL);
        db.execSQL("DROP TABLE IF EXISTS " + STAT_TABEL);
        onCreate(db);

    }

    // wprowadzenie danych uzytkownika do tabeli
    public void insertData(String imie, int wiek, int wzrost, int waga, String plec, long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + USER_TABLE);
        ContentValues wartosci = new ContentValues();
        wartosci.put(NAME, imie);
        wartosci.put(AGE, wiek);
        wartosci.put(HEIGHT, wzrost);
        wartosci.put(WEIGHT, waga);
        wartosci.put(SEX, plec);
        wartosci.put(TRAIN_ID, id);
        db.insert(USER_TABLE, null, wartosci);

    }

    // wstawienie do tabeli z danymi uzytkownika id treningu
    public void setMyActualTraining(long train_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues wartosci = new ContentValues();
        Cursor k = getUserData();
        k.moveToNext();
        wartosci.put(TRAIN_ID, train_id);
        db.update(USER_TABLE, wartosci, ID + " = ?", new String[]{String.valueOf(k.getInt(0))});
    }

    // pobranie aktualnego treningu uzytkownika
    public long getMyActualTraining() {
        SQLiteDatabase db = this.getReadableDatabase();
        String kolumna[] = {TRAIN_ID};
        Cursor cursor = db.query(USER_TABLE, kolumna, null, null, null, null, null);
        cursor.moveToNext();

        long train_id = cursor.getInt(0);

        return train_id;
    }


    // pobranie danych o uzytkowniku
    public Cursor getUserData() {
        String[] kolumny = {ID, NAME, AGE, HEIGHT, WEIGHT, SEX, TRAIN_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE, kolumny, null, null, null, null, null);
        return cursor;
    }

    public Cursor getStatistics() {
        String[] kolumny = {ID, YEAR, MONTH, LAST_TRAIN, WEEK, TIME, DONE, UNDONE};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(STAT_TABEL, kolumny, null, null, null, null, null);
        return cursor;
    }

    // wprowadzenie statystyk
    public void insertStatistics(int rok, int miesiac, long last_train, int week, int time, int done, int undone) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + STAT_TABEL);
        ContentValues wartosci = new ContentValues();
        wartosci.put(YEAR, rok);
        wartosci.put(MONTH, miesiac);
        wartosci.put(LAST_TRAIN, last_train);
        wartosci.put(WEEK, week);
        wartosci.put(TIME, time);
        wartosci.put(DONE, done);
        wartosci.put(UNDONE, undone);

        db.insert(STAT_TABEL, null, wartosci);
    }


    //stworzenie pojedynczego zadania
    public long createExercise(int distance, String style) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues wartosci = new ContentValues();
        wartosci.put(DISTANCE, distance);
        wartosci.put(STYLE, style);
        long exercise_id = db.insert(EXE_TABEL, null, wartosci);
        return exercise_id;
    }

    // pobranie pojedynczego zadania
    public Exercise getExercise(long exercise_id) {
        String selectQuery = "SELECT  * FROM " + EXE_TABEL + " WHERE "
                + ID + " = " + exercise_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        Exercise exercise = new Exercise();
        exercise.setId(c.getInt((c.getColumnIndex(ID))));
        exercise.setDistance((c.getInt(c.getColumnIndex(DISTANCE))));
        exercise.setStyle(c.getString(c.getColumnIndex(STYLE)));

        return exercise;
    }

    // pobranie wszystkich zadan z tabeli
    public ArrayList<Exercise> getAllExercises() {
        ArrayList<Exercise> all = new ArrayList<Exercise>();
        String selectQuery = "SELECT  * FROM " + EXE_TABEL;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Exercise exercise = new Exercise();
                exercise.setId(c.getInt((c.getColumnIndex(ID))));
                exercise.setDistance((c.getInt(c.getColumnIndex(DISTANCE))));
                exercise.setStyle(c.getString(c.getColumnIndex(STYLE)));

                all.add(exercise);
            } while (c.moveToNext());
        }
        return all;
    }

    // aktualizacja zadania
    public int updateExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues wartosci = new ContentValues();
        wartosci.put(DISTANCE, exercise.getDistance());
        wartosci.put(STYLE, exercise.getStyle());

        // updating row
        return db.update(EXE_TABEL, wartosci, ID + " = ?",
                new String[]{String.valueOf(exercise.getId())});
    }

    // usuniecie pojedynczego zadania
    public void deleteExercise(long exercise_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EXE_TABEL, ID + " = ?",
                new String[]{String.valueOf(exercise_id)});
    }

    // stworzenie pojedynczego treningu
    public long createTraining(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues wartosci = new ContentValues();
        wartosci.put(NAME, name);
        long training_id = db.insert(TRAIN_TABLE, null, wartosci);
        return training_id;

    }

    // pobranie pojedynczego treningu
    public Training getTraining(long train_id) {
        String selectQuery = "SELECT  * FROM " + TRAIN_TABLE + " WHERE "
                + ID + " = " + train_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        Training training = new Training();
        training.setId(c.getInt((c.getColumnIndex(ID))));
        training.setName((c.getString(c.getColumnIndex(NAME))));

        return training;
    }


    // pobranie treningu przez nazwe
    public Training getTrainingByName(String name) {
        String selectQuery = "SELECT  * FROM " + TRAIN_TABLE + " WHERE "
                + NAME + " = '" + name + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        Training training = new Training();
        training.setId(c.getInt((c.getColumnIndex(ID))));
        training.setName((c.getString(c.getColumnIndex(NAME))));

        return training;
    }

    // pobranie wszystkich treningow
    public ArrayList<Training> getAllTrainings() {
        ArrayList<Training> all = new ArrayList<Training>();
        String selectQuery = "SELECT  * FROM " + TRAIN_TABLE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Training training = new Training();
                training.setId(c.getInt((c.getColumnIndex(ID))));
                training.setName((c.getString(c.getColumnIndex(NAME))));
                all.add(training);
            } while (c.moveToNext());
        }
        return all;
    }

    // aktualizacja treningu
    public int updateTraining(Training training) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues wartosci = new ContentValues();
        wartosci.put(NAME, training.getName());

        // updating row
        return db.update(TRAIN_TABLE, wartosci, ID + " = ?",
                new String[]{String.valueOf(training.getId())});
    }

    // dodanie zadania do treningu
    public long addExerciseToTraining(long exercise_id, long training_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues wartosci = new ContentValues();
        wartosci.put(TRAINING_ID, training_id);
        wartosci.put(EXERCISE_ID, exercise_id);

        long id = db.insert(TREX_TABEL, null, wartosci);

        return id;
    }

    // usuniecie zadania z treningu
    public int deleteExerciseFromTraining(long id_exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        deleteExercise(id_exercise);
        return db.delete(TREX_TABEL, ID + " = ?",
                new String[]{String.valueOf(id_exercise)});
    }

    // pobranie zadan dla treningu
    public ArrayList<Exercise> getExercisesForTraining(String training_name) {
        ArrayList<Exercise> all = new ArrayList<Exercise>();

        String selectQuery = "SELECT  * FROM " + EXE_TABEL + " ex, "
                + TRAIN_TABLE + " tr, " + TREX_TABEL + " te WHERE tr."
                + NAME + " = '" + training_name + "'" + " AND tr." + ID
                + " = " + "te." + TRAINING_ID + " AND ex." + ID + " = "
                + "te." + EXERCISE_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Exercise exercise = new Exercise();
                exercise.setId(c.getInt((c.getColumnIndex(ID))));
                exercise.setDistance((c.getInt(c.getColumnIndex(DISTANCE))));
                exercise.setStyle(c.getString(c.getColumnIndex(STYLE)));

                all.add(exercise);
            } while (c.moveToNext());
        }

        return all;
    }

    public void deleteTraining(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Exercise> all = new ArrayList<Exercise>();
        all = getExerciseForTrain((long) id);
        for (Exercise e : all) {
            deleteExerciseFromTraining(e.getId());
        }
        db.delete(TRAIN_TABLE, ID + " = ?",
                new String[]{String.valueOf(id)});

    }

    public ArrayList<Exercise> getExerciseForTrain(Long id) {
        ArrayList<Exercise> all = new ArrayList<Exercise>();

        String selectQuery = "SELECT  * FROM " + EXE_TABEL + " ex, "
                + TRAIN_TABLE + " tr, " + TREX_TABEL + " te WHERE tr."
                + ID + " = '" + id + "'" + " AND tr." + ID
                + " = " + "te." + TRAINING_ID + " AND ex." + ID + " = "
                + "te." + EXERCISE_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Exercise exercise = new Exercise();
                exercise.setId(c.getInt((c.getColumnIndex(ID))));
                exercise.setDistance((c.getInt(c.getColumnIndex(DISTANCE))));
                exercise.setStyle(c.getString(c.getColumnIndex(STYLE)));

                all.add(exercise);
            } while (c.moveToNext());
        }
        return all;
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
