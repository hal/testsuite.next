package org.jboss.hal.testsuite.test.configuration.undertow;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Named
@Singleton
public class Counter {

    private int a = 1;
    private int b = 1;

    public void incrementA() {
        a++;
    }

    public void incrementB() {
        b++;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }
}
