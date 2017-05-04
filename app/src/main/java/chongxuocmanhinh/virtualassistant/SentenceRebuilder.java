package chongxuocmanhinh.virtualassistant;

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
    DateExtractor extractor = new DateExtractor();

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
        this.rebuild();
    }

    /**
     * Hàm này thực hiện việc kiểm tra các chuỗi alias có trong database
     * Sau đó thay thế chuỗi bằng dữ liệu được điều chỉnh bởi người dùng
     */
    public void rebuild(){
        StringBuffer buffer = new StringBuffer(_stringData);
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
                    extractor.setData(_stringData,supportCalculateDate.getCalendar());
                    extractor.extract();
                    return;
                }else{
                    buffer.replace(start,end,_userCustomizedStringData.get(key));
                }
            }
        }
        _stringData = buffer.toString();
        extractor.setData(_stringData);
        extractor.extract();
    }

    public DateExtractor getExtractor(){
        return extractor;
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
