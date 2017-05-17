package chongxuocmanhinh.virtualassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class CustomizeStringActivity extends AppCompatActivity {

    CustomizedStringHelper _customizedScheduleHelper;
    LinearLayout addNewCustomizedStringLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_string);
        _customizedScheduleHelper = new CustomizedStringHelper(this);
        populateCustomziedStringList();

        addNewCustomizedStringLayout = (LinearLayout) findViewById(R.id.add_new_customized_string_layout);
        addNewCustomizedStringLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomizeStringActivity.this, AddCustomizedStringActivity.class);
                Bundle b = new Bundle();
                b.putInt("mode", AddCustomizedStringActivity.MODE_ADD); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateCustomziedStringList();
    }

    private void populateCustomziedStringList() {
        // Construct the data source
        ArrayList<CustomizedString> arrayOfCustomizedStrings = queryData();
        // Create the adapter to convert the array to views
        CustomizedStringAdapter adapter = new CustomizedStringAdapter(this, arrayOfCustomizedStrings);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.lvCustomizedString);
        listView.setAdapter(adapter);
    }

    private ArrayList<CustomizedString> queryData(){
        return _customizedScheduleHelper.getAllCustomizedSchedule();
    }
}
