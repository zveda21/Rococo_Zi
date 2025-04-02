package guru.qa.rococo.controller.client;

import guru.qa.rococo.exception.RemoteServerException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Aspect
@Component
@SuppressWarnings("unused")
public class RestClientExceptionHandler {
    @Around("execution(* guru.qa.rococo.controller.client..*(..))")
    public Object handleRestClientException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (RestClientException e) {
            throw new RemoteServerException(e);
        }
    }
}
