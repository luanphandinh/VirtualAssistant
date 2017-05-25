package chongxuocmanhinh.virtualassistant;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends ListeningActivity {

    private final int SPEECH_RECOGNITION_CODE = 1;
    private TextView txtOutput;
    private Button btnMicrophone;
    private LinearLayout btnSchedule;
    private LinearLayout btnCustomize;
    private  TextView txtinOut;

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
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this.getApplicationContext();
        extractor = new DateExtractor();
        sentenceRebuilder = new SentenceRebuilder();
        txtOutput = (TextView) findViewById(R.id.txt_output);
        txtinOut = (TextView) findViewById(R.id.txt_InOut) ;
        btnMicrophone = (Button) findViewById(R.id.micro);
        changeBtnListeningIcon(listeningFlag);
        btnMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable(mContext)) {
                    clickBtnListening();
                }else{
                    txtOutput.setText(getResources().getString(R.string.needInternetConnection));
                }
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
                if(listeningFlag == FLAG_LISTENING){
                    clickBtnListening();
                }
            }
        });

        btnCustomize = (LinearLayout)findViewById(R.id.layout_customized);
        btnCustomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomizeStringActivity.class);
                startActivity(intent);
                if(listeningFlag == FLAG_LISTENING){
                    clickBtnListening();
                }
            }
        });



        // 3 dòng này dùng để hỗ trợ cho việc nhận diện giọng nói
        context = getApplicationContext(); // Needs to be set
        VoiceRecognitionListener.getInstance().setListener(this); // Gán mainActivity để nghe


        //Cài đặt phần tự động thông báo
        setupAlarm();
    }

    public void setupAlarm() {
        NotificationEventReceiver.setupAlarm(getApplicationContext());
    }

    @Override
    public void processVoiceCommands(String... voiceCommands) {
        for (String command : voiceCommands) {
            try {
                sentenceRebuilder.setString(command);
                if (sentenceRebuilder.rebuild()) {
                    String text1 = sentenceRebuilder.getDateExtractor().getDate();
                    String text2 = sentenceRebuilder.getDateExtractor().getAction();
                    String text3 = sentenceRebuilder.getTimeExtractor().getStartTime().toString();
                    String text4 = sentenceRebuilder.getTimeExtractor().getEndTime().toString();
                    int id = scheduleDB.insertSchedule(text1, text2, text3, text4);
                    if (text3.equals(TimeExtractor.NULLSTRING))
                        text3 = "";
                    if (text4.equals(TimeExtractor.NULLSTRING))
                        text4 = "";
                    txtOutput.setText(text1 + "\n" + text2 + "\n" + text3 + " - " + text4);
                } else {
                    txtOutput.setText("Xin lỗi!không thể hiểu ý của bạn");
                }
            }catch (Exception e){
                txtOutput.setText("Xin lỗi!không thể hiểu ý của bạn");
            }
        }
        restartListeningService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sentenceRebuilder.setClauseData(customizedScheduleHelper.getAllCustomizedSchedule());
    }

    private void clickBtnListening(){
        removeTextOutput();
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
                btnMicrophone.setBackgroundResource(R.drawable.microphone);
                didTapButton();
                txtinOut.setText(R.string.out);
                break;
            case FLAG_STOP:
                btnMicrophone.setBackgroundResource(R.drawable.amicrophone2);
                didTapButton();
                txtinOut.setText(R.string.in);
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
    public void didTapButton() {

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.animator.test );
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 30);
        myAnim.setInterpolator(interpolator);
        btnMicrophone.startAnimation(myAnim);
    }

    /**
     * Hàm kiểm tra xem thiết bị kết nối với internet hay chưa
     * @param context
     * @return
     */
    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void removeTextOutput(){
        txtOutput.setText("");
    }
}
