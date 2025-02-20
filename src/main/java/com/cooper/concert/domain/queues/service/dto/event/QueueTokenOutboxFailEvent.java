package com.cooper.concert.domain.queues.service.dto.event;

import java.util.UUID;

public record QueueTokenOutboxFailEvent(UUID paymentId)  {
}
