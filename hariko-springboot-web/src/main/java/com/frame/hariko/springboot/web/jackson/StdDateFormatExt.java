package com.frame.hariko.springboot.web.jackson;

import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.text.*;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Deprecated
public class StdDateFormatExt extends StdDateFormat{
    public final static StdDateFormat instance = new StdDateFormatExt();
    protected final static String DATE_FORMAT_STR_PLAIN_EXT = "yyyy-MM-dd HH:mm:ss";
    private final static Locale DEFAULT_LOCALE = Locale.US;
    protected final Locale _locale;

    protected final static String[] ALL_FORMATS = new String[] {
            DATE_FORMAT_STR_PLAIN_EXT,
            DATE_FORMAT_STR_ISO8601,
            DATE_FORMAT_STR_RFC1123,
            DATE_FORMAT_STR_PLAIN
    };

    public StdDateFormatExt(){
        _locale = DEFAULT_LOCALE;
    }

    public StdDateFormatExt(TimeZone tz, Locale loc) {
        _timezone = tz;
        _locale = loc;
    }

    /**
     *  先尝试使用yyyy-MM-dd HH:mm:ss转换一次,失败之后走父类原逻辑
     * @param dateStr
     * @return
     * @throws ParseException
     */
    @Override
    public Date parse(String dateStr) throws ParseException {
        dateStr = dateStr.trim();
        DateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STR_PLAIN_EXT);
        try {
           return sdf.parse(dateStr);
        }catch (Exception e){
            ParsePosition pos = new ParsePosition(0);
            Date result = parse(dateStr, pos);
            if (result != null) {
                return result;
            }

            StringBuilder sb = new StringBuilder();
            for (String f : ALL_FORMATS) {
                if (sb.length() > 0) {
                    sb.append("\", \"");
                } else {
                    sb.append('"');
                }
                sb.append(f);
            }
            sb.append('"');
            throw new ParseException
                    (String.format("Can not parse date \"%s\": not compatible with any of standard forms (%s)",
                            dateStr, sb.toString()), pos.getErrorIndex());
        }
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo,
                               FieldPosition fieldPosition){
            //当未配置dateformate的时候，与jackson默认转时间戳的配置保持一致
            return new StringBuffer(date.getTime()+"");
        }

        @Override
        public StdDateFormat clone() {
        /* Although there is that much state to share, we do need to
         * orchestrate a bit, mostly since timezones may be changed
         */
        return new StdDateFormatExt(_timezone, _locale);
    }
}
