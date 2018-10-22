package org.jboss.hal.testsuite.test.runtime.management.operations;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(name = "Slow", loadOnStartup = 1)
public class LongRunningServlet extends HttpServlet {

    public static final String
        DURATION_IN_SECONDS = "duration.in.seconds",
        DURATION_PROPERTIES_PATH = "/WEB-INF/classes/" + LongRunningServlet.class.getPackage().getName().replace('.', '/')
            + "/duration.properties";
    private static final long serialVersionUID = -6245929477386202878L;

    @Override
    public void init() throws ServletException {
        Properties properties = new Properties();
        try {
            properties.load(getServletContext().getResourceAsStream(DURATION_PROPERTIES_PATH));
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new IllegalStateException(e1);
        }
        int durationInSeconds = Integer.valueOf(properties.getProperty(DURATION_IN_SECONDS));
        for (int i = 1; i < 1 + 10 * durationInSeconds; i++) {
            System.out.println("Initialisation of LongRunningServlet, step no " + i);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        super.init();
    }

}
