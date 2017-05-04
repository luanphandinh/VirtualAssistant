package chongxuocmanhinh.virtualassistant;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class AddCustomizedStringActivity extends AppCompatActivity {
    private final int SPEECH_RECOGNITION_CODE = 1;
    private final int MODE_EDIT_KEY = 2;
    private final int MODE_EDIT_VALUE = 3;
    public static final int MODE_ADD = 4;
    public static final int MODE_UPDATE = 5;

    private final int MODE_NONE = 3;
    private int mode = MODE_NONE;
    EditText txtKey;
    EditText txtValue;
    ImageButton btnAddKey;
    ImageButton btnAddValue;
    Button btnAddCustomizedString;
    Button btnUpdateCustomizedString;
    int idToUpdate;

    CustomizedScheduleHelper customizedScheduleHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customized_string);
        customizedScheduleHelper = new CustomizedScheduleHelper(this);
        txtKey = (EditText) findViewById(R.id.txtKey);
        txtValue = (EditText) findViewById(R.id.txtValue);
        btnAddKey = (ImageButton) findViewById(R.id.btn_edit_key);
        btnAddKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = MODE_EDIT_KEY;
                startSpeechToText();
            }
        });

        btnAddValue = (ImageButton) findViewById(R.id.btn_edit_value);
        btnAddValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = MODE_EDIT_VALUE;
                startSpeechToText();
            }
        });

        btnAddCustomizedString = (Button) findViewById(R.id.btn_add);
        btnAddCustomizedString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addValue();
                finish();
            }
        });

        btnUpdateCustomizedString = (Button) findViewById(R.id.btn_update);
        btnUpdateCustomizedString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateValue();
                finish();
                //customizedScheduleHelper.insertSchedule(key,value);
            }
        });

        //Kiểm tra xem view được hiện dưới dạng để update hay để add mới
        Bundle b = getIntent().getExtras();
        int mode = MODE_ADD; // or other values
        if(b != null)
            mode = b.getInt("mode");

        if(mode == MODE_ADD) btnAddCustomizedString.setVisibility(View.VISIBLE);
        if(mode == MODE_UPDATE) {
            btnUpdateCustomizedString.setVisibility(View.VISIBLE);
            int pos = b.getInt("position");
            getRow(pos);
        }

    }

    private void getRow(int pos){
        ArrayList<CustomizedString> customizedStrings = customizedScheduleHelper.getAllCustomizedSchedule();
        String alias = customizedStrings.get(pos).alias;
        String value = customizedStrings.get(pos).value;
        idToUpdate = customizedStrings.get(pos).id;
        txtKey.setText(alias);
        txtValue.setText(value);
        Log.d("Test Update","Vào Update ở activity " + idToUpdate + " " + alias + " " + value);
    }

    private void addValue(){
        String key = StringUtils.normalizeString(txtKey.getText().toString());
        String value = StringUtils.normalizeString(txtValue.getText().toString());
        if(key.isEmpty() || value.isEmpty())
            return;
        if(key.length() < 5 || value.length() <5)
            return;

        customizedScheduleHelper.insertSchedule(key,value);
    }

    private void updateValue(){
        String key = StringUtils.normalizeString(txtKey.getText().toString());
        String value = StringUtils.normalizeString(txtValue.getText().toString());
        if(key.isEmpty() || value.isEmpty())
            return;
        if(key.length() < 5 || value.length() <5)
            return;
        Log.d("Test Update","Vào Update ở activity " + idToUpdate + " " + key + " " + value);
        customizedScheduleHelper.updateCustomizedString(idToUpdate,key,value);
    }

    /**
     * Start speech to text intent. This opens up Google Speech Recognition API dialog box to listen the speech input.
     * */
    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak something...");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Callback for speech recognition activity
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    switch (mode){
                        case MODE_EDIT_KEY :
                            txtKey.setText(text);
                            break;
                        case MODE_EDIT_VALUE :
                            txtValue.setText(text);
                            break;
                        default:
                            break;
                    }
                }
                break;
            }
        }
    }

}


