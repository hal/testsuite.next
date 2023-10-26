package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RunAs;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import org.jboss.logging.Logger;

import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ROLE_1;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ROLE_2;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/MessageDrivenRolesQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})
@DeclareRoles({ROLE_1, ROLE_2})
@RunAs(ROLE_1)
public class MessageDrivenEJBWithRoles implements MessageListener {

    private final Logger logger = Logger.getLogger("MessageDrivenEJBWithRoles");

    @Override
    public void onMessage(Message message) {
        TextMessage msg;
        try {
            if (message instanceof TextMessage) {
                msg = (TextMessage) message;
                logger.info("Received Message from MessageDrivenEJBWithRoles: " + msg.getText());
            } else {
                logger.warn("Message of wrong type: " + message.getClass().getName());
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

}
