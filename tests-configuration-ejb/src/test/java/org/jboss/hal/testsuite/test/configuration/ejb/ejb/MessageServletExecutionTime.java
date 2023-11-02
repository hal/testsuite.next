package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.annotation.Resource;
import jakarta.ejb.Timeout;
import jakarta.ejb.Timer;
import jakarta.ejb.TimerService;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSDestinationDefinition;
import jakarta.jms.Queue;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@JMSDestinationDefinition(name = "java:/queue/MessageDrivenExecutionTimeQueue",
    interfaceName = "jakarta.jms.Queue")
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
