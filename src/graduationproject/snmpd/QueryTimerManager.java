/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author cloud
 */
public class QueryTimerManager {

    private Timer deviceTimer;

    public synchronized void startDeviceTimer(TimerTask task, int delay, int period) {
        this.cancelDeviceTimer();
        this.deviceTimer = new Timer();
        this.deviceTimer.schedule(task, delay, period);
    }

    public synchronized void cancelDeviceTimer() {
        if (this.deviceTimer != null) {
            this.deviceTimer.cancel();
            this.deviceTimer.purge();
        }
    }

}
