package chongxuocmanhinh.virtualassistant;

/**
 * Created by L on 16/05/2017.
 */

public class Schedule {
    int id;
    String date;
    String action;
    String startTime;
    String endTime;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTime() {
        String value = "";
        if(!startTime.equals(TimeExtractor.NULLSTRING))
            value += startTime + "-";
        if(!endTime.equals(TimeExtractor.NULLSTRING))
            value += endTime;
        if(startTime.equals(TimeExtractor.NULLSTRING) && endTime.equals(TimeExtractor.NULLSTRING))
            value = "Không rõ thời gian";
        return value;
    }



    public Schedule(int id, String date, String action, String startTime, String endTime){
        this.id = id;
        this.date = date;
        this.action = action;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Schedule(int id, String action, String startTime, String endTime){
        this.id = id;
        this.action = action;
        this.startTime = startTime;
        this.endTime = endTime;
    }


}
