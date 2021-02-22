package com.galbern.req.utilities;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class DateUtil {

    public String formatDate(String format, Date date){
        return new SimpleDateFormat(format).format(date);
    }

    public String formatPath(String date){
        return date.replace("","");
    }
}
