package org.jboss.hal.testsuite.test.runtime.ejb.ejb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.jboss.logging.Logger;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/MessageDrivenQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})
public class MessageDrivenEJB implements MessageListener {

    private final Logger logger = Logger.getLogger("MessageDrivenEJB");

    @Override
    public void onMessage(Message message) {
        TextMessage msg;
        try {
            Thread.sleep(5000);
            if (message instanceof TextMessage) {
                msg = (TextMessage) message;
                logger.info("Received Message from MessageDrivenQueue: " + msg.getText());
            } else {
                logger.warn("Message of wrong type: " + message.getClass().getName());
            }
        } catch (JMSException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}