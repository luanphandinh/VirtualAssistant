package chongxuocmanhinh.virtualassistant;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by L on 16/05/2017.
 */
public class TimeExtractor {
    public static final String NULLSTRING = "null";

    private  LengthOfTime lengthOfTime;

    private String _stringData;

    private String[] FROMSTRINGS = {"Từ lúc","từ lúc","Từ","từ","lúc","Lúc"};

    private String[] TOSTRINGS = {"Đến lúc","đến lúc","Đến","đến"};

    private String[] SEPERATORS = {":"," giờ "," ",""};

    private String[] MINUTES = {"([0-9].?)",""};

    private String[] ENDS = {" phút"," ",""};

    private final int FROM = 1;
    private final int TO = 2;

    private Time _startTime;
    private String _startTimeString;
    private Time _endTime;
    private String _endTimeString;

    public TimeExtractor(){
        _startTime = new Time(0);
        _endTime = new Time(0);
        _startTimeString = NULLSTRING;
        _endTimeString = NULLSTRING;
    }

    public void setStringData(String stringData){
        this._stringData = stringData;
    }

    public void resetValues(){
        _stringData = "";
        _startTime.setTime(0);
        _endTime.setTime(0);
        _startTimeString = NULLSTRING;
        _endTimeString = NULLSTRING;
    }

    public String getStringData(){
        return _stringData;
    }


    public boolean extract(){
        checkFrom(FROM);
        checkFrom(TO);
        if(_startTime.compareTo(_endTime) > 0){
            return false;
        }
        return true;
    }

    public String getStartTime(){
        return _startTimeString;
    }

    public String getEndTime(){
        return _endTimeString;
    }

    private boolean checkFrom(int type){
        int hour = -1;
        int min = -1;
        String[] checkStrings = null;
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        _stringData += "  ";
        switch (type){
            case FROM:
                checkStrings = FROMSTRINGS;
                break;
            case TO:
                checkStrings = TOSTRINGS;
                break;
        }
        for(int i = 0 ; i <checkStrings.length;i++){
            if (_stringData.contains(checkStrings[i])){
                for (int j = 0 ; j < SEPERATORS.length;j++) {
                    for(int m = 0;m < MINUTES.length;m++)
                        for (int k = 0; k < ENDS.length; k++) {

                            String compileString = checkStrings[i] + " " + "([0-9].?)" + SEPERATORS[j] + MINUTES[m] + ENDS[k];
                            Pattern pattern = Pattern.compile(compileString);
                            Matcher matcher = pattern.matcher(_stringData);
                            if (matcher.find()) {
                                try {
                                    try {
                                        hour = Integer.parseInt(matcher.group(1));
                                        System.out.println(hour);
                                    }catch (NumberFormatException e){
                                        System.out.println(hour);
                                    }

                                    try{
                                        min = Integer.parseInt(matcher.group(2));
                                        System.out.println(min);
                                    }catch (Exception e){
                                        min = 0;
                                    }

                                    if(type == FROM) {
                                        _startTime.setHours(hour);
                                        _startTime.setMinutes(min);
                                        _startTimeString = StringUtils.convertTimeToString(_startTime);
                                    }else {
                                        _endTime.setHours(hour);
                                        _endTime.setMinutes(min);
                                        _endTimeString = StringUtils.convertTimeToString(_endTime);
                                    }
                                } catch (NumberFormatException e) {
                                } finally {
                                    int start = matcher.toMatchResult().start();
                                    int end = matcher.toMatchResult().end();
                                    _stringData = StringUtils.replaceString(_stringData, "", start, end);
                                    return true;
                                }
                            }
                        }
                }
            }
        }
        return true;

    }

}
