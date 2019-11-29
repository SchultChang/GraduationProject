/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author cloud
 */
public class DataConverter {
    public String convertDateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        return formatter.format(date);
    }
}
