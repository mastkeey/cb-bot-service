package ru.mastkey.telegrambot.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static ru.mastkey.telegrambot.util.Constants.MDC_REQUEST_ID;

@Aspect
@Component
public class StructuralLogAspect {

    @Around("@within(ru.mastkey.telegrambot.aop.StructuralLogWithRequestIdFieldAnnotation) || " +
            "@annotation(ru.mastkey.telegrambot.aop.StructuralLogWithRequestIdFieldAnnotation)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        var requestId = UUID.randomUUID().toString();

        try (var ignored = MDC.putCloseable(MDC_REQUEST_ID, requestId)) {
            return pjp.proceed();
        }
    }
}