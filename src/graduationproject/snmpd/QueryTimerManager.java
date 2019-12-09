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

    private Timer deviceActiveTimer;
    private Timer deviceResourceTimer;
    private Timer interfaceTimer;
    private Timer queryTimer;

    public synchronized void startDeviceActiveTimer(TimerTask task, int delay, int period) {
        this.cancelDeviceActiveTimer();
        System.out.println("STARTING DEVICE ACTIVE TIMER");
        this.deviceActiveTimer = new Timer();
        this.deviceActiveTimer.schedule(task, delay, period);
    }

    public synchronized void cancelDeviceActiveTimer() {
        if (this.deviceActiveTimer != null) {
            this.deviceActiveTimer.cancel();
            this.deviceActiveTimer.purge();
            this.deviceActiveTimer = null;
            System.out.println("STOPPING DEVICE ACTIVE TIMER");
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
    
    public synchronized void startDeviceResourceTimer(TimerTask task, int delay, int period) {
        this.cancelDeviceResourceTimer();
        System.out.println("STARTING DEVICE RESOURCE TIMER");
        this.deviceResourceTimer = new Timer();
        this.deviceResourceTimer.schedule(task, delay, period);
    }
    
    public synchronized void cancelDeviceResourceTimer() {
        if (this.deviceResourceTimer != null) {
            this.deviceResourceTimer.cancel();
            System.out.println("STOPPING DEVICE RESOURCE TIMER");
            this.deviceResourceTimer.purge();
            this.deviceResourceTimer = null;
        }
    }

}
