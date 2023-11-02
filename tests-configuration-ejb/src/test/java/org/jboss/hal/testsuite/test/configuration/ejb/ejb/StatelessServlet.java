package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "Stateless", urlPatterns = "/Stateless")
public class StatelessServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private RemoteEJBInterface greeterEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  {
        try {
            greeterEJB.invoke();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
