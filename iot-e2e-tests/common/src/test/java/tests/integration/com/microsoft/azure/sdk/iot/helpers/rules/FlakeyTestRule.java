package tests.integration.com.microsoft.azure.sdk.iot.helpers.rules;

import org.junit.Assume;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import tests.integration.com.microsoft.azure.sdk.iot.helpers.IntegrationTest;
import tests.integration.com.microsoft.azure.sdk.iot.helpers.annotations.FlakeyTest;

public class FlakeyTestRule implements TestRule
{
    @Override
    public Statement apply(Statement base, Description description) {
        return new IgnorableStatement(base, description);
    }

    private static class IgnorableStatement extends Statement {

        private final Statement base;
        private final Description description;

        public IgnorableStatement(Statement base, Description description) {
            this.base = base;
            this.description = description;
        }

        @Override
        public void evaluate() throws Throwable {
            FlakeyTest annotation = description.getAnnotation(FlakeyTest.class);
            FlakeyTest classAnnotation = description.getTestClass().getAnnotation(FlakeyTest.class);
            if (annotation != null || classAnnotation != null)
            {
                Assume.assumeTrue("Test is ignored", !IntegrationTest.isPullRequest);
            }

            base.evaluate();
        }
    }
}
