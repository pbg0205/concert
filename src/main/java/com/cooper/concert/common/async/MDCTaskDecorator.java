package com.cooper.concert.common.async;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

public class MDCTaskDecorator implements TaskDecorator {

	@Override
	public Runnable decorate(final Runnable task) {
		final Map<String, String> callerThreadContext = MDC.getCopyOfContextMap();
		return () -> {
			MDC.setContextMap(callerThreadContext);
			task.run();
		};
	}
}
