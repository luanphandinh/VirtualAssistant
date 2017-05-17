package chongxuocmanhinh.virtualassistant;

import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by L on 03/05/2017.
 */
public class StringUtils {

    /**
     * Hàm loại bỏ 1 chuỗi trên stringData bắt đầu từ startIndex và kết thúc ở endINdex
     * @param stringData
     * @param startIndex
     * @param endIndex
     * @return
     */
    public static String removeString(String stringData,int startIndex,int endIndex){

        return stringData.substring(0,startIndex)
                + stringData.substring(endIndex,stringData.length());
    }


    public static String replaceString(String stringData,String stringToAdd,int startReplaceIndex,int endReplaceIndex){

        return stringData.substring(0,startReplaceIndex) + stringToAdd
                + stringData.substring(endReplaceIndex,stringData.length());
    }

    /**
     * Lọc số từ chuỗi cho trước,từ vị trí startPos tới endPos
     * @param stringData chuỗi cần tách số
     * @param startPosition vị trí bắt đầu
     * @param endPosition vị trí kết thúc
     * @return trả về giá trị là số được tách ra,giá trị -1 nếu ko tách được số
     * ***NOTE : Giá trị lấy về có thể không chính xác
     *      VD : "ngày 12 tháng 3" nếu lấy từ đầu tới cuối sẽ lấy cả 123
     *              chọn startPos và endPos cho thích hợp để lấy số như mong muốn
     */
    public static int extractNumber(String stringData, int startPosition,int endPosition){
        //Lấy chuỗi có chữ đầu tiên là ngày
        int output = -1;

        if(startPosition > endPosition){
            return -1;
        }

        String dayString = stringData.substring(startPosition,endPosition);
        //Bỏ hết tất cả các chũ ra thay bằng dấu ","
        dayString = dayString.replaceAll("[^0-9,-\\.]", ",");
        //Tách thành mảng các kí tự riêng lẽ
        String[] days = dayString.split(",");
        //Duyệt hết các phần tử riệng lẽ sau khi xử lý,lấy số ra
        String day = "";
        for(int i = 0;i < days.length;i++){
            try {
                day += Integer.parseInt(days[i]);
            } catch (NumberFormatException e) {
            }
        }

        try {
            output = Integer.parseInt(day);
        }catch (NumberFormatException e){

        }

        return output;
    }

    public static String normalizeString(String stringData){
        String finalString = stringData;
        //Chuẩn hóa lại bằng cách loại bỏ các dấu cách thừa
        finalString = finalString.replaceAll("\\s+", " ");
        finalString = finalString.replaceAll("(^\\s+|\\s+$)", "");

        return finalString;
    }

    public static String convertTimeToString(Time time){
        Format formatter = new SimpleDateFormat("HH:mm");
        String timeString = formatter.format(time);
        return timeString;
    }


    public static Time covertStringToTime(String string){
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        try {
            return  new Time(formatter.parse(string).getTime());
        } catch (ParseException e) {
            return new Time(0);
        }
    }
}
