package chongxuocmanhinh.virtualassistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by L on 22/04/2017.
 */

public class ScheduleHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "ScheduleDatabase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String SCHEDULE_TABLE_NAME = "schedule";
    public static final String SCHEDULE_COLUMN_ID = "id";
    public static final String SCHEDULE_COLUMN_DATE = "name";
    public static final String SCHEDULE_COLUMN_ACTION = "email";

    private static final String DB_PROCESS_CREATE =
            "create table "
            + SCHEDULE_TABLE_NAME + "("
            + SCHEDULE_COLUMN_ID + " integer primary key autoincrement, "
            + SCHEDULE_COLUMN_ACTION + " text, "
            + SCHEDULE_COLUMN_DATE + " date)";


    public ScheduleHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_PROCESS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS schedule");
        onCreate(db);
    }

    public boolean insertSchedule(String date, String action){
        Log.d("Test Insert","do insert!");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SCHEDULE_COLUMN_ACTION, action);
        contentValues.put(SCHEDULE_COLUMN_DATE, date);
        db.insert(SCHEDULE_TABLE_NAME, null, contentValues);
        Log.d("Test Insert","Insert successfully");
        db.close();
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + SCHEDULE_TABLE_NAME + " where id = "+ id + "", null );
        db.close();
        return res;
    }

    public ArrayList<String> getAllActions(String beginDay,String endDay){
        ArrayList<String> array_list = new ArrayList<String>();
        Log.d("Lay Data","Bat dau lay");
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(
                "select " +SCHEDULE_COLUMN_ACTION + " from " + SCHEDULE_TABLE_NAME +
                        " where " + SCHEDULE_COLUMN_DATE + " between "
                        + "'" + beginDay + "'" +" and " + "'" + endDay + "'"
                , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(SCHEDULE_COLUMN_ACTION)));
            Log.d("Lay Data",res.getString(res.getColumnIndex(SCHEDULE_COLUMN_ACTION)));
            res.moveToNext();
        }

        res.close();
        db.close();
        return array_list;
    }

    public ArrayList<String> getAllSchedule() {
        ArrayList<String> array_list = new ArrayList<String>();
        Log.d("Lay Data","Bat dau lay");
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + SCHEDULE_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(SCHEDULE_COLUMN_DATE)));
            Log.d("Lay Data",res.getString(res.getColumnIndex(SCHEDULE_COLUMN_DATE)));
            res.moveToNext();
        }

        res.close();
        db.close();
        return array_list;
    }
}
