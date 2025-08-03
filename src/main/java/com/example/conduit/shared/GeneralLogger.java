package com.example.conduit.shared;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class GeneralLogger {
    Logger logger = LoggerFactory.getLogger(GeneralLogger.class);

    @Before("execution(* com.example.conduit.modules..service..*.get*(..)) ||" +
    "execution(* com.example.conduit.modules..service..*.create*(..)) ||" +
    "execution(* com.example.conduit.modules..service..*.update*(..)) ||" +
    "execution(* com.example.conduit.modules..service..*.delete*(..))")
    public void crudLogger(JoinPoint joinPoint) {
        logger.info(joinPoint.toShortString());
    }
}
