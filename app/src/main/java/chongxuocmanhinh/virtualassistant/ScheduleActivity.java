package chongxuocmanhinh.virtualassistant;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class ScheduleActivity extends AppCompatActivity {

    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    private ScheduleHelper scheduleDB;
    private ArrayList<String> schduleList;
    private ArrayList<String> actionList;
    private Looper mLooper;
    private Handler mHandler;

    Button deletButton;
    Button addButton;
    String selectedDate;

    private void setCustomResourceForDates() throws ParseException {
        Calendar cal = Calendar.getInstance();

//        // Min date is last 7 days
//        cal.add(Calendar.DATE, -8);
//        Date blueDate = cal.getTime();
//
//        // Max date is next 7 days
//        cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, 7);
//        Date greenDate = cal.getTime();

        schduleList = scheduleDB.getAllSchedule();
        ArrayList<String> stringDates;
        ArrayList<Date> dates;
        int size = 0;
        if(schduleList.size() > 0)
            size = schduleList.size();
        else return;
        stringDates = schduleList;
        dates = new ArrayList<>(size);

        for(int i = 0; i < stringDates.size();i ++){
            String sDate = stringDates.get(i);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
            Log.d("Lay Data",date.toString());
            dates.add(date);
        }


        if (caldroidFragment != null) {
            ColorDrawable red = new ColorDrawable(getResources().getColor(R.color.red));
            ColorDrawable green = new ColorDrawable(Color.GREEN);
            for(int i = 0; i < dates.size();i ++) {
                if(dates.get(i).compareTo(cal.getTime()) < 0) {
                    caldroidFragment.setBackgroundDrawableForDate(red, dates.get(i));

                }else{
                    caldroidFragment.setBackgroundDrawableForDate(green, dates.get(i));
                }
                caldroidFragment.setTextColorForDate(R.color.white, dates.get(i));
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        scheduleDB = new ScheduleHelper(this);
        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****

        // Setup arguments

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            // Uncomment this line to use Caldroid in compact mode
            // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);
        }

        try {
            setCustomResourceForDates();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();
                displayActions(date);
            }

            @Override
            public void onChangeMonth(int month, int year) {

            }

            @Override
            public void onLongClickDate(Date date, View view) {

            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    Toast.makeText(getApplicationContext(),
                            "Lịch đã hiển thị", Toast.LENGTH_SHORT)
                            .show();
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

//        // Setup Buttons
//        addButton = (Button) findViewById(R.id.btn_add);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        deletButton = (Button) findViewById(R.id.btn_delete);
//        deletButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ScheduleActivity.this, EraseScheduleActivity.class);
//                intent.putExtra("date",selectedDate);
//                startActivity(intent);
//            }
//        });
    }

    private void displayActions(Date date){
        TextView action = (TextView) findViewById(R.id.action);
        String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        actionList = scheduleDB.getAllActions(stringDate,stringDate);
        String stringAction = "";
        for(int i = 0;i < actionList.size();i++){
            stringAction += actionList.get(i) + '\n';
        }
        action.setText(stringAction);
    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }

}
