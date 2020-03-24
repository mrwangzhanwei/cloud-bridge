package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/24  19:52
 */
public class DateUtil {
    public static String DATE_FORMAT = "yyyy-MM-dd";

    public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String DATE_FORMAT_CHINESE = "yyyy年M月d日";



    public static String getCurrentDate() {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
        datestr = df.format(new Date());
        return datestr;
    }

    public static String getCurrentDateTime() {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_TIME_FORMAT);
        datestr = df.format(new Date());
        return datestr;
    }
}
