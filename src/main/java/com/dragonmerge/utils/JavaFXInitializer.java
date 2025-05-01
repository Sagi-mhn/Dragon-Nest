package com.dragonmerge.utils;

import javafx.application.Platform;
import java.util.concurrent.CountDownLatch;

public class JavaFXInitializer {
    private static boolean initialized = false;

    public static synchronized void initialize() throws Exception {
        if (!initialized) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(() -> latch.countDown());
            latch.await();
            initialized = true;
        }
    }
}
