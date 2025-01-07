package com.cooper.concert.domain.queues.service.dto;

import java.util.UUID;

public record QueueTokenIssueResult(UUID tokenId, Long waitingPosition) {
}
