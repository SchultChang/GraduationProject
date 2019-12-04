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
    private Timer interfaceTimer;
    private Timer queryTimer;

    public synchronized void startDeviceTimer(TimerTask task, int delay, int period) {
        this.cancelDeviceTimer();
        System.out.println("STARTING DEVICE TIMER");
        this.deviceTimer = new Timer();
        this.deviceTimer.schedule(task, delay, period);
    }

    public synchronized void cancelDeviceTimer() {
        if (this.deviceTimer != null) {
            this.deviceTimer.cancel();
            this.deviceTimer.purge();
            this.deviceTimer = null;
            System.out.println("STOPPING DEVICE TIMER");
        }
    }
    
    
    public synchronized void startInterfaceTimer(TimerTask task, int delay, int period) {
        this.cancelInterfaceTimer();
                System.out.println("STARTING INTERFACE TIMER");
        this.interfaceTimer = new Timer();
        this.interfaceTimer.schedule(task, delay, period);
    }   
    
    public synchronized void cancelInterfaceTimer() {        
        if (this.interfaceTimer != null) {
            this.interfaceTimer.cancel();
                    System.out.println("STOPPING INTERFACE TIMER");
            this.interfaceTimer.purge();
            this.interfaceTimer = null;
        }
    }

}
