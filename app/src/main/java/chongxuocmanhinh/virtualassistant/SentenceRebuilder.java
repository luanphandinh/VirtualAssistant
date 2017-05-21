package chongxuocmanhinh.virtualassistant;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by L on 01/05/2017.
 */
public class SentenceRebuilder {
    private HashMap<String,String> _userCustomizedStringData;
    private String _stringData;
    private SupportCaculateDate supportCalculateDate;
    TimeExtractor  timeExtractor = new TimeExtractor();
    DateExtractor dateExtractor = new DateExtractor();

    public SentenceRebuilder(){
        supportCalculateDate = new SupportCaculateDate();
        _userCustomizedStringData = new HashMap<String, String>();
//        _userCustomizedStringData.put("Ngày quốc tế lao động","ngày 1 tháng 5");
//        _userCustomizedStringData.put("Ngày tôi thích nhất","ngày 3 tháng 5");
//        _userCustomizedStringData.put("ngày tôi thích nhất","ngày 3 tháng 5");
//        _userCustomizedStringData.put("Ngày quốc tế thiếu nhi","ngày 10 tháng 5");
//        _userCustomizedStringData.put("Ngày tôi thích thứ 2","ngày 4 tháng 5");
//        _userCustomizedStringData.put("Tương lai gần của tôi","tăng 1 ngày 2 tháng");
//        _userCustomizedStringData.put("trong 2 ngày tới","tăng 2 ngày");
//        _userCustomizedStringData.put("trong năm tới tới tới","giờ mày tăng 10 năm cho tao");
    }

    public void setClauseData(HashMap<String,String> map){
        _userCustomizedStringData = map;
    }

    public void setClauseData(ArrayList<CustomizedString> customizedStrings){
        for (CustomizedString customizedString: customizedStrings) {
            _userCustomizedStringData.put(customizedString.alias,customizedString.value);
        }
    }

    public void setString(String string){
        _stringData = string;
    }

    /**
     * Hàm này thực hiện việc kiểm tra các chuỗi alias có trong database
     * Sau đó thay thế chuỗi bằng dữ liệu được điều chỉnh bởi người dùng
     */
    public boolean rebuild(){
        StringBuffer buffer = new StringBuffer(_stringData);
        dateExtractor.resetValues();
        timeExtractor.resetValues();
        for(String key : _userCustomizedStringData.keySet()){
            if(_stringData.contains(key)){
                int start = _stringData.indexOf(key);
                int end = start + key.length();
                /**
                 * Kiểm tra xem chuỗi alias có là custom theo kiểu "tăng" hay không
                 * vd : "Tăng thêm 1 ngày 2 tháng"
                 */
                supportCalculateDate.setStringData(_userCustomizedStringData.get(key));
                if(supportCalculateDate.caculateDate()){
                    buffer.replace(start,end,"");
                    _stringData = buffer.toString();
                    dateExtractor.setData(_stringData,supportCalculateDate.getCalendar());
                    //dateExtractor.extract();
                }else{
                    _stringData = buffer.replace(start,end,_userCustomizedStringData.get(key)).toString();
                }
            }
        }
        _stringData = buffer.toString();
        timeExtractor.setStringData(_stringData + " ");
        if(timeExtractor.extract()){
            _stringData = timeExtractor.getStringData();
        }else return false;
        dateExtractor.setData(_stringData);
        dateExtractor.extract();
        return true;
    }

    public DateExtractor getDateExtractor(){
        return dateExtractor;
    }

    public TimeExtractor getTimeExtractor(){
        return timeExtractor;
    }

    public void printMap() {
        Iterator it = _userCustomizedStringData.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

}
