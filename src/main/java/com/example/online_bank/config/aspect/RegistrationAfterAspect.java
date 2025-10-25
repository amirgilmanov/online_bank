package com.example.online_bank.config.aspect;

import com.example.online_bank.domain.dto.RegistrationDtoResponse;
import com.example.online_bank.service.impl.EmailNotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RegistrationAfterAspect {
    private final EmailNotificationServiceImpl emailNotificationService;

    @Pointcut(value = "execution(* com.example.online_bank.service.RegistrationService.signUp(..))")
    public void pointCut() {
    }

    @SneakyThrows
    @Around(value = "pointCut()")
    public Object after(ProceedingJoinPoint joinPoint) {
        RegistrationDtoResponse result = (RegistrationDtoResponse) joinPoint.proceed();
        emailNotificationService.sendOtpCode(result.email(), result.code());
        return result;
    }
}
