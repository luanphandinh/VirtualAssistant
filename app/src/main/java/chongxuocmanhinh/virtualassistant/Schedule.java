package chongxuocmanhinh.virtualassistant;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by L on 16/05/2017.
 */

public class Schedule {
    int id;
    Date date;
    String action;
    Time startDay;
    Time endDay;

    public Schedule(int id,String date,String startDay,String endDay){
        this.id = id;
    }
}
