package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RunAs;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.jboss.logging.Logger;

import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ROLE_1;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ROLE_2;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/MessageDrivenRolesQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
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
