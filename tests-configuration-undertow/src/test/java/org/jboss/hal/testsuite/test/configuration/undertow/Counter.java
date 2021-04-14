package org.jboss.hal.testsuite.test.configuration.undertow;

import javax.inject.Named;
import javax.inject.Singleton;

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
