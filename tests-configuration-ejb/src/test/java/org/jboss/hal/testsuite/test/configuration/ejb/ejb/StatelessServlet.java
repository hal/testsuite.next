package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
