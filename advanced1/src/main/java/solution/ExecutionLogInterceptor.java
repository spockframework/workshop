package solution;

import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.spockframework.runtime.extension.*;

public class ExecutionLogInterceptor extends AbstractMethodInterceptor {

    private final ConcurrentLinkedQueue<String> log;

    public ExecutionLogInterceptor(ConcurrentLinkedQueue<String> log) {
        this.log = log;
    }

    private void invoke(IMethodInvocation invocation, String message) throws Throwable {
        log.add(String.format("%s Start %s", Instant.now(), message));
        invocation.proceed();
        log.add(String.format("%s End   %s", Instant.now(), message));
    }

    @Override
    public void interceptSpecExecution(IMethodInvocation invocation) throws Throwable {
        invoke(invocation, invocation.getSpec().getName());
    }

    @Override
    public void interceptIterationExecution(IMethodInvocation invocation) throws Throwable {
        invoke(invocation, String.format("%s > %s", invocation.getSpec().getName(), invocation.getIteration().getName()));
    }
}
