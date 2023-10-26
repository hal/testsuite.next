package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import org.jboss.logging.Logger;

import static org.jboss.hal.testsuite.fixtures.EJBFixtures.SLEEP_TIME;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/MessageDrivenExecutionTimeQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})
public class MessageDrivenEJBExecutionTime implements MessageListener {

    private final Logger logger = Logger.getLogger("MessageDrivenEJBExecutionTime");

    @Override
    public void onMessage(Message message) {
        TextMessage msg;
        try {
            Thread.sleep(SLEEP_TIME);
            if (message instanceof TextMessage) {
                msg = (TextMessage) message;
                logger.info("Received Message from MessageDrivenExecutionTimeQueue: " + msg.getText());
            } else {
                logger.warn("Message of wrong type: " + message.getClass().getName());
            }
        } catch (JMSException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
