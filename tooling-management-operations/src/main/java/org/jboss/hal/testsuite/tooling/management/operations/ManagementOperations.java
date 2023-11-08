package org.jboss.hal.testsuite.tooling.management.operations;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.jboss.dmr.ModelNode;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;

public class ManagementOperations {

    private static final Address MANAGEMENT_OPERATIONS_ADDRESS = Address.coreService(MANAGEMENT)
            .and(SERVICE, "management-operations");

    private final Operations ops;

    public ManagementOperations(OnlineManagementClient client) {
        this.ops = new Operations(client);
    }

    public ModelNode getActiveDeploymentOperation() throws IOException {
        ModelNodeResult result = ops.invoke(QUERY, MANAGEMENT_OPERATIONS_ADDRESS.and(ACTIVE_OPERATION, "*"),
                Values.ofObject(WHERE, Values.of(OPERATION, DEPLOY)));
        result.assertSuccess();
        return result.listValue().get(0);
    }

    /**
     * @param timeoutInSeconds how many seconds to wait for any non progressing operation before {@link RuntimeException}
     * is thrown.
     * @return id of found non-progressing operation
     */
    public long waitForNonProgressingOperation(int timeoutInSeconds) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        ModelNodeResult nonProgressingOperationResult = findNonProgressingOperation();
        while (!nonProgressingOperationResult.hasDefinedValue()) {
            if (System.currentTimeMillis() - startTime > timeoutInSeconds * 1000L) {
                throw new RuntimeException("The timeout of '" + timeoutInSeconds
                        + "' seconds to find any non-progressing operation passed.");
            }
            TimeUnit.SECONDS.sleep(1);
            nonProgressingOperationResult = findNonProgressingOperation();
        }
        return Long.parseLong(nonProgressingOperationResult.stringValue());
    }

    public boolean areNonProgressiveOperationsCancelledATM() throws InterruptedException, IOException {
        int timeoutInMilis = 220; // just to be sure all actions are propagated
        long startTime = System.currentTimeMillis();
        while (!areNonProgressiveOperationsCancelled()) {
            if (System.currentTimeMillis() - startTime > timeoutInMilis) {
                return false;
            }
            TimeUnit.MILLISECONDS.sleep(50);
        }
        return true;
    }

    public long getActiveDeploymentOperationId() throws IOException {
        return Long.parseLong(getActiveDeploymentOperation().get("address").asList().get(2).get("active-operation").asString());
    }

    /**
     * If result is not defined there is no non-progressing operation at the moment.
     */
    private ModelNodeResult findNonProgressingOperation() throws IOException {
        ModelNodeResult result = ops.invoke(FIND_NON_PROGRESSING_OPERATION, MANAGEMENT_OPERATIONS_ADDRESS);
        result.assertSuccess();
        return result;
    }

    private boolean areNonProgressiveOperationsCancelled() throws IOException {
        ModelNodeResult nonProgressingOperationResult = findNonProgressingOperation();
        // just one operation is expected, improve this if needed
        String operationId = nonProgressingOperationResult.get(RESULT).asStringOrNull();
        if (operationId != null) {
            return isActiveOperationCancelled(operationId);
        }
        return true;
    }

    private boolean isActiveOperationCancelled(String operationId) throws IOException {
        ModelNodeResult result = ops.readAttribute(MANAGEMENT_OPERATIONS_ADDRESS.and(ACTIVE_OPERATION, operationId), CANCELLED);
        result.assertSuccess();
        return result.asBoolean();
    }
}
