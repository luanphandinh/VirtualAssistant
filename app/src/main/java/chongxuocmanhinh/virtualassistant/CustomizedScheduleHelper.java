package chongxuocmanhinh.virtualassistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by L on 01/05/2017.
 */

public class CustomizedScheduleHelper  {

    public static final String CUSTOMIZEDSCHEDULE_TABLE_NAME = "customizedSchedule";
    public static final String CUSTOMIZEDSCHEDULE_COLUMN_ID = "id";
    public static final String CUSTOMIZEDSCHEDULE_COLUMN_ALIAS = "alias";
    public static final String CUSTOMIZEDSCHEDULE_COLUMN_VALUE = "value";

    public static final String DB_PROCESS_CREATE =
            "create table "
                    + CUSTOMIZEDSCHEDULE_TABLE_NAME + "("
                    + CUSTOMIZEDSCHEDULE_COLUMN_ID + " integer primary key autoincrement, "
                    + CUSTOMIZEDSCHEDULE_COLUMN_ALIAS + " text, "
                    + CUSTOMIZEDSCHEDULE_COLUMN_VALUE + " text)";

    DBHelper dbHelper;
    public CustomizedScheduleHelper(Context context) {
        dbHelper = new DBHelper(context);
    }

    public boolean insertSchedule(String alias, String value){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CUSTOMIZEDSCHEDULE_COLUMN_ALIAS, alias);
        contentValues.put(CUSTOMIZEDSCHEDULE_COLUMN_VALUE, value);
        db.insert(CUSTOMIZEDSCHEDULE_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public boolean updateCustomizedString (Integer id, String alias, String value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d("Test Update","Vào Update ở db " + id + " " + alias + " " + value);
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTOMIZEDSCHEDULE_COLUMN_ALIAS, alias);
        contentValues.put(CUSTOMIZEDSCHEDULE_COLUMN_VALUE, value);
        db.update(CUSTOMIZEDSCHEDULE_TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        db.close();
        return true;
    }

    public ArrayList<CustomizedString> getAllCustomizedSchedule() {
        ArrayList<CustomizedString> list = new ArrayList<>();
//        Log.d("Lay Data","Bat dau lay");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + CUSTOMIZEDSCHEDULE_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            int id = res.getInt(res.getColumnIndex(CUSTOMIZEDSCHEDULE_COLUMN_ID));
            String key = res.getString(res.getColumnIndex(CUSTOMIZEDSCHEDULE_COLUMN_ALIAS));
            String value = res.getString(res.getColumnIndex(CUSTOMIZEDSCHEDULE_COLUMN_VALUE));
            CustomizedString customizedString = new CustomizedString(id,key,value);
            list.add(customizedString);
            res.moveToNext();
        }

        res.close();
        db.close();
        return list;
    }
}
