package com.cooper.concert.support.logging.pointcuts;

import org.aspectj.lang.annotation.Pointcut;

public final class LogPointCut {

	@Pointcut("@annotation(com.cooper.concert.support.logging.annotations.SchedulerLog)")
	public void scheduleLogPointCut() {}
}
