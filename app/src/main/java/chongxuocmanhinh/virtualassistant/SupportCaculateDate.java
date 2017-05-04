package chongxuocmanhinh.virtualassistant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by L on 03/05/2017.
 */
public class SupportCaculateDate {
    String _originalStringData;
    String _handlingStringData;
    /**
     * Danh scahs các cụm từ dùng để kiểm tra
     * vd : "tăng","Tăng thêm"
     */
    private HashMap<String,Integer> _clauseToCheck;
    /**
     * Danh sách các từ khóa để xác đinh loại cần tăng
     */
    private HashMap<String,Integer> _wordToCheck;

    /**
     *
     */
    private Calendar mDate;

    public static int PLUS = 100;
    public static int FAILURE = -1;

    private void initClauses(){
        _clauseToCheck = new HashMap<String, Integer>();
        _clauseToCheck.put("tăng",PLUS);
        _clauseToCheck.put("Tăng",PLUS);
        _clauseToCheck.put("tăng thêm",PLUS);
        _clauseToCheck.put("Tăng thêm",PLUS);
        _clauseToCheck.put("Cộng thêm",PLUS);
        _clauseToCheck.put("cộng thêm",PLUS);
    }

    private void initWords(){
        _wordToCheck = new HashMap<String, Integer>();
        _wordToCheck.put("ngày",Calendar.DAY_OF_YEAR);
        _wordToCheck.put("Ngày",Calendar.DAY_OF_YEAR);
        _wordToCheck.put("tháng",Calendar.MONTH);
        _wordToCheck.put("Tháng",Calendar.MONTH);
        _wordToCheck.put("năm",Calendar.YEAR);
        _wordToCheck.put("Năm",Calendar.YEAR);
    }


    public void addClauses(){

    }

    public SupportCaculateDate(){
        initClauses();
        initWords();
        resetValues();
    }

    public SupportCaculateDate(String stringData) {
        initClauses();
        initWords();
        setStringData(stringData);
    }

    public void setStringData(String stringToSet){
        if(_handlingStringData != stringToSet) {
            _originalStringData = stringToSet;
            _handlingStringData = _originalStringData;
            resetValues();
        }
    }

    public String getStringData(){
        return _handlingStringData;
    }

    private void resetValues(){
        mDate = Calendar.getInstance();
    }

    public Calendar getCalendar(){
        return mDate;
    }

    public String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        return dateFormat.format(mDate.getTime());
    }

    /**
     * Hàm thực hiện việc hỗ trợ tính toán ngày
     * @return
     */
    public boolean caculateDate(){
        /**
         * VD:
         *      Với String như sau "Tăng thêm 10 ngày 2 tháng"
         */
        int count = -1;
        if(isContainPlusString()){//Hàm này sẽ trả về true,và giá trị _handlingStringData sẽ thành " 10 ngày 2 tháng"
            while(getPlusType()){
                //Vòng lặp để tăng đơn vị theo ngày và tháng
                //Lần 1 sẽ tăng lên 10 ngày và _handlingStringData sẽ thành " 2 tháng"
                //Lần 1 sẽ tăng lên 2 tháng và _handlingStringData sẽ thành " "
                //Thoát vòng lặp
                count++;
            }
        }
        if(count != -1)
            return true;
        return false;
    }

    /**
     * Hàm xác đinh chuỗi có tồn tại các cụm từ nằm trong _clauseToCheck hay không
     * Nếu có thì xóa đi sau đó
     * @return index vị trí cuối cùng của cụm từ
     */
    public Boolean isContainPlusString(){
        for(String key : _clauseToCheck.keySet()){
            if(_handlingStringData.contains(key)){
                int start = _handlingStringData.indexOf(key);
                int end = start + key.length();
                _handlingStringData = StringUtils.removeString(_handlingStringData,start,end);
                return true;
            }
        }
        return false;
    }

    /**
     * Hàm lấy giá trị tăng theo tháng,ngày,năm
     * Sau khi xử lý sẽ xóa dữ liệu liên quan
     * @return
     * VD : "20 ngày 3 tháng"
     *      Xóa 20 ngày còn "3 tháng"
     */
    public boolean getPlusType(){
        int minIndex = 9999;
        String trackedKey ="";
        //Lần đầu lặp tất cả các key có trong danh sách từ khóa cần kiểm tra vd"ngày,tháng,năm..."
        for(String key : _wordToCheck.keySet()){
            if(_handlingStringData.contains(key)){
                int startIndex = _handlingStringData.indexOf(key);
                if(startIndex < minIndex) {
                    //Trả về giá trị minIndex là vị trí của từ khóa xuất hiện sớm nhất kể từ đầu chuỗi
                    minIndex = startIndex;
                    trackedKey = key;
                }
            }
        }
        //Lặp lần 2 để tiến hành tính toán và xóa dữ liệu khỏi chuỗi
        if(minIndex != 9999){
            int type = _wordToCheck.get(trackedKey);
            int endPos =  _handlingStringData.indexOf(trackedKey) + trackedKey.length();
            int plusValue = StringUtils.extractNumber(_handlingStringData,0,endPos);
            _handlingStringData = StringUtils.removeString(_handlingStringData,0,endPos);
            mDate.add(type,plusValue);
            mDate.getTime();
            return true;
        }else return false;
    }


}
