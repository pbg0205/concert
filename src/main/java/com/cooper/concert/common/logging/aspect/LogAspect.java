package com.cooper.concert.common.logging.aspect;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LogAspect {

	@Around("com.cooper.concert.common.logging.pointcuts.LogPointCut.scheduleLogPointCut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		final String signatureName = joinPoint.getSignature().getName();
		final Object result;

		try {
			log.info("{} start - stat time : {}", signatureName, LocalDateTime.now());

			result = joinPoint.proceed();

			log.info("{} end - completed time : {}, result : {}", signatureName, LocalDateTime.now(), result);
		} catch (RuntimeException exception) {
			errorLog(exception);
			throw exception;
		}

		return result;
	}

	private void errorLog(final RuntimeException exception) {
		final List<String> stackTraces = Arrays.stream(exception.getStackTrace())
			.map(StackTraceElement::toString)
			.toList();

		log.error("{} error : exception: {} \n stackTraces: {}",
			exception.getClass().getSimpleName(), exception.getMessage(), stackTraces);
	}
}
