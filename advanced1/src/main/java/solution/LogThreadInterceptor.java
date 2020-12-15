package solution;
import org.spockframework.runtime.extension.*;


class LogThreadInterceptor implements IMethodInterceptor {

    @Override
    public void intercept(IMethodInvocation invocation) throws Throwable {
        System.out.println(String.format("Feature '%s' is executed by Thread: '%s'", invocation.getFeature().getName(), Thread.currentThread().getName()));
        invocation.proceed();
    }

}