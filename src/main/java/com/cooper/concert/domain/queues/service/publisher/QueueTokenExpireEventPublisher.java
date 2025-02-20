package com.cooper.concert.domain.queues.service.publisher;

import com.cooper.concert.domain.queues.service.dto.event.QueueTokenExpireEvent;

public interface QueueTokenExpireEventPublisher {
	void send(QueueTokenExpireEvent queueTokenExpireEvent);
	void send(String topic, String payload);
}
