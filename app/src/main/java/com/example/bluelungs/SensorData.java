package com.example.bluelungs;

public class SensorData {
    private float temperature;
    private float turbidity;

    public SensorData() {
        // Default constructor required for Firebase
    }

    public SensorData(float temperature, float turbidity) {
        this.temperature = temperature;
        this.turbidity = turbidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getTurbidity() {
        return turbidity;
    }
}
