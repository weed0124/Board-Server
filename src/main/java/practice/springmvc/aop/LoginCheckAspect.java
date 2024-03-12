package practice.springmvc.aop;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import practice.springmvc.utils.SessionUtil;

@Slf4j
@Aspect
public class LoginCheckAspect {

    @Around("@annotation(practice.springmvc.annotation.LoginCheck)")
    public Object loginCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        String id = SessionUtil.getLoginMemberId(session);

        if (id == null) {
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "로그인 ID를 확인해주세요") {};
        }

        return joinPoint.proceed();
    }
}
