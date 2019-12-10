/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.controllers;

import graduationproject.data.DataConverter;
import graduationproject.data.DataManager;
import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceInterfaceDynamicData;
import graduationproject.data.models.DeviceMemoryState;
import graduationproject.data.models.DeviceNetworkInterface;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

/**
 *
 * @author cloud
 */
public class ChartManagementController {

    private final int CHART_WIDTH = 990;
    private final int CHART_HEIGHT = 190;
//    private final String DEFAULT_CHOICE_VALUE = "All";

    private String resultMessage;

    public enum DataType {
        CPU_LOAD,
        MEMORY_USAGE,
        BANDWIDTH_UTILIZATION
    }

    public enum QueryPeriod {
        TODAY("0"),
        YESTERDAY("1"),
        LAST_3_DAYS("2"),
        LAST_7_DAYS("3");

        private String value;

        private QueryPeriod(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public XYChart processGettingChart(int deviceId, DataType dataType, String period) {
        if (dataType == DataType.CPU_LOAD) {
            List<double[]> data = this.getDataForChart(deviceId, dataType, period, null);
            if (data != null) {
                XYChart chart = new XYChartBuilder()
                        .width(CHART_WIDTH)
                        .height(CHART_HEIGHT)
                        .xAxisTitle("Hour")
                        .yAxisTitle("Load")
                        .build();

                chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
                chart.getStyler().setYAxisLabelAlignment(Styler.TextAlignment.Right);
                chart.getStyler().setYAxisMin(0.0);
                chart.getStyler().setPlotMargin(0);
                chart.getStyler().setPlotContentSize(.95);
                chart.addSeries("CPU Load", data.get(0), data.get(1));
                return chart;
            }
        }

        if (dataType == DataType.MEMORY_USAGE) {

        }

        return null;
    }

    public List<double[]> getDataForChart(int deviceId, DataType dataType, String period, String choice) {
        Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
        if (device == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return null;
        }

        Calendar day = Calendar.getInstance();

        if (QueryPeriod.TODAY.getValue().equals(period)) {
            return this.getDataForDay(device, dataType, day, choice);
        }
        if (QueryPeriod.YESTERDAY.getValue().equals(period)) {
            day.add(Calendar.DAY_OF_YEAR, -1);
            return this.getDataForDay(device, dataType, day, choice);
        }

        return null;
    }

    public List<double[]> getDataForDay(Device device, DataType dataType, Calendar day, String choice) {
        List<double[]> result = new ArrayList<double[]>();
        int startHour = 0;
        int endHour = 23;

        Calendar startTime = (Calendar) day.clone();
        Calendar endTime = (Calendar) day.clone();
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);

        double[] xValues = new double[endHour - startHour + 1];
        double[] yValues = new double[endHour - startHour + 1];

        for (int i = startHour; i <= endHour; i++) {
            xValues[i] = i;
            startTime.set(Calendar.HOUR_OF_DAY, i);
            endTime.set(Calendar.HOUR_OF_DAY, i);
            yValues[i] = this.getDataForTime(device, dataType, startTime, endTime, choice);
        }

        result.add(xValues);
        result.add(yValues);

        return result;
    }

    public double getDataForTime(Device device, DataType dataType, Calendar startTime, Calendar endTime, String choice) {
        if (dataType == DataType.CPU_LOAD) {
            return this.getAverageCpuLoadForTime(device, startTime, endTime);
        }
        return 0;
    }

    public double getAverageCpuLoadForTime(Device device, Calendar startTime, Calendar endTime) {
        return DataManager.getInstance().getDeviceCpuManager().getAverageCpuLoad(device, startTime, endTime);
    }

    public double getAverageMemoryUsageForTime(Device device, Calendar startTime, Calendar endTime, String choice) {
        List<DeviceMemoryState> memoryStates
                = DataManager.getInstance().getDeviceMemoryManager().getDeviceMemoryStates(device, startTime, endTime, choice);
        if (memoryStates == null && memoryStates.isEmpty()) {
            return 0.0;
        }

        int tempSize = memoryStates.size();
        double percentageSum = 0.0;

        for (DeviceMemoryState memoryState : memoryStates) {
            percentageSum += memoryState.getUsagePercentage();
        }

        return percentageSum / tempSize;
    }

    public double getAverageBandwidthUsageForTime(Device device, Calendar startTime, Calendar endTime, String choice) {
        DeviceNetworkInterface networkInterface = DataManager.getInstance().getNetworkInterfaceManager().getNetworkInterface(device, choice);
        if (networkInterface == null) {
            return 0.0;
        }

        List<DeviceInterfaceDynamicData> interfaceDataList = DataManager.getInstance().getInterfaceDynamicDataManager()
                .getDeviceDynamicData(networkInterface, startTime, endTime);
        if (interfaceDataList == null && interfaceDataList.isEmpty()) {
            return 0.0;
        }

        int tempSize = interfaceDataList.size();
        double percentageSum = 0.0;
        DeviceInterfaceDynamicData previous, current;
        double upperPart, lowerPart;
        DataConverter dataConverter = new DataConverter();

        for (int i = 1; i < tempSize; i++) {
            previous = interfaceDataList.get(i - 1);
            current = interfaceDataList.get(i);
            upperPart = Math.max(current.getInboundBytes() - previous.getInboundBytes(),
                    current.getOutboundBytes() - previous.getOutboundBytes()) * 8 * 100 * 1.0d;
            lowerPart = (dataConverter.convertCalendarTimeToSecond(current.getUpdatedTime())
                    - dataConverter.convertCalendarTimeToSecond(previous.getUpdatedTime())) * current.getBandwidth();
            percentageSum += (upperPart / lowerPart);
        }

        return percentageSum / tempSize;
    }

    public class ResultMessageGenerator {

        public String GETTING_FAILED_OTHER = "Some errors happend when getting device data from database";
    }
}
