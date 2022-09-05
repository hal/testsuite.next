package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import java.io.IOException;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
