package com.cooper.concert.common.logging.pointcuts;

import org.aspectj.lang.annotation.Pointcut;

public final class LogPointCut {

	@Pointcut("@annotation(com.cooper.concert.common.logging.annotations.SchedulerLog)")
	public void scheduleLogPointCut() {}
}
