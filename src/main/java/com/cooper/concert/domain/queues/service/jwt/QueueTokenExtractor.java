package com.cooper.concert.domain.queues.service.jwt;

public interface QueueTokenExtractor {
	Long extractUserIdFromToken(String token);
}
