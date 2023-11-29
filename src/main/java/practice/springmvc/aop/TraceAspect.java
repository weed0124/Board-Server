package practice.springmvc.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import practice.springmvc.trace.TraceStatus;
import practice.springmvc.trace.logtrace.LogTrace;

@Slf4j
@Aspect
public class TraceAspect {

    private final LogTrace logTrace;

    public TraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    /**
     * AspectJ 표현식으로 패키지를 지정할 수 있고 어노테이션을 생성해서 해당 어노테이션을 사용한 메소드에만 지정할 수 있음
     */
//    @Around("execution(* practice.springmvc.domain..*(..)) || execution(* practice.springmvc.web..*(..))")
    @Around("@annotation(practice.springmvc.annotation.Trace)")
    public Object doTrace(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
        try {
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);

            // 비즈니스 로직 호출
            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
