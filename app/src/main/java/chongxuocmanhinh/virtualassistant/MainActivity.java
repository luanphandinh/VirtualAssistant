package chongxuocmanhinh.virtualassistant;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends ListeningActivity {

    private final int SPEECH_RECOGNITION_CODE = 1;
    private TextView txtOutput;
    private ImageButton btnMicrophone;
    private ImageButton btnSchedule;
    private ImageButton btnCustomize;

    private ScheduleHelper scheduleDB;
    private CustomizedScheduleHelper customizedScheduleHelper;
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
        btnMicrophone = (ImageButton) findViewById(R.id.btn_mic);
        changeBtnListeningIcon(listeningFlag);
        btnMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtnListening();
            }
        });

        scheduleDB = new ScheduleHelper(this);
        customizedScheduleHelper = new CustomizedScheduleHelper(this);
        sentenceRebuilder.setClauseData(customizedScheduleHelper.getAllCustomizedSchedule());

        btnSchedule = (ImageButton)findViewById(R.id.btn_schedule);
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        btnCustomize = (ImageButton)findViewById(R.id.btn_customize);
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
            String text1 = sentenceRebuilder.getExtractor().getDate();
            String text2 = sentenceRebuilder.getExtractor().getAction();

            if(scheduleDB.insertSchedule(text1,text2))
                txtOutput.setText(text1 + "\n" + text2);
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
                btnMicrophone.setImageResource(R.mipmap.ic_x_round);
                break;
            case FLAG_STOP:
                btnMicrophone.setImageResource(R.mipmap.ic_microphone);
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
