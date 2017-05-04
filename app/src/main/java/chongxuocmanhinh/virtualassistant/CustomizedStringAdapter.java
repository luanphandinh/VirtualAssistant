package chongxuocmanhinh.virtualassistant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by L on 04/05/2017.
 */

public class CustomizedStringAdapter extends ArrayAdapter<CustomizedString> {
    public CustomizedStringAdapter(Context context, ArrayList<CustomizedString> strings) {
        super(context, 0, strings);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CustomizedString customizedString = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customized_string_layout, parent, false);
        }
        // Lookup view for data population
        TextView tvAlias = (TextView) convertView.findViewById(R.id.tvAlias);
        TextView tvValue = (TextView) convertView.findViewById(R.id.tvValue);
        // Populate the data into the template view using the data object
        tvAlias.setText(customizedString.alias);
        tvValue.setText(customizedString.value);
        // Return the completed view to render on screen
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddCustomizedStringActivity.class);
                Bundle b = new Bundle();
                b.putInt("position",position); //Your id
                b.putInt("mode",AddCustomizedStringActivity.MODE_UPDATE); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                getContext().startActivity(intent);
            }
        });
        return convertView;
    }

}
