import java.util.Arrays;
import java.util.concurrent.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final int ROUTE_COUNT = 100;
    private static final String SEED = "RLRFR";
    public static final Map<Integer, Integer> freqList = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        go();
        printMap();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static void go() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < ROUTE_COUNT; i++) {
            Thread thread = new Thread(() -> {
                String route = generateRoute(SEED, ROUTE_COUNT);
                int rFreq = charPerc(route, 'R');
                int rRow = charRow(route, 'R');
                System.out.println("Сгенерирован маршрут " + route + ", частота R -- " + rFreq
                        + ", макс. ряд -- " + rRow);
                synchronized (freqList) {
                    updateMap(rFreq);
                }
            });
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads
        ) { thread.join();
        }
    }

    public static void updateMap(int rFreq) {
        if (freqList.containsKey(rFreq)) {
            freqList.put(rFreq, freqList.get(rFreq) + 1);
        } else {
            freqList.put(rFreq, 1);
        }
    }

    public static void printMap() {
        freqList.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .forEach((entry) -> System.out.println("[Процент R] : " + entry.getKey()
                        + " [Частота] : " + entry.getValue()));

    }

    public static int charPerc(String route, char ch) {
        int count = 0;
        for (int i = 0; i < route.length(); i++) {
            if (route.charAt(i) == ch) {
                count++;
            }
        }
        return ((count * 100) / route.length());
    }

    public static int charRow(String route, char ch) {
        int maxRowLength = 0;
        int rowLength = 0;
        char last = ' ';

        for (int i = 0; i < route.length(); i++) {
            if (route.charAt(i) == ch) {
                if (route.charAt(i) == last) {
                    rowLength++;
                } else {
                    if (rowLength > maxRowLength) {
                        maxRowLength = rowLength;
                    }
                    rowLength = 1;
                }
            }
            last = route.charAt(i);
        }
        return maxRowLength;
    }
}