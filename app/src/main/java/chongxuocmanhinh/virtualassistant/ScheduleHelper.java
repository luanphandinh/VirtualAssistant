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

public class ScheduleHelper {
    public static final String SCHEDULE_TABLE_NAME = "schedule";
    public static final String SCHEDULE_COLUMN_ID = "id";
    public static final String SCHEDULE_COLUMN_DATE = "name";
    public static final String SCHEDULE_COLUMN_ACTION = "email";

    public static final String DB_PROCESS_CREATE =
            "create table "
            + SCHEDULE_TABLE_NAME + "("
            + SCHEDULE_COLUMN_ID + " integer primary key autoincrement, "
            + SCHEDULE_COLUMN_ACTION + " text, "
            + SCHEDULE_COLUMN_DATE + " date)";
    DBHelper dbHelper;
//
    public ScheduleHelper(Context context) {
        dbHelper = new DBHelper(context);
    }

    public boolean insertSchedule(String date, String action){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SCHEDULE_COLUMN_ACTION, action);
        contentValues.put(SCHEDULE_COLUMN_DATE, date);
        db.insert(SCHEDULE_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + SCHEDULE_TABLE_NAME + " where id = "+ id + "", null );
        db.close();
        return res;
    }

    public ArrayList<String> getAllActions(String beginDay,String endDay){
        ArrayList<String> array_list = new ArrayList<String>();
        Log.d("Lay Data","Bat dau lay");
        //hp = new HashMap();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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

    public Cursor getData(String beginDay,String endDay){
        ArrayList<String> array_list = new ArrayList<String>();
        Log.d("Lay Data","Bat dau lay");
        //hp = new HashMap();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res =  db.rawQuery(
                "select * " + " from " + SCHEDULE_TABLE_NAME +
                        " where " + SCHEDULE_COLUMN_DATE + " between "
                        + "'" + beginDay + "'" +" and " + "'" + endDay + "'"
                , null );
        db.close();
        return res;
    }

    public Integer deleteData(Integer id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(SCHEDULE_TABLE_NAME,
                SCHEDULE_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllSchedule() {
        ArrayList<String> array_list = new ArrayList<String>();
        Log.d("Lay Data","Bat dau lay");
        //hp = new HashMap();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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
