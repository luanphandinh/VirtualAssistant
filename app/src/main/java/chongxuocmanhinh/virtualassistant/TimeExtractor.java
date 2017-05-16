package chongxuocmanhinh.virtualassistant;

import java.sql.Time;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by L on 16/05/2017.
 */
public class TimeExtractor {

    private  LengthOfTime lengthOfTime;

    private String _stringData;

    private String[] FROMSTRINGS = {"Từ lúc","từ lúc","Từ","từ","lúc","Lúc"};

    private String[] TOSTRINGS = {"Đến lúc","đến lúc","Đến","đến"};

    private String[] SEPERATORS = {":"," giờ "," "};

    private String[] ENDS = {" phút"," "};

    private final int FROM = 1;
    private final int TO = 2;

    private Time _startTime;
    private Time _endTime;

    public TimeExtractor(){
        _startTime = new Time(0,0,0);
        _endTime = new Time(23,59,0);
    }

    public void setStringData(String stringData){
        this._stringData = stringData;
    }

    public void resetValues(){
        _stringData = "";
        _startTime.setTime(0);
        _endTime = new Time(23,59,0);
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

    public Time getStartTime(){
        return _startTime;
    }

    public Time getEndTime(){
        return _endTime;
    }


    private boolean checkFrom(int type){
        int hour;
        int min;
        String[] checkStrings = null;
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
                    for (int k = 0; k < ENDS.length; k++) {
                        String compileString = checkStrings[i] + " " + "([0-9].?)" + SEPERATORS[j] + "([0-9].?)" + ENDS[k];
                        Pattern pattern = Pattern.compile(compileString);
                        Matcher matcher = pattern.matcher(_stringData);
                        if (matcher.find()) {
                            try {
                                hour = Integer.parseInt(matcher.group(1));
                                min = Integer.parseInt(matcher.group(2));
                                if(type == FROM) {
                                    _startTime.setHours(hour);
                                    _startTime.setMinutes(min);
                                }else {
                                    _endTime.setHours(hour);
                                    _endTime.setMinutes(min);
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
