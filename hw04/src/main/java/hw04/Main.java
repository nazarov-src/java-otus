package hw04;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.List;

/*
        -Xms256ms
        -Xmx256m
        -XX:+UseG1GC
        */

public class Main {

    private static final int MAX_SIZE = 90_000;

    private static int opsCounter = 0;
    private static long total_duration = 0;
    private static long startTime = 0;

    public static void main(String[] args) {

        switchOnMonitoring();

        startTime = System.currentTimeMillis();

        try {
            memoryTest();

        } catch (Exception e) {
            System.out.println("exc: " + e.getMessage());

        } finally {
            printMetrics("END", 0, getTimeSpent());
        }
    }

    private static long getTimeSpent() {
        return (System.currentTimeMillis() - startTime)/1000;
    }

    private static void printMetrics(String operation, long size, long timeSpent) {
        System.out.println("[" + operation + "] size = " + size +
                ", ops counter = " + opsCounter +
                ", time spent = " + timeSpent + " secs, GC duration: " + total_duration + " ms");
    }

    private static void memoryTest() throws InterruptedException {

        List<String> storage = new ArrayList<>();

        while (true) {

            // printMetrics("ADD", storage.size(), getTimeSpent());

            for (int i = 0; i < MAX_SIZE; i++) {
                storage.add(new String(new char[255]));
                opsCounter++;
            }

            // printMetrics("REMOVE", storage.size(), getTimeSpent());

            int eraseAmount = storage.size()/10;
            for (int i = 0; i < eraseAmount; i++) {
                storage.remove(i);
                //list.set(i, null);
                opsCounter++;
            }

            Thread.sleep(1);
        }
    }

    private static void switchOnMonitoring() {

        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;

            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info =
                            GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    total_duration += duration;

                    System.out.println(
                            "start:" + startTime +
                            " Name:" + gcName +
                            ", action:" + gcAction +
                            ", gcCause:" + gcCause +
                            "(" + duration + " ms)");
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }
}
