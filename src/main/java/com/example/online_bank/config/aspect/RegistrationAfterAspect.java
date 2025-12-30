package com.example.online_bank.config.aspect;

import com.example.online_bank.domain.dto.RegistrationDtoResponse;
import com.example.online_bank.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationAfterAspect {
    private final NotificationService notificationService;

    @Pointcut(value = "execution(* com.example.online_bank.service.RegistrationProcessor.register(..))")
    public void pointCut() {
    }

    @SneakyThrows
    @Around(value = "pointCut()")
    public Object after(ProceedingJoinPoint joinPoint) {
        log.info("Начало отправки email");
        RegistrationDtoResponse result = (RegistrationDtoResponse) joinPoint.proceed();
        notificationService.sendOtpCode(result.email(), result.code());
        return result;
    }
}
