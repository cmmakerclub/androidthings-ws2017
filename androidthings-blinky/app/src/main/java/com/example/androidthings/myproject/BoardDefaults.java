package com.example.androidthings.myproject;

import android.os.Build;

import com.google.android.things.pio.PeripheralManagerService;

import java.util.List;

/**
 * Created by nat on 1/13/2017 AD.
 */

@SuppressWarnings("WeakerAccess")
public class BoardDefaults {
    private static final String DEVICE_EDISON_ARDUINO = "edison_arduino";
    private static final String DEVICE_EDISON = "edison";
    private static final String DEVICE_RPI3 = "rpi3";
    private static final String DEVICE_NXP = "imx6ul";
    private static String sBoardVariant = "";

    /**
     * Return the GPIO pin that the LED is connected on.
     * For example, on Intel Edison Arduino breakout, pin "IO13" is connected to an onboard LED
     * that turns on when the GPIO pin is HIGH, and off when low.
     */
    public static String getGPIOForLED() {
        switch (getBoardVariant()) {
            case DEVICE_EDISON_ARDUINO:
                return "IO13";
            case DEVICE_EDISON:
                return "GP45";
            case DEVICE_RPI3:
                return "BCM6";
            case DEVICE_NXP:
                return "GPIO4_IO20";
            default:
                throw new IllegalStateException("Unknown Build.DEVICE " + Build.DEVICE);
        }
    }

    private static String getBoardVariant() {
        if (!sBoardVariant.isEmpty()) {
            return sBoardVariant;
        }
        sBoardVariant = Build.DEVICE;
        // For the edison check the pin prefix
        // to always return Edison Breakout pin name when applicable.
        if (sBoardVariant.equals(DEVICE_EDISON)) {
            PeripheralManagerService pioService = new PeripheralManagerService();
            List<String> gpioList = pioService.getGpioList();
            if (gpioList.size() != 0) {
                String pin = gpioList.get(0);
                if (pin.startsWith("IO")) {
                    sBoardVariant = DEVICE_EDISON_ARDUINO;
                }
            }
        }
        return sBoardVariant;
    }
}