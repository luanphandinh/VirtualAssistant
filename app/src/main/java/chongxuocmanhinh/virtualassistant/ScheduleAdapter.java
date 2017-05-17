package chongxuocmanhinh.virtualassistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by L on 17/05/2017.
 */

public class ScheduleAdapter extends BaseAdapter {

    ArrayList<Schedule> schedules;
    private Context context;

    public ScheduleAdapter(Context context, ArrayList<Schedule> schedules) {
        this.context = context;
        this.schedules = schedules;
    }

    @Override
    public int getCount() {
        if(schedules != null)
            return schedules.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(schedules != null)
            return schedules.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.schedule, parent, false);
        }
        // Lookup view for data population
        TextView tvAction = (TextView) convertView.findViewById(R.id.tvAction);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        // Populate the data into the template view using the data object
        tvAction.setText(schedules.get(position).getAction());
        tvTime.setText(schedules.get(position).getTime());
        // Return the completed view to render on screen
        convertView.setTag(schedules.get(position).getId());
        return convertView;
    }
}