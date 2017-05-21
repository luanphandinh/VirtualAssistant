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
    public static final String SCHEDULE_COLUMN_DATE = "date";
    public static final String SCHEDULE_COLUMN_ACTION = "action";
    public static final String SCHEDULE_COLUMN_STARTIME = "startTime";
    public static final String SCHEDULE_COLUMN_ENDTIME = "endTime";

    public static final String DB_PROCESS_CREATE =
            "create table "
            + SCHEDULE_TABLE_NAME + "("
            + SCHEDULE_COLUMN_ID + " integer primary key autoincrement, "
            + SCHEDULE_COLUMN_ACTION + " text, "
            + SCHEDULE_COLUMN_STARTIME + " time, "
            + SCHEDULE_COLUMN_ENDTIME + " time, "
            + SCHEDULE_COLUMN_DATE + " date)";
    DBHelper dbHelper;
//
    public ScheduleHelper(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insertSchedule(String date, String action){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SCHEDULE_COLUMN_ACTION, action);
        contentValues.put(SCHEDULE_COLUMN_ACTION, action);
        contentValues.put(SCHEDULE_COLUMN_STARTIME, "null");
        contentValues.put(SCHEDULE_COLUMN_ENDTIME, "null");
        int id  = (int) db.insert(SCHEDULE_TABLE_NAME, null, contentValues);
        db.close();
        return id;
    }

    public int insertSchedule(String date, String action,String startTime,String endTime){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SCHEDULE_COLUMN_ACTION, action);
        contentValues.put(SCHEDULE_COLUMN_DATE, date);
        contentValues.put(SCHEDULE_COLUMN_STARTIME, startTime);
        contentValues.put(SCHEDULE_COLUMN_ENDTIME, endTime);
        int id  = (int) db.insert(SCHEDULE_TABLE_NAME, null, contentValues);
        db.close();
        return id;
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
                "select *" + " from " + SCHEDULE_TABLE_NAME +
                        " where " + SCHEDULE_COLUMN_DATE + " between "
                        + "'" + beginDay + "'" +" and " + "'" + endDay + "'"
                , null );
//        Cursor res =  db.rawQuery(
//                "select " +  LengthOfTimeHelper.LENGTH_OF_TIME_COLUMN_START_TIME + ", "
//                        + LengthOfTimeHelper.LENGTH_OF_TIME_COLUMN_END_TIME + ", "
//                        + SCHEDULE_COLUMN_ACTION
//                        + " from " + SCHEDULE_TABLE_NAME + "," + LengthOfTimeHelper.LENGTH_OF_TIME_TABLE_NAME
//                        + " where " + ScheduleHelper.SCHEDULE_TABLE_NAME +"." +ScheduleHelper.SCHEDULE_COLUMN_ID + " = " + LengthOfTimeHelper.SCHEDULE_COLUMN_ID
//                        + " and " + SCHEDULE_COLUMN_DATE + " between "
//                        + "'" + beginDay + "'" +" and " + "'" + endTime + "'"
//                , null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(SCHEDULE_COLUMN_STARTIME))
                    + "-"
                    + res.getString(res.getColumnIndex(SCHEDULE_COLUMN_ENDTIME))
                    + " : "
                    + res.getString(res.getColumnIndex(SCHEDULE_COLUMN_ACTION)));
//            Log.d("Lay Data",res.getString(res.getColumnIndex(SCHEDULE_COLUMN_ACTION)));
            res.moveToNext();
        }

        res.close();
        db.close();
        return array_list;
    }

//    public ArrayList<Schedule> getAllActions(String beginDay){
//        ArrayList<String> array_list = new ArrayList<String>();
//        Log.d("Lay Data","Bat dau lay");
//        //hp = new HashMap();
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor res =  db.rawQuery(
//                "select *" + " from " + SCHEDULE_TABLE_NAME +
//                        " where " + SCHEDULE_COLUMN_DATE + " between "
//                        + "'" + beginDay + "'" +" and " + "'" + beginDay + "'"
//                , null );
//        res.moveToFirst();
//        Schedule schedule = null;
//        while(res.isAfterLast() == false){
//
//            array_list.add(res.getString(res.getColumnIndex(SCHEDULE_COLUMN_ACTION)));
//            Log.d("Lay Data",res.getString(res.getColumnIndex(SCHEDULE_COLUMN_ACTION)));
//            res.moveToNext();
//        }
//
//        res.close();
//        db.close();
//        return array_list;
//    }

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

    public ArrayList<Schedule> getData(String date) {
        ArrayList<Schedule> list = new ArrayList<>();
//        Log.d("Lay Data","Bat dau lay");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * " + " from " + SCHEDULE_TABLE_NAME +
                " where " + SCHEDULE_COLUMN_DATE + " between "
                + "'" + date + "'" +" and " + "'" + date + "'",null );
        if(res != null && res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                int id = res.getInt(res.getColumnIndex(SCHEDULE_COLUMN_ID));
                String actions = res.getString(res.getColumnIndex(SCHEDULE_COLUMN_ACTION));
                String startTime = res.getString(res.getColumnIndex(SCHEDULE_COLUMN_STARTIME));
                String endTime = res.getString(res.getColumnIndex(SCHEDULE_COLUMN_ENDTIME));
                Schedule schedule = new Schedule(id, actions, startTime, endTime);
                Log.d("Test SCHEDULE", res.getString(res.getColumnIndex(SCHEDULE_COLUMN_ID)));
                list.add(schedule);
                res.moveToNext();
            }
        }

        res.close();
        db.close();
        return list;
    }
}
