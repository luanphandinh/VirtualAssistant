package chongxuocmanhinh.virtualassistant;

import java.sql.Time;

/**
 * Created by L on 16/05/2017.
 */
public class LengthOfTime {
    public static final int MORNING = 0;
    public static final int AFTERNOON = 1;
    public static final int EVENING = 2;
    public static final int NIGHT = 3;


    private int period = MORNING;
    private Time startTime;
    private Time endTime;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }


    public LengthOfTime(){
        setTime(MORNING);
    }

    public LengthOfTime(int id,Time startTime,Time endTime){
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LengthOfTime(int period){
        setTime(period);
    }

    private void setTime(int period){
        switch (period){
            case MORNING:
                startTime = new Time(6,30,00);
                endTime =  new Time(10,30,00);
                break;
            case AFTERNOON:
                startTime = new Time(11,00,00);
                endTime =  new Time(14,00,00);
                break;
            case EVENING:
                startTime = new Time(18,30,00);
                endTime =  new Time(21,30,00);
                break;
            case NIGHT:
                startTime = new Time(22,00,00);
                endTime =  new Time(23,99,00);
                break;
        }
    }
}
