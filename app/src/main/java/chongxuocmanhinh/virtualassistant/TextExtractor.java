package chongxuocmanhinh.virtualassistant;

import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by L on 11/04/2017.
 */
public class TextExtractor {

    static int DAYPREFIX = 8;
    static int MONTHPREFIX = 9;
    static int YEARPREFIX = 10;

    static final int  _EXTRACTDAY = 0;
    static final int  _EXTRACTMONTH = 1;
    static final int  _EXTRACTYEAR = 2;
    static final int  _EXTRACTWEEK = 3;
    static final int _EXTRACTDAYOFWEEK = 4;

    Boolean _checkByWeek = false;
    Boolean _checkByDayofWeek = false;
    int mLastWordLength = 0;

    public TextExtractor(String stringData) {
        setStringData(stringData);
    }

    public TextExtractor() {
    }

    private void resetValues(){
        mFirstDayIndex = 9999;
        mLastDayIndex = 0;
        mDate = Calendar.getInstance();
    }

    public String getStringData() {
        return mStringData;
    }

    public void setStringData(String stringData) {
        //Tăng thêm 1 vài khoảng trắng ở đầu và cuối chuỗi cho việc lọc ngày tháng dễ dàng
        this.mStringData ="  " + stringData + "  ";
        resetValues();
        this.extract();
    }

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(mDate.getTime());
    }

    public void setDate(Calendar date) {
        this.mDate = date;
    }

    public String getAction() {
        return mAction;
    }

    public void setAction(String action) {
        this.mAction = action;
    }

    //Chuỗi dữ liệu cần xử lý
    String mStringData;
    //Chuỗi ngày
    Calendar mDate;
    //Chuỗi hành động người dùng mong muốn
    String mAction;

    int mDay = 0;
    int mMonth = 0;
    int mYear = 0;


    public int getDay() {
        return mDay;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getYear() {
        return mYear;
    }

    //hai biến dùng để giữ vị trí đầu và cuối của chuỗi ngày trong chuỗi dữ liệu
    //cần xử lý
    int mFirstDayIndex = 9999;
    int mLastDayIndex = 0;

    public void extract(){
            extractDate();
            mAction = extractAction();
    }

    private String extractAction(){
//        if(mLastDayIndex >= stringData.length())
//            action = stringData.substring(0,mFirstDayIndex);
//        else
//            action = stringData.substring(0,mFirstDayIndex) + stringData.substring(mLastDayIndex,stringData.length()-1);

        //Loại bỏ các chữ ko cần thiết
        String action = removeStrings(mStringData);

        return action;
    }

    /**
     * Hàm để tách ngày ra khỏi chuỗi dữ liệu được đưa vào
     * @return Calendar chứa thông tin ngày ,tháng ,năm
     */
    private Calendar extractDate(){
        Calendar currentDate = Calendar.getInstance();

        int endIndex;
        //Xem có tuần hay không
        int index = isContainsWeek(mStringData);
        if(index != -1){
            try {
                mDay = isNextOrAfter(mStringData.substring(index,index + 8),_EXTRACTWEEK);
                if(mDay != -1) {
                    mStringData = replaceString(mStringData,"   ",index,index + 4 + 4) + " ";
                    _checkByWeek = true;
                    mDate.set(Calendar.DAY_OF_MONTH,mDay);
                    //return date;
                }
            }catch(NumberFormatException e){
            }
            index = -1;
        }
        //Check thứ mấy
        index = isContainsDayOfWeek(mStringData);
        int dayOfWeek= mDate.getFirstDayOfWeek();
        if(index != -1){
            try {
                System.out.println("Vào kiểm tra thứ");
                dayOfWeek = extractDayOfWeek(mStringData,index,_EXTRACTDAYOFWEEK);
                System.out.println("Thứ " + dayOfWeek );
            }catch(NumberFormatException e){
            }
            index = -1;
        }
        //Tách năm ra
        index = isContainsYear(mStringData);
        if(index != -1){
            try {
                mMonth = extractDate(mStringData, _EXTRACTYEAR, index);
                mDate.set(Calendar.YEAR,mMonth);
                endIndex = index + 4 + (int)(Math.log10(mMonth)+1);
                mStringData = removeString(mStringData,index,endIndex);
            }catch(NumberFormatException e){
            }
            index = -1;
        }
        //Tách tháng ra
        index = isContainsMonth(mStringData);
        int month;
        if(index != -1){
            try{
                System.out.println("Vô phần tháng!");
                month = extractDate(mStringData,_EXTRACTMONTH,index) - 1;
                mDate.set(Calendar.MONTH,month);
                endIndex = index + 6 + (int)(Math.log10(month)+1);
                mStringData = removeString(mStringData,index,endIndex);
            }catch(NumberFormatException e){
            }

            index = -1;
        }

        //Tách ngày ra
        index = isContainsDay(mStringData);
        if(index != -1){
            try {
                System.out.println("vo phan ngay`");
                mDay = extractDate(mStringData, _EXTRACTDAY, index);

                if(_checkByWeek) {
                    int currentDay = mDate.get(Calendar.DAY_OF_MONTH);
                    if(currentDay + 7 > mDay && mDay > currentDay){
                        mDate.set(Calendar.DAY_OF_MONTH, mDay);
                    }else{
                        mDate.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);

                        mDay = mDate.get(Calendar.DAY_OF_MONTH);
                        mDate.set(Calendar.DAY_OF_MONTH, mDay);
                    }
                }else{

                    mDate.set(Calendar.DAY_OF_MONTH, mDay);
                }
                endIndex = index + 5 + (int)(Math.log10(mDay)+1);
                mStringData = removeString(mStringData,index,endIndex);
            }catch(NumberFormatException e){
            }
            index = -1;
        }

        //date = mDay + "/" + mMonth + "/" + mYear;

        return currentDate;
    }



    //Nếu chuỗi có ngày thì lấy index ra
    private int isContainsDay(String date){
        if(date.contains("Ngày")){
            return date.indexOf("Ngày");
        }
        else if (date.contains("ngày")) {
            return date.indexOf("ngày");
        }
        else return -1;
    }

    //Nếu chuỗi có tháng thì lấy index ra
    private int isContainsMonth(String date){
        if(date.contains("Tháng")){
            return date.indexOf("Tháng");
        }
        else if (date.contains("tháng")) {
            return date.indexOf("tháng");
        }
        else return -1;
    }
    //Nếu chuỗi có năm thì lấy index ra
    private int isContainsYear(String date){
        if(date.contains("Năm")){
            return date.indexOf("Năm");
        }
        else if (date.contains("năm")) {
            return date.indexOf("năm");
        }
        else return -1;
    }
    //Nếu chuỗi có tuần thì lấy index ra
    private int isContainsWeek(String date){
        if(date.contains("Tuần")){
            return date.indexOf("Tuần");
        }
        else if (date.contains("tuần")) {
            return date.indexOf("tuần");
        }
        else return -1;
    }
    //Nếu chuỗi có thứ thì lấy index ra
    private int isContainsDayOfWeek(String date){
        if(date.contains("Thứ")){
            return date.indexOf("Thứ");
        }
        else if (date.contains("thứ")) {
            return date.indexOf("thứ");
        }
        else return -1;
    }
    /*
    *
    * @type : là 1 trong _EXTRACTDAY,_EXTRACTMONTH ,_EXTRACTYEAR
    */

    /**
     * Tách ngày tháng năm ra khỏi chuỗi đc truyền vào tùy thuộc vào type
     * @param stringData chuỗi cần xử lý
     * @param type loại kiểm tra, ngày tháng hay năm là 1 trong 3 kiểu _EXTRACTDAY,MONTH,YEAR
     * @param startPosition
     * @return
     */
    private int extractDate(String stringData, int type, int startPosition){
        //Lấy chuỗi có chữ đầu tiên là ngày
        int prefix = 0;
        int keyWordLength = 4;
        switch (type){
            case _EXTRACTDAY:
                prefix = DAYPREFIX;
                keyWordLength = 4;
                break;
            case _EXTRACTMONTH:
                prefix = MONTHPREFIX;
                keyWordLength = 5;
                break;
            case _EXTRACTYEAR:
                prefix = YEARPREFIX;
                keyWordLength = 3;
                break;
            default:
                prefix = 0;
                break;
        }

        if(startPosition + prefix > stringData.length()){
            prefix = prefix - 1;
        }

        String dayString = stringData.substring(startPosition,startPosition + prefix);
        //Kiểm tra xem có gặp các từ khóa như "sau" "tới" "mai" "mốt" hay ko
        //Nếu có thì thay mStringData với các từ khóa trên bằng khoảng trắng
        int date = isNextOrAfter(dayString,type);
        if(date != -1) {
            mStringData = replaceString(mStringData,"   ",startPosition + keyWordLength,startPosition + keyWordLength + 4) + " ";
            return date;
        }
        //Bỏ hết tất cả các chũ ra thay bằng dấu ","
        dayString = dayString.replaceAll("[^0-9,-\\.]", ",");
        String[] days = dayString.split(",");
        //Duyệt hết String sau khi xử lý,lấy số ra
        String day = "";
        for(int i = 0;i < days.length;i++){
            try {
                day += Integer.parseInt(days[i]);
            } catch (NumberFormatException e) {
            }
        }

        return Integer.parseInt(day);
    }


    /**
     * Hàm kiểm tra các từ khóa như "sau" "tới" "mai" "mốt"
     * @param stringData chuỗi được truyền vào
     * @param extractType loại kiểm tra, ngày tháng hay năm
     * @return dữ liệu là ngày,tháng hoặc năm tương ứng sau khi xử lý
     */
    private int isNextOrAfter(String stringData,int extractType){
        if(stringData.contains("mai") && extractType == _EXTRACTDAY){
            mDate.set(Calendar.DAY_OF_MONTH,mDate.get(Calendar.DAY_OF_MONTH) + 1);
            return mDate.get(Calendar.DAY_OF_MONTH);
        } else if(stringData.contains("mốt") && extractType == _EXTRACTDAY){
            mDate.set(Calendar.DAY_OF_MONTH,mDate.get(Calendar.DAY_OF_MONTH) + 2);
            return mDate.get(Calendar.DAY_OF_MONTH);
        } else if(stringData.contains("sau") || stringData.contains("tới") ){
            switch (extractType){
                case _EXTRACTMONTH:
                    mDate.set(Calendar.MONTH, mDate.get(Calendar.MONTH) + 1);
                    mDate.set(Calendar.DAY_OF_MONTH,1);
                    mDay = mDate.get(Calendar.DAY_OF_MONTH);
                    return mDate.get(Calendar.MONTH) + 1;
                case _EXTRACTYEAR:
                    mDate.set(Calendar.YEAR,mDate.get(Calendar.YEAR) + 1);
                    return mDate.get(Calendar.YEAR);
                case _EXTRACTWEEK:
                    //Tăng lên tới ngày đầu tiên của tuần sau
                    mDate.setFirstDayOfWeek(Calendar.MONDAY);
                    mDate.set(Calendar.DAY_OF_YEAR,mDate.get(Calendar.DAY_OF_YEAR) + 7);
                    //Vì sau khi set thì ta vẫn chưa thực hiện compute lại
                    //Cho nên viết luôn 1 hàm get để tính toàn lại ngày
                    //sau đó set lên dayofweek.
                    mDate.get(Calendar.DAY_OF_YEAR);
                    mDate.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);

                    return mDate.get(Calendar.DAY_OF_MONTH);
            }
        }
        return  -1;
    }


    private int extractDayOfWeek(String stringData,int index,int type){
        int currentDay = mDate.get(Calendar.DAY_OF_WEEK);
        int day = currentDay;
        String subString;
        if(index + 13 <= stringData.length())
             subString = stringData.substring(index,index + 13);
        else subString = stringData.substring(index,stringData.length());
        if(_checkByWeek){
            day = convertSpecifiedStringToNumber(subString);
            if (day > currentDay) {
                mDate.set(Calendar.DAY_OF_WEEK, day);
            }else{
                mDate.set(Calendar.DAY_OF_YEAR,mDate.get(Calendar.DAY_OF_YEAR) + 7);
                //Vì sau khi set thì ta vẫn chưa thực hiện compute lại
                //Cho nên viết luôn 1 hàm get để tính toàn lại ngày
                //sau đó set lên dayofweek.
                mDate.get(Calendar.DAY_OF_YEAR);
                mDate.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            }
        }
        return day;
    }

    /**
     * Hàm chuyển các chữ hai ba tư thành số trong Calendar.DAY_OF_WEEK
     * @param data chuỗi đưa vào eg: "hai"
     * @return  Calendar.DAY_OF_WEEK* eg : Calendar.MONDAY
     */
    public int convertSpecifiedStringToNumber(String data){
        //Mặc định trả về ngày đầu tiên trong tuần
        int dayOfWeek = mDate.getFirstDayOfWeek();
        if(data.contains("hai") || data.contains("2")){
            return Calendar.MONDAY;
        }else if(data.contains("ba") || data.contains("3")){
            return Calendar.TUESDAY;
        }else if(data.contains("tư") || data.contains("4")){
            return Calendar.WEDNESDAY;
        }else if(data.contains("năm") || data.contains("5")){
            return Calendar.THURSDAY;
        }else if(data.contains("sáu") || data.contains("6")){
            return Calendar.FRIDAY;
        }else if(data.contains("bảy") || data.contains("7")){
            return Calendar.SATURDAY;
        }else if(data.contains("chủ nhật")){
            return Calendar.SUNDAY;
        }
        return dayOfWeek;
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    //Hàm này được dùng để loại bỏ một số từ ko cần thiết
    //ví dụ như "vào"
    private String removeStrings(String stringData){
        String finalString = stringData;
        finalString = normalizeString(finalString);

        //Trường hợp gặp chữ vào hoặc "Vào"
        finalString = removeString(finalString,"Vào",0,4);
        finalString = removeString(finalString,"Vào",finalString.length() - 4,finalString.length());
        finalString = removeString(finalString,"vào",0,4);
        finalString = removeString(finalString,"vào",finalString.length() - 4,finalString.length());

        //Chuẩn hóa lại bằng cách loại bỏ các dấu cách thừa

        return normalizeString(finalString);
    }

    private String normalizeString(String stringData){
        String finalString = stringData;
        //Chuẩn hóa lại bằng cách loại bỏ các dấu cách thừa
        finalString = finalString.replaceAll("\\s+", " ");
        finalString = finalString.replaceAll("(^\\s+|\\s+$)", "");

        return finalString;
    }



    /**
     * Hàm dùng để loại bỏ tất removeString từ stringData
        Bắt đầu từ startindex và kết thúc ở endindex
     * @param stringData chuỗi cần xử lý
     * @param removeString chuỗi cần loại bỏ
     * @param startIndex vị trí bắt đầu trên @stringData
     * @param endIndex vị trí kết thúc trên @stringData
     * @return
     */
    private String removeString(String stringData,String removeString,int startIndex,int endIndex){

        String finalString = stringData;
        int removeIndex = -1;

        String subString = stringData.substring(startIndex,endIndex);

        if(subString.contains(removeString)){
            removeIndex = subString.indexOf(removeString) + startIndex;
        }

        if(removeIndex != -1){
            finalString = stringData.substring(0,removeIndex)
                    + stringData.substring(removeIndex + removeString.length(),stringData.length());
        }

        return finalString;
    }

    /**
     * Hàm loại bỏ 1 chuỗi trên stringData bắt đầu từ startIndex và kết thúc ở endINdex
     * @param stringData
     * @param startIndex
     * @param endIndex
     * @return
     */
    private String removeString(String stringData,int startIndex,int endIndex){

         return stringData.substring(0,startIndex)
                    + stringData.substring(endIndex,stringData.length());
    }


    private String replaceString(String stringData,String stringToAdd,int startReplaceIndex,int endReplaceIndex){

        return stringData.substring(0,startReplaceIndex) + stringToAdd
                + stringData.substring(endReplaceIndex,stringData.length());
    }
}
