package practice.springmvc.config.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import practice.springmvc.trace.TraceStatus;
import practice.springmvc.trace.logtrace.LogTrace;

public class LogTraceAdvice implements MethodInterceptor {

    private final LogTrace logTrace;

    public LogTraceAdvice(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TraceStatus status = null;
        try {
            String message = invocation.getMethod().getDeclaringClass().getSimpleName() + "." + invocation.getMethod().getName() + "()";
            status = logTrace.begin(message);

            // 비즈니스 로직 호출
            Object result = invocation.proceed();

            logTrace.end(status);
            return result;

        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
