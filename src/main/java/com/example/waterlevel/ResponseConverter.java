package com.example.waterlevel;

public class ResponseConverter {

    private static int sensorPos = 204;
    private static double radius = 12.5;

    public static int getSensorPos() {
        return sensorPos;
    }

    public static void setSensorPos(int sensorPos) {
        ResponseConverter.sensorPos = sensorPos;
    }

    private static double baseArea() {
        return Math.PI * Math.pow(radius, 2);
    }

    public static int waterLevelFillCm(String response) {
        int h1 = Integer.valueOf(response);
        return sensorPos - h1;
    }

    public static int waterLevelLiters(String response) {
        double h1DM = Double.valueOf(response) / 10;
        double h2DM = ((double)sensorPos / 10) - h1DM;
        return (int) (h2DM * baseArea());
    }

    public static int waterLevelPercent(String response) {
        int v1 = waterLevelLiters(response);
        int v2 = (int) (baseArea() * sensorPos / 10);
        return 100 * v1 / v2;
    }

}
