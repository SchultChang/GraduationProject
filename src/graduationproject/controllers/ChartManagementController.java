/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.controllers;

import graduationproject.helpers.DataConverter;
import graduationproject.data.DataManager;
import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceInterfaceDynamicData;
import graduationproject.data.models.DeviceMemoryState;
import graduationproject.data.models.DeviceNetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

/**
 *
 * @author cloud
 */
public class ChartManagementController extends ManagementController {

    private final int CHART_WIDTH = 990;
    private final int CHART_HEIGHT = 190;
//    private final String DEFAULT_CHOICE_VALUE = "All";

    public enum DataType {
        CPU_LOAD,
        MEMORY_USAGE,
        BANDWIDTH_USAGE
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

    public Chart processGettingChart(int deviceId, DataType dataType, String period, Object[] choices) {
        String xTitle = "Hour";
        String yTitle = "Avg Usage(%)";
        if (dataType == DataType.CPU_LOAD) {
            yTitle = "Avg Load(%)";
        }

        if (QueryPeriod.TODAY.getValue().equals(period) || QueryPeriod.YESTERDAY.getValue().equals(period)) {
            XYChart chart = this.buildChartForADay(xTitle, yTitle);
            List<Object> data = null;
            if (choices != null) {
                for (Object choice : choices) {
                    data = this.getDataForChart(deviceId, dataType, period, choice);
                    if (data != null) {
                        if (DataType.CPU_LOAD == dataType) {
                            chart.addSeries("CPU " + String.valueOf((Integer) choice - (Integer) choices[0] + 1),
                                    (double[]) data.get(0), (double[]) data.get(1));
                        } else {
                            chart.addSeries(String.valueOf(choice), (double[]) data.get(0), (double[]) data.get(1));
                        }
                    }
                }
            }
            return chart;
        }

        xTitle = "Day";
        if (QueryPeriod.LAST_3_DAYS.getValue().equals(period) || QueryPeriod.LAST_7_DAYS.getValue().equals(period)) {
            CategoryChart chart = this.buildChartForDays(xTitle, yTitle);
            List<Object> data = null;
            if (choices != null) {
                for (Object choice : choices) {
                    data = this.getDataForChart(deviceId, dataType, period, choice);
                    if (data != null) {
                        if (DataType.CPU_LOAD == dataType) {
                            chart.addSeries("CPU " + String.valueOf((Integer) choice - (Integer) choices[0] + 1),
                                    Arrays.asList((Date[]) data.get(0)), Arrays.asList((Double[]) data.get(1)));
                        } else {
                            chart.addSeries(String.valueOf(choice), Arrays.asList((Date[]) data.get(0)), Arrays.asList((Double[]) data.get(1)));
                        }
                    }
                }
            }
            return chart;
        }

        return null;
    }

    private XYChart buildChartForADay(String xTitle, String yTitle) {
        XYChart chart = new XYChartBuilder()
                .width(CHART_WIDTH)
                .height(CHART_HEIGHT)
                .xAxisTitle(xTitle)
                .yAxisTitle(yTitle)
                .build();

        chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
        chart.getStyler().setYAxisLabelAlignment(Styler.TextAlignment.Right);
        chart.getStyler().setYAxisMin(0.0);
        chart.getStyler().setLegendPadding(2);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setLegendVisible(true);
        return chart;
    }

    private CategoryChart buildChartForDays(String xTitle, String yTitle) {
        CategoryChart chart = new CategoryChartBuilder()
                .width(CHART_WIDTH)
                .height(CHART_HEIGHT)
                .xAxisTitle(xTitle)
                .yAxisTitle(yTitle)
                .theme(Styler.ChartTheme.XChart)
                .build();

        chart.getStyler().setDatePattern("dd-MM");
        chart.getStyler().setYAxisLabelAlignment(Styler.TextAlignment.Right);
        chart.getStyler().setYAxisMin(0.0);
        chart.getStyler().setLegendPadding(2);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setLegendVisible(true);
        return chart;
    }

    public List<Object> getDataForChart(int deviceId, DataType dataType, String period, Object choice) {
        Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
        if (device == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return null;
        }

        Calendar day = Calendar.getInstance();
        if (QueryPeriod.TODAY.getValue().equals(period)) {
            return this.getDataForADay(device, dataType, day, choice);
        }
        if (QueryPeriod.YESTERDAY.getValue().equals(period)) {
            day.add(Calendar.DAY_OF_YEAR, -1);
            return this.getDataForADay(device, dataType, day, choice);
        }
        if (QueryPeriod.LAST_3_DAYS.getValue().equals(period)) {
            day.add(Calendar.DAY_OF_YEAR, -4);
            return this.getDataForDays(device, dataType, day, 3, choice);
        }
        if (QueryPeriod.LAST_7_DAYS.getValue().equals(period)) {
            day.add(Calendar.DAY_OF_YEAR, -8);
            return this.getDataForDays(device, dataType, day, 7, choice);
        }

        return null;
    }

    public List<Object> getDataForADay(Device device, DataType dataType, Calendar day, Object choice) {
        List<Object> result = new ArrayList<Object>();
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

    public List<Object> getDataForDays(Device device, DataType dataType, Calendar day, int dayCount, Object choice) {
        List<Object> result = new ArrayList<Object>();

        Calendar startTime = (Calendar) day.clone();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);

        Calendar endTime = (Calendar) day.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);

        Date[] xValues = new Date[dayCount];
        Double[] yValues = new Double[dayCount];

        for (int i = 0; i < dayCount; i++) {
            startTime.add(Calendar.DAY_OF_YEAR, 1);
            endTime.add(Calendar.DAY_OF_YEAR, 1);
            xValues[i] = startTime.getTime();
            yValues[i] = this.getDataForTime(device, dataType, startTime, endTime, choice);
        }

        result.add(xValues);
        result.add(yValues);
        return result;
    }

    public double getDataForTime(Device device, DataType dataType, Calendar startTime, Calendar endTime, Object choice) {
        if (dataType == DataType.CPU_LOAD) {
            return this.getAverageCpuLoadForTime(device, startTime, endTime, (Integer) choice);
        }
        if (dataType == DataType.MEMORY_USAGE) {
            return this.getAverageMemoryUsageForTime(device, startTime, endTime, (String) choice);
        }
        if (dataType == DataType.BANDWIDTH_USAGE) {
            return this.getAverageBandwidthUsageForTime(device, startTime, endTime, (String) choice);
        }
        return 0;
    }

    public double getAverageCpuLoadForTime(Device device, Calendar startTime, Calendar endTime, Integer choice) {
        List<Float> loads = DataManager.getInstance().getDeviceCpuManager().getDeviceCPULoads(device, startTime, endTime, "cpuLoad", choice);
        if (loads == null && loads.isEmpty()) {
            return 0.0;
        }

        int tempSize = loads.size();
        double loadSum = 0.0;
        for (Float load : loads) {
            loadSum += load;
        }

        if (tempSize != 0) {
            return loadSum * 1.0 / tempSize;
        }
        return 0.0;
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
            if (!memoryState.isIsSummarized()) {
                percentageSum += memoryState.getUsagePercentage();
            } else {
                percentageSum += memoryState.getUsedSize();
            }
        }

        if (tempSize != 0) {
            return percentageSum / tempSize;
        }
        return 0.0;
    }

    public double getAverageBandwidthUsageForTime(Device device, Calendar startTime, Calendar endTime, String choice) {
        DeviceNetworkInterface networkInterface = null;
        List<DeviceNetworkInterface> networkInterfaces = device.getNetworkInterfaces();
        for (DeviceNetworkInterface temp : networkInterfaces) {
            if (temp.getName().equals(choice)) {
                networkInterface = temp;
                break;
            }
        }
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

        if (tempSize > 0 && interfaceDataList.get(0).isIsSummarized()) {
            percentageSum = interfaceDataList.get(0).getInboundBytes();
        }

        for (int i = 1; i < tempSize; i++) {
            current = interfaceDataList.get(i);

            if (!current.isIsSummarized()) {
                previous = interfaceDataList.get(i - 1);
                upperPart = Math.max(current.getInboundBytes() - previous.getInboundBytes(),
                        current.getOutboundBytes() - previous.getOutboundBytes()) * 8 * 100 * 1.0d;
                lowerPart = (dataConverter.convertCalendarTimeToSecond(current.getUpdatedTime())
                        - dataConverter.convertCalendarTimeToSecond(previous.getUpdatedTime())) * current.getBandwidth();
                if (lowerPart != 0) {
                    percentageSum += (upperPart / lowerPart);
                }
            } else {
                percentageSum += current.getInboundBytes();
            }
        }

        if (tempSize > 0 && interfaceDataList.get(0).isIsSummarized()) {
            return percentageSum / tempSize;
        }
        if (tempSize - 1 > 0) {
            return percentageSum / (tempSize - 1);
        } 
        return 0.0;
    }

    public class ResultMessageGenerator {

        public String GETTING_FAILED_OTHER = "Some errors happend when getting device data from database";
    }
}
