package org.jboss.hal.testsuite.test.configuration.batch;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import jakarta.batch.api.AbstractBatchlet;
import jakarta.batch.api.BatchProperty;
import org.jboss.logging.Logger;

@Named("testBatchlet")
public class Batchlet extends AbstractBatchlet {

    private static final Logger LOGGER = Logger.getLogger(Batchlet.class);
    private Thread currentThread;

    @Inject
    @BatchProperty
    private Long stoppingInterval;

    @Override
    public String process() throws Exception {
        currentThread = Thread.currentThread();
        //Unreachable code error "hack"
        if (true) {
            for (;;) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                    LOGGER.debug("Processing");
                } catch (InterruptedException e) {
                    TimeUnit.MILLISECONDS.sleep(stoppingInterval);
                    return "STOPPED";
                }
            }
        }
        return "COMPLETED";
    }

    @Override
    public void stop() {
        currentThread.interrupt();
    }
}
