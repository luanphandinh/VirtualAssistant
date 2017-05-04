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
public class DateExtractor {

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

    public DateExtractor(String stringData) {
        setData(stringData);
    }

    public DateExtractor() {
    }

    private void resetValues(){
        mFirstDayIndex = 9999;
        mLastDayIndex = 0;
        mDate = Calendar.getInstance();
    }

    public String getStringData() {
        return mStringData;
    }

    public void setData(String stringData) {
        //Tăng thêm 1 vài khoảng trắng ở đầu và cuối chuỗi cho việc lọc ngày tháng dễ dàng
        this.mStringData ="  " + stringData + "  ";
        resetValues();
        //this.extract();
    }

    public void setData(String stringData,Calendar calendar) {
        //Tăng thêm 1 vài khoảng trắng ở đầu và cuối chuỗi cho việc lọc ngày tháng dễ dàng
        this.mStringData ="  " + stringData + "  ";
        resetValues();
        mDate = calendar;
        //this.extract();
    }

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        isLastDay(mStringData,_EXTRACTWEEK);
        int index = isContainsWeek(mStringData);
        if(index != -1){
            try {
                mDay = isNextOrAfter(mStringData.substring(index,index + 8),_EXTRACTWEEK,index);
                if(mDay != -1) {
                    mStringData = replaceString(mStringData,"   ",index,index + 4) + " ";
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
        isLastDay(mStringData,_EXTRACTDAYOFWEEK);
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
        isLastDay(mStringData,_EXTRACTYEAR);
        index = isContainsYear(mStringData);
        if(index != -1){
            try {
                mYear = extractDate(mStringData, _EXTRACTYEAR, index);
                mDate.set(Calendar.YEAR,mYear);
            }catch(NumberFormatException e){
            }
            index = -1;
        }
        //Tách tháng ra
        isLastDay(mStringData,_EXTRACTMONTH);
        index = isContainsMonth(mStringData);
        int month;
        if(index != -1){
            try{
                System.out.println("Vô phần tháng!");
                month = extractDate(mStringData,_EXTRACTMONTH,index) - 1;
                mDate.set(Calendar.MONTH,month);
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
//                endIndex = index + 5 + (int)(Math.log10(mDay)+1);
//                mStringData = removeString(mStringData,index,endIndex);
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
     * Hàm xử lý với giá trị nằm sau chữ ngày ,tháng ,năm thương là lấy 3-4 chữ sau đó
     * Nếu có sau mai mốt thì trả về,ko thì lây số đằng sau,sau đó xóa luôn chuỗi
     * Vd "Ngầy mai" test xong xóa luôn
     * @param stringData chuỗi cần xử lý
     * @param type loại kiểm tra, ngày tháng hay năm là 1 trong 3 kiểu _EXTRACTDAY,MONTH,YEAR
     * @param startPosition
     * @return
     */
    private int extractDate(String stringData, int type, int startPosition){
        //Lấy chuỗi có chữ đầu tiên là ngày
        int prefix = 0;
        int keyWordLength = 4;
        int endIndex = startPosition;
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
        int date = isNextOrAfter(dayString,type,startPosition);
        if(date != -1) {
            mStringData = replaceString(mStringData,"   ",startPosition,startPosition + keyWordLength) + " ";
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

        try {
            date = Integer.parseInt(day);
            endIndex = startPosition + keyWordLength + (int)(Math.log10(date)+1);
            mStringData = removeString(mStringData,startPosition,endIndex + 1);
        }catch (NumberFormatException e){
            switch (type){
                case _EXTRACTDAY:
                    date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    break;
                case _EXTRACTMONTH:
                    date = Calendar.getInstance().get(Calendar.MONTH);
                    break;
                case _EXTRACTYEAR:
                    date = Calendar.getInstance().get(Calendar.YEAR);
                    break;
                default:
                    date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    break;
            }
        }

        return date;
    }


    /**
     * Hàm kiểm tra các từ khóa như "sau" "tới" "mai" "mốt","này"
     * @param stringData chuỗi được truyền vào
     * @param extractType loại kiểm tra, ngày tháng hay năm
     * @param index index của chuỗi mStringData
     * @return dữ liệu là ngày,tháng hoặc năm tương ứng sau khi xử lý
     */
    private int isNextOrAfter(String stringData,int extractType,int index){
        boolean isNext = false;
        int date  = -1;
        int startIndex = index;
        if(stringData.contains("mai") && extractType == _EXTRACTDAY){
            mDate.set(Calendar.DAY_OF_MONTH,mDate.get(Calendar.DAY_OF_MONTH) + 1);
            isNext = true;
            startIndex = index + stringData.indexOf("mai");
            date = mDate.get(Calendar.DAY_OF_MONTH);
        } else if(stringData.contains("mốt") && extractType == _EXTRACTDAY){
            mDate.set(Calendar.DAY_OF_MONTH,mDate.get(Calendar.DAY_OF_MONTH) + 2);
            isNext = true;
            startIndex = index + stringData.indexOf("mốt");
            date = mDate.get(Calendar.DAY_OF_MONTH);
        } else if(stringData.contains("sau") || stringData.contains("tới") ){
            isNext = true;
            if(stringData.contains("sau"))
                startIndex = index + stringData.indexOf("sau");
            else
                startIndex = index + stringData.indexOf("tới");
            switch (extractType){
                case _EXTRACTMONTH:
                    mDate.set(Calendar.MONTH, mDate.get(Calendar.MONTH) + 1);
                    mDate.set(Calendar.DAY_OF_MONTH,1);
                    mDay = mDate.get(Calendar.DAY_OF_MONTH);
                    date = mDate.get(Calendar.MONTH) + 1;
                    break;
                case _EXTRACTYEAR:
                    mDate.set(Calendar.YEAR,mDate.get(Calendar.YEAR) + 1);
                    date = mDate.get(Calendar.YEAR);
                    break;
                case _EXTRACTWEEK:
                    //Tăng lên tới ngày đầu tiên của tuần sau
                    mDate.setFirstDayOfWeek(Calendar.MONDAY);
                    mDate.set(Calendar.DAY_OF_YEAR,mDate.get(Calendar.DAY_OF_YEAR) + 7);
                    //Vì sau khi set thì ta vẫn chưa thực hiện compute lại
                    //Cho nên viết luôn 1 hàm get để tính toàn lại ngày
                    //sau đó set lên dayofweek.
                    mDate.get(Calendar.DAY_OF_YEAR);
                    mDate.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);

                    date = mDate.get(Calendar.DAY_OF_MONTH);
            }
        }else if(stringData.contains("này")){
            isNext = true;
            startIndex = index + stringData.indexOf("này");
            switch (extractType){
                case _EXTRACTMONTH:
                    date =  mDate.get(Calendar.MONTH) + 1;
                    break;
                case _EXTRACTYEAR:
                    date = mDate.get(Calendar.YEAR);
                    break;
                case _EXTRACTWEEK:
                    date = mDate.get(Calendar.DAY_OF_MONTH);
                    break;
            }
        }

        if(isNext){
            mStringData = replaceString(mStringData," ",startIndex,startIndex + 3);
        }
        return  date;
    }


    private int extractDayOfWeek(String stringData,int index,int type){
        int currentDay = mDate.get(Calendar.DAY_OF_WEEK);
        int day = currentDay;
        String subString;
        if(index + 13 <= stringData.length())
            subString = stringData.substring(index,index + 13);
        else subString = stringData.substring(index,stringData.length());
        //if(_checkByWeek){
        day = convertSpecifiedStringToNumber(subString,index + 3);
        System.out.println("Thứ " + day + " tách từ chuỗi so sánh với " + currentDay);
        if (day > currentDay) {
            System.out.println("Thứ " + day + " lớn hơn " + currentDay);
            mDate.set(Calendar.DAY_OF_WEEK, day);
            mDate.get(Calendar.DAY_OF_YEAR);
        }else{
            System.out.println("Thứ " + day + " nhỏ hơn " + currentDay);
            mDate.add(Calendar.DAY_OF_YEAR,7);
            //Vì sau khi set thì ta vẫn chưa thực hiện compute lại
            //Cho nên viết luôn 1 hàm get để tính toàn lại ngày
            //sau đó set lên dayofweek.
            mDate.get(Calendar.DAY_OF_YEAR);
            mDate.set(Calendar.DAY_OF_WEEK,day);
        }
        // }
        return day;
    }

    /**
     * Hàm kiểm tra xem có phải là cuối tuần,tháng,hay năm hay không
     * @param stringData
     * @param type
     * @return
     */
    private boolean isLastDay(String stringData,int type){
        mStringData = mStringData += "      ";
        boolean isLast = false;
        int startIndex = 0;
        int endIndex = 0;
        int stringLength = 0;
        if(type == _EXTRACTMONTH  && (stringData.contains("cuối tháng") || stringData.contains("Cuối tháng"))){
            //Xóa chuối này khỏi mStringData
            {
                if (stringData.contains("cuối tháng")) startIndex = stringData.indexOf("cuối tháng");
                else startIndex = stringData.indexOf("Cuối tháng");

                isNextOrAfter(mStringData.substring(startIndex,startIndex + 10 + 4),_EXTRACTMONTH,startIndex);

                endIndex = startIndex + 10;
                mStringData = replaceString(mStringData, " ", startIndex, endIndex);
            }
            isLast = true;
            {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, 1);// This is necessary to get proper results
                cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
                mDay = cal.get(Calendar.DAY_OF_MONTH);
                mDate.set(Calendar.DAY_OF_MONTH,mDay);
            }

        } else if(
                ((type == _EXTRACTWEEK) && (stringData.contains("cuối tuần") || stringData.contains("Cuối tuần")))
                        || ((type == _EXTRACTDAYOFWEEK) && (stringData.contains("chủ nhật") || stringData.contains("chúa nhật") || stringData.contains("Chúa nhật") || stringData.contains("Chủ nhật")))){
            //Xóa chuối này khỏi mStringData
            if(type == _EXTRACTWEEK)
            {
                if (stringData.contains("cuối tuần")) startIndex = stringData.indexOf("cuối tuần");
                else startIndex = stringData.indexOf("Cuối tuần");
                stringLength = 9;
                isNextOrAfter(mStringData.substring(startIndex,startIndex + stringLength + 4),_EXTRACTWEEK,startIndex);

                endIndex = startIndex + stringLength;
                mStringData = replaceString(mStringData, " ", startIndex, endIndex);
            }else if(type == _EXTRACTDAYOFWEEK){
                if (stringData.contains("chủ nhật")) {
                    startIndex = stringData.indexOf("chủ nhật");
                    stringLength = 8;
                }
                else if(stringData.contains("Chủ nhật")){
                    startIndex = stringData.indexOf("Chủ nhật");
                    stringLength = 8;
                }
                else if(stringData.contains("Chúa nhật")){
                    startIndex = stringData.indexOf("Chúa nhật");
                    stringLength = 9;
                }
                else {
                    startIndex = stringData.indexOf("chúa nhật");
                    stringLength = 9;
                }

                isNextOrAfter(mStringData.substring(startIndex,startIndex + stringLength + 4),_EXTRACTWEEK,startIndex);

                endIndex = startIndex + stringLength;
                mStringData = replaceString(mStringData, " ", startIndex, endIndex);
            }

            isLast = true;
            {
                int dayOfWeek = mDate.get(Calendar.DAY_OF_WEEK) - mDate.getFirstDayOfWeek();
                mDate.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

                Date weekStart = mDate.getTime();
                // we do not need the same day a week after, that's why use 6, not 7
                mDate.add(Calendar.DAY_OF_MONTH, 6);
                mDate.getTime();
            }

        } else if((type == _EXTRACTYEAR) && (stringData.contains("cuối năm") || stringData.contains("Cuối năm"))){
            //Xóa chuối này khỏi mStringData
            {
                if (stringData.contains("cuối năm")) startIndex = stringData.indexOf("cuối năm");
                else startIndex = stringData.indexOf("Cuối năm");

                isNextOrAfter(mStringData.substring(startIndex,startIndex + 8 + 4),_EXTRACTYEAR,startIndex);

                endIndex = startIndex + 8;
                mStringData = replaceString(mStringData, " ", startIndex, endIndex);
            }
            isLast = true;
            {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
                cal.set(Calendar.DAY_OF_MONTH, 1);// This is necessary to get proper results
                cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);
                mDate.set(Calendar.MONTH,mMonth);
                mDate.set(Calendar.DAY_OF_MONTH,mDay);
            }

        }
        return isLast;
    }

    /**
     * Hàm chuyển các chữ hai ba tư thành số trong Calendar.DAY_OF_WEEK
     * @param data chuỗi đưa vào eg: "hai"
     * @return  Calendar.DAY_OF_WEEK* eg : Calendar.MONDAY
     */
    public int convertSpecifiedStringToNumber(String data,int startIndex){
        //Mặc định trả về ngày đầu tiên trong tuần
        int dayOfWeek = mDate.getFirstDayOfWeek();
        boolean isContains = false;
        int endIndex = startIndex;
        if(data.contains("hai") || data.contains("2")){
            isContains = true;
            if(data.contains("2")) endIndex = startIndex + 1;
            else endIndex = startIndex + 3;
            dayOfWeek = Calendar.MONDAY;
        }else if(data.contains("ba") || data.contains("3")){
            isContains = true;
            if(data.contains("3")) endIndex = startIndex + 1;
            else endIndex = startIndex + 2;
            dayOfWeek = Calendar.TUESDAY;
        }else if(data.contains("tư") || data.contains("4")){
            isContains = true;
            if(data.contains("4")) endIndex = startIndex + 1;
            else endIndex = startIndex + 2;
            dayOfWeek = Calendar.WEDNESDAY;
        }else if(data.contains("năm") || data.contains("5")){
            isContains = true;
            if(data.contains("5")) endIndex = startIndex +1;
            else endIndex = startIndex + 3;
            dayOfWeek = Calendar.THURSDAY;
        }else if(data.contains("sáu") || data.contains("6")){
            isContains = true;
            if(data.contains("6")) endIndex = startIndex + 1;
            else endIndex = startIndex  + 3;
            dayOfWeek = Calendar.FRIDAY;
        }else if(data.contains("bảy") || data.contains("7")){
            isContains = true;
            if(data.contains("7")) endIndex = startIndex + 1;
            else endIndex = startIndex  + 3;
            dayOfWeek = Calendar.SATURDAY;
        }else if(data.contains("chủ nhật")){
            isContains = true;
            dayOfWeek = Calendar.SUNDAY;
        }
        if(isContains) {
            mStringData = replaceString(mStringData, " ", startIndex -3, endIndex + 1);
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
