package chongxuocmanhinh.virtualassistant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends ListeningActivity {

    private final int SPEECH_RECOGNITION_CODE = 1;
    private TextView txtOutput;
    private ImageView btnMicrophone;
    private LinearLayout btnSchedule;
    private LinearLayout btnCustomize;

    private ScheduleHelper scheduleDB;
    private LengthOfTimeHelper lengthOfTimeDB;
    private CustomizedStringHelper customizedScheduleHelper;
    //===========================================================
    private ArrayList<String> listActions;
    //===========================================================
    private DateExtractor extractor;
    private SentenceRebuilder sentenceRebuilder;
    //===========================================================
    private final int FLAG_LISTENING = 21;
    private final int FLAG_STOP = 31;
    private int listeningFlag = FLAG_STOP;
    //===========================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        extractor = new DateExtractor();
        sentenceRebuilder = new SentenceRebuilder();
        txtOutput = (TextView) findViewById(R.id.txt_output);
        btnMicrophone = (ImageView) findViewById(R.id.micro);
        changeBtnListeningIcon(listeningFlag);
        btnMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtnListening();
            }
        });

        if(PermissionRequestActivity.havePermissions(this) == false) {
            PermissionRequestActivity.showWarning(this, getIntent());
        }
        scheduleDB = new ScheduleHelper(this);
        lengthOfTimeDB = new LengthOfTimeHelper(this);
        customizedScheduleHelper = new CustomizedStringHelper(this);
        sentenceRebuilder.setClauseData(customizedScheduleHelper.getAllCustomizedSchedule());

        btnSchedule = (LinearLayout)findViewById(R.id.layout_schedule);
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        btnCustomize = (LinearLayout)findViewById(R.id.layout_customized);
        btnCustomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomizeStringActivity.class);
                startActivity(intent);
            }
        });



        // 3 dòng này dùng để hỗ trợ cho việc nhận diện giọng nói
        context = getApplicationContext(); // Needs to be set
        VoiceRecognitionListener.getInstance().setListener(this); // Gán mainActivity để nghe

    }

    @Override
    public void processVoiceCommands(String... voiceCommands) {
        for (String command : voiceCommands) {
            sentenceRebuilder.setString(command);
            String text1 = sentenceRebuilder.getDateExtractor().getDate();
            String text2 = sentenceRebuilder.getDateExtractor().getAction();
            String text3 = sentenceRebuilder.getTimeExtractor().getStartTime().toString();
            String text4 = sentenceRebuilder.getTimeExtractor().getEndTime().toString();
            int id = scheduleDB.insertSchedule(text1,text2,text3,text4);
            txtOutput.setText(text1 + "\n" + text2 + "\n" + text3 + " - " + text4);
        }
        restartListeningService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sentenceRebuilder.setClauseData(customizedScheduleHelper.getAllCustomizedSchedule());
    }

    private void clickBtnListening(){
        if(listeningFlag == FLAG_STOP){
            listeningFlag = FLAG_LISTENING;
            startListening();
        }else if (listeningFlag == FLAG_LISTENING){
            listeningFlag = FLAG_STOP;
            stopListening();
        }
        changeBtnListeningIcon(listeningFlag);
    }

    private void changeBtnListeningIcon(int flag){
        switch (flag){
            case FLAG_LISTENING:
                btnMicrophone.setImageResource(R.drawable.cancel);
                break;
            case FLAG_STOP:
                btnMicrophone.setImageResource(R.drawable.micro_big);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
