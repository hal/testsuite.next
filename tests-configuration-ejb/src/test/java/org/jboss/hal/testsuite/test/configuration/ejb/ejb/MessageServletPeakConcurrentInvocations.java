package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jms.Destination;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSDestinationDefinition;
import jakarta.jms.Queue;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@JMSDestinationDefinition(name = "java:/queue/MessageDrivenPeakConcurrentInvocationsQueue",
    interfaceName = "jakarta.jms.Queue")
@WebServlet(name = "MessageDriven", urlPatterns = "/MessageDriven")
public class MessageServletPeakConcurrentInvocations extends HttpServlet {

    private static final long serialVersionUID = -8314035702649252239L;

    private static final int MSG_COUNT = 5;

    @Inject
    private JMSContext context;

    @Resource(lookup = "java:/queue/MessageDrivenPeakConcurrentInvocationsQueue")
    private Queue queue;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.write("<h1>Quickstart: Example demonstrates the use of <strong>JMS 2.0</strong> and <strong>EJB 3.2 Message-Driven Bean</strong> in JBoss EAP.</h1>");
        try {
            final Destination destination = queue;
            out.write("<p>Sending messages to <em>" + destination + "</em></p>");
            out.write("<h2>The following messages will be sent to the destination:</h2>");
            for (int i = 0; i < MSG_COUNT; i++) {
                String text = "This is message for MessageDrivenPeakConcurrentInvocations " + (i + 1);
                context.createProducer().send(destination, text);
                out.write("Message (" + i + "): " + text + "</br>");
            }
            out.write("<p><i>Go to your JBoss EAP server console or server log to see the result of messages processing.</i></p>");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
