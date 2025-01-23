package com.cooper.concert.storage.redis.redisson.components.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.cooper.concert.storage.redis.redisson.components.annotations.DistributedLock;
import com.cooper.concert.storage.redis.redisson.components.parser.CustomSpringELParser;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {

	private static final String REDISSON_LOCK_PREFIX = "LOCK:";

	private final RedissonClient redissonClient;
	private final AopForTransaction aopForTransaction;

	@Around("@annotation(com.cooper.concert.storage.redis.redisson.components.annotations.DistributedLock)")
	public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

		String key =
			REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
				signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());

		RLock rLock = redissonClient.getLock(key);
		try {
			log.debug("lock trial key= {}, tokenId={}, seatId={}", key, joinPoint.getArgs()[0], joinPoint.getArgs()[1]);

			boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(),
				distributedLock.timeUnit());
			if (!available) {
				return false;
			}

			log.debug("lock acquired key= {}, tokenId={}, seatId={}", key, joinPoint.getArgs()[0],
				joinPoint.getArgs()[1]);

			return aopForTransaction.proceed(joinPoint);
		} catch (InterruptedException e) {
			throw new InterruptedException();
		} finally {
			unlock(joinPoint, rLock, key, method);
		}
	}

	private void unlock(final ProceedingJoinPoint joinPoint, final RLock rLock, final String key, final Method method) {
		try {
			rLock.unlock();
			log.debug("unlock key= {}, tokenId={}, seatId={}", key, joinPoint.getArgs()[0], joinPoint.getArgs()[1]);
		} catch (IllegalMonitorStateException e) {
			log.warn("Redisson Lock Already UnLock serviceName : {}, key : {}", method.getName(), key);
		}
	}
}
