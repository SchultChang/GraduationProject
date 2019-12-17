/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.helpers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
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

    public String convertCalendarToTimeString(Calendar time) {
        if (time == null) {
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(time.getTime());
    }

    public double convertCalendarTimeToSecond(Calendar time) {
        return time.get(Calendar.HOUR_OF_DAY) * 3600 * 1.0 + time.get(Calendar.MINUTE) * 60 + time.get(Calendar.SECOND);
    }

    public String convertBytesToStringMacAddress(byte[] mac) {
        StringBuilder builder = new StringBuilder();
        if (mac != null) {
            for (int i = 0; i < mac.length; i++) {
                builder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
            }
        }
        return builder.toString();
    }
    
    public boolean[] convertDecimalToBinary(int number, int base) {
        boolean[] result = new boolean[base];
        for (int i = base - 1; i >= 0; i--) {
            result[base - i - 1] = (number & (1 << i)) != 0;
        }
        return result;
    }
    
    public int convertBinaryToDecimal(boolean[] input) {
        int result = 0;
        for (int i = 0; i < input.length; i++) {
            if (input[i]) {
                result += (int) Math.pow(2, input.length - 1 - i);
            }
        }
        return result;
    }

}
