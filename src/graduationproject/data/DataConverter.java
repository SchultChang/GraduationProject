/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author cloud
 */
public class DataConverter {
    public String convertDateToString(Date date) {
        if (date == null) {
            return "";
        }
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
    
    public String convertCalendarToString(Calendar time) {
        if (time == null) {
            return "";
        }
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(time.getTime());
    }
}
