package de.ait.chat.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectLogging {

    private Logger logger = LoggerFactory.getLogger(AspectLogging.class);

    @Around("userServiceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            logger.info("Method {} executed successfully", joinPoint.getSignature().getName());
            return result;
        } catch (Exception e) {
            logger.error("Exception in method {}: {}", joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            logger.info("Method {} executed in {} ms", joinPoint.getSignature().getName(), executionTime);
        }
    }

    @Pointcut("execution(* de.aittr.project_wishlist.service.impl.UserServiceImpl.*(..))")
    public void userServiceMethods() {}

}
