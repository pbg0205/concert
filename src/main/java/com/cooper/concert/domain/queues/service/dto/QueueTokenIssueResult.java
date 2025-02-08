package com.cooper.concert.domain.queues.service.dto;

public record QueueTokenIssueResult(String token, Long waitingPosition) {
}
