package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.Queue;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@JMSDestinationDefinition(name = "java:/queue/MessageDrivenExecutionTimeQueue",
    interfaceName = "javax.jms.Queue")
@WebServlet(name = "MessageDriven", urlPatterns = "/MessageDriven")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MessageServletExecutionTime extends HttpServlet {

    private static final long serialVersionUID = -8314035702649252239L;

    @Inject
    private JMSContext context;

    @Resource(lookup = "java:/queue/MessageDrivenExecutionTimeQueue")
    private Queue queue;

    @Resource
    private TimerService timerService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.write(
            "<h1>Quickstart: Example demonstrates the use of <strong>JMS 2.0</strong> and <strong>EJB 3.2 Message-Driven Bean</strong> in JBoss EAP.</h1>");
        try {
            out.write("<p>Sending messages to <em>" + queue + "</em></p>");
            out.write("<h2>The following messages will be sent to the destination:</h2>");
            String text = "Hello from timer";
            timerService.createTimer(5000, text);
            out.write(
                "<p><i>Go to your JBoss EAP server console or server log to see the result of messages processing.</i></p>");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @Timeout
    public void sendResponse(Timer timer) {
        context.createProducer().send(queue, timer.getInfo());
    }
}
