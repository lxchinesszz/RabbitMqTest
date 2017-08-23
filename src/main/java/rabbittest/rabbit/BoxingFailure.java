package rabbittest.rabbit;


import java.util.concurrent.locks.LockSupport;

public class BoxingFailure {

    private static volatile Double sensorValue;

    private static void readSensor() {
        while(true) {
            sensorValue = Math.random();
        }
    }

    private static void processSensorValue(Double value) {
        if(value != null) {
            // Be warned: may take more than one usec on some machines, especially Windows
            LockSupport.parkNanos(1000);
        }
    }

    public static void main(String[] args) {
        int iterations = args.length > 0 ? Integer.parseInt(args[0]) : 1_000_000;

        initSensor();

        for(int i = 0; i < iterations; i ++) {
            processSensorValue(sensorValue);
        }
    }

    private static void initSensor() {
        Thread sensorReader = new Thread(BoxingFailure::readSensor);

        sensorReader.setDaemon(true);
        sensorReader.start();
    }
}