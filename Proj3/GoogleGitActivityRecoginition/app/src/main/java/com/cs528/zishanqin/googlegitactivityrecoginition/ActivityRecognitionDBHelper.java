package com.cs528.zishanqin.googlegitactivityrecoginition;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zishanqin on 3/15/16.
 */
public class ActivityRecognitionDBHelper extends SQLiteOpenHelper {
    private static final String TAG="ActivityRecognitionDBHelper";
    private static final int VERSION=1;
    private static final String DATABASE_NAME = "activityRecognitionBase.db";

    public ActivityRecognitionDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ActivityRecognitionSchema.ActivityRecognitionTable.Name + "(" +
//                        " _id integer primary key autoincrement, " +
                        ActivityRecognitionSchema.ActivityRecognitionTable.Cols.START_TIME + ", " +
                        ActivityRecognitionSchema.ActivityRecognitionTable.Cols.STATE +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ActivityRecognitionSchema.ActivityRecognitionTable.Name);
        onCreate(db);

    }
    public void insert(Date time, String state){
        ContentValues newRecord=new ContentValues();
        newRecord.put(ActivityRecognitionSchema.ActivityRecognitionTable.Cols.START_TIME,time.getHours()+":"+time.getMinutes());
        newRecord.put(ActivityRecognitionSchema.ActivityRecognitionTable.Cols.STATE, state);
        this.getWritableDatabase().insert(ActivityRecognitionSchema.ActivityRecognitionTable.Name, null,newRecord);
    }
    public void printDB(){


        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ActivityRecognitionSchema.ActivityRecognitionTable.Name, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(DatabaseUtils.dumpCurrentRowToString(res));
            res.moveToNext();
        }
        Log.w("#################printDB###############", array_list.toArray().toString());
        //return array_list;
    }

}
