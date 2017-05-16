package chongxuocmanhinh.virtualassistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by L on 16/05/2017.
 */

public class LengthOfTimeHelper {
    public static final String LENGTH_OF_TIME_TABLE_NAME = "lengthOfTime";
    public static final String LENGTH_OF_TIME_COLUMN_ID = "id";
    public static final String SCHEDULE_COLUMN_ID = "idSchedule";
    public static final String LENGTH_OF_TIME_COLUMN_START_TIME = "startTime";
    public static final String LENGTH_OF_TIME_COLUMN_END_TIME = "endTime";

    public static final String DB_PROCESS_CREATE =
            "create table "
                    + LENGTH_OF_TIME_TABLE_NAME + "("
                    + LENGTH_OF_TIME_COLUMN_ID + " integer primary key autoincrement, "
                    + SCHEDULE_COLUMN_ID + " integer, "
                    + LENGTH_OF_TIME_COLUMN_START_TIME + " date, "
                    + LENGTH_OF_TIME_COLUMN_END_TIME + " date,"
    +" FOREIGN KEY ("+LengthOfTimeHelper.SCHEDULE_COLUMN_ID+") REFERENCES "+ScheduleHelper.SCHEDULE_TABLE_NAME+"("+ScheduleHelper.SCHEDULE_COLUMN_ID+")) ";
    DBHelper dbHelper;
    public LengthOfTimeHelper(Context context) {
        dbHelper = new DBHelper(context);
    }

    public boolean insertLengthOfTime(int idSchedule,String startDate, String endDate){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(LengthOfTimeHelper.SCHEDULE_COLUMN_ID, idSchedule);
        contentValues.put(LENGTH_OF_TIME_COLUMN_START_TIME, startDate);
        contentValues.put(LENGTH_OF_TIME_COLUMN_END_TIME, endDate);
        db.insert(LENGTH_OF_TIME_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public LengthOfTime getData(int idSchedule) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + LENGTH_OF_TIME_TABLE_NAME
                                + " where "+ SCHEDULE_COLUMN_ID + " = " + idSchedule + " ", null );
        LengthOfTime lengthOfTime = null;
        if(res.moveToFirst()){
            int id = res.getInt(res.getColumnIndex(LENGTH_OF_TIME_COLUMN_ID));
            String startDate = res.getString(res.getColumnIndex(LENGTH_OF_TIME_COLUMN_START_TIME));
            String endDate = res.getString(res.getColumnIndex(LENGTH_OF_TIME_COLUMN_END_TIME));
            DateFormat formatter = new SimpleDateFormat("HH:mm");
            Time startTime = null,endTime = null;
            try {
                startTime = new Time(formatter.parse(startDate).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                endTime = new Time(formatter.parse(endDate).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            lengthOfTime = new LengthOfTime(id,startTime,endTime);
        }
        db.close();
        res.close();
        return lengthOfTime;
    }
}
