package chongxuocmanhinh.virtualassistant;

import java.util.ArrayList;

/**
 * Created by L on 04/05/2017.
 */

public class CustomizedString {
    public int id;
    public String alias;
    public String value;

    public CustomizedString(int id,String alias, String value) {
        this.id = id;
        this.alias = alias;
        this.value = value;
    }

    public static ArrayList<CustomizedString> getUsers() {
        ArrayList<CustomizedString> _userCustomizedStringData = new ArrayList<CustomizedString>();
        _userCustomizedStringData.add(new CustomizedString(1,"Ngày quốc tế lao động","ngày 1 tháng 5"));
        _userCustomizedStringData.add(new CustomizedString(1,"Ngày tôi thích nhất","ngày 3 tháng 5"));
        return _userCustomizedStringData;
    }


}
