package chongxuocmanhinh.virtualassistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by L on 04/05/2017.
 */

public class DBHelper extends SQLiteOpenHelper{
        public static String DATABASE_NAME = "ScheduleDatabase.db";
        public static final int DATABASE_VERSION = 1;

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys = ON;");
            db.execSQL(ScheduleHelper.DB_PROCESS_CREATE);
            db.execSQL(CustomizedScheduleHelper.DB_PROCESS_CREATE);
            db.execSQL(LengthOfTimeHelper.DB_PROCESS_CREATE);
            insertDefaultCustomizedSchedule(db,"ngày tôi thích nhất","ngày 10 tháng 5");
            insertDefaultCustomizedSchedule(db,"đầu tuần sau","thứ 2 tuần sau");
            insertDefaultCustomizedSchedule(db,"cuối tuần sau","chủ nhật tuần sau");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS schedule");
            db.execSQL("DROP TABLE IF EXISTS customizedSchedule");
            db.execSQL("DROP TABLE IF EXISTS lengthOfTime");
            onCreate(db);
        }

    public boolean insertDefaultCustomizedSchedule(SQLiteDatabase db,String alias, String value){
        ContentValues contentValues = new ContentValues();

        contentValues.put(CustomizedScheduleHelper.CUSTOMIZEDSCHEDULE_COLUMN_ALIAS, alias);
        contentValues.put(CustomizedScheduleHelper.CUSTOMIZEDSCHEDULE_COLUMN_VALUE, value);
        db.insert(CustomizedScheduleHelper.CUSTOMIZEDSCHEDULE_TABLE_NAME, null, contentValues);
        return true;
    }


}
