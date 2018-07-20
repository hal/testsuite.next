package org.jboss.hal.testsuite.test.runtime.ejb.ejb;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Singleton", urlPatterns = "/Singleton")
public class SingletonServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private RemoteEJBInterface singletonCounterEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            singletonCounterEJB.invoke();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
